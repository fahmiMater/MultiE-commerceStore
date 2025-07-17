package com.ecommerce.multistore.order.infrastructure;

import com.ecommerce.multistore.order.domain.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * مستودع عناصر الطلب
 * Order Item Repository
 */
@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, UUID> {
    
    List<OrderItem> findByOrderId(UUID orderId);
    void deleteByOrderId(UUID orderId);
}
