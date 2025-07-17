package com.ecommerce.multistore.payment.infrastructure;

import com.ecommerce.multistore.payment.domain.WalletTransaction;
import com.ecommerce.multistore.payment.domain.WalletType;
import com.ecommerce.multistore.payment.domain.PaymentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * مستودع معاملات المحافظ الإلكترونية
 * Wallet Transaction Repository
 * 
 * @author Multi-Store Team
 * @version 1.0
 */
@Repository
public interface WalletTransactionRepository extends JpaRepository<WalletTransaction, UUID> {
    
    /**
     * البحث بواسطة Display ID
     * Find by display ID
     */
    Optional<WalletTransaction> findByDisplayId(String displayId);
    
    /**
     * البحث بواسطة معرف الدفع
     * Find by payment ID
     */
    Optional<WalletTransaction> findByPaymentId(UUID paymentId);
    
    /**
     * البحث بواسطة مرجع المعاملة
     * Find by transaction reference
     */
    Optional<WalletTransaction> findByTransactionReference(String transactionReference);
    
    /**
     * البحث بواسطة معرف معاملة المحفظة
     * Find by wallet transaction ID
     */
    Optional<WalletTransaction> findByWalletTransactionId(String walletTransactionId);
    
    /**
     * البحث بواسطة نوع المحفظة
     * Find by wallet type
     */
    Page<WalletTransaction> findByWalletType(WalletType walletType, Pageable pageable);
    
    /**
     * البحث بواسطة رقم الهاتف
     * Find by wallet phone
     */
    Page<WalletTransaction> findByWalletPhone(String walletPhone, Pageable pageable);
    
    /**
     * البحث بواسطة الحالة
     * Find by status
     */
    Page<WalletTransaction> findByStatus(PaymentStatus status, Pageable pageable);
    
    /**
     * البحث بواسطة نوع المحفظة والحالة
     * Find by wallet type and status
     */
    List<WalletTransaction> findByWalletTypeAndStatus(WalletType walletType, PaymentStatus status);
    
    /**
     * إحصائيات معاملات المحافظ حسب النوع
     * Wallet transaction statistics by type
     */
    @Query("SELECT COUNT(wt) FROM WalletTransaction wt WHERE wt.walletType = :walletType")
    long countByWalletType(@Param("walletType") WalletType walletType);
    
    /**
     * المعاملات المعلقة أكثر من ساعة
     * Pending transactions older than one hour
     */
    @Query("SELECT wt FROM WalletTransaction wt WHERE wt.status = 'PENDING' AND wt.createdAt < :cutoffTime")
    List<WalletTransaction> findPendingTransactionsOlderThan(@Param("cutoffTime") LocalDateTime cutoffTime);
}
