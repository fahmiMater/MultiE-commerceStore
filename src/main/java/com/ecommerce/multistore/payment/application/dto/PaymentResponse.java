package com.ecommerce.multistore.payment.application.dto;

import com.ecommerce.multistore.payment.domain.PaymentMethod;
import com.ecommerce.multistore.payment.domain.PaymentStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * استجابة الدفع
 * Payment response DTO
 * 
 * @author Multi-Store Team
 * @version 1.0
 */
public class PaymentResponse {
    
    private UUID id;
    private String displayId;
    private UUID orderId;
    private PaymentMethod paymentMethod;
    private String paymentGateway;
    private String transactionId;
    private String gatewayTransactionId;
    private BigDecimal amount;
    private String currency;
    private PaymentStatus status;
    private String gatewayResponse;
    private String failureReason;
    private LocalDateTime processedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // معلومات إضافية للواجهة
    private String paymentMethodDisplayName;
    private String statusDisplayName;
    private WalletTransactionResponse walletTransaction;
    
    // Getters and Setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id;    }
    
    public String getDisplayId() { return displayId; }
    public void setDisplayId(String displayId) { this.displayId = displayId; }
    
    public UUID getOrderId() { return orderId; }
    public void setOrderId(UUID orderId) { this.orderId = orderId; }
    
    public PaymentMethod getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(PaymentMethod paymentMethod) { this.paymentMethod = paymentMethod; }
    
    public String getPaymentGateway() { return paymentGateway; }
    public void setPaymentGateway(String paymentGateway) { this.paymentGateway = paymentGateway; }
    
    public String getTransactionId() { return transactionId; }
    public void setTransactionId(String transactionId) { this.transactionId = transactionId; }
    
    public String getGatewayTransactionId() { return gatewayTransactionId; }
    public void setGatewayTransactionId(String gatewayTransactionId) { this.gatewayTransactionId = gatewayTransactionId; }
    
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    
    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
    
    public PaymentStatus getStatus() { return status; }
    public void setStatus(PaymentStatus status) { this.status = status; }
    
    public String getGatewayResponse() { return gatewayResponse; }
    public void setGatewayResponse(String gatewayResponse) { this.gatewayResponse = gatewayResponse; }
    
    public String getFailureReason() { return failureReason; }
    public void setFailureReason(String failureReason) { this.failureReason = failureReason; }
    
    public LocalDateTime getProcessedAt() { return processedAt; }
    public void setProcessedAt(LocalDateTime processedAt) { this.processedAt = processedAt; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    
    public String getPaymentMethodDisplayName() { return paymentMethodDisplayName; }
    public void setPaymentMethodDisplayName(String paymentMethodDisplayName) { this.paymentMethodDisplayName = paymentMethodDisplayName; }
    
    public String getStatusDisplayName() { return statusDisplayName; }
    public void setStatusDisplayName(String statusDisplayName) { this.statusDisplayName = statusDisplayName; }
    
    public WalletTransactionResponse getWalletTransaction() { return walletTransaction; }
    public void setWalletTransaction(WalletTransactionResponse walletTransaction) { this.walletTransaction = walletTransaction; }
}
