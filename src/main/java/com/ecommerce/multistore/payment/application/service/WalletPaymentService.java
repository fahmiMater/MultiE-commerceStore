package com.ecommerce.multistore.payment.application.service;

import com.ecommerce.multistore.payment.domain.WalletTransaction;
import com.ecommerce.multistore.payment.domain.PaymentStatus;
import com.ecommerce.multistore.payment.infrastructure.WalletTransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * خدمة المحافظ الإلكترونية
 * Wallet Payment Service
 * 
 * @author Multi-Store Team
 * @version 1.0
 */
@Service
@Transactional
public class WalletPaymentService {

    private final WalletTransactionRepository walletTransactionRepository;

    @Autowired
    public WalletPaymentService(WalletTransactionRepository walletTransactionRepository) {
        this.walletTransactionRepository = walletTransactionRepository;
    }

    /**
     * معالجة الدفع بالمحفظة الإلكترونية
     * Process wallet payment
     * 
     * @param walletTransaction معاملة المحفظة
     */
    public void processWalletPayment(WalletTransaction walletTransaction) {
        try {
            // تحديث الحالة إلى "جاري المعالجة"
            walletTransaction.setStatus(PaymentStatus.PROCESSING);
            walletTransactionRepository.save(walletTransaction);

            // محاكاة استدعاء API الخارجي
            boolean success = callExternalWalletAPI(walletTransaction);

            if (success) {
                // نجح الدفع
                String walletTxnId = "WLT-" + System.currentTimeMillis();
                String responsePayload = createSuccessResponse(walletTxnId);                walletTransaction.markAsCompleted(walletTxnId, responsePayload);
            } else {
                // فشل الدفع
                walletTransaction.markAsFailed("Insufficient balance or transaction declined");
            }

            walletTransactionRepository.save(walletTransaction);

        } catch (Exception e) {
            walletTransaction.markAsFailed("Payment processing error: " + e.getMessage());
            walletTransactionRepository.save(walletTransaction);
            throw new RuntimeException("Failed to process wallet payment", e);
        }
    }

    /**
     * محاكاة استدعاء API خارجي للمحفظة
     * Simulate external wallet API call
     * 
     * @param walletTransaction معاملة المحفظة
     * @return boolean نجح أم فشل
     */
    private boolean callExternalWalletAPI(WalletTransaction walletTransaction) {
        // محاكاة - في التطبيق الحقيقي، سيتم استدعاء API الفعلي
        try {
            Thread.sleep(2000); // محاكاة وقت المعالجة
            
            // محاكاة نسبة نجاح 85%
            return Math.random() > 0.15;
            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return false;
        }
    }

    /**
     * إنشاء استجابة النجاح
     * Create success response
     */
    private String createSuccessResponse(String walletTxnId) {
        return String.format(
            "{\"status\":\"success\",\"transaction_id\":\"%s\",\"timestamp\":\"%s\"}",
            walletTxnId,
            java.time.LocalDateTime.now()
        );
    }
}
