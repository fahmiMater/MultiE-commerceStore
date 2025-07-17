package com.ecommerce.multistore.payment.application.service;

import com.ecommerce.multistore.payment.application.dto.*;
import com.ecommerce.multistore.payment.domain.*;
import com.ecommerce.multistore.payment.infrastructure.PaymentRepository;
import com.ecommerce.multistore.payment.infrastructure.WalletTransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * خدمة إدارة المدفوعات
 * تحتوي على جميع العمليات المتعلقة بالمدفوعات والمحافظ الإلكترونية
 * 
 * Payment Management Service
 * Contains all payment and e-wallet related operations
 * 
 * @author Multi-Store Team
 * @version 1.0
 * @since 2024-01-01
 */
@Service
@Transactional
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final WalletTransactionRepository walletTransactionRepository;
    private final WalletPaymentService walletPaymentService;

    /**
     * Constructor لحقن Dependencies
     * Constructor for dependency injection
     * 
     * @param paymentRepository مستودع المدفوعات
     * @param walletTransactionRepository مستودع معاملات المحافظ
     * @param walletPaymentService خدمة المحافظ الإلكترونية
     */
    @Autowired
    public PaymentService(PaymentRepository paymentRepository,
                         WalletTransactionRepository walletTransactionRepository,
                         WalletPaymentService walletPaymentService) {
        this.paymentRepository = paymentRepository;
        this.walletTransactionRepository = walletTransactionRepository;
        this.walletPaymentService = walletPaymentService;
    }

    /**
     * إنشاء دفع جديد
     * Creates a new payment
     * 
     * @param request بيانات الدفع الجديد
     * @return PaymentResponse الدفع المُنشأ
     * @throws IllegalArgumentException إذا كانت البيانات غير صحيحة
     */
    public PaymentResponse createPayment(CreatePaymentRequest request) {
        // التحقق من صحة البيانات
        validatePaymentRequest(request);

        // إنشاء كائن الدفع
        Payment payment = buildPaymentFromRequest(request);
        payment.setTransactionId(generateTransactionId());
        
        // حفظ الدفع
        Payment savedPayment = paymentRepository.save(payment);
        
        // معالجة الدفع حسب النوع
        PaymentResponse response = processPaymentByMethod(savedPayment, request);
        
        return response;
    }

    /**
     * البحث عن دفع بواسطة UUID
     * Finds a payment by UUID
     * 
     * @param id المعرف الفريد للدفع
     * @return Optional<PaymentResponse> الدفع إذا وُجد
     */
    @Transactional(readOnly = true)
    public Optional<PaymentResponse> findById(UUID id) {
        return paymentRepository.findById(id)
                .map(this::convertToResponse);
    }

    /**
     * البحث عن دفع بواسطة Display ID
     * Finds a payment by display ID
     * 
     * @param displayId المعرف المعروض
     * @return Optional<PaymentResponse> الدفع إذا وُجد
     */
    @Transactional(readOnly = true)
    public Optional<PaymentResponse> findByDisplayId(String displayId) {
        return paymentRepository.findByDisplayId(displayId)
                .map(this::convertToResponse);
    }

    /**
     * البحث عن دفع بواسطة رقم المعاملة
     * Finds a payment by transaction ID
     * 
     * @param transactionId رقم المعاملة
     * @return Optional<PaymentResponse> الدفع إذا وُجد
     */
    @Transactional(readOnly = true)
    public Optional<PaymentResponse> findByTransactionId(String transactionId) {
        return paymentRepository.findByTransactionId(transactionId)
                .map(this::convertToResponse);
    }

    /**
     * الحصول على مدفوعات الطلب
     * Get order payments
     * 
     * @param orderId معرف الطلب
     * @return List<PaymentResponse> قائمة بمدفوعات الطلب
     */
    @Transactional(readOnly = true)
    public List<PaymentResponse> getOrderPayments(UUID orderId) {
        return paymentRepository.findByOrderId(orderId)
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    /**
     * الحصول على جميع المدفوعات مع الصفحات
     * Retrieves all payments with pagination
     * 
     * @param page رقم الصفحة
     * @param size عدد العناصر في الصفحة
     * @param sortBy الحقل المطلوب ترتيبه
     * @param sortDir اتجاه الترتيب
     * @return Page<PaymentResponse> صفحة من المدفوعات
     */
    @Transactional(readOnly = true)
    public Page<PaymentResponse> getAllPayments(int page, int size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
                   Sort.by(sortBy).descending() : 
                   Sort.by(sortBy).ascending();
        
        Pageable pageable = PageRequest.of(page, size, sort);
        
        return paymentRepository.findAll(pageable)
                .map(this::convertToResponse);
    }

    /**
     * البحث بواسطة الحالة
     * Find payments by status
     * 
     * @param status حالة الدفع
     * @param page رقم الصفحة
     * @param size عدد العناصر في الصفحة
     * @return Page<PaymentResponse> صفحة من المدفوعات
     */
    @Transactional(readOnly = true)
    public Page<PaymentResponse> getPaymentsByStatus(PaymentStatus status, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return paymentRepository.findByStatus(status, pageable)
                .map(this::convertToResponse);
    }

    /**
     * البحث بواسطة طريقة الدفع
     * Find payments by method
     * 
     * @param paymentMethod طريقة الدفع
     * @param page رقم الصفحة
     * @param size عدد العناصر في الصفحة
     * @return Page<PaymentResponse> صفحة من المدفوعات
     */
    @Transactional(readOnly = true)
    public Page<PaymentResponse> getPaymentsByMethod(PaymentMethod paymentMethod, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return paymentRepository.findByPaymentMethod(paymentMethod, pageable)
                .map(this::convertToResponse);
    }

    /**
     * تأكيد الدفع
     * Confirm payment
     * 
     * @param paymentId معرف الدفع
     * @param gatewayTransactionId رقم معاملة البوابة
     * @return PaymentResponse الدفع المحدث
     */
    public PaymentResponse confirmPayment(UUID paymentId, String gatewayTransactionId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new IllegalArgumentException("Payment not found with ID: " + paymentId));

        if (!payment.canBeProcessed()) {
            throw new IllegalStateException("Payment cannot be processed in current state: " + payment.getStatus());
        }

        payment.markAsCompleted(gatewayTransactionId);
        Payment updatedPayment = paymentRepository.save(payment);
        
        return convertToResponse(updatedPayment);
    }

    /**
     * رفض الدفع
     * Reject payment
     * 
     * @param paymentId معرف الدفع
     * @param reason سبب الرفض
     * @return PaymentResponse الدفع المحدث
     */
    public PaymentResponse rejectPayment(UUID paymentId, String reason) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new IllegalArgumentException("Payment not found with ID: " + paymentId));

        payment.markAsFailed(reason);
        Payment updatedPayment = paymentRepository.save(payment);
        
        return convertToResponse(updatedPayment);
    }

    /**
     * استرداد الدفع
     * Refund payment
     * 
     * @param paymentId معرف الدفع
     * @return PaymentResponse الدفع المحدث
     */
    public PaymentResponse refundPayment(UUID paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new IllegalArgumentException("Payment not found with ID: " + paymentId));

        if (!payment.isSuccessful()) {
            throw new IllegalStateException("Only successful payments can be refunded");
        }

        payment.markAsRefunded();
        Payment updatedPayment = paymentRepository.save(payment);
        
        return convertToResponse(updatedPayment);
    }

    /**
     * الحصول على إحصائيات المدفوعات
     * Get payment statistics
     * 
     * @return PaymentStatisticsResponse إحصائيات المدفوعات
     */
    @Transactional(readOnly = true)
    public PaymentStatisticsResponse getPaymentStatistics() {
        PaymentStatisticsResponse stats = new PaymentStatisticsResponse();
        
        // إحصائيات عامة
        stats.setTotalPayments(paymentRepository.count());
        stats.setSuccessfulPayments(paymentRepository.countByStatus(PaymentStatus.COMPLETED));
        stats.setFailedPayments(paymentRepository.countByStatus(PaymentStatus.FAILED));
        stats.setPendingPayments(paymentRepository.countByStatus(PaymentStatus.PENDING));
        
        // المبالغ
        stats.setTotalAmount(paymentRepository.sumSuccessfulPayments());
        
        // اليوم
        LocalDateTime startOfDay = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
        LocalDateTime endOfDay = LocalDateTime.now().withHour(23).withMinute(59).withSecond(59);
        stats.setTodayAmount(paymentRepository.sumPaymentsByDateRange(startOfDay, endOfDay));
        
        // إحصائيات طرق الدفع
        for (PaymentMethod method : PaymentMethod.values()) {
            long count = paymentRepository.countByPaymentMethod(method);
            stats.addMethodStatistic(method.getValue(), count);
        }
        
        return stats;
    }

    // ===============================
    // Private Helper Methods
    // ===============================

    /**
     * التحقق من صحة طلب الدفع
     * Validates payment request
     */
    private void validatePaymentRequest(CreatePaymentRequest request) {
        if (request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Payment amount must be greater than 0");
        }

        // التحقق من طريقة الدفع
        if (request.getPaymentMethod().isEWallet() && 
            (request.getWalletPhone() == null || request.getWalletPhone().trim().isEmpty())) {
            throw new IllegalArgumentException("Wallet phone is required for e-wallet payments");
        }

        // التحقق من رقم الهاتف للمحافظ الإلكترونية
        if (request.getPaymentMethod().isEWallet()) {
            WalletType walletType = WalletType.valueOf(request.getPaymentMethod().name());
            if (!walletType.supportsPhoneNumber(request.getWalletPhone())) {
                throw new IllegalArgumentException("Invalid phone number for " + walletType.getArabicName());
            }
        }
    }

    /**
     * بناء كائن الدفع من الطلب
     * Builds Payment entity from request
     */
    private Payment buildPaymentFromRequest(CreatePaymentRequest request) {
        Payment payment = new Payment();
        payment.setOrderId(request.getOrderId());
        payment.setPaymentMethod(request.getPaymentMethod());
        payment.setAmount(request.getAmount());
        payment.setCurrency(request.getCurrency());
        
        // تحديد البوابة حسب طريقة الدفع
        if (request.getPaymentMethod().isEWallet()) {
            payment.setPaymentGateway(request.getPaymentMethod().getValue() + "_gateway");
        }
        
        return payment;
    }

    /**
     * معالجة الدفع حسب النوع
     * Process payment by method
     */
    private PaymentResponse processPaymentByMethod(Payment payment, CreatePaymentRequest request) {
        switch (request.getPaymentMethod()) {
            case JEEB:
            case FLOUSI:
            case MOBILE_MONEY:
                return processWalletPayment(payment, request);
            case CASH_ON_DELIVERY:
                return processCashOnDeliveryPayment(payment);
            case BANK_TRANSFER:
                return processBankTransferPayment(payment, request);
            default:
                throw new IllegalArgumentException("Unsupported payment method: " + request.getPaymentMethod());
        }
    }

    /**
     * معالجة الدفع بالمحفظة الإلكترونية
     * Process wallet payment
     */
    private PaymentResponse processWalletPayment(Payment payment, CreatePaymentRequest request) {
        try {
            // إنشاء معاملة المحفظة
            WalletType walletType = WalletType.valueOf(request.getPaymentMethod().name());
            WalletTransaction walletTransaction = new WalletTransaction(
                payment.getId(), 
                walletType, 
                request.getWalletPhone(), 
                payment.getAmount()
            );
            
            walletTransaction.setTransactionReference(generateWalletTransactionReference());
            WalletTransaction savedWalletTransaction = walletTransactionRepository.save(walletTransaction);
            
            // معالجة الدفع مع الخدمة الخارجية
            walletPaymentService.processWalletPayment(savedWalletTransaction);
            
            // تحديث حالة الدفع
            payment.setStatus(PaymentStatus.PROCESSING);
            paymentRepository.save(payment);
            
            return convertToResponse(payment);
            
        } catch (Exception e) {
            payment.markAsFailed("Wallet payment processing failed: " + e.getMessage());
            paymentRepository.save(payment);
            throw new RuntimeException("Failed to process wallet payment", e);
        }
    }

    /**
     * معالجة الدفع النقدي عند التسليم
     * Process cash on delivery payment
     */
    private PaymentResponse processCashOnDeliveryPayment(Payment payment) {
        // الدفع النقدي يبقى في حالة pending حتى التسليم
        payment.setStatus(PaymentStatus.PENDING);
        paymentRepository.save(payment);
        
        return convertToResponse(payment);
    }

    /**
     * معالجة التحويل البنكي
     * Process bank transfer payment
     */
    private PaymentResponse processBankTransferPayment(Payment payment, CreatePaymentRequest request) {
        // التحويل البنكي يحتاج مراجعة يدوية
        payment.setStatus(PaymentStatus.PENDING);
        
        if (request.getBankReference() != null) {
            payment.setGatewayTransactionId(request.getBankReference());
        }
        
        paymentRepository.save(payment);
        return convertToResponse(payment);
    }

    /**
     * توليد رقم معاملة فريد
     * Generate unique transaction ID
     */
    private String generateTransactionId() {
        return "TXN-" + System.currentTimeMillis() + "-" + 
               (int)(Math.random() * 10000);
    }

    /**
     * توليد مرجع معاملة المحفظة
     * Generate wallet transaction reference
     */
    private String generateWalletTransactionReference() {
        return "WLT-" + System.currentTimeMillis() + "-" + 
               (int)(Math.random() * 10000);
    }

    /**
     * تحويل إلى Response
     * Convert to response
     */
    private PaymentResponse convertToResponse(Payment payment) {
        PaymentResponse response = new PaymentResponse();
        response.setId(payment.getId());
        response.setDisplayId(payment.getDisplayId());
        response.setOrderId(payment.getOrderId());
        response.setPaymentMethod(payment.getPaymentMethod());
        response.setPaymentGateway(payment.getPaymentGateway());
        response.setTransactionId(payment.getTransactionId());
        response.setGatewayTransactionId(payment.getGatewayTransactionId());
        response.setAmount(payment.getAmount());
        response.setCurrency(payment.getCurrency());
        response.setStatus(payment.getStatus());
        response.setGatewayResponse(payment.getGatewayResponse());
        response.setFailureReason(payment.getFailureReason());
        response.setProcessedAt(payment.getProcessedAt());
        response.setCreatedAt(payment.getCreatedAt());
        response.setUpdatedAt(payment.getUpdatedAt());
        
        // أسماء العرض
        response.setPaymentMethodDisplayName(getPaymentMethodDisplayName(payment.getPaymentMethod()));
        response.setStatusDisplayName(getPaymentStatusDisplayName(payment.getStatus()));
        
        // إضافة معاملة المحفظة إن وجدت
        if (payment.getPaymentMethod().isEWallet()) {
            walletTransactionRepository.findByPaymentId(payment.getId())
                    .ifPresent(walletTransaction -> {
                        response.setWalletTransaction(convertWalletTransactionToResponse(walletTransaction));
                    });
        }
        
        return response;
    }

    /**
     * تحويل معاملة المحفظة إلى Response
     * Convert wallet transaction to response
     */
    private WalletTransactionResponse convertWalletTransactionToResponse(WalletTransaction walletTransaction) {
        WalletTransactionResponse response = new WalletTransactionResponse();
        response.setId(walletTransaction.getId());
        response.setDisplayId(walletTransaction.getDisplayId());
        response.setPaymentId(walletTransaction.getPaymentId());
        response.setWalletType(walletTransaction.getWalletType());
        response.setWalletPhone(walletTransaction.getWalletPhone());
        response.setTransactionReference(walletTransaction.getTransactionReference());
        response.setWalletTransactionId(walletTransaction.getWalletTransactionId());
        response.setAmount(walletTransaction.getAmount());
        response.setFees(walletTransaction.getFees());
        response.setStatus(walletTransaction.getStatus());
        response.setErrorMessage(walletTransaction.getErrorMessage());
        response.setProcessedAt(walletTransaction.getProcessedAt());
        response.setCreatedAt(walletTransaction.getCreatedAt());
        response.setUpdatedAt(walletTransaction.getUpdatedAt());
        
        // أسماء العرض
        response.setWalletTypeDisplayName(walletTransaction.getWalletType().getArabicName());
        response.setStatusDisplayName(getPaymentStatusDisplayName(walletTransaction.getStatus()));
        
        return response;
    }

    /**
     * الحصول على اسم طريقة الدفع للعرض
     * Get payment method display name
     */
    private String getPaymentMethodDisplayName(PaymentMethod method) {
        switch (method) {
            case JEEB: return "جيب";
            case FLOUSI: return "فلوسي";
            case MOBILE_MONEY: return "موبايل موني";
            case CASH_ON_DELIVERY: return "الدفع عند التسليم";
            case BANK_TRANSFER: return "تحويل بنكي";
            default: return method.getValue();
        }
    }

    /**
     * الحصول على اسم حالة الدفع للعرض
     * Get payment status display name
     */
    private String getPaymentStatusDisplayName(PaymentStatus status) {
        switch (status) {
            case PENDING: return "في الانتظار";
            case PROCESSING: return "جاري المعالجة";
            case COMPLETED: return "مكتمل";
            case FAILED: return "فشل";
            case CANCELLED: return "ملغي";
            case REFUNDED: return "مسترد";
            default: return status.getValue();
        }
    }
}
