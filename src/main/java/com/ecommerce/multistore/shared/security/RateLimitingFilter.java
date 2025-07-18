package com.ecommerce.multistore.shared.security;

import com.ecommerce.multistore.shared.constants.AppConstants;
import com.ecommerce.multistore.shared.dto.ApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * مرشح تحديد معدل الطلبات
 * Rate Limiting Filter
 * 
 * يحد من عدد الطلبات المسموح بها لكل عميل في فترة زمنية محددة
 * Limits the number of requests allowed per client in a specified time window
 * 
 * @author Multi-Store Team
 * @version 1.0
 * @since 2024-01-01
 */
@Component
public class RateLimitingFilter extends OncePerRequestFilter {

    private final ConcurrentHashMap<String, RateLimitInfo> rateLimitMap = new ConcurrentHashMap<>();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doFilterInternal(HttpServletRequest request, 
                                  HttpServletResponse response, 
                                  FilterChain filterChain) throws ServletException, IOException {

        String clientIp = getClientIpAddress(request);
        RateLimitInfo rateLimitInfo = rateLimitMap.computeIfAbsent(clientIp, k -> new RateLimitInfo());

        if (isRateLimitExceeded(rateLimitInfo)) {
            handleRateLimitExceeded(response, rateLimitInfo);
            return;
        }

        rateLimitInfo.incrementRequests();
        
        // إضافة Rate Limit headers للطلبات الناجحة
        addRateLimitHeaders(response, rateLimitInfo);
        
        filterChain.doFilter(request, response);
    }

    /**
     * معالجة تجاوز حد الطلبات
     * Handle rate limit exceeded
     */
    private void handleRateLimitExceeded(HttpServletResponse response, RateLimitInfo rateLimitInfo) throws IOException {
        // إعداد response status وheaders
        response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        // حساب وقت إعادة التعيين
        long resetTimeMillis = System.currentTimeMillis() + (AppConstants.RATE_LIMIT_WINDOW_MINUTES * 60 * 1000);
        int retryAfterSeconds = AppConstants.RATE_LIMIT_WINDOW_MINUTES * 60;
        
        // إضافة Rate Limit headers حسب RFC 6585
        response.setHeader("X-RateLimit-Limit", String.valueOf(AppConstants.RATE_LIMIT_REQUESTS));
        response.setHeader("X-RateLimit-Remaining", "0");
        response.setHeader("X-RateLimit-Reset", String.valueOf(resetTimeMillis));
        response.setHeader("Retry-After", String.valueOf(retryAfterSeconds));
        
        // إنشاء response موحد
        ApiResponse<Map<String, Object>> errorResponse = ApiResponse.<Map<String, Object>>error(
            "Rate limit exceeded",
            Map.of(
                "limit", AppConstants.RATE_LIMIT_REQUESTS,
                "windowMinutes", AppConstants.RATE_LIMIT_WINDOW_MINUTES,
                "retryAfterSeconds", retryAfterSeconds,
                "resetTime", resetTimeMillis,
                "currentRequests", rateLimitInfo.getRequestCount()
            ),
            HttpStatus.TOO_MANY_REQUESTS.value()
        );
        errorResponse.setMessageAr("تم تجاوز حد الطلبات المسموح");
        
        // تحويل إلى JSON وإرسال
        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }

    /**
     * إضافة Rate Limit headers للطلبات الناجحة
     * Add rate limit headers for successful requests
     */
    private void addRateLimitHeaders(HttpServletResponse response, RateLimitInfo rateLimitInfo) {
        int remainingRequests = Math.max(0, AppConstants.RATE_LIMIT_REQUESTS - rateLimitInfo.getRequestCount());
        long resetTimeMillis = System.currentTimeMillis() + (AppConstants.RATE_LIMIT_WINDOW_MINUTES * 60 * 1000);
        
        response.setHeader("X-RateLimit-Limit", String.valueOf(AppConstants.RATE_LIMIT_REQUESTS));
        response.setHeader("X-RateLimit-Remaining", String.valueOf(remainingRequests));
        response.setHeader("X-RateLimit-Reset", String.valueOf(resetTimeMillis));
    }

