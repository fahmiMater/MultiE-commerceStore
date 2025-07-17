package com.ecommerce.multistore.payment.domain;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * طرق الدفع المتاحة
 * Available payment methods
 * 
 * @author Multi-Store Team
 * @version 1.0
 */
public enum PaymentMethod {
    JEEB("jeeb"),
    FLOUSI("flousi"),
    MOBILE_MONEY("mobile_money"),
    CASH_ON_DELIVERY("cash_on_delivery"),
    BANK_TRANSFER("bank_transfer");

    private final String value;

    PaymentMethod(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    public static PaymentMethod fromValue(String value) {
        for (PaymentMethod method : PaymentMethod.values()) {
            if (method.value.equals(value)) {
                return method;
            }
        }
        throw new IllegalArgumentException("Unknown payment method: " + value);
    }
    
    /**
     * فحص إذا كانت طريقة الدفع إلكترونية
     * Check if payment method is electronic
     */
    public boolean isElectronic() {
        return this == JEEB || this == FLOUSI || this == MOBILE_MONEY || this == BANK_TRANSFER;
    }
    
    /**
     * فحص إذا كانت طريقة الدفع محفظة إلكترونية
     * Check if payment method is e-wallet
     */
    public boolean isEWallet() {
        return this == JEEB || this == FLOUSI || this == MOBILE_MONEY;
    }
}
