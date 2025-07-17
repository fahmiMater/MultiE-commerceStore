package com.ecommerce.multistore.payment.domain;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * حالات الدفع
 * Payment status enum
 * 
 * @author Multi-Store Team
 * @version 1.0
 */
public enum PaymentStatus {
    PENDING("pending"),
    PROCESSING("processing"),
    COMPLETED("completed"),
    FAILED("failed"),
    CANCELLED("cancelled"),
    REFUNDED("refunded");

    private final String value;

    PaymentStatus(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    public static PaymentStatus fromValue(String value) {
        for (PaymentStatus status : PaymentStatus.values()) {
            if (status.value.equals(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown payment status: " + value);
    }
    
    /**
     * فحص إذا كانت الحالة نهائية
     * Check if status is final
     */
    public boolean isFinal() {
        return this == COMPLETED || this == FAILED || this == CANCELLED || this == REFUNDED;
    }
    
    /**
     * فحص إذا كانت الحالة ناجحة
     * Check if status is successful
     */
    public boolean isSuccessful() {
        return this == COMPLETED;
    }
}