    /**
     * الحصول على عنوان IP الخاص بالعميل
     * Get client IP address
     */
    private String getClientIpAddress(HttpServletRequest request) {
        // التحقق من X-Forwarded-For header (للـ proxy servers)
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty() && !"unknown".equalsIgnoreCase(xForwardedFor)) {
            return xForwardedFor.split(",")[0].trim();
        }
        
        // التحقق من X-Real-IP header
        String xRealIp = request.getHeader("X-Real-IP");
        if (xRealIp != null && !xRealIp.isEmpty() && !"unknown".equalsIgnoreCase(xRealIp)) {
            return xRealIp;
        }
        
        // التحقق من X-Forwarded-For header (Cloudflare)
        String cfConnectingIp = request.getHeader("CF-Connecting-IP");
        if (cfConnectingIp != null && !cfConnectingIp.isEmpty() && !"unknown".equalsIgnoreCase(cfConnectingIp)) {
            return cfConnectingIp;
        }
        
        // استخدام الـ IP الافتراضي
        return request.getRemoteAddr();
    }

    /**
     * التحقق من تجاوز حد الطلبات
     * Check if rate limit is exceeded
     */
    private boolean isRateLimitExceeded(RateLimitInfo rateLimitInfo) {
        LocalDateTime now = LocalDateTime.now();

        // إعادة تعيين العداد إذا انتهت النافذة الزمنية
        if (now.isAfter(rateLimitInfo.getWindowStart().plusMinutes(AppConstants.RATE_LIMIT_WINDOW_MINUTES))) {
            rateLimitInfo.reset(now);
        }

        return rateLimitInfo.getRequestCount() >= AppConstants.RATE_LIMIT_REQUESTS;
    }

    /**
     * تنظيف البيانات المنتهية الصلاحية
     * Clean up expired entries
     */
    private void cleanupExpiredEntries() {
        LocalDateTime cutoffTime = LocalDateTime.now().minusMinutes(AppConstants.RATE_LIMIT_WINDOW_MINUTES * 2);
        
        rateLimitMap.entrySet().removeIf(entry -> 
            entry.getValue().getWindowStart().isBefore(cutoffTime)
        );
    }

    /**
     * معلومات تحديد معدل الطلبات
     * Rate limit information
     */
    private static class RateLimitInfo {
        private AtomicInteger requestCount = new AtomicInteger(0);
        private volatile LocalDateTime windowStart = LocalDateTime.now();

        /**
         * زيادة عدد الطلبات
         * Increment request count
         */
        public void incrementRequests() {
            requestCount.incrementAndGet();
        }

        /**
         * إعادة تعيين العداد
         * Reset counter
         */
        public void reset(LocalDateTime newWindowStart) {
            requestCount.set(0);
            windowStart = newWindowStart;
        }

        /**
         * الحصول على عدد الطلبات الحالي
         * Get current request count
         */
        public int getRequestCount() {
            return requestCount.get();
        }

        /**
         * الحصول على بداية النافذة الزمنية
         * Get window start time
         */
        public LocalDateTime getWindowStart() {
            return windowStart;
        }

        /**
         * الحصول على الطلبات المتبقية
         * Get remaining requests
         */
        public int getRemainingRequests() {
            return Math.max(0, AppConstants.RATE_LIMIT_REQUESTS - requestCount.get());
        }

        /**
         * التحقق من انتهاء النافذة الزمنية
         * Check if window has expired
         */
        public boolean isWindowExpired() {
            return LocalDateTime.now().isAfter(windowStart.plusMinutes(AppConstants.RATE_LIMIT_WINDOW_MINUTES));
        }
    }
}
