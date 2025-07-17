package com.ecommerce.multistore.shared.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * استجابة أخطاء التحقق من صحة البيانات
 * Validation error response
 * 
 * يحتوي على تفاصيل شاملة لأخطاء التحقق مع الترجمة العربية
 * Contains comprehensive validation error details with Arabic translation
 * 
 * @author Multi-Store Team
 * @version 1.0
 * @since 2024-01-01
 */
@Schema(description = "استجابة أخطاء التحقق / Validation error response")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ValidationErrorResponse {
    
    /**
     * أخطاء التحقق لكل حقل
     * Validation errors per field
     */
    @Schema(description = "أخطاء التحقق لكل حقل / Validation errors per field")
    private Map<String, String> validationErrors;
    
    /**
     * أخطاء التحقق لكل حقل بالعربية
     * Validation errors per field in Arabic
     */
    @Schema(description = "أخطاء التحقق لكل حقل بالعربية / Validation errors per field in Arabic")
    private Map<String, String> validationErrorsAr;
    
    /**
     * قائمة أخطاء التحقق التفصيلية
     * Detailed validation errors list
     */
    @Schema(description = "قائمة أخطاء التحقق التفصيلية / Detailed validation errors list")
    private List<FieldError> fieldErrors;
    
    /**
     * الحقل الذي فشل في التحقق أولاً
     * First failed validation field
     */
    @Schema(description = "الحقل الذي فشل في التحقق أولاً / First failed validation field")
    private String failedField;
    
    /**
     * إجمالي عدد الأخطاء
     * Total number of errors
     */
    @Schema(description = "إجمالي عدد الأخطاء / Total number of errors")
    private int totalErrors;
    
    /**
     * نوع خطأ التحقق
     * Validation error type
     */
    @Schema(description = "نوع خطأ التحقق / Validation error type")
    private String validationType;
    
    /**
     * وقت حدوث الخطأ
     * Error occurrence time
     */
    @Schema(description = "وقت حدوث الخطأ / Error occurrence time")
    private LocalDateTime errorTime;
    
    /**
     * اقتراحات لإصلاح الأخطاء
     * Suggestions to fix errors
     */
    @Schema(description = "اقتراحات لإصلاح الأخطاء / Suggestions to fix errors")
    private List<String> suggestions;
    
    /**
     * اقتراحات لإصلاح الأخطاء بالعربية
     * Suggestions to fix errors in Arabic
     */
    @Schema(description = "اقتراحات لإصلاح الأخطاء بالعربية / Suggestions to fix errors in Arabic")
    private List<String> suggestionsAr;
    
    // ===============================
    // Constructors
    // ===============================
    
    /**
     * Constructor افتراضي
     * Default constructor
     */
    public ValidationErrorResponse() {
        this.errorTime = LocalDateTime.now();
        this.validationType = "FIELD_VALIDATION";
    }
    
    /**
     * Constructor مع الأخطاء الأساسية
     * Constructor with basic errors
     * 
     * @param validationErrors أخطاء التحقق
     * @param validationErrorsAr أخطاء التحقق بالعربية
     */
    public ValidationErrorResponse(Map<String, String> validationErrors, 
                                 Map<String, String> validationErrorsAr) {
        this();
        this.validationErrors = validationErrors;
        this.validationErrorsAr = validationErrorsAr;
        this.totalErrors = validationErrors != null ? validationErrors.size() : 0;
    }
    
    // ===============================
    // Builder Pattern
    // ===============================
    
    /**
     * بناء استجابة أخطاء التحقق
     * Build validation error response
     * 
     * @return Builder
     */
    public static Builder builder() {
        return new Builder();
    }
    
    /**
     * فئة البناء لاستجابة أخطاء التحقق
     * Builder class for validation error response
     */
    public static class Builder {
        private final ValidationErrorResponse response;
        
        public Builder() {
            this.response = new ValidationErrorResponse();
        }
        
        public Builder validationErrors(Map<String, String> validationErrors) {
            response.validationErrors = validationErrors;
            return this;
        }
        
        public Builder validationErrorsAr(Map<String, String> validationErrorsAr) {
            response.validationErrorsAr = validationErrorsAr;
            return this;
        }
        
        public Builder fieldErrors(List<FieldError> fieldErrors) {
            response.fieldErrors = fieldErrors;
            return this;
        }
        
        public Builder failedField(String failedField) {
            response.failedField = failedField;
            return this;
        }
        
        public Builder totalErrors(int totalErrors) {
            response.totalErrors = totalErrors;
            return this;
        }
        
        public Builder validationType(String validationType) {
            response.validationType = validationType;
            return this;
        }
        
        public Builder suggestions(List<String> suggestions) {
            response.suggestions = suggestions;
            return this;
        }
        
        public Builder suggestionsAr(List<String> suggestionsAr) {
            response.suggestionsAr = suggestionsAr;
            return this;
        }
        
        public ValidationErrorResponse build() {
            return response;
        }
    }
    
    // ===============================
    // Utility Methods
    // ===============================
    
    /**
     * التحقق من وجود أخطاء
     * Check if has errors
     * 
     * @return boolean
     */
    public boolean hasErrors() {
        return totalErrors > 0;
    }
    
    /**
     * التحقق من وجود أخطاء لحقل معين
     * Check if has errors for specific field
     * 
     * @param fieldName اسم الحقل
     * @return boolean
     */
    public boolean hasErrorForField(String fieldName) {
        return validationErrors != null && validationErrors.containsKey(fieldName);
    }
    
    /**
     * الحصول على خطأ حقل معين
     * Get error for specific field
     * 
     * @param fieldName اسم الحقل
     * @return String رسالة الخطأ
     */
    public String getErrorForField(String fieldName) {
        return validationErrors != null ? validationErrors.get(fieldName) : null;
    }
    
    /**
     * الحصول على خطأ حقل معين بالعربية
     * Get error for specific field in Arabic
     * 
     * @param fieldName اسم الحقل
     * @return String رسالة الخطأ بالعربية
     */
    public String getErrorForFieldAr(String fieldName) {
        return validationErrorsAr != null ? validationErrorsAr.get(fieldName) : null;
    }
    
    /**
     * إضافة خطأ تحقق جديد
     * Add new validation error
     * 
     * @param fieldName اسم الحقل
     * @param errorMessage رسالة الخطأ
     * @param errorMessageAr رسالة الخطأ بالعربية
     */
    public void addValidationError(String fieldName, String errorMessage, String errorMessageAr) {
        if (validationErrors == null) {
            validationErrors = new java.util.HashMap<>();
        }
        if (validationErrorsAr == null) {
            validationErrorsAr = new java.util.HashMap<>();
        }
        
        validationErrors.put(fieldName, errorMessage);
        validationErrorsAr.put(fieldName, errorMessageAr);
        
        // تحديث إجمالي الأخطاء
        this.totalErrors = validationErrors.size();
        
        // تحديث الحقل الفاشل إذا كان أول خطأ
        if (failedField == null) {
            failedField = fieldName;
        }
    }
    
    /**
     * إضافة اقتراح لإصلاح الأخطاء
     * Add suggestion to fix errors
     * 
     * @param suggestion الاقتراح
     * @param suggestionAr الاقتراح بالعربية
     */
    public void addSuggestion(String suggestion, String suggestionAr) {
        if (suggestions == null) {
            suggestions = new java.util.ArrayList<>();
        }
        if (suggestionsAr == null) {
            suggestionsAr = new java.util.ArrayList<>();
        }
        
        suggestions.add(suggestion);
        suggestionsAr.add(suggestionAr);
    }
    
    // ===============================
    // Inner Classes
    // ===============================
    
    /**
     * خطأ الحقل التفصيلي
     * Detailed field error
     */
    @Schema(description = "خطأ الحقل التفصيلي / Detailed field error")
    public static class FieldError {
        
        /**
         * اسم الحقل
         * Field name
         */
        @Schema(description = "اسم الحقل / Field name")
        private String fieldName;
        
        /**
         * القيمة المرفوضة
         * Rejected value
         */
        @Schema(description = "القيمة المرفوضة / Rejected value")
        private Object rejectedValue;
        
        /**
         * رسالة الخطأ
         * Error message
         */
        @Schema(description = "رسالة الخطأ / Error message")
        private String errorMessage;
        
        /**
         * رسالة الخطأ بالعربية
         * Error message in Arabic
         */
        @Schema(description = "رسالة الخطأ بالعربية / Error message in Arabic")
        private String errorMessageAr;
        
        /**
         * رمز خطأ التحقق
         * Validation error code
         */
        @Schema(description = "رمز خطأ التحقق / Validation error code")
        private String errorCode;
        
        /**
         * نوع القيمة المتوقعة
         * Expected value type
         */
        @Schema(description = "نوع القيمة المتوقعة / Expected value type")
        private String expectedType;
        
        /**
         * القيمة الدنيا المسموحة
         * Minimum allowed value
         */
        @Schema(description = "القيمة الدنيا المسموحة / Minimum allowed value")
        private Object minValue;
        
        /**
         * القيمة العليا المسموحة
         * Maximum allowed value
         */
        @Schema(description = "القيمة العليا المسموحة / Maximum allowed value")
        private Object maxValue;
        
        /**
         * نمط القيمة المطلوب
         * Required value pattern
         */
        @Schema(description = "نمط القيمة المطلوب / Required value pattern")
        private String pattern;
        
        // Constructors
        public FieldError() {}
        
        public FieldError(String fieldName, Object rejectedValue, String errorMessage) {
            this.fieldName = fieldName;
            this.rejectedValue = rejectedValue;
            this.errorMessage = errorMessage;
        }
        
        public FieldError(String fieldName, Object rejectedValue, String errorMessage, String errorMessageAr) {
            this.fieldName = fieldName;
            this.rejectedValue = rejectedValue;
            this.errorMessage = errorMessage;
            this.errorMessageAr = errorMessageAr;
        }
        
        // Builder pattern
        public static Builder builder() {
            return new Builder();
        }
        
        public static class Builder {
            private final FieldError fieldError;
            
            public Builder() {
                this.fieldError = new FieldError();
            }
            
            public Builder fieldName(String fieldName) {
                fieldError.fieldName = fieldName;
                return this;
            }
            
            public Builder rejectedValue(Object rejectedValue) {
                fieldError.rejectedValue = rejectedValue;
                return this;
            }
            
            public Builder errorMessage(String errorMessage) {
                fieldError.errorMessage = errorMessage;
                return this;
            }
            
            public Builder errorMessageAr(String errorMessageAr) {
                fieldError.errorMessageAr = errorMessageAr;
                return this;
            }
            
            public Builder errorCode(String errorCode) {
                fieldError.errorCode = errorCode;
                return this;
            }
            
            public Builder expectedType(String expectedType) {
                fieldError.expectedType = expectedType;
                return this;
            }
            
            public Builder minValue(Object minValue) {
                fieldError.minValue = minValue;
                return this;
            }
            
            public Builder maxValue(Object maxValue) {
                fieldError.maxValue = maxValue;
                return this;
            }
            
            public Builder pattern(String pattern) {
                fieldError.pattern = pattern;
                return this;
            }
            
            public FieldError build() {
                return fieldError;
            }
        }
        
        // Getters and Setters
        public String getFieldName() { return fieldName; }
        public void setFieldName(String fieldName) { this.fieldName = fieldName; }
        
        public Object getRejectedValue() { return rejectedValue; }
        public void setRejectedValue(Object rejectedValue) { this.rejectedValue = rejectedValue; }
        
        public String getErrorMessage() { return errorMessage; }
        public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }
        
        public String getErrorMessageAr() { return errorMessageAr; }
        public void setErrorMessageAr(String errorMessageAr) { this.errorMessageAr = errorMessageAr; }
        
        public String getErrorCode() { return errorCode; }
        public void setErrorCode(String errorCode) { this.errorCode = errorCode; }
        
        public String getExpectedType() { return expectedType; }
        public void setExpectedType(String expectedType) { this.expectedType = expectedType; }
        
        public Object getMinValue() { return minValue; }
        public void setMinValue(Object minValue) { this.minValue = minValue; }
        
        public Object getMaxValue() { return maxValue; }
        public void setMaxValue(Object maxValue) { this.maxValue = maxValue; }
        
        public String getPattern() { return pattern; }
        public void setPattern(String pattern) { this.pattern = pattern; }
        
        @Override
        public String toString() {
            return "FieldError{" +
                    "fieldName='" + fieldName + '\'' +
                    ", rejectedValue=" + rejectedValue +
                    ", errorMessage='" + errorMessage + '\'' +
                    ", errorMessageAr='" + errorMessageAr + '\'' +
                    ", errorCode='" + errorCode + '\'' +
                    '}';
        }
    }
    
    // ===============================
    // Static Factory Methods
    // ===============================
    
    /**
     * إنشاء استجابة خطأ واحد
     * Create single error response
     * 
     * @param fieldName اسم الحقل
     * @param errorMessage رسالة الخطأ
     * @param errorMessageAr رسالة الخطأ بالعربية
     * @return ValidationErrorResponse
     */
    public static ValidationErrorResponse singleError(String fieldName, String errorMessage, String errorMessageAr) {
        ValidationErrorResponse response = new ValidationErrorResponse();
        response.addValidationError(fieldName, errorMessage, errorMessageAr);
        response.setFailedField(fieldName);
        return response;
    }
    
    /**
     * إنشاء استجابة أخطاء متعددة
     * Create multiple errors response
     * 
     * @param validationErrors أخطاء التحقق
     * @param validationErrorsAr أخطاء التحقق بالعربية
     * @return ValidationErrorResponse
     */
    public static ValidationErrorResponse multipleErrors(Map<String, String> validationErrors, 
                                                       Map<String, String> validationErrorsAr) {
        return new ValidationErrorResponse(validationErrors, validationErrorsAr);
    }
    
    // ===============================
    // Getters and Setters
    // ===============================
    
    public Map<String, String> getValidationErrors() { return validationErrors; }
    public void setValidationErrors(Map<String, String> validationErrors) { this.validationErrors = validationErrors; }
    
    public Map<String, String> getValidationErrorsAr() { return validationErrorsAr; }
    public void setValidationErrorsAr(Map<String, String> validationErrorsAr) { this.validationErrorsAr = validationErrorsAr; }
    
    public List<FieldError> getFieldErrors() { return fieldErrors; }
    public void setFieldErrors(List<FieldError> fieldErrors) { this.fieldErrors = fieldErrors; }
    
    public String getFailedField() { return failedField; }
    public void setFailedField(String failedField) { this.failedField = failedField; }
    
    public int getTotalErrors() { return totalErrors; }
    public void setTotalErrors(int totalErrors) { this.totalErrors = totalErrors; }
    
    public String getValidationType() { return validationType; }
    public void setValidationType(String validationType) { this.validationType = validationType; }
    
    public LocalDateTime getErrorTime() { return errorTime; }
    public void setErrorTime(LocalDateTime errorTime) { this.errorTime = errorTime; }
    
    public List<String> getSuggestions() { return suggestions; }
    public void setSuggestions(List<String> suggestions) { this.suggestions = suggestions; }
    
    public List<String> getSuggestionsAr() { return suggestionsAr; }
    public void setSuggestionsAr(List<String> suggestionsAr) { this.suggestionsAr = suggestionsAr; }
    
    // ===============================
    // toString Method
    // ===============================
    
    @Override
    public String toString() {
        return "ValidationErrorResponse{" +
                "validationErrors=" + validationErrors +
                ", validationErrorsAr=" + validationErrorsAr +
                ", fieldErrors=" + fieldErrors +
                ", failedField='" + failedField + '\'' +
                ", totalErrors=" + totalErrors +
                ", validationType='" + validationType + '\'' +
                ", errorTime=" + errorTime +
                ", suggestions=" + suggestions +
                ", suggestionsAr=" + suggestionsAr +
                '}';
    }
}
