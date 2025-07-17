package com.ecommerce.multistore.user.domain;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * أدوار المستخدمين في النظام
 * User roles in the system
 * 
 * @author Multi-Store Team
 * @version 1.0
 */
public enum UserRole {
    /**
     * عميل عادي
     * Regular customer
     */
    CUSTOMER("customer"),
    
    /**
     * مدير
     * Administrator
     */
    ADMIN("admin"),
    
    /**
     * مدير عام
     * Super administrator
     */
    SUPER_ADMIN("super_admin"),
    
    /**
     * تاجر
     * Merchant
     */
    MERCHANT("merchant");

    private final String value;

    /**
     * Constructor للإنشاء بالقيمة
     * Constructor with value
     * 
     * @param value القيمة المخزنة في قاعدة البيانات
     */
    UserRole(String value) {
        this.value = value;
    }

    /**
     * الحصول على القيمة المخزنة في قاعدة البيانات
     * Get the database value
     * 
     * @return String القيمة
     */
    @JsonValue
    public String getValue() {
        return value;
    }

    /**
     * تحويل من String إلى Enum
     * Convert from String to Enum
     * 
     * @param value القيمة النصية
     * @return UserRole الدور المقابل
     */
    public static UserRole fromValue(String value) {
        for (UserRole role : UserRole.values()) {
            if (role.value.equals(value)) {
                return role;
            }
        }
        throw new IllegalArgumentException("Unknown role: " + value);
    }
}
