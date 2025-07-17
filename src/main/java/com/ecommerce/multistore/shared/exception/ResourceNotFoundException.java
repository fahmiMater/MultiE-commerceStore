package com.ecommerce.multistore.shared.exception;

/**
 * استثناء المورد غير الموجود
 * Resource not found exception
 * 
 * يُستخدم عندما لا يتم العثور على مورد مطلوب
 * Used when a requested resource is not found
 * 
 * @author Multi-Store Team
 * @version 1.0
 * @since 2024-01-01
 */
public class ResourceNotFoundException extends RuntimeException {
    
    /**
     * رسالة الخطأ بالعربية
     * Error message in Arabic
     */
    private String messageAr;
    
    /**
     * نوع المورد غير الموجود
     * Type of resource not found
     */
    private String resourceType;
    
    /**
     * معرف المورد غير الموجود
     * ID of resource not found
     */
    private Object resourceId;
    
    // ===============================
    // Constructors
    // ===============================
    
    /**
     * Constructor مع رسالة فقط
     * Constructor with message only
     * 
     * @param message رسالة الخطأ
     */
    public ResourceNotFoundException(String message) {
        super(message);
    }
    
    /**
     * Constructor مع رسالة ورسالة عربية
     * Constructor with message and Arabic message
     * 
     * @param message رسالة الخطأ
     * @param messageAr رسالة الخطأ بالعربية
     */
    public ResourceNotFoundException(String message, String messageAr) {
        super(message);
        this.messageAr = messageAr;
    }
    
    /**
     * Constructor مع نوع المورد ومعرفه
     * Constructor with resource type and ID
     * 
     * @param resourceType نوع المورد
     * @param resourceId معرف المورد
     */
    public ResourceNotFoundException(String resourceType, Object resourceId) {
        super(String.format("%s not found with ID: %s", resourceType, resourceId));
        this.resourceType = resourceType;
        this.resourceId = resourceId;
        this.messageAr = String.format("%s غير موجود بالمعرف: %s", 
                                     translateResourceType(resourceType), resourceId);
    }
    
    /**
     * Constructor كامل
     * Full constructor
     * 
     * @param message رسالة الخطأ
     * @param messageAr رسالة الخطأ بالعربية
     * @param resourceType نوع المورد
     * @param resourceId معرف المورد
     */
    public ResourceNotFoundException(String message, String messageAr, 
                                   String resourceType, Object resourceId) {
        super(message);
        this.messageAr = messageAr;
        this.resourceType = resourceType;
        this.resourceId = resourceId;
    }
    
    // ===============================
    // Static Factory Methods
    // ===============================
    
    /**
     * إنشاء استثناء للمنتج غير الموجود
     * Create product not found exception
     * 
     * @param productId معرف المنتج
     * @return ResourceNotFoundException
     */
    public static ResourceNotFoundException product(Object productId) {
        return new ResourceNotFoundException("Product", productId);
    }
    
    /**
     * إنشاء استثناء للمستخدم غير الموجود
     * Create user not found exception
     * 
     * @param userId معرف المستخدم
     * @return ResourceNotFoundException
     */
    public static ResourceNotFoundException user(Object userId) {
        return new ResourceNotFoundException("User", userId);
    }
    
    /**
     * إنشاء استثناء للطلب غير الموجود
     * Create order not found exception
     * 
     * @param orderId معرف الطلب
     * @return ResourceNotFoundException
     */
    public static ResourceNotFoundException order(Object orderId) {
        return new ResourceNotFoundException("Order", orderId);
    }
    
    /**
     * إنشاء استثناء للفئة غير الموجودة
     * Create category not found exception
     * 
     * @param categoryId معرف الفئة
     * @return ResourceNotFoundException
     */
    public static ResourceNotFoundException category(Object categoryId) {
        return new ResourceNotFoundException("Category", categoryId);
    }
    
    /**
     * إنشاء استثناء للعلامة التجارية غير الموجودة
     * Create brand not found exception
     * 
     * @param brandId معرف العلامة التجارية
     * @return ResourceNotFoundException
     */
    public static ResourceNotFoundException brand(Object brandId) {
        return new ResourceNotFoundException("Brand", brandId);
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
            case "order": return "الطلب";
            case "category": return "الفئة";
            case "brand": return "العلامة التجارية";
            case "payment": return "الدفع";
            default: return resourceType;
        }
    }
    
    // ===============================
    // Getters and Setters
    // ===============================
    
    public String getMessageAr() { return messageAr; }
    public void setMessageAr(String messageAr) { this.messageAr = messageAr; }
    
    public String getResourceType() { return resourceType; }
    public void setResourceType(String resourceType) { this.resourceType = resourceType; }
    
    public Object getResourceId() { return resourceId; }
    public void setResourceId(Object resourceId) { this.resourceId = resourceId; }
}
