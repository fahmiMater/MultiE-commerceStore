package com.ecommerce.multistore.payment.domain;

import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * معاملة المحفظة الإلكترونية
 * Wallet Transaction Entity
 * 
 * @author Multi-Store Team
 * @version 1.0
 */
@Entity
@Table(name = "wallet_transactions")
public class WalletTransaction {
    
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;
    
    @Column(name = "display_id", unique = true)
    private String displayId;
    
    @Column(name = "payment_id", nullable = false)
    private UUID paymentId;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "wallet_type", nullable = false)
    private WalletType walletType;
    
    @Column(name = "wallet_phone", nullable = false)
    private String walletPhone;
    
    @Column(name = "transaction_reference")
    private String transactionReference;
    
    @Column(name = "wallet_transaction_id")
    private String walletTransactionId;
    
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;
    
    @Column(precision = 10, scale = 2)
    private BigDecimal fees = BigDecimal.ZERO;
    
    @Enumerated(EnumType.STRING)
    private PaymentStatus status = PaymentStatus.PENDING;
    
    @Column(name = "request_payload", columnDefinition = "jsonb")
    private String requestPayload;
    
    @Column(name = "response_payload", columnDefinition = "jsonb")
    private String responsePayload;
    
    @Column(name = "error_message", columnDefinition = "TEXT")
    private String errorMessage;
    
    @Column(name = "processed_at")
    private LocalDateTime processedAt;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    // Constructors
    public WalletTransaction() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    public WalletTransaction(UUID paymentId, WalletType walletType, String walletPhone, BigDecimal amount) {
        this();
        this.paymentId = paymentId;
        this.walletType = walletType;
        this.walletPhone = walletPhone;
        this.amount = amount;
    }
    
    // Business Methods
    public void markAsCompleted(String walletTransactionId, String responsePayload) {
        this.status = PaymentStatus.COMPLETED;
        this.walletTransactionId = walletTransactionId;
        this.responsePayload = responsePayload;
        this.processedAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    public void markAsFailed(String errorMessage) {
        this.status = PaymentStatus.FAILED;
        this.errorMessage = errorMessage;
        this.processedAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
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
    
    public String getRequestPayload() { return requestPayload; }
    public void setRequestPayload(String requestPayload) { this.requestPayload = requestPayload; }
    
    public String getResponsePayload() { return responsePayload; }
    public void setResponsePayload(String responsePayload) { this.responsePayload = responsePayload; }
    
    public String getErrorMessage() { return errorMessage; }
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }
    
    public LocalDateTime getProcessedAt() { return processedAt; }
    public void setProcessedAt(LocalDateTime processedAt) { this.processedAt = processedAt; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
