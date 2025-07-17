package com.ecommerce.multistore.payment.infrastructure;

import com.ecommerce.multistore.payment.domain.Payment;
import com.ecommerce.multistore.payment.domain.PaymentMethod;
import com.ecommerce.multistore.payment.domain.PaymentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * مستودع المدفوعات
 * Payment Repository
 * 
 * @author Multi-Store Team
 * @version 1.0
 */
@Repository
public interface PaymentRepository extends JpaRepository<Payment, UUID> {
    
    /**
     * البحث بواسطة Display ID
     * Find by display ID
     */
    Optional<Payment> findByDisplayId(String displayId);
    
    /**
     * البحث بواسطة رقم المعاملة
     * Find by transaction ID
     */
    Optional<Payment> findByTransactionId(String transactionId);
    
    /**
     * البحث بواسطة رقم معاملة البوابة
     * Find by gateway transaction ID
     */
    Optional<Payment> findByGatewayTransactionId(String gatewayTransactionId);
    
    /**
     * البحث بواسطة معرف الطلب
     * Find by order ID
     */
    List<Payment> findByOrderId(UUID orderId);
    
    /**
     * البحث بواسطة الحالة
     * Find by status
     */
    Page<Payment> findByStatus(PaymentStatus status, Pageable pageable);
    
    /**
     * البحث بواسطة طريقة الدفع
     * Find by payment method
     */
    Page<Payment> findByPaymentMethod(PaymentMethod paymentMethod, Pageable pageable);
    
    /**
     * البحث بواسطة معرف الطلب والحالة
     * Find by order ID and status
     */
    Optional<Payment> findByOrderIdAndStatus(UUID orderId, PaymentStatus status);
    
    /**
     * البحث في نطاق تاريخي
     * Find by date range
     */
    @Query("SELECT p FROM Payment p WHERE p.createdAt BETWEEN :startDate AND :endDate")
    Page<Payment> findByDateRange(@Param("startDate") LocalDateTime startDate, 
                                 @Param("endDate") LocalDateTime endDate, Pageable pageable);
    
    /**
     * البحث في نطاق مبلغ
     * Find by amount range
     */
    @Query("SELECT p FROM Payment p WHERE p.amount BETWEEN :minAmount AND :maxAmount")
    Page<Payment> findByAmountRange(@Param("minAmount") BigDecimal minAmount, 
                                   @Param("maxAmount") BigDecimal maxAmount, Pageable pageable);
    
    /**
     * إحصائيات المدفوعات حسب الحالة
     * Payment statistics by status
     */
    @Query("SELECT COUNT(p) FROM Payment p WHERE p.status = :status")
    long countByStatus(@Param("status") PaymentStatus status);
    
    /**
     * إحصائيات المدفوعات حسب طريقة الدفع
     * Payment statistics by method
     */
    @Query("SELECT COUNT(p) FROM Payment p WHERE p.paymentMethod = :method")
    long countByPaymentMethod(@Param("method") PaymentMethod method);
    
    /**
     * مجموع المدفوعات الناجحة
     * Sum of successful payments
     */
    @Query("SELECT COALESCE(SUM(p.amount), 0) FROM Payment p WHERE p.status = 'COMPLETED'")
    BigDecimal sumSuccessfulPayments();
    
    /**
     * مجموع المدفوعات خلال فترة
     * Sum of payments in date range
     */
    @Query("SELECT COALESCE(SUM(p.amount), 0) FROM Payment p WHERE p.status = 'COMPLETED' AND p.createdAt BETWEEN :startDate AND :endDate")
    BigDecimal sumPaymentsByDateRange(@Param("startDate") LocalDateTime startDate, 
                                     @Param("endDate") LocalDateTime endDate);
}
