package com.ecommerce.multistore.shared.exception;

/**
 * استثناء منطق الأعمال المخصص
 * Custom business logic exception
 * 
 * يُستخدم لأخطاء منطق الأعمال مثل قواعد التطبيق والتحقق من المنطق
 * Used for business logic errors like application rules and logic validation
 * 
 * @author Multi-Store Team
 * @version 1.0
 * @since 2024-01-01
 */
public class BusinessException extends RuntimeException {
    
    /**
     * رسالة الخطأ بالعربية
     * Error message in Arabic
     */
    private String messageAr;
    
    /**
     * رمز الحالة HTTP
     * HTTP status code
     */
    private int statusCode;
    
    /**
     * رمز الخطأ المخصص
     * Custom error code
     */
    private String errorCode;
    
    /**
     * بيانات إضافية للخطأ
     * Additional error data
     */
    private Object additionalData;
    
    // ===============================
    // Constructors
    // ===============================
    
    /**
     * Constructor مع رسالة فقط
     * Constructor with message only
     * 
     * @param message رسالة الخطأ
     */
    public BusinessException(String message) {
        super(message);
        this.statusCode = 400; // Bad Request by default
    }
    
    /**
     * Constructor مع رسالة ورمز حالة
     * Constructor with message and status code
     * 
     * @param message رسالة الخطأ
     * @param statusCode رمز الحالة
     */
    public BusinessException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }
    
    /**
     * Constructor مع رسالة ورسالة عربية
     * Constructor with message and Arabic message
     * 
     * @param message رسالة الخطأ
     * @param messageAr رسالة الخطأ بالعربية
     */
    public BusinessException(String message, String messageAr) {
        super(message);
        this.messageAr = messageAr;
        this.statusCode = 400;
    }
    
    /**
     * Constructor كامل
     * Full constructor
     * 
     * @param message رسالة الخطأ
     * @param messageAr رسالة الخطأ بالعربية
     * @param statusCode رمز الحالة
     */
    public BusinessException(String message, String messageAr, int statusCode) {
        super(message);
        this.messageAr = messageAr;
        this.statusCode = statusCode;
    }
    
    /**
     * Constructor مع رسالة وسبب
     * Constructor with message and cause
     * 
     * @param message رسالة الخطأ
     * @param cause السبب
     */
    public BusinessException(String message, Throwable cause) {
        super(message, cause);
        this.statusCode = 400;
    }
    
    /**
     * Constructor مع جميع المعاملات
     * Constructor with all parameters
     * 
     * @param message رسالة الخطأ
     * @param messageAr رسالة الخطأ بالعربية
     * @param statusCode رمز الحالة
     * @param errorCode رمز الخطأ المخصص
     * @param additionalData بيانات إضافية
     */
    public BusinessException(String message, String messageAr, int statusCode, 
                           String errorCode, Object additionalData) {
        super(message);
        this.messageAr = messageAr;
        this.statusCode = statusCode;
        this.errorCode = errorCode;
        this.additionalData = additionalData;
    }
    
    // ===============================
    // Static Factory Methods
    // ===============================
    
    /**
     * إنشاء استثناء طلب خاطئ
     * Create bad request exception
     * 
     * @param message رسالة الخطأ
     * @return BusinessException
     */
    public static BusinessException badRequest(String message) {
        return new BusinessException(message, 400);
    }
    
    /**
     * إنشاء استثناء طلب خاطئ مع رسالة عربية
     * Create bad request exception with Arabic message
     * 
     * @param message رسالة الخطأ
     * @param messageAr رسالة الخطأ بالعربية
     * @return BusinessException
     */
    public static BusinessException badRequest(String message, String messageAr) {
        return new BusinessException(message, messageAr, 400);
    }
    
    /**
     * إنشاء استثناء تعارض
     * Create conflict exception
     * 
     * @param message رسالة الخطأ
     * @return BusinessException
     */
    public static BusinessException conflict(String message) {
        return new BusinessException(message, 409);
    }
    
    /**
     * إنشاء استثناء تعارض مع رسالة عربية
     * Create conflict exception with Arabic message
     * 
     * @param message رسالة الخطأ
     * @param messageAr رسالة الخطأ بالعربية
     * @return BusinessException
     */
    public static BusinessException conflict(String message, String messageAr) {
        return new BusinessException(message, messageAr, 409);
    }
    
    /**
     * إنشاء استثناء وصول مرفوض
     * Create forbidden exception
     * 
     * @param message رسالة الخطأ
     * @return BusinessException
     */
    public static BusinessException forbidden(String message) {
        return new BusinessException(message, 403);
    }
    
    /**
     * إنشاء استثناء وصول مرفوض مع رسالة عربية
     * Create forbidden exception with Arabic message
     * 
     * @param message رسالة الخطأ
     * @param messageAr رسالة الخطأ بالعربية
     * @return BusinessException
     */
    public static BusinessException forbidden(String message, String messageAr) {
        return new BusinessException(message, messageAr, 403);
    }
    
    // ===============================
    // Getters and Setters
    // ===============================
    
    /**
     * الحصول على رسالة الخطأ بالعربية
     * Get Arabic error message
     * 
     * @return String رسالة الخطأ بالعربية
     */
    public String getMessageAr() {
        return messageAr;
    }
    
    /**
     * تعيين رسالة الخطأ بالعربية
     * Set Arabic error message
     * 
     * @param messageAr رسالة الخطأ بالعربية
     */
    public void setMessageAr(String messageAr) {
        this.messageAr = messageAr;
    }
    
    /**
     * الحصول على رمز الحالة
     * Get status code
     * 
     * @return int رمز الحالة
     */
    public int getStatusCode() {
        return statusCode;
    }
    
    /**
     * تعيين رمز الحالة
     * Set status code
     * 
     * @param statusCode رمز الحالة
     */
    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }
    
    /**
     * الحصول على رمز الخطأ المخصص
     * Get custom error code
     * 
     * @return String رمز الخطأ المخصص
     */
    public String getErrorCode() {
        return errorCode;
    }
    
    /**
     * تعيين رمز الخطأ المخصص
     * Set custom error code
     * 
     * @param errorCode رمز الخطأ المخصص
     */
    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }
    
    /**
     * الحصول على البيانات الإضافية
     * Get additional data
     * 
     * @return Object البيانات الإضافية
     */
    public Object getAdditionalData() {
        return additionalData;
    }
    
    /**
     * تعيين البيانات الإضافية
     * Set additional data
     * 
     * @param additionalData البيانات الإضافية
     */
    public void setAdditionalData(Object additionalData) {
        this.additionalData = additionalData;
    }
    
    // ===============================
    // Utility Methods
    // ===============================
    
    /**
     * التحقق من وجود رسالة عربية
     * Check if Arabic message exists
     * 
     * @return boolean
     */
    public boolean hasArabicMessage() {
        return messageAr != null && !messageAr.trim().isEmpty();
    }
    
    /**
     * التحقق من وجود رمز خطأ مخصص
     * Check if custom error code exists
     * 
     * @return boolean
     */
    public boolean hasErrorCode() {
        return errorCode != null && !errorCode.trim().isEmpty();
    }
    
    /**
     * التحقق من وجود بيانات إضافية
     * Check if additional data exists
     * 
     * @return boolean
     */
    public boolean hasAdditionalData() {
        return additionalData != null;
    }
    
    // ===============================
    // toString Method
    // ===============================
    
    @Override
    public String toString() {
        return "BusinessException{" +
                "message='" + getMessage() + '\'' +
                ", messageAr='" + messageAr + '\'' +
                ", statusCode=" + statusCode +
                ", errorCode='" + errorCode + '\'' +
                ", additionalData=" + additionalData +
                '}';
    }
}
