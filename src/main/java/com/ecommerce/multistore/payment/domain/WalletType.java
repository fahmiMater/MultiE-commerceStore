package com.ecommerce.multistore.payment.domain;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * أنواع المحافظ الإلكترونية اليمنية
 * Yemeni E-Wallet Types
 * 
 * @author Multi-Store Team
 * @version 1.0
 */
public enum WalletType {
    JEEB("jeeb"),
    FLOUSI("flousi"),
    MOBILE_MONEY("mobile_money");

    private final String value;

    WalletType(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    public static WalletType fromValue(String value) {
        for (WalletType type : WalletType.values()) {
            if (type.value.equals(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown wallet type: " + value);
    }
    
    /**
     * الحصول على اسم المحفظة باللغة العربية
     * Get wallet name in Arabic
     */
    public String getArabicName() {
        switch (this) {
            case JEEB: return "جيب";
            case FLOUSI: return "فلوسي";
            case MOBILE_MONEY: return "موبايل موني";
            default: return this.value;
        }
    }
    
    /**
     * التحقق من أرقام الهواتف المدعومة
     * Check if phone number is supported
     */
    public boolean supportsPhoneNumber(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.length() < 8) {
            return false;
        }
        
        // إزالة الأرقام غير الرقمية
        String cleanPhone = phoneNumber.replaceAll("[^0-9]", "");
        
        switch (this) {
            case JEEB:
                return cleanPhone.startsWith("77") && cleanPhone.length() == 9;
            case FLOUSI:
                return cleanPhone.startsWith("73") && cleanPhone.length() == 9;
            case MOBILE_MONEY:
                return (cleanPhone.startsWith("70") || cleanPhone.startsWith("71") || cleanPhone.startsWith("78")) 
                       && cleanPhone.length() == 9;
            default:
                return false;
        }
    }
}
