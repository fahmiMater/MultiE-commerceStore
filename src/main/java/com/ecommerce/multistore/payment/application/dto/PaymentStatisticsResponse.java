package com.ecommerce.multistore.payment.application.dto;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * إحصائيات المدفوعات
 * Payment statistics response DTO
 * 
 * @author Multi-Store Team
 * @version 1.0
 */
public class PaymentStatisticsResponse {
    
    private long totalPayments;
    private long successfulPayments;
    private long failedPayments;
    private long pendingPayments;
    private BigDecimal totalAmount;
    private BigDecimal todayAmount;
    private Map<String, Long> methodStatistics;
    
    public PaymentStatisticsResponse() {
        this.methodStatistics = new HashMap<>();
        this.totalAmount = BigDecimal.ZERO;
        this.todayAmount = BigDecimal.ZERO;
    }
    
    // Helper method
    public void addMethodStatistic(String method, Long count) {
        this.methodStatistics.put(method, count);
    }
    
    // Getters and Setters
    public long getTotalPayments() { return totalPayments; }
    public void setTotalPayments(long totalPayments) { this.totalPayments = totalPayments; }
    
    public long getSuccessfulPayments() { return successfulPayments; }
    public void setSuccessfulPayments(long successfulPayments) { this.successfulPayments = successfulPayments; }
    
    public long getFailedPayments() { return failedPayments; }
    public void setFailedPayments(long failedPayments) { this.failedPayments = failedPayments; }
    
    public long getPendingPayments() { return pendingPayments; }
    public void setPendingPayments(long pendingPayments) { this.pendingPayments = pendingPayments; }
    
    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }
    
    public BigDecimal getTodayAmount() { return todayAmount; }
    public void setTodayAmount(BigDecimal todayAmount) { this.todayAmount = todayAmount; }
    
    public Map<String, Long> getMethodStatistics() { return methodStatistics; }
    public void setMethodStatistics(Map<String, Long> methodStatistics) { this.methodStatistics = methodStatistics; }
}
