package com.ecommerce.multistore.shared.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * إعدادات Swagger/OpenAPI
 * Swagger/OpenAPI Configuration
 * 
 * يوفر واجهة توثيق تفاعلية لجميع APIs
 * Provides interactive documentation interface for all APIs
 * 
 * @author Multi-Store Team
 * @version 1.0
 * @since 2024-01-01
 */
@Configuration
public class SwaggerConfig {

    @Value("${server.port:8080}")
    private String serverPort;

    @Value("${spring.application.name:multi-store-backend}")
    private String applicationName;

    /**
     * إعداد OpenAPI للتوثيق
     * OpenAPI configuration for documentation
     * 
     * @return OpenAPI instance
     */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(createApiInfo())
                .servers(createServers())
                .components(createComponents())
                .security(createSecurityRequirements())
                .tags(createTags());
    }

    /**
     * إنشاء معلومات API
     * Create API information
     */
    private Info createApiInfo() {
        return new Info()
                .title("Multi-Store E-commerce API")
                .description("""
                    **API متكامل للمتجر الإلكتروني متعدد المجالات**
                    
                    A comprehensive RESTful API for multi-domain e-commerce platform built with Spring Boot.
                    
                    ## Features:
                    - 👥 User Management
                    - 🏷️ Brand Management  
                    - 📂 Category Management
                    - 🛍️ Product Management
                    - 📦 Order Management
                    - 💳 Payment Processing
                    - 📊 Analytics & Reporting
                    - 🔐 Security & Authentication
                    - 📱 Mobile-First Design
                    
                    ## Authentication:
                    - Use API Key in header: `X-API-Key`
                    - Or Bearer Token: `Authorization: Bearer <token>`
                    
                    ## Rate Limiting:
                    - 100 requests per minute per IP
                    - Headers: `X-RateLimit-Limit`, `X-RateLimit-Remaining`, `X-RateLimit-Reset`
                    
                    ## Response Format:
                    All responses follow unified `ApiResponse<T>` format with Arabic/English messages.
                    """)
                .version("1.0.0")
                .contact(new Contact()
                        .name("Multi-Store Development Team")
                        .email("dev@multistore.com")
                        .url("https://github.com/multistore/backend"))
                .license(new License()
                        .name("MIT License")
                        .url("https://opensource.org/licenses/MIT"));
    }

    /**
     * إنشاء قائمة الخوادم
     * Create servers list
     */
    private List<Server> createServers() {
        return List.of(
                new Server()
                        .url("http://localhost:" + serverPort)
                        .description("🔧 Development Server - خادم التطوير"),
                new Server()
                        .url("https://api-dev.multistore.com")
                        .description("🧪 Staging Server - خادم الاختبار"),
                new Server()
                        .url("https://api.multistore.com")
                        .description("🚀 Production Server - خادم الإنتاج")
        );
    }

    /**
     * إنشاء مكونات الأمان
     * Create security components
     */
    private Components createComponents() {
        return new Components()
                .addSecuritySchemes("ApiKey", new SecurityScheme()
                        .type(SecurityScheme.Type.APIKEY)
                        .in(SecurityScheme.In.HEADER)
                        .name("X-API-Key")
                        .description("API Key للتحقق من الهوية"))
                .addSecuritySchemes("Bearer", new SecurityScheme()
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT")
                        .description("JWT Bearer Token"))
                .addSecuritySchemes("Basic", new SecurityScheme()
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("basic")
                        .description("Basic Authentication"));
    }

    /**
     * إنشاء متطلبات الأمان
     * Create security requirements
     */
    private List<SecurityRequirement> createSecurityRequirements() {
        return List.of(
                new SecurityRequirement().addList("ApiKey"),
                new SecurityRequirement().addList("Bearer"),
                new SecurityRequirement().addList("Basic")
        );
    }

    /**
     * إنشاء العلامات
     * Create tags
     */
    private List<Tag> createTags() {
        return List.of(
                new Tag()
                        .name("Users")
                        .description("👥 إدارة المستخدمين - User Management"),
                new Tag()
                        .name("Brands")
                        .description("🏷️ إدارة العلامات التجارية - Brand Management"),
                new Tag()
                        .name("Categories")
                        .description("📂 إدارة الفئات - Category Management"),
                new Tag()
                        .name("Products")
                        .description("🛍️ إدارة المنتجات - Product Management"),
                new Tag()
                        .name("Orders")
                        .description("📦 إدارة الطلبات - Order Management"),
                new Tag()
                        .name("Payments")
                        .description("💳 إدارة المدفوعات - Payment Management"),
                new Tag()
                        .name("Analytics")
                        .description("📊 التحليلات والتقارير - Analytics & Reports"),
                new Tag()
                        .name("Inventory")
                        .description("📋 إدارة المخزون - Inventory Management")
        );
    }
}
