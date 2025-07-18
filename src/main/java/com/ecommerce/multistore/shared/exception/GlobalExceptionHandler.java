package com.ecommerce.multistore.shared.exception;

import com.ecommerce.multistore.shared.dto.ApiResponse;
import com.ecommerce.multistore.shared.dto.ValidationErrorResponse;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * معالج الأخطاء العام للتطبيق
 * Global exception handler for the application
 * 
 * يتعامل مع جميع أنواع الأخطاء ويحولها إلى استجابات API موحدة
 * Handles all types of exceptions and converts them to unified API responses
 * 
 * @author Multi-Store Team
 * @version 1.0
 * @since 2024-01-01
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    
    // ===============================
    // Business Logic Exceptions
    // ===============================
    
    /**
     * معالجة أخطاء منطق الأعمال المخصصة
     * Handle custom business logic exceptions
     * 
     * @param ex الاستثناء
     * @param request الطلب
     * @return ResponseEntity<ApiResponse<Object>>
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponse<Object>> handleBusinessException(
            BusinessException ex, HttpServletRequest request) {
        
        logger.warn("Business exception occurred: {} | Request: {} {}", 
                   ex.getMessage(), request.getMethod(), request.getRequestURI());
        
        ApiResponse<Object> response = ApiResponse.<Object>builder()
                .success(false)
                .message(ex.getMessage())
                .messageAr(ex.getMessageAr())
                .statusCode(ex.getStatusCode())
                .requestId(generateRequestId(request))
                .metadata(createErrorMetadata(ex, request))
                .build();
        
        return new ResponseEntity<>(response, HttpStatus.valueOf(ex.getStatusCode()));
    }
    
    /**
     * معالجة أخطاء المورد غير الموجود
     * Handle resource not found exceptions
     * 
     * @param ex الاستثناء
     * @param request الطلب
     * @return ResponseEntity<ApiResponse<Object>>
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleResourceNotFoundException(
            ResourceNotFoundException ex, HttpServletRequest request) {
        
        logger.warn("Resource not found: {} | Request: {} {}", 
                   ex.getMessage(), request.getMethod(), request.getRequestURI());
        
        ApiResponse<Object> response = ApiResponse.<Object>builder()
                .success(false)
                .message(ex.getMessage())
                .messageAr(ex.getMessageAr())
                .statusCode(HttpStatus.NOT_FOUND.value())
                .requestId(generateRequestId(request))
                .metadata(createErrorMetadata(ex, request))
                .build();
        
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
    
    /**
     * معالجة أخطاء تكرار المورد
     * Handle duplicate resource exceptions
     * 
     * @param ex الاستثناء
     * @param request الطلب
     * @return ResponseEntity<ApiResponse<Object>>
     */
    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ApiResponse<Object>> handleDuplicateResourceException(
            DuplicateResourceException ex, HttpServletRequest request) {
        
        logger.warn("Duplicate resource: {} | Request: {} {}", 
                   ex.getMessage(), request.getMethod(), request.getRequestURI());
        
        ApiResponse<Object> response = ApiResponse.<Object>builder()
                .success(false)
                .message(ex.getMessage())
                .messageAr(ex.getMessageAr())
                .statusCode(HttpStatus.CONFLICT.value())
                .requestId(generateRequestId(request))
                .metadata(createErrorMetadata(ex, request))
                .build();
        
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }
    
    // ===============================
    // Validation Exceptions
    // ===============================
    
    /**
     * معالجة أخطاء التحقق من صحة البيانات
     * Handle validation exceptions
     * 
     * @param ex الاستثناء
     * @param request الطلب
     * @return ResponseEntity<ApiResponse<Object>>
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Object>> handleValidationException(
            MethodArgumentNotValidException ex, HttpServletRequest request) {
        
        logger.warn("Validation error: {} | Request: {} {}", 
                   ex.getMessage(), request.getMethod(), request.getRequestURI());
        
        Map<String, String> validationErrors = new HashMap<>();
        Map<String, String> validationErrorsAr = new HashMap<>();
        
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            
            validationErrors.put(fieldName, errorMessage);
            validationErrorsAr.put(fieldName, translateValidationMessage(errorMessage));
        });
        
        ValidationErrorResponse errorResponse = ValidationErrorResponse.builder()
                .validationErrors(validationErrors)
                .validationErrorsAr(validationErrorsAr)
                .failedField(validationErrors.keySet().iterator().next())
                .totalErrors(validationErrors.size())
                .build();
        
        ApiResponse<Object> response = ApiResponse.<Object>builder()
                .success(false)
                .message("Validation failed")
                .messageAr("فشل في التحقق من صحة البيانات")
                .errors(errorResponse)
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .requestId(generateRequestId(request))
                .metadata(createErrorMetadata(ex, request))
                .build();
        
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
    
    /**
     * معالجة أخطاء التحقق من القيود
     * Handle constraint violation exceptions
     * 
     * @param ex الاستثناء
     * @param request الطلب
     * @return ResponseEntity<ApiResponse<Object>>
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse<Object>> handleConstraintViolationException(
            ConstraintViolationException ex, HttpServletRequest request) {
        
        logger.warn("Constraint violation: {} | Request: {} {}", 
                   ex.getMessage(), request.getMethod(), request.getRequestURI());
        
        Map<String, String> constraintErrors = new HashMap<>();
        
        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            String fieldName = violation.getPropertyPath().toString();
            String errorMessage = violation.getMessage();
            constraintErrors.put(fieldName, errorMessage);
        }
        
        ApiResponse<Object> response = ApiResponse.<Object>builder()
                .success(false)
                .message("Constraint validation failed")
                .messageAr("فشل في التحقق من القيود")
                .errors(constraintErrors)
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .requestId(generateRequestId(request))
                .metadata(createErrorMetadata(ex, request))
                .build();
        
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
    
    // ===============================
    // Security Exceptions
    // ===============================
    
    /**
     * معالجة أخطاء المصادقة
     * Handle authentication exceptions
     * 
     * @param ex الاستثناء
     * @param request الطلب
     * @return ResponseEntity<ApiResponse<Object>>
     */
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiResponse<Object>> handleAuthenticationException(
            AuthenticationException ex, HttpServletRequest request) {
        
        logger.warn("Authentication failed: {} | Request: {} {}", 
                   ex.getMessage(), request.getMethod(), request.getRequestURI());
        
        ApiResponse<Object> response = ApiResponse.<Object>builder()
                .success(false)
                .message("Authentication failed")
                .messageAr("فشل في المصادقة")
                .statusCode(HttpStatus.UNAUTHORIZED.value())
                .requestId(generateRequestId(request))
                .metadata(createErrorMetadata(ex, request))
                .build();
        
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }
    
    /**
     * معالجة أخطاء رفض الوصول
     * Handle access denied exceptions
     * 
     * @param ex الاستثناء
     * @param request الطلب
     * @return ResponseEntity<ApiResponse<Object>>
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse<Object>> handleAccessDeniedException(
            AccessDeniedException ex, HttpServletRequest request) {
        
        logger.warn("Access denied: {} | Request: {} {}", 
                   ex.getMessage(), request.getMethod(), request.getRequestURI());
        
        ApiResponse<Object> response = ApiResponse.<Object>builder()
                .success(false)
                .message("Access denied")
                .messageAr("تم رفض الوصول")
                .statusCode(HttpStatus.FORBIDDEN.value())
                .requestId(generateRequestId(request))
                .metadata(createErrorMetadata(ex, request))
                .build();
        
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }
    
    /**
     * معالجة أخطاء بيانات الاعتماد الخاطئة
     * Handle bad credentials exceptions
     * 
     * @param ex الاستثناء
     * @param request الطلب
     * @return ResponseEntity<ApiResponse<Object>>
     */
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiResponse<Object>> handleBadCredentialsException(
            BadCredentialsException ex, HttpServletRequest request) {
        
        logger.warn("Bad credentials: {} | Request: {} {}", 
                   ex.getMessage(), request.getMethod(), request.getRequestURI());
        
        ApiResponse<Object> response = ApiResponse.<Object>builder()
                .success(false)
                .message("Invalid credentials")
                .messageAr("بيانات اعتماد غير صحيحة")
                .statusCode(HttpStatus.UNAUTHORIZED.value())
                .requestId(generateRequestId(request))
                .metadata(createErrorMetadata(ex, request))
                .build();
        
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }
    
    // ===============================
    // Database Exceptions
    // ===============================
    
    /**
     * معالجة أخطاء سلامة البيانات
     * Handle data integrity violation exceptions
     * 
     * @param ex الاستثناء
     * @param request الطلب
     * @return ResponseEntity<ApiResponse<Object>>
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiResponse<Object>> handleDataIntegrityViolationException(
            DataIntegrityViolationException ex, HttpServletRequest request) {
        
        logger.error("Data integrity violation: {} | Request: {} {}", 
                    ex.getMessage(), request.getMethod(), request.getRequestURI());
        
        String userMessage = "Data integrity violation occurred";
        String userMessageAr = "حدث انتهاك لسلامة البيانات";
        
        // تحليل نوع الخطأ وإرسال رسالة مناسبة
        if (ex.getMessage().contains("unique constraint") || ex.getMessage().contains("duplicate")) {
            userMessage = "This record already exists";
            userMessageAr = "هذا السجل موجود بالفعل";
        } else if (ex.getMessage().contains("foreign key")) {
            userMessage = "Cannot delete this record because it is referenced by other records";
            userMessageAr = "لا يمكن حذف هذا السجل لأنه مرتبط بسجلات أخرى";
        }
        
        ApiResponse<Object> response = ApiResponse.<Object>builder()
                .success(false)
                .message(userMessage)
                .messageAr(userMessageAr)
                .statusCode(HttpStatus.CONFLICT.value())
                .requestId(generateRequestId(request))
                .metadata(createErrorMetadata(ex, request))
                .build();
        
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }
    
    // ===============================
    // HTTP Exceptions
    // ===============================
    
    /**
     * معالجة أخطاء الطريقة غير المدعومة
     * Handle method not supported exceptions
     * 
     * @param ex الاستثناء
     * @param request الطلب
     * @return ResponseEntity<ApiResponse<Object>>
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiResponse<Object>> handleMethodNotSupportedException(
            HttpRequestMethodNotSupportedException ex, HttpServletRequest request) {
        
        logger.warn("Method not supported: {} | Request: {} {}", 
                   ex.getMessage(), request.getMethod(), request.getRequestURI());
        
        ApiResponse<Object> response = ApiResponse.<Object>builder()
                .success(false)
                .message("HTTP method not supported: " + ex.getMethod())
                .messageAr("طريقة HTTP غير مدعومة: " + ex.getMethod())
                .statusCode(HttpStatus.METHOD_NOT_ALLOWED.value())
                .requestId(generateRequestId(request))
                .metadata(Map.of(
                    "supportedMethods", ex.getSupportedMethods(),
                    "requestedMethod", ex.getMethod()
                ))
                .build();
        
        return new ResponseEntity<>(response, HttpStatus.METHOD_NOT_ALLOWED);
    }
    
    /**
     * معالجة أخطاء نوع المحتوى غير المدعوم
     * Handle unsupported media type exceptions
     * 
     * @param ex الاستثناء
     * @param request الطلب
     * @return ResponseEntity<ApiResponse<Object>>
     */
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ApiResponse<Object>> handleMediaTypeNotSupportedException(
            HttpMediaTypeNotSupportedException ex, HttpServletRequest request) {
        
        logger.warn("Media type not supported: {} | Request: {} {}", 
                   ex.getMessage(), request.getMethod(), request.getRequestURI());
        
        ApiResponse<Object> response = ApiResponse.<Object>builder()
                .success(false)
                .message("Media type not supported")
                .messageAr("نوع المحتوى غير مدعوم")
                .statusCode(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value())
                .requestId(generateRequestId(request))
                .metadata(Map.of(
                    "supportedMediaTypes", ex.getSupportedMediaTypes(),
                    "contentType", ex.getContentType()
                ))
                .build();
        
        return new ResponseEntity<>(response, HttpStatus.UNSUPPORTED_MEDIA_TYPE);
    }
    
    /**
     * معالجة أخطاء المسار غير الموجود
     * Handle no handler found exceptions
     * 
     * @param ex الاستثناء
     * @param request الطلب
     * @return ResponseEntity<ApiResponse<Object>>
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleNoHandlerFoundException(
            NoHandlerFoundException ex, HttpServletRequest request) {
        
        logger.warn("No handler found: {} | Request: {} {}", 
                   ex.getMessage(), request.getMethod(), request.getRequestURI());
        
        ApiResponse<Object> response = ApiResponse.<Object>builder()
                .success(false)
                .message("Endpoint not found: " + ex.getRequestURL())
                .messageAr("نقطة النهاية غير موجودة: " + ex.getRequestURL())
                .statusCode(HttpStatus.NOT_FOUND.value())
                .requestId(generateRequestId(request))
                .metadata(createErrorMetadata(ex, request))
                .build();
        
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
    
    /**
     * معالجة أخطاء المعامل المفقود
     * Handle missing parameter exceptions
     * 
     * @param ex الاستثناء
     * @param request الطلب
     * @return ResponseEntity<ApiResponse<Object>>
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ApiResponse<Object>> handleMissingParameterException(
            MissingServletRequestParameterException ex, HttpServletRequest request) {
        
        logger.warn("Missing parameter: {} | Request: {} {}", 
                   ex.getMessage(), request.getMethod(), request.getRequestURI());
        
        ApiResponse<Object> response = ApiResponse.<Object>builder()
                .success(false)
                .message("Missing required parameter: " + ex.getParameterName())
                .messageAr("معامل مطلوب مفقود: " + ex.getParameterName())
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .requestId(generateRequestId(request))
                .metadata(Map.of(
                    "parameterName", ex.getParameterName(),
                    "parameterType", ex.getParameterType()
                ))
                .build();
        
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
    
    /**
     * معالجة أخطاء نوع المعامل الخاطئ
     * Handle method argument type mismatch exceptions
     * 
     * @param ex الاستثناء
     * @param request الطلب
     * @return ResponseEntity<ApiResponse<Object>>
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiResponse<Object>> handleTypeMismatchException(
            MethodArgumentTypeMismatchException ex, HttpServletRequest request) {
        
        logger.warn("Type mismatch: {} | Request: {} {}", 
                   ex.getMessage(), request.getMethod(), request.getRequestURI());
        
        ApiResponse<Object> response = ApiResponse.<Object>builder()
                .success(false)
                .message("Invalid parameter type for: " + ex.getName())
                .messageAr("نوع معامل غير صحيح لـ: " + ex.getName())
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .requestId(generateRequestId(request))
                .metadata(Map.of(
                    "parameterName", ex.getName(),
                    "providedValue", ex.getValue(),
                    "expectedType", ex.getRequiredType().getSimpleName()
                ))
                .build();
        
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
    
    /**
     * معالجة أخطاء قراءة الرسالة
     * Handle message not readable exceptions
     * 
     * @param ex الاستثناء
     * @param request الطلب
     * @return ResponseEntity<ApiResponse<Object>>
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<Object>> handleMessageNotReadableException(
            HttpMessageNotReadableException ex, HttpServletRequest request) {
        
        logger.warn("Message not readable: {} | Request: {} {}", 
                   ex.getMessage(), request.getMethod(), request.getRequestURI());
        
        ApiResponse<Object> response = ApiResponse.<Object>builder()
                .success(false)
                .message("Invalid request body format")
                .messageAr("تنسيق نص الطلب غير صحيح")
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .requestId(generateRequestId(request))
                .metadata(createErrorMetadata(ex, request))
                .build();
        
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
    
    // ===============================
    // Generic Exceptions
    // ===============================
    
    /**
     * معالجة أخطاء IllegalArgumentException
     * Handle illegal argument exceptions
     * 
     * @param ex الاستثناء
     * @param request الطلب
     * @return ResponseEntity<ApiResponse<Object>>
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<Object>> handleIllegalArgumentException(
            IllegalArgumentException ex, HttpServletRequest request) {
        
        logger.warn("Illegal argument: {} | Request: {} {}", 
                   ex.getMessage(), request.getMethod(), request.getRequestURI());
        
        ApiResponse<Object> response = ApiResponse.<Object>builder()
                .success(false)
                .message(ex.getMessage())
                .messageAr("معامل غير صحيح")
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .requestId(generateRequestId(request))
                .metadata(createErrorMetadata(ex, request))
                .build();
        
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
    
    /**
     * معالجة جميع الأخطاء العامة الأخرى
     * Handle all other generic exceptions
     * 
     * @param ex الاستثناء
     * @param request الطلب
     * @return ResponseEntity<ApiResponse<Object>>
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleGenericException(
            Exception ex, HttpServletRequest request) {
        
        logger.error("Unexpected error occurred: {} | Request: {} {}", 
                    ex.getMessage(), request.getMethod(), request.getRequestURI(), ex);
        
        ApiResponse<Object> response = ApiResponse.<Object>builder()
                .success(false)
                .message("An unexpected error occurred")
                .messageAr("حدث خطأ غير متوقع")
                .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .requestId(generateRequestId(request))
                .metadata(createErrorMetadata(ex, request))
                .build();
        
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
    // ===============================
    // Helper Methods
    // ===============================
    
    /**
     * توليد معرف فريد للطلب
     * Generate unique request ID
     * 
     * @param request الطلب
     * @return String معرف الطلب
     */
    private String generateRequestId(HttpServletRequest request) {
        return UUID.randomUUID().toString().substring(0, 8);
    }
    
    /**
     * إنشاء معلومات إضافية للخطأ
     * Create error metadata
     * 
     * @param ex الاستثناء
     * @param request الطلب
     * @return Map<String, Object>
     */
    private Map<String, Object> createErrorMetadata(Exception ex, HttpServletRequest request) {
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("timestamp", LocalDateTime.now());
        metadata.put("path", request.getRequestURI());
        metadata.put("method", request.getMethod());
        metadata.put("exceptionType", ex.getClass().getSimpleName());
        
        if (request.getQueryString() != null) {
            metadata.put("queryString", request.getQueryString());
        }
        
        return metadata;
    }
    
    /**
     * ترجمة رسائل التحقق إلى العربية
     * Translate validation messages to Arabic
     * 
     * @param message الرسالة الأصلية
     * @return String الرسالة المترجمة
     */
    private String translateValidationMessage(String message) {
        Map<String, String> translations = Map.of(
            "must not be blank", "يجب ألا يكون فارغاً",
            "must not be null", "يجب ألا يكون فارغاً",
            "size must be between", "يجب أن يكون الحجم بين",
            "must be greater than", "يجب أن يكون أكبر من",
            "must be less than", "يجب أن يكون أصغر من",
            "is required", "مطلوب",
            "already exists", "موجود بالفعل",
            "not found", "غير موجود"
        );
        
        return translations.entrySet().stream()
                .filter(entry -> message.contains(entry.getKey()))
                .findFirst()
                .map(Map.Entry::getValue)
                .orElse(message);
    }
}
