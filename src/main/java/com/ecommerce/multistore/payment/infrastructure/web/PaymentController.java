package com.ecommerce.multistore.payment.infrastructure.web;

import com.ecommerce.multistore.payment.application.dto.*;
import com.ecommerce.multistore.payment.application.service.PaymentService;
import com.ecommerce.multistore.payment.domain.PaymentMethod;
import com.ecommerce.multistore.payment.domain.PaymentStatus;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * وحدة التحكم في المدفوعات - REST API
 * Payment Controller - REST API for payment management
 * 
 * يوفر APIs لإدارة المدفوعات والمحافظ الإلكترونية
 * Provides APIs for payment and e-wallet management
 * 
 * @author Multi-Store Team
 * @version 1.0
 * @since 2024-01-01
 */
@RestController
@RequestMapping("/api/v1/payments")
@CrossOrigin(origins = "*")
public class PaymentController {

    private final PaymentService paymentService;

    /**
     * Constructor لحقن PaymentService
     * Constructor for PaymentService dependency injection
     * 
     * @param paymentService خدمة المدفوعات
     */
    @Autowired
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    /**
     * إنشاء دفع جديد
     * Create a new payment
     * 
     * @param request بيانات الدفع الجديد
     * @return ResponseEntity<PaymentResponse> الدفع المُنشأ أو رسالة خطأ
     * 
     * @apiNote POST /api/v1/payments
     * @since 1.0
     */
    @PostMapping
    public ResponseEntity<PaymentResponse> createPayment(@Valid @RequestBody CreatePaymentRequest request) {
        try {
            PaymentResponse payment = paymentService.createPayment(request);
            return new ResponseEntity<>(payment, HttpStatus.CREATED);
        } catch (IllegalArgumentException | IllegalStateException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * الحصول على جميع المدفوعات مع الصفحات
     * Get all payments with pagination
     * 
     * @param page رقم الصفحة
     * @param size عدد العناصر في الصفحة
     * @param sortBy الحقل المطلوب ترتيبه
     * @param sortDir اتجاه الترتيب
     * @return ResponseEntity<Page<PaymentResponse>> صفحة من المدفوعات
     * 
     * @apiNote GET /api/v1/payments?page=0&size=10&sortBy=createdAt&sortDir=desc
     * @since 1.0
     */
    @GetMapping
    public ResponseEntity<Page<PaymentResponse>> getAllPayments(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        
        Page<PaymentResponse> payments = paymentService.getAllPayments(page, size, sortBy, sortDir);
        return new ResponseEntity<>(payments, HttpStatus.OK);
    }

    /**
     * البحث عن دفع بواسطة UUID
     * Find payment by UUID
     * 
     * @param id المعرف الفريد للدفع
     * @return ResponseEntity<PaymentResponse> الدفع أو 404
     * 
     * @apiNote GET /api/v1/payments/{id}
     * @since 1.0
     */
    @GetMapping("/{id}")
    public ResponseEntity<PaymentResponse> getPaymentById(@PathVariable UUID id) {
        Optional<PaymentResponse> payment = paymentService.findById(id);
        return payment.map(paymentResponse -> new ResponseEntity<>(paymentResponse, HttpStatus.OK))
                     .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * البحث عن دفع بواسطة Display ID
     * Find payment by display ID
     * 
     * @param displayId المعرف المعروض
     * @return ResponseEntity<PaymentResponse> الدفع أو 404
     * 
     * @apiNote GET /api/v1/payments/display/{displayId}
     * @since 1.0
     */
    @GetMapping("/display/{displayId}")
    public ResponseEntity<PaymentResponse> getPaymentByDisplayId(@PathVariable String displayId) {
        Optional<PaymentResponse> payment = paymentService.findByDisplayId(displayId);
        return payment.map(paymentResponse -> new ResponseEntity<>(paymentResponse, HttpStatus.OK))
                     .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * البحث عن دفع بواسطة رقم المعاملة
     * Find payment by transaction ID
     * 
     * @param transactionId رقم المعاملة
     * @return ResponseEntity<PaymentResponse> الدفع أو 404
     * 
     * @apiNote GET /api/v1/payments/transaction/{transactionId}
     * @since 1.0
     */
    @GetMapping("/transaction/{transactionId}")
    public ResponseEntity<PaymentResponse> getPaymentByTransactionId(@PathVariable String transactionId) {
        Optional<PaymentResponse> payment = paymentService.findByTransactionId(transactionId);
        return payment.map(paymentResponse -> new ResponseEntity<>(paymentResponse, HttpStatus.OK))
                     .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * الحصول على مدفوعات الطلب
     * Get order payments
     * 
     * @param orderId معرف الطلب
     * @return ResponseEntity<List<PaymentResponse>> قائمة بمدفوعات الطلب
     * 
     * @apiNote GET /api/v1/payments/order/{orderId}
     * @since 1.0
     */
    @GetMapping("/order/{orderId}")
    public ResponseEntity<List<PaymentResponse>> getOrderPayments(@PathVariable UUID orderId) {
        List<PaymentResponse> payments = paymentService.getOrderPayments(orderId);
        return new ResponseEntity<>(payments, HttpStatus.OK);
    }

    /**
     * البحث بواسطة الحالة
     * Find payments by status
     * 
     * @param status حالة الدفع
     * @param page رقم الصفحة
     * @param size عدد العناصر في الصفحة
     * @return ResponseEntity<Page<PaymentResponse>> صفحة من المدفوعات
     * 
     * @apiNote GET /api/v1/payments/status/{status}?page=0&size=10
     * @since 1.0
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<Page<PaymentResponse>> getPaymentsByStatus(
            @PathVariable PaymentStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Page<PaymentResponse> payments = paymentService.getPaymentsByStatus(status, page, size);
        return new ResponseEntity<>(payments, HttpStatus.OK);
    }

    /**
     * البحث بواسطة طريقة الدفع
     * Find payments by method
     * 
     * @param paymentMethod طريقة الدفع
     * @param page رقم الصفحة
     * @param size عدد العناصر في الصفحة
     * @return ResponseEntity<Page<PaymentResponse>> صفحة من المدفوعات
     * 
     * @apiNote GET /api/v1/payments/method/{paymentMethod}?page=0&size=10
     * @since 1.0
     */
    @GetMapping("/method/{paymentMethod}")
    public ResponseEntity<Page<PaymentResponse>> getPaymentsByMethod(
            @PathVariable PaymentMethod paymentMethod,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Page<PaymentResponse> payments = paymentService.getPaymentsByMethod(paymentMethod, page, size);
        return new ResponseEntity<>(payments, HttpStatus.OK);
    }

    /**
     * تأكيد الدفع
     * Confirm payment
     * 
     * @param id معرف الدفع
     * @param gatewayTransactionId رقم معاملة البوابة
     * @return ResponseEntity<PaymentResponse> الدفع المحدث أو رسالة خطأ
     * 
     * @apiNote PUT /api/v1/payments/{id}/confirm?gatewayTransactionId=GTW123456
     * @since 1.0
     */
    @PutMapping("/{id}/confirm")
    public ResponseEntity<PaymentResponse> confirmPayment(
            @PathVariable UUID id,
            @RequestParam String gatewayTransactionId) {
        try {
            PaymentResponse payment = paymentService.confirmPayment(id, gatewayTransactionId);
            return new ResponseEntity<>(payment, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } catch (IllegalStateException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * رفض الدفع
     * Reject payment
     * 
     * @param id معرف الدفع
     * @param reason سبب الرفض
     * @return ResponseEntity<PaymentResponse> الدفع المحدث أو رسالة خطأ
     * 
     * @apiNote PUT /api/v1/payments/{id}/reject?reason=Insufficient+funds
     * @since 1.0
     */
    @PutMapping("/{id}/reject")
    public ResponseEntity<PaymentResponse> rejectPayment(
            @PathVariable UUID id,
            @RequestParam String reason) {
        try {
            PaymentResponse payment = paymentService.rejectPayment(id, reason);
            return new ResponseEntity<>(payment, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    /**
     * استرداد الدفع
     * Refund payment
     * 
     * @param id معرف الدفع
     * @return ResponseEntity<PaymentResponse> الدفع المحدث أو رسالة خطأ
     * 
     * @apiNote PUT /api/v1/payments/{id}/refund
     * @since 1.0
     */
    @PutMapping("/{id}/refund")
    public ResponseEntity<PaymentResponse> refundPayment(@PathVariable UUID id) {
        try {
            PaymentResponse payment = paymentService.refundPayment(id);
            return new ResponseEntity<>(payment, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } catch (IllegalStateException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * الحصول على إحصائيات المدفوعات
     * Get payment statistics
     * 
     * @return ResponseEntity<PaymentStatisticsResponse> إحصائيات المدفوعات
     * 
     * @apiNote GET /api/v1/payments/statistics
     * @since 1.0
     */
    @GetMapping("/statistics")
    public ResponseEntity<PaymentStatisticsResponse> getPaymentStatistics() {
        PaymentStatisticsResponse statistics = paymentService.getPaymentStatistics();
        return new ResponseEntity<>(statistics, HttpStatus.OK);
    }

    /**
     * فحص صحة نظام المدفوعات
     * Payment system health check
     * 
     * @return ResponseEntity<String> حالة النظام
     * 
     * @apiNote GET /api/v1/payments/health
     * @since 1.0
     */
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return new ResponseEntity<>("Payment Service is running", HttpStatus.OK);
    }
}
