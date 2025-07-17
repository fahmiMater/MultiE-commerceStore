package com.ecommerce.multistore.payment.application.dto;

import com.ecommerce.multistore.payment.domain.WalletType;
import com.ecommerce.multistore.payment.domain.PaymentStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * استجابة معاملة المحفظة الإلكترونية
 * Wallet transaction response DTO
 * 
 * @author Multi-Store Team
 * @version 1.0
 */
public class WalletTransactionResponse {
    
    private UUID id;
    private String displayId;
    private UUID paymentId;
    private WalletType walletType;
    private String walletPhone;
    private String transactionReference;
    private String walletTransactionId;
    private BigDecimal amount;
    private BigDecimal fees;
    private PaymentStatus status;
    private String errorMessage;
    private LocalDateTime processedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // معلومات إضافية للواجهة
    private String walletTypeDisplayName;
    private String statusDisplayName;
    
    // Getters and Setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    
    public String getDisplayId() { return displayId; }
    public void setDisplayId(String displayId) { this.displayId = displayId; }
    
    public UUID getPaymentId() { return paymentId; }
    public void setPaymentId(UUID paymentId) { this.paymentId = paymentId; }
    
    public WalletType getWalletType() { return walletType; }
    public void setWalletType(WalletType walletType) { this.walletType = walletType; }
    
    public String getWalletPhone() { return walletPhone; }
    public void setWalletPhone(String walletPhone) { this.walletPhone = walletPhone; }
    
    public String getTransactionReference() { return transactionReference; }
    public void setTransactionReference(String transactionReference) { this.transactionReference = transactionReference; }
    
    public String getWalletTransactionId() { return walletTransactionId; }
    public void setWalletTransactionId(String walletTransactionId) { this.walletTransactionId = walletTransactionId; }
    
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    
    public BigDecimal getFees() { return fees; }
    public void setFees(BigDecimal fees) { this.fees = fees; }
    
    public PaymentStatus getStatus() { return status; }
    public void setStatus(PaymentStatus status) { this.status = status; }
    
    public String getErrorMessage() { return errorMessage; }
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }
    
    public LocalDateTime getProcessedAt() { return processedAt; }
    public void setProcessedAt(LocalDateTime processedAt) { this.processedAt = processedAt; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    
    public String getWalletTypeDisplayName() { return walletTypeDisplayName; }
    public void setWalletTypeDisplayName(String walletTypeDisplayName) { this.walletTypeDisplayName = walletTypeDisplayName; }
    
    public String getStatusDisplayName() { return statusDisplayName; }
    public void setStatusDisplayName(String statusDisplayName) { this.statusDisplayName = statusDisplayName; }
}
