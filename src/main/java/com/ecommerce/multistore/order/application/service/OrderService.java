package com.ecommerce.multistore.order.application.service;

import com.ecommerce.multistore.order.application.dto.*;
import com.ecommerce.multistore.order.domain.*;
import com.ecommerce.multistore.order.infrastructure.OrderRepository;
import com.ecommerce.multistore.order.infrastructure.OrderItemRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
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
 * خدمة إدارة الطلبات
 * تحتوي على جميع العمليات المتعلقة بالطلبات
 * 
 * Order Management Service
 * Contains all order-related operations
 * 
 * @author Multi-Store Team
 * @version 1.0
 * @since 2024-01-01
 */
@Service
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ObjectMapper objectMapper;

    /**
     * Constructor لحقن Dependencies
     * Constructor for dependency injection
     * 
     * @param orderRepository مستودع الطلبات
     * @param orderItemRepository مستودع عناصر الطلبات
     * @param objectMapper محول JSON
     */
    @Autowired
    public OrderService(OrderRepository orderRepository, 
                       OrderItemRepository orderItemRepository,
                       ObjectMapper objectMapper) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.objectMapper = objectMapper;
    }

    /**
     * إنشاء طلب جديد
     * Creates a new order
     * 
     * @param request بيانات الطلب الجديد
     * @return OrderResponse الطلب المُنشأ
     * @throws IllegalArgumentException إذا كانت البيانات غير صحيحة
     */
    public OrderResponse createOrder(CreateOrderRequest request) {
        // التحقق من صحة البيانات
        validateOrderRequest(request);

        // إنشاء كائن الطلب
        Order order = buildOrderFromRequest(request);
        
        // حساب المبالغ
        calculateOrderAmounts(order, request.getItems());
        
        // حفظ الطلب
        Order savedOrder = orderRepository.save(order);
        
        // إنشاء عناصر الطلب
        List<OrderItem> orderItems = createOrderItems(savedOrder.getId(), request.getItems());
        orderItemRepository.saveAll(orderItems);
        
        // تحويل إلى Response وإرجاع النتيجة
        return convertToResponse(savedOrder, orderItems);
    }

    /**
     * البحث عن طلب بواسطة UUID
     * Finds an order by UUID
     * 
     * @param id المعرف الفريد للطلب
     * @return Optional<OrderResponse> الطلب إذا وُجد
     */
    @Transactional(readOnly = true)
    public Optional<OrderResponse> findById(UUID id) {
        return orderRepository.findById(id)
                .map(order -> {
                    List<OrderItem> items = orderItemRepository.findByOrderId(order.getId());
                    return convertToResponse(order, items);
                });
    }

    /**
     * البحث عن طلب بواسطة Display ID
     * Finds an order by display ID
     * 
     * @param displayId المعرف المعروض
     * @return Optional<OrderResponse> الطلب إذا وُجد
     */
    @Transactional(readOnly = true)
    public Optional<OrderResponse> findByDisplayId(String displayId) {
        return orderRepository.findByDisplayId(displayId)
                .map(order -> {
                    List<OrderItem> items = orderItemRepository.findByOrderId(order.getId());
                    return convertToResponse(order, items);
                });
    }

    /**
     * البحث عن طلب بواسطة رقم الطلب
     * Finds an order by order number
     * 
     * @param orderNumber رقم الطلب
     * @return Optional<OrderResponse> الطلب إذا وُجد
     */
    @Transactional(readOnly = true)
    public Optional<OrderResponse> findByOrderNumber(String orderNumber) {
        return orderRepository.findByOrderNumber(orderNumber)
                .map(order -> {
                    List<OrderItem> items = orderItemRepository.findByOrderId(order.getId());
                    return convertToResponse(order, items);
                });
    }

    /**
     * الحصول على جميع الطلبات مع الصفحات
     * Retrieves all orders with pagination
     * 
     * @param page رقم الصفحة
     * @param size عدد العناصر في الصفحة
     * @param sortBy الحقل المطلوب ترتيبه
     * @param sortDir اتجاه الترتيب
     * @return Page<OrderResponse> صفحة من الطلبات
     */
    @Transactional(readOnly = true)
    public Page<OrderResponse> getAllOrders(int page, int size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
                   Sort.by(sortBy).descending() : 
                   Sort.by(sortBy).ascending();
        
        Pageable pageable = PageRequest.of(page, size, sort);
        
        return orderRepository.findAll(pageable)
                .map(order -> {
                    List<OrderItem> items = orderItemRepository.findByOrderId(order.getId());
                    return convertToResponse(order, items);
                });
    }

    /**
     * الحصول على طلبات المستخدم
     * Get user orders
     * 
     * @param userId معرف المستخدم
     * @param page رقم الصفحة
     * @param size عدد العناصر في الصفحة
     * @return Page<OrderResponse> صفحة من طلبات المستخدم
     */
    @Transactional(readOnly = true)
    public Page<OrderResponse> getUserOrders(UUID userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return orderRepository.findByUserId(userId, pageable)
                .map(order -> {
                    List<OrderItem> items = orderItemRepository.findByOrderId(order.getId());
                    return convertToResponse(order, items);
                });
    }

    /**
     * البحث بواسطة الحالة
     * Find orders by status
     * 
     * @param status حالة الطلب
     * @param page رقم الصفحة
     * @param size عدد العناصر في الصفحة
     * @return Page<OrderResponse> صفحة من الطلبات
     */
    @Transactional(readOnly = true)
    public Page<OrderResponse> getOrdersByStatus(OrderStatus status, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return orderRepository.findByStatus(status, pageable)
                .map(order -> {
                    List<OrderItem> items = orderItemRepository.findByOrderId(order.getId());
                    return convertToResponse(order, items);
                });
    }

    /**
     * تحديث حالة الطلب
     * Update order status
     * 
     * @param orderId معرف الطلب
     * @param newStatus الحالة الجديدة
     * @return OrderResponse الطلب المحدث
     * @throws IllegalArgumentException إذا لم يوجد الطلب
     */
    public OrderResponse updateOrderStatus(UUID orderId, OrderStatus newStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found with ID: " + orderId));

        order.setStatus(newStatus);
        order.setUpdatedAt(LocalDateTime.now());

        Order updatedOrder = orderRepository.save(order);
        List<OrderItem> items = orderItemRepository.findByOrderId(order.getId());
        
        return convertToResponse(updatedOrder, items);
    }

    /**
     * تحديث حالة الدفع
     * Update payment status
     * 
     * @param orderId معرف الطلب
     * @param paymentStatus حالة الدفع الجديدة
     * @return OrderResponse الطلب المحدث
     */
    public OrderResponse updatePaymentStatus(UUID orderId, PaymentStatus paymentStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found with ID: " + orderId));

        order.setPaymentStatus(paymentStatus);
        order.setUpdatedAt(LocalDateTime.now());

        // إذا تم الدفع وكانت الحالة pending، قم بتحديثها إلى confirmed
        if (paymentStatus == PaymentStatus.PAID && order.getStatus() == OrderStatus.PENDING) {
            order.setStatus(OrderStatus.CONFIRMED);
        }

        Order updatedOrder = orderRepository.save(order);
        List<OrderItem> items = orderItemRepository.findByOrderId(order.getId());
        
        return convertToResponse(updatedOrder, items);
    }

    /**
     * شحن الطلب
     * Ship order
     * 
     * @param orderId معرف الطلب
     * @param trackingNumber رقم التتبع
     * @return OrderResponse الطلب المحدث
     */
    public OrderResponse shipOrder(UUID orderId, String trackingNumber) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found with ID: " + orderId));

        order.ship(trackingNumber);
        Order updatedOrder = orderRepository.save(order);
        List<OrderItem> items = orderItemRepository.findByOrderId(order.getId());
        
        return convertToResponse(updatedOrder, items);
    }

    /**
     * تسليم الطلب
     * Deliver order
     * 
     * @param orderId معرف الطلب
     * @return OrderResponse الطلب المحدث
     */
    public OrderResponse deliverOrder(UUID orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found with ID: " + orderId));

        order.deliver();
        Order updatedOrder = orderRepository.save(order);
        List<OrderItem> items = orderItemRepository.findByOrderId(order.getId());
        
        return convertToResponse(updatedOrder, items);
    }

    /**
     * إلغاء الطلب
     * Cancel order
     * 
     * @param orderId معرف الطلب
     * @return OrderResponse الطلب المحدث
     */
    public OrderResponse cancelOrder(UUID orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found with ID: " + orderId));

        if (!order.canBeCancelled()) {
            throw new IllegalStateException("Order cannot be cancelled in current state: " + order.getStatus());
        }

        order.setStatus(OrderStatus.CANCELLED);
        order.setUpdatedAt(LocalDateTime.now());

        Order updatedOrder = orderRepository.save(order);
        List<OrderItem> items = orderItemRepository.findByOrderId(order.getId());
        
        return convertToResponse(updatedOrder, items);
    }

    // ===============================
    // Private Helper Methods
    // ===============================

    /**
     * التحقق من صحة طلب الإنشاء
     * Validates create order request
     */
    private void validateOrderRequest(CreateOrderRequest request) {
        if (request.getItems() == null || request.getItems().isEmpty()) {
            throw new IllegalArgumentException("Order must contain at least one item");
        }

        for (OrderItemDto item : request.getItems()) {
            if (item.getQuantity() <= 0) {
                throw new IllegalArgumentException("Item quantity must be greater than 0");
            }
            if (item.getUnitPrice().compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalArgumentException("Item price must be greater than 0");
            }
        }
    }

    /**
     * بناء كائن الطلب من الطلب
     * Builds Order entity from request
     */
    private Order buildOrderFromRequest(CreateOrderRequest request) {
        Order order = new Order();
        order.setUserId(request.getUserId());
        order.setCustomerEmail(request.getCustomerEmail());
        order.setCustomerPhone(request.getCustomerPhone());
        order.setCustomerName(request.getCustomerName());
        order.setShippingMethod(request.getShippingMethod());
        order.setCouponCode(request.getCouponCode());
        order.setNotes(request.getNotes());
        
        // تحويل العناوين إلى JSON
        try {
            order.setShippingAddress(objectMapper.writeValueAsString(request.getShippingAddress()));
            if (request.getBillingAddress() != null) {
                order.setBillingAddress(objectMapper.writeValueAsString(request.getBillingAddress()));
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid address format", e);
        }
        
        return order;
    }

    /**
     * حساب مبالغ الطلب
     * Calculate order amounts
     */
    private void calculateOrderAmounts(Order order, List<OrderItemDto> items) {
        BigDecimal subtotal = items.stream()
                .map(item -> item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        order.setSubtotal(subtotal);
        
        // حساب الضريبة (يمكن تحسينه لاحقاً)
        BigDecimal taxAmount = BigDecimal.ZERO;
        order.setTaxAmount(taxAmount);
        
        // حساب الشحن (يمكن تحسينه لاحقاً)
        BigDecimal shippingAmount = BigDecimal.valueOf(5000); // افتراضي 5000 ريال
        order.setShippingAmount(shippingAmount);
        
        // حساب الخصم (يمكن تحسينه لاحقاً)
        BigDecimal discountAmount = BigDecimal.ZERO;
        order.setDiscountAmount(discountAmount);
        
        // المجموع الكلي
        BigDecimal totalAmount = subtotal
                .add(taxAmount)
                .add(shippingAmount)
                .subtract(discountAmount);
        
        order.setTotalAmount(totalAmount);
    }

    /**
     * إنشاء عناصر الطلب
     * Create order items
     */
    private List<OrderItem> createOrderItems(UUID orderId, List<OrderItemDto> itemDtos) {
        return itemDtos.stream()
                .map(dto -> {
                    OrderItem item = new OrderItem();
                    item.setOrderId(orderId);
                    item.setProductId(dto.getProductId());
                    item.setProductName(dto.getProductName());
                    item.setProductNameAr(dto.getProductNameAr());
                    item.setProductSku(dto.getProductSku());
                    item.setQuantity(dto.getQuantity());
                    item.setUnitPrice(dto.getUnitPrice());
                    item.setTotalPrice(dto.getUnitPrice().multiply(BigDecimal.valueOf(dto.getQuantity())));
                    item.setAttributes(dto.getAttributes());
                    return item;
                })
                .collect(Collectors.toList());
    }

    /**
     * تحويل إلى Response
     * Convert to response
     */
    private OrderResponse convertToResponse(Order order, List<OrderItem> items) {
        OrderResponse response = new OrderResponse();
        response.setId(order.getId());
        response.setDisplayId(order.getDisplayId());
        response.setOrderNumber(order.getOrderNumber());
        response.setUserId(order.getUserId());
        response.setStatus(order.getStatus());
        response.setPaymentStatus(order.getPaymentStatus());
        response.setCustomerEmail(order.getCustomerEmail());
        response.setCustomerPhone(order.getCustomerPhone());
        response.setCustomerName(order.getCustomerName());
        response.setShippingAddress(order.getShippingAddress());
        response.setBillingAddress(order.getBillingAddress());
        response.setSubtotal(order.getSubtotal());
        response.setTaxAmount(order.getTaxAmount());
        response.setShippingAmount(order.getShippingAmount());
        response.setDiscountAmount(order.getDiscountAmount());
        response.setTotalAmount(order.getTotalAmount());
        response.setShippingMethod(order.getShippingMethod());
        response.setTrackingNumber(order.getTrackingNumber());
        response.setShippedAt(order.getShippedAt());
        response.setDeliveredAt(order.getDeliveredAt());
        response.setNotes(order.getNotes());
        response.setCouponCode(order.getCouponCode());
        response.setCurrency(order.getCurrency());
        response.setOnexQuoteId(order.getOnexQuoteId());
        response.setOnexInvoiceId(order.getOnexInvoiceId());
        response.setOnexStatus(order.getOnexStatus());
        response.setOnexSyncedAt(order.getOnexSyncedAt());
        response.setCreatedAt(order.getCreatedAt());
        response.setUpdatedAt(order.getUpdatedAt());
        
        // تحويل عناصر الطلب
        List<OrderItemResponse> itemResponses = items.stream()
                .map(this::convertItemToResponse)
                .collect(Collectors.toList());
        response.setItems(itemResponses);
        
        return response;
    }

    /**
     * تحويل عنصر الطلب إلى Response
     * Convert order item to response
     */
    private OrderItemResponse convertItemToResponse(OrderItem item) {
        OrderItemResponse response = new OrderItemResponse();
        response.setId(item.getId());
        response.setDisplayId(item.getDisplayId());
        response.setProductId(item.getProductId());
        response.setProductName(item.getProductName());
        response.setProductNameAr(item.getProductNameAr());
        response.setProductSku(item.getProductSku());
        response.setQuantity(item.getQuantity());
        response.setUnitPrice(item.getUnitPrice());
        response.setTotalPrice(item.getTotalPrice());
        response.setAttributes(item.getAttributes());
        response.setCreatedAt(item.getCreatedAt());
        return response;
    }
}
