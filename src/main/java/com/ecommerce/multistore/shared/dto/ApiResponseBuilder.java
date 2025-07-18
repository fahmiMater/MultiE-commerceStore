package com.ecommerce.multistore.shared.dto;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class ApiResponseBuilder<T> {
    private boolean success = true;
    private String message;
    private String messageAr;
    private T data;
    private int statusCode = 200;
    private String requestId;
    private LocalDateTime timestamp = LocalDateTime.now();
    private Map<String, Object> metadata = new HashMap<>();
    private Object errors; // إضافة هذا الحقل

    public ApiResponseBuilder<T> success(boolean success) {
        this.success = success;
        return this;
    }

    public ApiResponseBuilder<T> message(String message) {
        this.message = message;
        return this;
    }

    public ApiResponseBuilder<T> messageAr(String messageAr) {
        this.messageAr = messageAr;
        return this;
    }

    public ApiResponseBuilder<T> data(T data) {
        this.data = data;
        return this;
    }

    public ApiResponseBuilder<T> statusCode(int statusCode) {
        this.statusCode = statusCode;
        return this;
    }

    public ApiResponseBuilder<T> requestId(String requestId) {
        this.requestId = requestId;
        return this;
    }

    public ApiResponseBuilder<T> timestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public ApiResponseBuilder<T> metadata(Map<String, Object> metadata) {
        this.metadata = metadata;
        return this;
    }

    public ApiResponseBuilder<T> addMetadata(String key, Object value) {
        this.metadata.put(key, value);
        return this;
    }

    // إضافة method للـ errors
    public ApiResponseBuilder<T> errors(Object errors) {
        this.errors = errors;
        return this;
    }

    public ApiResponse<T> build() {
        ApiResponse<T> response = new ApiResponse<>();
        response.setSuccess(this.success);
        response.setMessage(this.message);
        response.setMessageAr(this.messageAr);
        response.setData(this.data);
        response.setStatusCode(this.statusCode);
        response.setRequestId(this.requestId);
        response.setTimestamp(this.timestamp);
        response.setMetadata(this.metadata);
        response.setErrors(this.errors); // إضافة هذا السطر
        return response;
    }
}