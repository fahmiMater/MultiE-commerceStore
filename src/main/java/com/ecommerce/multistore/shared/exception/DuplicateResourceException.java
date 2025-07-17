package com.ecommerce.multistore.shared.exception;

/**
 * استثناء تكرار المورد
 * Duplicate resource exception
 * 
 * يُستخدم عندما يحاول المستخدم إنشاء مورد موجود بالفعل
 * Used when user attempts to create a resource that already exists
 * 
 * @author Multi-Store Team
 * @version 1.0
 * @since 2024-01-01
 */
public class DuplicateResourceException extends RuntimeException {
    
    /**
     * رسالة الخطأ بالعربية
     * Error message in Arabic
     */
    private String messageAr;
    
    /**
     * نوع المورد المكرر
     * Type of duplicate resource
     */
    private String resourceType;
    
    /**
     * القيمة المكررة
     * Duplicate value
     */
    private Object duplicateValue;
    
    /**
     * الحقل المكرر
     * Duplicate field
     */
    private String duplicateField;
    
    // ===============================
    // Constructors
    // ===============================
    
    /**
     * Constructor مع رسالة فقط
     * Constructor with message only
     * 
     * @param message رسالة الخطأ
     */
    public DuplicateResourceException(String message) {
        super(message);
    }
    
    /**
     * Constructor مع رسالة ورسالة عربية
     * Constructor with message and Arabic message
     * 
     * @param message رسالة الخطأ
     * @param messageAr رسالة الخطأ بالعربية
     */
    public DuplicateResourceException(String message, String messageAr) {
        super(message);
        this.messageAr = messageAr;
    }
    
    /**
     * Constructor مع تفاصيل التكرار
     * Constructor with duplication details
     * 
     * @param resourceType نوع المورد
     * @param duplicateField الحقل المكرر
     * @param duplicateValue القيمة المكررة
     */
    public DuplicateResourceException(String resourceType, String duplicateField, Object duplicateValue) {
        super(String.format("%s already exists with %s: %s", resourceType, duplicateField, duplicateValue));
        this.resourceType = resourceType;
        this.duplicateField = duplicateField;
        this.duplicateValue = duplicateValue;
        this.messageAr = String.format("%s موجود بالفعل بـ %s: %s", 
                                     translateResourceType(resourceType), 
                                     translateField(duplicateField), 
                                     duplicateValue);
    }
    
    // ===============================
    // Static Factory Methods
    // ===============================
    
    /**
     * إنشاء استثناء تكرار SKU
     * Create duplicate SKU exception
     * 
     * @param sku رمز المنتج المكرر
     * @return DuplicateResourceException
     */
    public static DuplicateResourceException sku(String sku) {
        return new DuplicateResourceException("Product", "SKU", sku);
    }
    
    /**
     * إنشاء استثناء تكرار البريد الإلكتروني
     * Create duplicate email exception
     * 
     * @param email البريد الإلكتروني المكرر
     * @return DuplicateResourceException
     */
    public static DuplicateResourceException email(String email) {
        return new DuplicateResourceException("User", "email", email);
    }
    
    /**
     * إنشاء استثناء تكرار رقم الهاتف
     * Create duplicate phone exception
     * 
     * @param phone رقم الهاتف المكرر
     * @return DuplicateResourceException
     */
    public static DuplicateResourceException phone(String phone) {
        return new DuplicateResourceException("User", "phone", phone);
    }
    
    /**
     * إنشاء استثناء تكرار الرابط الودود
     * Create duplicate slug exception
     * 
     * @param slug الرابط الودود المكرر
     * @return DuplicateResourceException
     */
    public static DuplicateResourceException slug(String slug) {
        return new DuplicateResourceException("Resource", "slug", slug);
    }
    
    // ===============================
    // Helper Methods
    // ===============================
    
    /**
     * ترجمة نوع المورد إلى العربية
     * Translate resource type to Arabic
     * 
     * @param resourceType نوع المورد
     * @return String الترجمة العربية
     */
    private String translateResourceType(String resourceType) {
        switch (resourceType.toLowerCase()) {
            case "product": return "المنتج";
            case "user": return "المستخدم";
            case "category": return "الفئة";
            case "brand": return "العلامة التجارية";
            case "resource": return "المورد";
            default: return resourceType;
        }
    }
    
    /**
     * ترجمة اسم الحقل إلى العربية
     * Translate field name to Arabic
     * 
     * @param field اسم الحقل
     * @return String الترجمة العربية
     */
    private String translateField(String field) {
        switch (field.toLowerCase()) {
            case "sku": return "رمز المنتج";
            case "email": return "البريد الإلكتروني";
            case "phone": return "رقم الهاتف";
            case "slug": return "الرابط الودود";
            case "name": return "الاسم";
            default: return field;
        }
    }
    
    // ===============================
    // Getters and Setters
    // ===============================
    
    public String getMessageAr() { return messageAr; }
    public void setMessageAr(String messageAr) { this.messageAr = messageAr; }
    
    public String getResourceType() { return resourceType; }
    public void setResourceType(String resourceType) { this.resourceType = resourceType; }
    
    public Object getDuplicateValue() { return duplicateValue; }
    public void setDuplicateValue(Object duplicateValue) { this.duplicateValue = duplicateValue; }
    
    public String getDuplicateField() { return duplicateField; }
    public void setDuplicateField(String duplicateField) { this.duplicateField = duplicateField; }
}
