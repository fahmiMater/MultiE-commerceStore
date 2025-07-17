package com.ecommerce.multistore.order.infrastructure;

import com.ecommerce.multistore.order.domain.Order;
import com.ecommerce.multistore.order.domain.OrderStatus;
import com.ecommerce.multistore.order.domain.PaymentStatus;
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
 * مستودع الطلبات
 * Order Repository
 */
@Repository
public interface OrderRepository extends JpaRepository<Order, UUID> {
    
    Optional<Order> findByDisplayId(String displayId);
    Optional<Order> findByOrderNumber(String orderNumber);
    Page<Order> findByUserId(UUID userId, Pageable pageable);
    Page<Order> findByStatus(OrderStatus status, Pageable pageable);
    Page<Order> findByPaymentStatus(PaymentStatus paymentStatus, Pageable pageable);
    Page<Order> findByUserIdAndStatus(UUID userId, OrderStatus status, Pageable pageable);
    
    @Query("SELECT o FROM Order o WHERE o.customerEmail = :email ORDER BY o.createdAt DESC")
    Page<Order> findByCustomerEmail(@Param("email") String email, Pageable pageable);
    
    @Query("SELECT o FROM Order o WHERE o.createdAt BETWEEN :startDate AND :endDate")
    Page<Order> findByDateRange(@Param("startDate") LocalDateTime startDate, 
                               @Param("endDate") LocalDateTime endDate, Pageable pageable);
    
    List<Order> findByStatusAndShippedAtIsNull(OrderStatus status);
    
    @Query("SELECT COUNT(o) FROM Order o WHERE o.status = :status")
    long countByStatus(@Param("status") OrderStatus status);
    
    @Query("SELECT COUNT(o) FROM Order o WHERE o.paymentStatus = :paymentStatus")
    long countByPaymentStatus(@Param("paymentStatus") PaymentStatus paymentStatus);
}
