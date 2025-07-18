package com.ecommerce.multistore.shared.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDateTime;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {
    private boolean success;
    private String message;
    private String messageAr;
    private T data;
    private int statusCode;
    private String requestId;
    private LocalDateTime timestamp;
    private Map<String, Object> metadata;
    private Object errors; // إضافة هذا الحقل

    // Constructor الأساسي
    public ApiResponse() {
        this.timestamp = LocalDateTime.now();
        this.success = true;
        this.statusCode = 200;
    }

    // Constructor مع البيانات فقط
    public ApiResponse(T data) {
        this();
        this.data = data;
    }

    // Constructor مع البيانات والرسالة
    public ApiResponse(T data, String message) {
        this(data);
        this.message = message;
    }

    // Constructor مع البيانات والرسالة والكود
    public ApiResponse(T data, String message, int statusCode) {
        this(data, message);
        this.statusCode = statusCode;
    }

    // Constructor للأخطاء
    public ApiResponse(String message, int statusCode) {
        this();
        this.success = false;
        this.message = message;
        this.statusCode = statusCode;
        this.data = null;
    }
// إضافة هذه المethods إلى ApiResponse class:

// Static factory method للـ unauthorized
public static <T> ApiResponse<T> unauthorized(String message) {
    return new ApiResponse<>(message, 401);
}

// Static factory method للـ error مع data
public static <T> ApiResponse<T> error(String message, T data, int statusCode) {
    ApiResponse<T> response = new ApiResponse<>(message, statusCode);
    response.setData(data);
    return response;
}

    // Static Factory Methods
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(data);
    }

    public static <T> ApiResponse<T> success(T data, String message) {
        return new ApiResponse<>(data, message);
    }

    public static <T> ApiResponse<T> success(T data, String message, int statusCode) {
        return new ApiResponse<>(data, message, statusCode);
    }

    public static <T> ApiResponse<T> error(String message, int statusCode) {
        return new ApiResponse<>(message, statusCode);
    }

    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>(message, 500);
    }

    // Builder Pattern
    public static <T> ApiResponseBuilder<T> builder() {
        return new ApiResponseBuilder<>();
    }

    // Getters and Setters
    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getMessageAr() { return messageAr; }
    public void setMessageAr(String messageAr) { this.messageAr = messageAr; }

    public T getData() { return data; }
    public void setData(T data) { this.data = data; }

    public int getStatusCode() { return statusCode; }
    public void setStatusCode(int statusCode) { this.statusCode = statusCode; }

    public String getRequestId() { return requestId; }
    public void setRequestId(String requestId) { this.requestId = requestId; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    public Map<String, Object> getMetadata() { return metadata; }
    public void setMetadata(Map<String, Object> metadata) { this.metadata = metadata; }

    // إضافة getter و setter للـ errors
    public Object getErrors() { return errors; }
    public void setErrors(Object errors) { this.errors = errors; }
}