package com.ecommerce.multistore.shared.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

/**
 * استجابة API موحدة لجميع endpoints
 * Unified API response wrapper for all endpoints
 * 
 * يوفر تنسيق موحد للاستجابات مع معلومات إضافية مثل الرسائل والأخطاء
 * Provides consistent response format with additional info like messages and errors
 * 
 * @param <T> نوع البيانات المرجعة / Type of data being returned
 * 
 * @author Multi-Store Team
 * @version 1.0
 * @since 2024-01-01
 */
@Schema(description = "استجابة API موحدة / Unified API Response")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {
    
    /**
     * حالة نجاح العملية
     * Operation success status
     */
    @Schema(description = "حالة نجاح العملية / Operation success status", example = "true")
    private boolean success;
    
    /**
     * رسالة الاستجابة
     * Response message
     */
    @Schema(description = "رسالة الاستجابة / Response message", example = "تم إنشاء المنتج بنجاح")
    private String message;
    
    /**
     * رسالة الاستجابة بالعربية
     * Response message in Arabic
     */
    @Schema(description = "رسالة الاستجابة بالعربية / Response message in Arabic")
    private String messageAr;
    
    /**
     * البيانات المرجعة
     * Returned data
     */
    @Schema(description = "البيانات المرجعة / Returned data")
    private T data;
    
    /**
     * تفاصيل الأخطاء إن وجدت
     * Error details if any
     */
    @Schema(description = "تفاصيل الأخطاء / Error details")
    private Object errors;
    
    /**
     * رمز الحالة
     * Status code
     */
    @Schema(description = "رمز الحالة / Status code", example = "200")
    private int statusCode;
    
    /**
     * وقت الاستجابة
     * Response timestamp
     */
    @Schema(description = "وقت الاستجابة / Response timestamp")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;
    
    /**
     * معرف الطلب لتتبع العمليات
     * Request ID for operation tracking
     */
    @Schema(description = "معرف الطلب للتتبع / Request ID for tracking")
    private String requestId;
    
    /**
     * معلومات إضافية اختيارية
     * Optional additional metadata
     */
    @Schema(description = "معلومات إضافية / Additional metadata")
    private Object metadata;
    
    // ===============================
    // Constructors
    // ===============================
    
    /**
     * Constructor افتراضي
     * Default constructor
     */
    public ApiResponse() {
        this.timestamp = LocalDateTime.now();
    }
    
    /**
     * Constructor للاستجابة الناجحة
     * Constructor for successful response
     * 
     * @param data البيانات المرجعة
     * @param message رسالة النجاح
     * @param statusCode رمز الحالة
     */
    public ApiResponse(T data, String message, int statusCode) {
        this();
        this.success = true;
        this.data = data;
        this.message = message;
        this.statusCode = statusCode;
    }
    
    /**
     * Constructor للاستجابة الفاشلة
     * Constructor for error response
     * 
     * @param message رسالة الخطأ
     * @param errors تفاصيل الأخطاء
     * @param statusCode رمز الحالة
     */
    public ApiResponse(String message, Object errors, int statusCode) {
        this();
        this.success = false;
        this.message = message;
        this.errors = errors;
        this.statusCode = statusCode;
    }
    
    // ===============================
    // Static Factory Methods
    // ===============================
    
    /**
     * إنشاء استجابة ناجحة
     * Create successful response
     * 
     * @param <T> نوع البيانات
     * @param data البيانات
     * @return ApiResponse<T>
     */
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(data, "Operation completed successfully", 200);
    }
    
    /**
     * إنشاء استجابة ناجحة مع رسالة مخصصة
     * Create successful response with custom message
     * 
     * @param <T> نوع البيانات
     * @param data البيانات
     * @param message رسالة النجاح
     * @return ApiResponse<T>
     */
    public static <T> ApiResponse<T> success(T data, String message) {
        return new ApiResponse<>(data, message, 200);
    }
    
    /**
     * إنشاء استجابة ناجحة مع رسالة مخصصة ورمز حالة
     * Create successful response with custom message and status code
     * 
     * @param <T> نوع البيانات
     * @param data البيانات
     * @param message رسالة النجاح
     * @param statusCode رمز الحالة
     * @return ApiResponse<T>
     */
    public static <T> ApiResponse<T> success(T data, String message, int statusCode) {
        return new ApiResponse<>(data, message, statusCode);
    }
    
    /**
     * إنشاء استجابة فاشلة
     * Create error response
     * 
     * @param <T> نوع البيانات
     * @param message رسالة الخطأ
     * @return ApiResponse<T>
     */
    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>(message, null, 400);
    }
    
    /**
     * إنشاء استجابة فاشلة مع تفاصيل الأخطاء
     * Create error response with error details
     * 
     * @param <T> نوع البيانات
     * @param message رسالة الخطأ
     * @param errors تفاصيل الأخطاء
     * @return ApiResponse<T>
     */
    public static <T> ApiResponse<T> error(String message, Object errors) {
        return new ApiResponse<>(message, errors, 400);
    }
    
    /**
     * إنشاء استجابة فاشلة مع رمز حالة مخصص
     * Create error response with custom status code
     * 
     * @param <T> نوع البيانات
     * @param message رسالة الخطأ
     * @param errors تفاصيل الأخطاء
     * @param statusCode رمز الحالة
     * @return ApiResponse<T>
     */
    public static <T> ApiResponse<T> error(String message, Object errors, int statusCode) {
        return new ApiResponse<>(message, errors, statusCode);
    }
    
    /**
     * إنشاء استجابة للمورد غير الموجود
     * Create not found response
     * 
     * @param <T> نوع البيانات
     * @param message رسالة عدم الوجود
     * @return ApiResponse<T>
     */
    public static <T> ApiResponse<T> notFound(String message) {
        return new ApiResponse<>(message, null, 404);
    }
    
    /**
     * إنشاء استجابة للوصول المرفوض
     * Create forbidden response
     * 
     * @param <T> نوع البيانات
     * @param message رسالة الرفض
     * @return ApiResponse<T>
     */
    public static <T> ApiResponse<T> forbidden(String message) {
        return new ApiResponse<>(message, null, 403);
    }
    
    /**
     * إنشاء استجابة للطلب غير المصرح به
     * Create unauthorized response
     * 
     * @param <T> نوع البيانات
     * @param message رسالة عدم التصريح
     * @return ApiResponse<T>
     */
    public static <T> ApiResponse<T> unauthorized(String message) {
        return new ApiResponse<>(message, null, 401);
    }
    
    // ===============================
    // Builder Pattern
    // ===============================
    
    /**
     * بناء استجابة مخصصة
     * Build custom response
     * 
     * @param <T> نوع البيانات
     * @return Builder<T>
     */
    public static <T> Builder<T> builder() {
        return new Builder<>();
    }
    
    /**
     * فئة البناء للاستجابة
     * Response builder class
     * 
     * @param <T> نوع البيانات
     */
    public static class Builder<T> {
        private final ApiResponse<T> response;
        
        public Builder() {
            this.response = new ApiResponse<>();
        }
        
        public Builder<T> success(boolean success) {
            response.success = success;
            return this;
        }
        
        public Builder<T> message(String message) {
            response.message = message;
            return this;
        }
        
        public Builder<T> messageAr(String messageAr) {
            response.messageAr = messageAr;
            return this;
        }
        
        public Builder<T> data(T data) {
            response.data = data;
            return this;
        }
        
        public Builder<T> errors(Object errors) {
            response.errors = errors;
            return this;
        }
        
        public Builder<T> statusCode(int statusCode) {
            response.statusCode = statusCode;
            return this;
        }
        
        public Builder<T> requestId(String requestId) {
            response.requestId = requestId;
            return this;
        }
        
        public Builder<T> metadata(Object metadata) {
            response.metadata = metadata;
            return this;
        }
        
        public ApiResponse<T> build() {
            return response;
        }
    }
    
    // ===============================
    // Utility Methods
    // ===============================
    
    /**
     * التحقق من وجود أخطاء
     * Check if there are errors
     * 
     * @return boolean
     */
    public boolean hasErrors() {
        return errors != null;
    }
    
    /**
     * التحقق من وجود بيانات
     * Check if data exists
     * 
     * @return boolean
     */
    public boolean hasData() {
        return data != null;
    }
    
    // ===============================
    // Getters and Setters
    // ===============================
    
    /**
     * التحقق من نجاح العملية وتعديل الحالة
     * Check operation success and modify state
     * 
     * @return boolean حالة النجاح
     */
    public boolean isSuccess() { 
        return success; 
    }
    
    /**
     * تعديل حالة النجاح
     * Set success state
     * 
     * @param success حالة النجاح الجديدة
     */
    public void setSuccess(boolean success) { 
        this.success = success; 
    }
    
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    
    public String getMessageAr() { return messageAr; }
    public void setMessageAr(String messageAr) { this.messageAr = messageAr; }
    
    public T getData() { return data; }
    public void setData(T data) { this.data = data; }
    
    public Object getErrors() { return errors; }
    public void setErrors(Object errors) { this.errors = errors; }
    
    public int getStatusCode() { return statusCode; }
    public void setStatusCode(int statusCode) { this.statusCode = statusCode; }
    
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
    
    public String getRequestId() { return requestId; }
    public void setRequestId(String requestId) { this.requestId = requestId; }
    
    public Object getMetadata() { return metadata; }
    public void setMetadata(Object metadata) { this.metadata = metadata; }
    
    // ===============================
    // toString Method
    // ===============================
    
    @Override
    public String toString() {
        return "ApiResponse{" +
                "success=" + success +
                ", message='" + message + '\'' +
                ", messageAr='" + messageAr + '\'' +
                ", data=" + data +
                ", errors=" + errors +
                ", statusCode=" + statusCode +
                ", timestamp=" + timestamp +
                ", requestId='" + requestId + '\'' +
                ", metadata=" + metadata +
                '}';
    }
}
