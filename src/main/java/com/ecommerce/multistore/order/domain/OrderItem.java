package com.ecommerce.multistore.order.domain;

import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * عنصر الطلب - منتج واحد في الطلب
 * Order Item - Single product in an order
 */
@Entity
@Table(name = "order_items")
public class OrderItem {
    
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;
    
    @Column(name = "display_id", unique = true)
    private String displayId;
    
    @Column(name = "order_id", nullable = false)
    private UUID orderId;
    
    @Column(name = "product_id")
    private UUID productId;
    
    @Column(name = "product_name", nullable = false)
    private String productName;
    
    @Column(name = "product_name_ar")
    private String productNameAr;
    
    @Column(name = "product_sku")
    private String productSku;
    
    @Column(nullable = false)
    private Integer quantity;
    
    @Column(name = "unit_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal unitPrice;
    
    @Column(name = "total_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalPrice;
    
    @Column(columnDefinition = "jsonb")
    private String attributes;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    // Constructors
    public OrderItem() {
        this.createdAt = LocalDateTime.now();
    }
    
    public OrderItem(UUID productId, String productName, Integer quantity, BigDecimal unitPrice) {
        this();
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.totalPrice = unitPrice.multiply(BigDecimal.valueOf(quantity));
    }
    
    // Business Methods
    public void updateQuantity(Integer newQuantity) {
        this.quantity = newQuantity;
        this.totalPrice = this.unitPrice.multiply(BigDecimal.valueOf(newQuantity));
    }
    
    // Getters and Setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    
    public String getDisplayId() { return displayId; }
    public void setDisplayId(String displayId) { this.displayId = displayId; }
    
    public UUID getOrderId() { return orderId; }
    public void setOrderId(UUID orderId) { this.orderId = orderId; }
    
    public UUID getProductId() { return productId; }
    public void setProductId(UUID productId) { this.productId = productId; }
    
    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }
    
    public String getProductNameAr() { return productNameAr; }
    public void setProductNameAr(String productNameAr) { this.productNameAr = productNameAr; }
    
    public String getProductSku() { return productSku; }
    public void setProductSku(String productSku) { this.productSku = productSku; }
    
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    
    public BigDecimal getUnitPrice() { return unitPrice; }
    public void setUnitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; }
    
    public BigDecimal getTotalPrice() { return totalPrice; }
    public void setTotalPrice(BigDecimal totalPrice) { this.totalPrice = totalPrice; }
    
    public String getAttributes() { return attributes; }
    public void setAttributes(String attributes) { this.attributes = attributes; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
