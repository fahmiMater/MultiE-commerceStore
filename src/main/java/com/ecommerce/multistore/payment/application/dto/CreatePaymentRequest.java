package com.ecommerce.multistore.payment.application.dto;

import com.ecommerce.multistore.payment.domain.PaymentMethod;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.util.UUID;

/**
 * طلب إنشاء دفع جديد
 * Create payment request DTO
 * 
 * @author Multi-Store Team
 * @version 1.0
 */
public class CreatePaymentRequest {
    
    @NotNull(message = "Order ID is required")
    private UUID orderId;
    
    @NotNull(message = "Payment method is required")
    private PaymentMethod paymentMethod;
    
    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Amount must be greater than 0")
    @Digits(integer = 8, fraction = 2, message = "Amount format is invalid")
    private BigDecimal amount;
    
    @Size(max = 3, message = "Currency code cannot exceed 3 characters")
    private String currency = "YER";
    
    // للمحافظ الإلكترونية
    private String walletPhone;
    
    // للدفع النقدي عند التسليم
    private String deliveryAddress;
    
    // للتحويل البنكي
    private String bankReference;
    
    // معلومات إضافية
    private String notes;
    
    // Constructors
    public CreatePaymentRequest() {}
    
    public CreatePaymentRequest(UUID orderId, PaymentMethod paymentMethod, BigDecimal amount) {
        this.orderId = orderId;
        this.paymentMethod = paymentMethod;
        this.amount = amount;
    }
    
    // Getters and Setters
    public UUID getOrderId() { return orderId; }
    public void setOrderId(UUID orderId) { this.orderId = orderId; }
    
    public PaymentMethod getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(PaymentMethod paymentMethod) { this.paymentMethod = paymentMethod; }
    
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    
    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
    
    public String getWalletPhone() { return walletPhone; }
    public void setWalletPhone(String walletPhone) { this.walletPhone = walletPhone; }
    
    public String getDeliveryAddress() { return deliveryAddress; }
    public void setDeliveryAddress(String deliveryAddress) { this.deliveryAddress = deliveryAddress; }
    
    public String getBankReference() { return bankReference; }
    public void setBankReference(String bankReference) { this.bankReference = bankReference; }
    
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}
