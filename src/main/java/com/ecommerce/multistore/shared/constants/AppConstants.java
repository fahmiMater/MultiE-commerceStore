package com.ecommerce.multistore.shared.constants;

/**
 * ثوابت التطبيق العامة
 * Application Constants
 * 
 * @author Multi-Store Team
 * @version 1.0
 * @since 2024-01-01
 */
public class AppConstants {
  
    // Rate Limiting Constants
    public static final int RATE_LIMIT_REQUESTS = 100;
    public static final int RATE_LIMIT_PERIOD = 60; // seconds
    public static final int RATE_LIMIT_WINDOW_MINUTES = 1; // 1 minute window
    public static final int RATE_LIMIT_WINDOW_SECONDS = RATE_LIMIT_WINDOW_MINUTES * 60;
    public static final int RATE_LIMIT_WINDOW_MILLISECONDS = RATE_LIMIT_WINDOW_SECONDS * 1000;
    
    // Rate Limiting Time Windows
    public static final int RATE_LIMIT_WINDOW_MINUTES_SHORT = 1; // 1 minute
    public static final int RATE_LIMIT_WINDOW_MINUTES_MEDIUM = 5; // 5 minutes
    public static final int RATE_LIMIT_WINDOW_MINUTES_LONG = 15; // 15 minutes
    public static final int RATE_LIMIT_WINDOW_MINUTES_EXTENDED = 60; // 1 hour
    
    // Rate Limiting per different time windows
    public static final int RATE_LIMIT_REQUESTS_PER_MINUTE = 100;
    public static final int RATE_LIMIT_REQUESTS_PER_5_MINUTES = 300;
    public static final int RATE_LIMIT_REQUESTS_PER_15_MINUTES = 500;
    public static final int RATE_LIMIT_REQUESTS_PER_HOUR = 1000;
    public static final int RATE_LIMIT_REQUESTS_PER_DAY = 10000;
    
    // Rate Limiting Burst Settings
    public static final int RATE_LIMIT_BURST_SIZE = 20; // burst requests
    public static final int RATE_LIMIT_BURST_WINDOW_SECONDS = 10; // burst window
    
    // Rate Limiting Cleanup and Maintenance
    public static final int RATE_LIMIT_CLEANUP_INTERVAL = 300; // 5 minutes
    public static final int RATE_LIMIT_CACHE_SIZE = 10000; // max cached entries
    public static final int RATE_LIMIT_CACHE_EXPIRY_MINUTES = 60; // cache expiry
    
    

    // ثوابت عامة - General Constants
    public static final String DEFAULT_LANGUAGE = "ar";
    public static final String DEFAULT_CURRENCY = "YER";
    public static final String DEFAULT_COUNTRY = "Yemen";
    public static final String DEFAULT_TIMEZONE = "Asia/Aden";

     // API Base Path
    public static final String API_BASE_PATH = "/api/v1";
    // Security Constants
    public static final String API_KEY_HEADER = "X-API-Key";
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";
    // ثوابت التصفح - Pagination Constants
    public static final int DEFAULT_PAGE_SIZE = 20;
    public static final int MAX_PAGE_SIZE = 100;
    public static final String DEFAULT_SORT_BY = "createdAt";
    public static final String DEFAULT_SORT_DIR = "desc";
    
    // ثوابت الملفات - File Constants
    public static final long MAX_FILE_SIZE = 10 * 1024 * 1024; // 10MB
    public static final String[] ALLOWED_IMAGE_TYPES = {"jpg", "jpeg", "png", "gif", "webp"};
    public static final String UPLOAD_DIR = "uploads/";
    public static final String IMAGES_DIR = "images/";
    
    // ثوابت الأمان - Security Constants
    public static final int MIN_PASSWORD_LENGTH = 6;
    public static final int MAX_PASSWORD_LENGTH = 100;
    public static final int TOKEN_EXPIRY_HOURS = 24;
    public static final int REFRESH_TOKEN_EXPIRY_DAYS = 30;
    public static final int MAX_LOGIN_ATTEMPTS = 5;
    public static final int LOCKOUT_DURATION_MINUTES = 15;
    
    // ثوابت التحقق - Validation Constants
    public static final int MIN_PRODUCT_NAME_LENGTH = 3;
    public static final int MAX_PRODUCT_NAME_LENGTH = 255;
    public static final int MIN_DESCRIPTION_LENGTH = 10;
    public static final int MAX_DESCRIPTION_LENGTH = 5000;
    public static final int MAX_SLUG_LENGTH = 255;
    
    // ثوابت الطلبات - Order Constants
    public static final int ORDER_NUMBER_LENGTH = 12;
    public static final int MAX_ORDER_ITEMS = 50;
    public static final double MIN_ORDER_AMOUNT = 0.01;
    public static final double MAX_ORDER_AMOUNT = 999999.99;
    
    // ثوابت الشحن - Shipping Constants
    public static final double DEFAULT_SHIPPING_COST = 5000.0;
    public static final double FREE_SHIPPING_THRESHOLD = 50000.0;
    public static final int DELIVERY_DAYS_MIN = 1;
    public static final int DELIVERY_DAYS_MAX = 7;
    
    // ثوابت المدفوعات - Payment Constants
    public static final double PAYMENT_PROCESSING_FEE = 0.025; // 2.5%
    public static final double MAX_PAYMENT_AMOUNT = 1000000.0;
    public static final int PAYMENT_TIMEOUT_MINUTES = 15;
    
    // ثوابت المخزون - Inventory Constants
    public static final int LOW_STOCK_THRESHOLD = 10;
    public static final int DEFAULT_STOCK_QUANTITY = 0;
    public static final int MAX_STOCK_QUANTITY = 999999;
    
    // ثوابت التحليلات - Analytics Constants
    public static final int ANALYTICS_RETENTION_DAYS = 365;
    public static final int TOP_PRODUCTS_LIMIT = 10;
    public static final int REPORT_CACHE_HOURS = 1;
    
    // رسائل الأخطاء - Error Messages
    public static final String ERROR_INVALID_INPUT = "بيانات غير صحيحة";
    public static final String ERROR_RESOURCE_NOT_FOUND = "المورد غير موجود";
    public static final String ERROR_UNAUTHORIZED = "غير مصرح";
    public static final String ERROR_FORBIDDEN = "ممنوع";
    public static final String ERROR_SERVER_ERROR = "خطأ في الخادم";
    
    // رسائل النجاح - Success Messages
    public static final String SUCCESS_CREATED = "تم الإنشاء بنجاح";
    public static final String SUCCESS_UPDATED = "تم التحديث بنجاح";
    public static final String SUCCESS_DELETED = "تم الحذف بنجاح";
    
    // صفحات الأخطاء - Error Pages
    public static final String ERROR_PAGE_404 = "/error/404";
    public static final String ERROR_PAGE_500 = "/error/500";
    public static final String ERROR_PAGE_403 = "/error/403";
    
    // Headers
    public static final String HEADER_API_KEY = "X-API-Key";
    public static final String HEADER_RATE_LIMIT = "X-Rate-Limit";
    public static final String HEADER_RATE_REMAINING = "X-Rate-Limit-Remaining";
    public static final String HEADER_LANGUAGE = "Accept-Language";
    
    // Cache Names
    public static final String CACHE_PRODUCTS = "products";
    public static final String CACHE_CATEGORIES = "categories";
    public static final String CACHE_BRANDS = "brands";
    public static final String CACHE_SETTINGS = "settings";
    
    // Private constructor to prevent instantiation
    private AppConstants() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}
  