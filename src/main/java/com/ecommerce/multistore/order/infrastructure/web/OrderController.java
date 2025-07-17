package com.ecommerce.multistore.order.infrastructure.web;

import com.ecommerce.multistore.order.application.dto.CreateOrderRequest;
import com.ecommerce.multistore.order.application.dto.OrderResponse;
import com.ecommerce.multistore.order.application.service.OrderService;
import com.ecommerce.multistore.order.domain.OrderStatus;
import com.ecommerce.multistore.order.domain.PaymentStatus;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

/**
 * وحدة التحكم في الطلبات - REST API
 * Order Controller - REST API for order management
 * 
 * @author Multi-Store Team
 * @version 1.0
 * @since 2024-01-01
 */
@RestController
@RequestMapping("/api/v1/orders")
@CrossOrigin(origins = "*")
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    /**
     * إنشاء طلب جديد
     * Create a new order
     * 
     * @param request بيانات الطلب الجديد
     * @return ResponseEntity<OrderResponse> الطلب المُنشأ أو رسالة خطأ
     * 
     * @apiNote POST /api/v1/orders
     * @since 1.0
     */
    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@Valid @RequestBody CreateOrderRequest request) {
        try {
            OrderResponse order = orderService.createOrder(request);
            return new ResponseEntity<>(order, HttpStatus.CREATED);
        } catch (IllegalArgumentException | IllegalStateException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * الحصول على جميع الطلبات مع الصفحات
     * Get all orders with pagination
     * 
     * @param page رقم الصفحة
     * @param size عدد العناصر في الصفحة
     * @param sortBy الحقل المطلوب ترتيبه
     * @param sortDir اتجاه الترتيب
     * @return ResponseEntity<Page<OrderResponse>> صفحة من الطلبات
     * 
     * @apiNote GET /api/v1/orders?page=0&size=10&sortBy=createdAt&sortDir=desc
     * @since 1.0
     */
    @GetMapping
    public ResponseEntity<Page<OrderResponse>> getAllOrders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        
        Page<OrderResponse> orders = orderService.getAllOrders(page, size, sortBy, sortDir);
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    /**
     * البحث عن طلب بواسطة UUID
     * Find order by UUID
     * 
     * @param id المعرف الفريد للطلب
     * @return ResponseEntity<OrderResponse> الطلب أو 404
     * 
     * @apiNote GET /api/v1/orders/{id}
     * @since 1.0
     */
    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getOrderById(@PathVariable UUID id) {
        Optional<OrderResponse> order = orderService.findById(id);
        return order.map(orderResponse -> new ResponseEntity<>(orderResponse, HttpStatus.OK))
                   .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * البحث عن طلب بواسطة Display ID
     * Find order by display ID
     * 
     * @param displayId المعرف المعروض
     * @return ResponseEntity<OrderResponse> الطلب أو 404
     * 
     * @apiNote GET /api/v1/orders/display/{displayId}
     * @since 1.0
     */
    @GetMapping("/display/{displayId}")
    public ResponseEntity<OrderResponse> getOrderByDisplayId(@PathVariable String displayId) {
        Optional<OrderResponse> order = orderService.findByDisplayId(displayId);
        return order.map(orderResponse -> new ResponseEntity<>(orderResponse, HttpStatus.OK))
                   .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * البحث عن طلب بواسطة رقم الطلب
     * Find order by order number
     * 
     * @param orderNumber رقم الطلب
     * @return ResponseEntity<OrderResponse> الطلب أو 404
     * 
     * @apiNote GET /api/v1/orders/number/{orderNumber}
     * @since 1.0
     */
    @GetMapping("/number/{orderNumber}")
    public ResponseEntity<OrderResponse> getOrderByNumber(@PathVariable String orderNumber) {
        Optional<OrderResponse> order = orderService.findByOrderNumber(orderNumber);
        return order.map(orderResponse -> new ResponseEntity<>(orderResponse, HttpStatus.OK))
                   .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * الحصول على طلبات المستخدم
     * Get user orders
     * 
     * @param userId معرف المستخدم
     * @param page رقم الصفحة
     * @param size عدد العناصر في الصفحة
     * @return ResponseEntity<Page<OrderResponse>> صفحة من طلبات المستخدم
     * 
     * @apiNote GET /api/v1/orders/user/{userId}?page=0&size=10
     * @since 1.0
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<Page<OrderResponse>> getUserOrders(
            @PathVariable UUID userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Page<OrderResponse> orders = orderService.getUserOrders(userId, page, size);
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    /**
     * البحث بواسطة الحالة
     * Find orders by status
     * 
     * @param status حالة الطلب
     * @param page رقم الصفحة
     * @param size عدد العناصر في الصفحة
     * @return ResponseEntity     * @return ResponseEntity<Page<OrderResponse>> صفحة من الطلبات
     * 
     * @apiNote GET /api/v1/orders/status/{status}?page=0&size=10
     * @since 1.0
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<Page<OrderResponse>> getOrdersByStatus(
            @PathVariable OrderStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Page<OrderResponse> orders = orderService.getOrdersByStatus(status, page, size);
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    /**
     * تحديث حالة الطلب
     * Update order status
     * 
     * @param id معرف الطلب
     * @param status الحالة الجديدة
     * @return ResponseEntity<OrderResponse> الطلب المحدث أو رسالة خطأ
     * 
     * @apiNote PUT /api/v1/orders/{id}/status?status=confirmed
     * @since 1.0
     */
    @PutMapping("/{id}/status")
    public ResponseEntity<OrderResponse> updateOrderStatus(
            @PathVariable UUID id,
            @RequestParam OrderStatus status) {
        try {
            OrderResponse order = orderService.updateOrderStatus(id, status);
            return new ResponseEntity<>(order, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } catch (IllegalStateException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * تحديث حالة الدفع
     * Update payment status
     * 
     * @param id معرف الطلب
     * @param paymentStatus حالة الدفع الجديدة
     * @return ResponseEntity<OrderResponse> الطلب المحدث أو رسالة خطأ
     * 
     * @apiNote PUT /api/v1/orders/{id}/payment-status?paymentStatus=paid
     * @since 1.0
     */
    @PutMapping("/{id}/payment-status")
    public ResponseEntity<OrderResponse> updatePaymentStatus(
            @PathVariable UUID id,
            @RequestParam PaymentStatus paymentStatus) {
        try {
            OrderResponse order = orderService.updatePaymentStatus(id, paymentStatus);
            return new ResponseEntity<>(order, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    /**
     * شحن الطلب
     * Ship order
     * 
     * @param id معرف الطلب
     * @param trackingNumber رقم التتبع
     * @return ResponseEntity<OrderResponse> الطلب المحدث أو رسالة خطأ
     * 
     * @apiNote PUT /api/v1/orders/{id}/ship?trackingNumber=TRK123456
     * @since 1.0
     */
    @PutMapping("/{id}/ship")
    public ResponseEntity<OrderResponse> shipOrder(
            @PathVariable UUID id,
            @RequestParam String trackingNumber) {
        try {
            OrderResponse order = orderService.shipOrder(id, trackingNumber);
            return new ResponseEntity<>(order, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } catch (IllegalStateException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * تسليم الطلب
     * Deliver order
     * 
     * @param id معرف الطلب
     * @return ResponseEntity<OrderResponse> الطلب المحدث أو رسالة خطأ
     * 
     * @apiNote PUT /api/v1/orders/{id}/deliver
     * @since 1.0
     */
    @PutMapping("/{id}/deliver")
    public ResponseEntity<OrderResponse> deliverOrder(@PathVariable UUID id) {
        try {
            OrderResponse order = orderService.deliverOrder(id);
            return new ResponseEntity<>(order, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } catch (IllegalStateException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * إلغاء الطلب
     * Cancel order
     * 
     * @param id معرف الطلب
     * @return ResponseEntity<OrderResponse> الطلب المحدث أو رسالة خطأ
     * 
     * @apiNote PUT /api/v1/orders/{id}/cancel
     * @since 1.0
     */
    @PutMapping("/{id}/cancel")
    public ResponseEntity<OrderResponse> cancelOrder(@PathVariable UUID id) {
        try {
            OrderResponse order = orderService.cancelOrder(id);
            return new ResponseEntity<>(order, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } catch (IllegalStateException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * فحص صحة نظام الطلبات
     * Order system health check
     * 
     * @return ResponseEntity<String> حالة النظام
     * 
     * @apiNote GET /api/v1/orders/health
     * @since 1.0
     */
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return new ResponseEntity<>("Order Service is running", HttpStatus.OK);
    }
}
