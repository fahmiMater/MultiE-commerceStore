package com.ecommerce.multistore.shared.utils;

import java.util.regex.Pattern;

/**
 * أدوات التحقق من صحة البيانات
 * Validation Utilities
 */
public class ValidationUtils {
    
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"
    );
    
    private static final Pattern PHONE_PATTERN = Pattern.compile(
        "^(\\+967|967|0)?[1-9][0-9]{7,8}$"
    );
    
    private static final Pattern STRONG_PASSWORD_PATTERN = Pattern.compile(
        "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$"
    );
    
    /**
     * التحقق من صحة البريد الإلكتروني
     */
    public static boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        return EMAIL_PATTERN.matcher(email.trim()).matches();
    }
    
    /**
     * التحقق من صحة رقم الهاتف اليمني
     */
    public static boolean isValidYemeniPhone(String phone) {
        if (phone == null || phone.trim().isEmpty()) {
            return false;
        }
        return PHONE_PATTERN.matcher(phone.trim()).matches();
    }
    
    /**
     * التحقق من قوة كلمة المرور
     */
    public static boolean isStrongPassword(String password) {
        if (password == null || password.length() < 8) {
            return false;
        }
        return STRONG_PASSWORD_PATTERN.matcher(password).matches();
    }
    
    /**
     * التحقق من صحة الـ SKU
     */
    public static boolean isValidSku(String sku) {
        if (sku == null || sku.trim().isEmpty()) {
            return false;
        }
        
        String cleaned = sku.trim().toUpperCase();
        return cleaned.matches("^[A-Z0-9-]{3,50}$");
    }
    
    /**
     * التحقق من صحة الرقم الضريبي
     */
    public static boolean isValidTaxNumber(String taxNumber) {
        if (taxNumber == null || taxNumber.trim().isEmpty()) {
            return false;
        }
        
        String cleaned = taxNumber.replaceAll("[^0-9]", "");
        return cleaned.length() >= 9 && cleaned.length() <= 15;
    }
    
    /**
     * تنظيف رقم الهاتف
     */
    public static String normalizePhone(String phone) {
        if (phone == null) {
            return null;
        }
        
        String cleaned = phone.replaceAll("[^0-9+]", "");
        
        if (cleaned.startsWith("+967")) {
            return cleaned;
        } else if (cleaned.startsWith("967")) {
            return "+" + cleaned;
        } else if (cleaned.startsWith("0")) {
            return "+967" + cleaned.substring(1);
        }
        
        return "+967" + cleaned;
    }
    
    /**
     * التحقق من صحة السعر
     */
    public static boolean isValidPrice(Double price) {
        return price != null && price > 0 && price <= 999999.99;
    }
    
    /**
     * التحقق من صحة الكمية
     */
    public static boolean isValidQuantity(Integer quantity) {
        return quantity != null && quantity >= 0 && quantity <= 999999;
    }
    
    /**
     * التحقق من صحة الخصم
     */
    public static boolean isValidDiscount(Double discount) {
        return discount != null && discount >= 0 && discount <= 100;
    }
    
    /**
     * التحقق من صحة الرمز البريدي
     */
    public static boolean isValidPostalCode(String postalCode) {
        if (postalCode == null || postalCode.trim().isEmpty()) {
            return true; // Optional field
        }
        
        String cleaned = postalCode.trim();
        return cleaned.matches("^[0-9]{4,10}$");
    }
    
    /**
     * التحقق من صحة رقم التتبع
     */
    public static boolean isValidTrackingNumber(String trackingNumber) {
        if (trackingNumber == null || trackingNumber.trim().isEmpty()) {
            return false;
        }
        
        String cleaned = trackingNumber.trim().toUpperCase();
        return cleaned.matches("^[A-Z0-9]{6,30}$");
    }
    
    /**
     * التحقق من صحة اللون بصيغة HEX
     */
    public static boolean isValidHexColor(String color) {
        if (color == null || color.trim().isEmpty()) {
            return false;
        }
        
        String cleaned = color.trim();
        return cleaned.matches("^#[0-9A-Fa-f]{6}$");
    }
    
    /**
     * التحقق من صحة الـ URL
     */
    public static boolean isValidUrl(String url) {
        if (url == null || url.trim().isEmpty()) {
            return false;
        }
        
        try {
            new java.net.URL(url);
            return true;
        } catch (java.net.MalformedURLException e) {
            return false;
        }
    }
    
    /**
     * التحقق من صحة JSON
     */
    public static boolean isValidJson(String json) {
        if (json == null || json.trim().isEmpty()) {
            return false;
        }
        
        try {
            com.fasterxml.jackson.databind.ObjectMapper mapper = 
                new com.fasterxml.jackson.databind.ObjectMapper();
            mapper.readTree(json);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
    