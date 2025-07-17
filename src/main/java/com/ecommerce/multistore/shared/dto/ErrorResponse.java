
/* ---- File: src/main/java/com/ecommerce/multistore/shared/dto/ErrorResponse.java ---- */

package com.ecommerce.multistore.shared.dto;

import java.time.LocalDateTime;
import java.util.List;

/**
 * استجابة الأخطاء الموحدة
 * Unified Error Response
 */
public class ErrorResponse {

    private String error;
    private String message;
    private String messageAr;
    private int status;
    private String path;
    private LocalDateTime timestamp;
    private List<String> details;

    public ErrorResponse() {
        this.timestamp = LocalDateTime.now();
    }

    public ErrorResponse(String error, String message, int status, String path) {
        this();
        this.error = error;
        this.message = message;
        this.status = status;
        this.path = path;
    }

    public ErrorResponse(String error, String message, String messageAr, int status, String path) {
        this(error, message, status, path);
        this.messageAr = messageAr;
    }

    // Getters and Setters
    public String getError() { return error; }
    public void setError(String error) { this.error = error; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getMessageAr() { return messageAr; }
    public void setMessageAr(String messageAr) { this.messageAr = messageAr; }

    public int getStatus() { return status; }
    public void setStatus(int status) { this.status = status; }

    public String getPath() { return path; }
    public void setPath(String path) { this.path = path; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    public List<String> getDetails() { return details; }
    public void setDetails(List<String> details) { this.details = details; }
}
