
/* ---- File: src/main/java/com/ecommerce/multistore/shared/security/ApiKeyAuthFilter.java ---- */

package com.ecommerce.multistore.shared.security;

import com.ecommerce.multistore.shared.constants.AppConstants;
import com.ecommerce.multistore.shared.dto.ApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * مرشح مصادقة مفتاح API
 * API Key Authentication Filter
 */
@Component
public class ApiKeyAuthFilter extends OncePerRequestFilter {

    @Value("${app.api.key:default-api-key}")
    private String validApiKey;

    @Override
    protected void doFilterInternal(HttpServletRequest request, 
                                  HttpServletResponse response, 
                                  FilterChain filterChain) throws ServletException, IOException {

        String requestPath = request.getRequestURI();

        // تخطي المسارات العامة
        if (isPublicPath(requestPath)) {
            filterChain.doFilter(request, response);
            return;
        }

        String apiKey = request.getHeader(AppConstants.API_KEY_HEADER);

       if (apiKey == null || !validApiKey.equals(apiKey)) {
    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");
    
    // استخدام ApiResponse للتنسيق الموحد
   ApiResponse<Void> errorResponse = ApiResponse.<Void>unauthorized("Invalid API Key");
    errorResponse.setMessageAr("مفتاح API غير صحيح");
    
    // تحويل إلى JSON
    ObjectMapper objectMapper = new ObjectMapper();
    String jsonString = objectMapper.writeValueAsString(errorResponse);
    
    response.getWriter().write(jsonString);
    return;
}


        filterChain.doFilter(request, response);
    }

    private boolean isPublicPath(String path) {
        return path.startsWith("/api/v1/auth/") ||
               path.startsWith("/swagger-ui/") ||
               path.startsWith("/v3/api-docs") ||
               path.equals("/health") ||
               path.equals("/actuator/health");
    }
}
