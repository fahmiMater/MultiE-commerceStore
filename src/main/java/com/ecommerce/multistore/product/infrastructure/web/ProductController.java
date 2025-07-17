package com.ecommerce.multistore.product.infrastructure.web;

import com.ecommerce.multistore.product.application.dto.CreateProductRequest;
import com.ecommerce.multistore.product.application.dto.ProductResponse;
import com.ecommerce.multistore.product.application.service.ProductService;
import com.ecommerce.multistore.shared.dto.ApiResponse;
import com.ecommerce.multistore.shared.dto.PaginatedResponse;
import com.ecommerce.multistore.shared.exception.ResourceNotFoundException;
import com.ecommerce.multistore.shared.exception.DuplicateResourceException;
import com.ecommerce.multistore.shared.exception.BusinessException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * وحدة التحكم في المنتجات المحسنة - REST API
 * Enhanced Product Controller - REST API for product management
 * 
 * يوفر APIs محسنة لإدارة المنتجات مع استجابات موحدة ومعالجة أخطاء متقدمة
 * Provides enhanced APIs for product management with unified responses and advanced error handling
 * 
 * @author Multi-Store Team
 * @version 2.0
 * @since 2024-01-01
 */
@RestController
@RequestMapping("/api/v1/products")
@CrossOrigin(origins = "*")
@Tag(name = "Products", description = "منتجات المتجر / Store Products")
public class ProductController {

    private final ProductService productService;

    /**
     * Constructor لحقن ProductService
     * Constructor for ProductService dependency injection
     * 
     * @param productService خدمة المنتجات
     */
    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    /**
     * إنشاء منتج جديد
     * Create a new product
     * 
     * @param request بيانات المنتج الجديد
     * @param httpRequest معلومات الطلب
     * @return ResponseEntity<ApiResponse<ProductResponse>>
     * 
     * @apiNote POST /api/v1/products
     * @since 2.0
     */
    @PostMapping
    @Operation(
        summary = "إنشاء منتج جديد / Create new product",
        description = "إنشاء منتج جديد في المتجر مع التحقق من صحة البيانات / Create a new product in the store with data validation"
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "201",
            description = "تم إنشاء المنتج بنجاح / Product created successfully",
            content = @Content(schema = @Schema(implementation = ApiResponse.class))
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400",
            description = "بيانات غير صحيحة / Invalid data",
            content = @Content(schema = @Schema(implementation = ApiResponse.class))
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "409",
            description = "المنتج موجود بالفعل / Product already exists",
            content = @Content(schema = @Schema(implementation = ApiResponse.class))
        )
    })
    public ResponseEntity<ApiResponse<ProductResponse>> createProduct(
            @Valid @RequestBody CreateProductRequest request,
            HttpServletRequest httpRequest) {
        
        try {
            ProductResponse product = productService.createProduct(request);
            
            ApiResponse<ProductResponse> response = ApiResponse.<ProductResponse>builder()
                    .success(true)
                    .message("Product created successfully")
                    .messageAr("تم إنشاء المنتج بنجاح")
                    .data(product)
                    .statusCode(HttpStatus.CREATED.value())
                    .requestId(generateRequestId(httpRequest))
                    .metadata(createSuccessMetadata("product_created", product.getDisplayId()))
                    .build();
            
            return new ResponseEntity<>(response, HttpStatus.CREATED);
            
        } catch (IllegalArgumentException e) {
            // This will be handled by GlobalExceptionHandler
            if (e.getMessage().contains("already exists")) {
                throw new DuplicateResourceException(e.getMessage(), "المنتج موجود بالفعل");
            }
            throw new BusinessException(e.getMessage(), "خطأ في إنشاء المنتج");
        }
    }

    /**
     * الحصول على جميع المنتجات مع الصفحات
     * Get all products with pagination
     * 
     * @param page رقم الصفحة (افتراضي: 0)
     * @param size عدد العناصر في الصفحة (افتراضي: 10)
     * @param sortBy الحقل المطلوب ترتيبه (افتراضي: createdAt)
     * @param sortDir اتجاه الترتيب (افتراضي: desc)
     * @param httpRequest معلومات الطلب
     * @return ResponseEntity<ApiResponse<PaginatedResponse<ProductResponse>>>
     * 
     * @apiNote GET /api/v1/products?page=0&size=10&sortBy=name&sortDir=asc
     * @since 2.0
     */
    @GetMapping
    @Operation(
        summary = "الحصول على جميع المنتجات / Get all products",
        description = "الحصول على جميع المنتجات مع إمكانية التصفح والترتيب / Get all products with pagination and sorting"
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "تم الحصول على المنتجات بنجاح / Products retrieved successfully",
            content = @Content(schema = @Schema(implementation = ApiResponse.class))
        )
    })
    public ResponseEntity<ApiResponse<PaginatedResponse<ProductResponse>>> getAllProducts(
            @Parameter(description = "رقم الصفحة / Page number") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "حجم الصفحة / Page size") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "ترتيب حسب / Sort by") @RequestParam(defaultValue = "createdAt") String sortBy,
            @Parameter(description = "اتجاه الترتيب / Sort direction") @RequestParam(defaultValue = "desc") String sortDir,
            HttpServletRequest httpRequest) {
        
        Page<ProductResponse> productsPage = productService.getAllProducts(page, size, sortBy, sortDir);
        PaginatedResponse<ProductResponse> paginatedResponse = PaginatedResponse.from(productsPage);
        
        ApiResponse<PaginatedResponse<ProductResponse>> response = ApiResponse.<PaginatedResponse<ProductResponse>>builder()
                .success(true)
                .message("Products retrieved successfully")
                .messageAr("تم الحصول على المنتجات بنجاح")
                .data(paginatedResponse)
                .statusCode(HttpStatus.OK.value())
                .requestId(generateRequestId(httpRequest))
                .metadata(createPaginationMetadata(page, size, productsPage.getTotalElements()))
                .build();
        
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * الحصول على المنتجات النشطة فقط
     * Get active products only
     * 
     * @param page رقم الصفحة
     * @param size عدد العناصر في الصفحة
     * @param httpRequest معلومات الطلب
     * @return ResponseEntity<ApiResponse<PaginatedResponse<ProductResponse>>>
     * 
     * @apiNote GET /api/v1/products/active?page=0&size=10
     * @since 2.0
     */
    @GetMapping("/active")
    @Operation(
        summary = "الحصول على المنتجات النشطة / Get active products",
        description = "الحصول على المنتجات النشطة فقط / Get only active products"
    )
    public ResponseEntity<ApiResponse<PaginatedResponse<ProductResponse>>> getActiveProducts(
            @Parameter(description = "رقم الصفحة / Page number") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "حجم الصفحة / Page size") @RequestParam(defaultValue = "10") int size,
            HttpServletRequest httpRequest) {
        
        Page<ProductResponse> productsPage = productService.getActiveProducts(page, size);
        PaginatedResponse<ProductResponse> paginatedResponse = PaginatedResponse.from(productsPage);
        
        ApiResponse<PaginatedResponse<ProductResponse>> response = ApiResponse.<PaginatedResponse<ProductResponse>>builder()
                .success(true)
                .message("Active products retrieved successfully")
                .messageAr("تم الحصول على المنتجات النشطة بنجاح")
                .data(paginatedResponse)
                .statusCode(HttpStatus.OK.value())
                .requestId(generateRequestId(httpRequest))
                .metadata(createPaginationMetadata(page, size, productsPage.getTotalElements()))
                .build();
        
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * الحصول على المنتجات المميزة
     * Get featured products
     * 
     * @param httpRequest معلومات الطلب
     * @return ResponseEntity<ApiResponse<List<ProductResponse>>>
     * 
     * @apiNote GET /api/v1/products/featured
     * @since 2.0
     */
    @GetMapping("/featured")
    @Operation(
        summary = "الحصول على المنتجات المميزة / Get featured products",
        description = "الحصول على المنتجات المميزة في المتجر / Get featured products in the store"
    )
    public ResponseEntity<ApiResponse<List<ProductResponse>>> getFeaturedProducts(
            HttpServletRequest httpRequest) {
        
        List<ProductResponse> products = productService.getFeaturedProducts();
        
        ApiResponse<List<ProductResponse>> response = ApiResponse.<List<ProductResponse>>builder()
                .success(true)
                .message("Featured products retrieved successfully")
                .messageAr("تم الحصول على المنتجات المميزة بنجاح")
                .data(products)
                .statusCode(HttpStatus.OK.value())
                .requestId(generateRequestId(httpRequest))
                .metadata(createSuccessMetadata("featured_products_count", products.size()))
                .build();
        
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * البحث عن منتج بواسطة UUID
     * Find product by UUID
     * 
     * @param id المعرف الفريد للمنتج
     * @param httpRequest معلومات الطلب
     * @return ResponseEntity<ApiResponse<ProductResponse>>
     * 
     * @apiNote GET /api/v1/products/{id}
     * @since 2.0
     */
    @GetMapping("/{id}")
    @Operation(
        summary = "البحث عن منتج بالمعرف / Find product by ID",
        description = "البحث عن منتج محدد بواسطة المعرف الفريد / Find a specific product by unique ID"
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "تم العثور على المنتج / Product found",
            content = @Content(schema = @Schema(implementation = ApiResponse.class))
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "المنتج غير موجود / Product not found",
            content = @Content(schema = @Schema(implementation = ApiResponse.class))
        )
    })
    public ResponseEntity<ApiResponse<ProductResponse>> getProductById(
            @Parameter(description = "معرف المنتج / Product ID") @PathVariable UUID id,
            HttpServletRequest httpRequest) {
        
        Optional<ProductResponse> product = productService.findById(id);
        
        if (product.isPresent()) {
            ApiResponse<ProductResponse> response = ApiResponse.<ProductResponse>builder()
                    .success(true)
                    .message("Product found successfully")
                    .messageAr("تم العثور على المنتج بنجاح")
                    .data(product.get())
                    .statusCode(HttpStatus.OK.value())
                    .requestId(generateRequestId(httpRequest))
                    .metadata(createSuccessMetadata("product_found", product.get().getDisplayId()))
                    .build();
            
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            throw ResourceNotFoundException.product(id);
        }
    }

    /**
     * البحث عن منتج بواسطة Display ID
     * Find product by display ID
     * 
     * @param displayId المعرف المعروض (مثل PRD-000001)
     * @param httpRequest معلومات الطلب
     * @return ResponseEntity<ApiResponse<ProductResponse>>
     * 
     * @apiNote GET /api/v1/products/display/{displayId}
     * @since 2.0
     */
    @GetMapping("/display/{displayId}")
    @Operation(
        summary = "البحث عن منتج بالمعرف المعروض / Find product by display ID",
        description = "البحث عن منتج بواسطة المعرف المعروض مثل PRD-000001 / Find product by display ID like PRD-000001"
    )
    public ResponseEntity<ApiResponse<ProductResponse>> getProductByDisplayId(
            @Parameter(description = "المعرف المعروض / Display ID") @PathVariable String displayId,
            HttpServletRequest httpRequest) {
        
        Optional<ProductResponse> product = productService.findByDisplayId(displayId);
        
        if (product.isPresent()) {
            ApiResponse<ProductResponse> response = ApiResponse.<ProductResponse>builder()
                    .success(true)
                    .message("Product found successfully")
                    .messageAr("تم العثور على المنتج بنجاح")
                    .data(product.get())
                    .statusCode(HttpStatus.OK.value())
                    .requestId(generateRequestId(httpRequest))
                    .metadata(createSuccessMetadata("product_found_by_display_id", displayId))
                    .build();
            
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            throw new ResourceNotFoundException("Product not found with display ID: " + displayId,
                                              "المنتج غير موجود بالمعرف المعروض: " + displayId);
        }
    }

    /**
     * البحث عن منتج بواسطة SKU
     * Find product by SKU
     * 
     * @param sku رمز المنتج
     * @param httpRequest معلومات الطلب
     * @return ResponseEntity<ApiResponse<ProductResponse>>
     * 
     * @apiNote GET /api/v1/products/sku/{sku}
     * @since 2.0
     */
    @GetMapping("/sku/{sku}")
    @Operation(
        summary = "البحث عن منتج بالرمز / Find product by SKU",
        description = "البحث عن منتج بواسطة رمز المنتج الفريد / Find product by unique SKU code"
    )
    public ResponseEntity<ApiResponse<ProductResponse>> getProductBySku(
            @Parameter(description = "رمز المنتج / Product SKU") @PathVariable String sku,
            HttpServletRequest httpRequest) {
        
        Optional<ProductResponse> product = productService.findBySku(sku);
        
        if (product.isPresent()) {
            ApiResponse<ProductResponse> response = ApiResponse.<ProductResponse>builder()
                    .success(true)
                    .message("Product found successfully")
                    .messageAr("تم العثور على المنتج بنجاح")
                    .data(product.get())
                    .statusCode(HttpStatus.OK.value())
                    .requestId(generateRequestId(httpRequest))
                    .metadata(createSuccessMetadata("product_found_by_sku", sku))
                    .build();
            
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            throw new ResourceNotFoundException("Product not found with SKU: " + sku,
                                              "المنتج غير موجود بالرمز: " + sku);
        }
    }

    /**
     * البحث عن منتج بواسطة Slug
     * Find product by slug
     * 
     * @param slug الرابط الودود
     * @param httpRequest معلومات الطلب
     * @return ResponseEntity<ApiResponse<ProductResponse>>
     * 
     * @apiNote GET /api/v1/products/slug/{slug}
     * @since 2.0
     */
    @GetMapping("/slug/{slug}")
    @Operation(
        summary = "البحث عن منتج بالرابط الودود / Find product by slug",
        description = "البحث عن منتج بواسطة الرابط الودود / Find product by friendly URL slug"
    )
    public ResponseEntity<ApiResponse<ProductResponse>> getProductBySlug(
            @Parameter(description = "الرابط الودود / URL slug") @PathVariable String slug,
            HttpServletRequest httpRequest) {
        
        Optional<ProductResponse> product = productService.findBySlug(slug);
        
        if (product.isPresent()) {
            ApiResponse<ProductResponse> response = ApiResponse.<ProductResponse>builder()
                    .success(true)
                    .message("Product found successfully")
                    .messageAr("تم العثور على المنتج بنجاح")
                    .data(product.get())
                    .statusCode(HttpStatus.OK.value())
                    .requestId(generateRequestId(httpRequest))
                    .metadata(createSuccessMetadata("product_found_by_slug", slug))
                    .build();
            
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            throw new ResourceNotFoundException("Product not found with slug: " + slug,
                                              "المنتج غير موجود بالرابط الودود: " + slug);
        }
    }

    /**
     * البحث في المنتجات
     * Search products
     * 
     * @param q مصطلح البحث
     * @param page رقم الصفحة
     * @param size عدد العناصر في الصفحة
     * @param httpRequest معلومات الطلب
     * @return ResponseEntity<ApiResponse<PaginatedResponse<ProductResponse>>>
     * 
     * @apiNote GET /api/v1/products/search?q=smartwatch&page=0&size=10
     * @since 2.0
     */
    @GetMapping("/search")
    @Operation(
        summary = "البحث في المنتجات / Search products",
        description = "البحث في المنتجات بواسطة الاسم أو الوصف / Search products by name or description"
    )
    public ResponseEntity<ApiResponse<PaginatedResponse<ProductResponse>>> searchProducts(
            @Parameter(description = "مصطلح البحث / Search term") @RequestParam("q") String searchTerm,
            @Parameter(description = "رقم الصفحة / Page number") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "حجم الصفحة / Page size") @RequestParam(defaultValue = "10") int size,
            HttpServletRequest httpRequest) {
        
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            throw new BusinessException("Search term cannot be empty", "مصطلح البحث لا يمكن أن يكون فارغاً");
        }
        
        Page<ProductResponse> productsPage = productService.searchProducts(searchTerm, page, size);
        PaginatedResponse<ProductResponse> paginatedResponse = PaginatedResponse.from(productsPage);
        
        ApiResponse<PaginatedResponse<ProductResponse>> response = ApiResponse.<PaginatedResponse<ProductResponse>>builder()
                .success(true)
                .message("Search completed successfully")
                .messageAr("تم البحث بنجاح")
                .data(paginatedResponse)
                .statusCode(HttpStatus.OK.value())
                .requestId(generateRequestId(httpRequest))
                .metadata(createSearchMetadata(searchTerm, productsPage.getTotalElements()))
                .build();
        
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * البحث بواسطة الفئة
     * Find products by category
     * 
     * @param categoryId معرف الفئة
     * @param page رقم الصفحة
     * @param size عدد العناصر في الصفحة
     * @param httpRequest معلومات الطلب
     * @return ResponseEntity<ApiResponse<PaginatedResponse<ProductResponse>>>
     * 
     * @apiNote GET /api/v1/products/category/{categoryId}?page=0&size=10
     * @since 2.0
     */
    @GetMapping("/category/{categoryId}")
    @Operation(
        summary = "البحث بواسطة الفئة / Find products by category",
        description = "الحصول على المنتجات حسب الفئة المحددة / Get products by specific category"
    )
    public ResponseEntity<ApiResponse<PaginatedResponse<ProductResponse>>> getProductsByCategory(
            @Parameter(description = "معرف الفئة / Category ID") @PathVariable UUID categoryId,
            @Parameter(description = "رقم الصفحة / Page number") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "حجم الصفحة / Page size") @RequestParam(defaultValue = "10") int size,
            HttpServletRequest httpRequest) {
        
        Page<ProductResponse> productsPage = productService.getProductsByCategory(categoryId, page, size);
        PaginatedResponse<ProductResponse> paginatedResponse = PaginatedResponse.from(productsPage);
        
        ApiResponse<PaginatedResponse<ProductResponse>> response = ApiResponse.<PaginatedResponse<ProductResponse>>builder()
                .success(true)
                .message("Products by category retrieved successfully")
                .messageAr("تم الحصول على المنتجات حسب الفئة بنجاح")
                .data(paginatedResponse)
                .statusCode(HttpStatus.OK.value())
                .requestId(generateRequestId(httpRequest))
                .metadata(createCategoryMetadata(categoryId, productsPage.getTotalElements()))
                .build();
        
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * البحث بواسطة العلامة التجارية
     * Find products by brand
     * 
     * @param brandId معرف العلامة التجارية
     * @param page رقم الصفحة
     * @param size عدد العناصر في الصفحة
     * @param httpRequest معلومات الطلب
     * @return ResponseEntity<ApiResponse<PaginatedResponse<ProductResponse>>>
     * 
     * @apiNote GET /api/v1/products/brand/{brandId}?page=0&size=10
     * @since 2.0
     */
    @GetMapping("/brand/{brandId}")
    @Operation(
        summary = "البحث بواسطة العلامة التجارية / Find products by brand",
        description = "الحصول على المنتجات حسب العلامة التجارية / Get products by specific brand"
    )
    public ResponseEntity<ApiResponse<PaginatedResponse<ProductResponse>>> getProductsByBrand(
            @Parameter(description = "معرف العلامة التجارية / Brand ID") @PathVariable UUID brandId,
            @Parameter(description = "رقم الصفحة / Page number") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "حجم الصفحة / Page size") @RequestParam(defaultValue = "10") int size,
            HttpServletRequest httpRequest) {
        
        Page<ProductResponse> productsPage = productService.getProductsByBrand(brandId, page, size);
        PaginatedResponse<ProductResponse> paginatedResponse = PaginatedResponse.from(productsPage);
        
        ApiResponse<PaginatedResponse<ProductResponse>> response = ApiResponse.<PaginatedResponse<ProductResponse>>builder()
                .success(true)
                .message("Products by brand retrieved successfully")
                .messageAr("تم الحصول على المنتجات حسب العلامة التجارية بنجاح")
                .data(paginatedResponse)
                .statusCode(HttpStatus.OK.value())
                .requestId(generateRequestId(httpRequest))
                .metadata(createBrandMetadata(brandId, productsPage.getTotalElements()))
                .build();
        
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * البحث في نطاق سعري
     * Find products by price range
     * 
     * @param minPrice أقل سعر
     * @param maxPrice أعلى سعر
     * @param page رقم الصفحة
     * @param size عدد العناصر في الصفحة
     * @param httpRequest معلومات الطلب
     * @return ResponseEntity<ApiResponse<PaginatedResponse<ProductResponse>>>
     * 
     * @apiNote GET /api/v1/products/price-range?minPrice=100&maxPrice=500&page=0&size=10
     * @since 2.0
     */
    @GetMapping("/price-range")
    @Operation(
        summary = "البحث في نطاق سعري / Find products by price range",
        description = "الحصول على المنتجات ضمن نطاق سعري محدد / Get products within specific price range"
    )
    public ResponseEntity<ApiResponse<PaginatedResponse<ProductResponse>>> getProductsByPriceRange(
            @Parameter(description = "أقل سعر / Minimum price") @RequestParam BigDecimal minPrice,
            @Parameter(description = "أعلى سعر / Maximum price") @RequestParam BigDecimal maxPrice,
            @Parameter(description = "رقم الصفحة / Page number") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "حجم الصفحة / Page size") @RequestParam(defaultValue = "10") int size,
            HttpServletRequest httpRequest) {
        
        if (minPrice.compareTo(maxPrice) > 0) {
            throw new BusinessException("Minimum price cannot be greater than maximum price", 
                                      "أقل سعر لا يمكن أن يكون أكبر من أعلى سعر");
        }
        
        Page<ProductResponse> productsPage = productService.getProductsByPriceRange(minPrice, maxPrice, page, size);
        PaginatedResponse<ProductResponse> paginatedResponse = PaginatedResponse.from(productsPage);
        
        ApiResponse<PaginatedResponse<ProductResponse>> response = ApiResponse.<PaginatedResponse<ProductResponse>>builder()
                .success(true)
                .message("Products by price range retrieved successfully")
                .messageAr("تم الحصول على المنتجات في النطاق السعري بنجاح")
                .data(paginatedResponse)
                .statusCode(HttpStatus.OK.value())
                .requestId(generateRequestId(httpRequest))
                .metadata(createPriceRangeMetadata(minPrice, maxPrice, productsPage.getTotalElements()))
                .build();
        
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * تحديث بيانات المنتج
     * Update product information
     * 
     * @param id معرف المنتج
     * @param request البيانات الجديدة
     * @param httpRequest معلومات الطلب
     * @return ResponseEntity<ApiResponse<ProductResponse>>
     * 
     * @apiNote PUT /api/v1/products/{id}
     * @since 2.0
     */
    @PutMapping("/{id}")
    @Operation(
        summary = "تحديث بيانات المنتج / Update product information",
        description = "تحديث بيانات منتج موجود / Update existing product information"
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "تم تحديث المنتج بنجاح / Product updated successfully",
            content = @Content(schema = @Schema(implementation = ApiResponse.class))
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "المنتج غير موجود / Product not found",
            content = @Content(schema = @Schema(implementation = ApiResponse.class))
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400",
            description = "بيانات غير صحيحة / Invalid data",
            content = @Content(schema = @Schema(implementation = ApiResponse.class))
        )
    })
    public ResponseEntity<ApiResponse<ProductResponse>> updateProduct(
            @Parameter(description = "معرف المنتج / Product ID") @PathVariable UUID id,
            @Valid @RequestBody CreateProductRequest request,
            HttpServletRequest httpRequest) {
        
        try {
            ProductResponse product = productService.updateProduct(id, request);
            
            ApiResponse<ProductResponse> response = ApiResponse.<ProductResponse>builder()
                    .success(true)
                    .message("Product updated successfully")
                    .messageAr("تم تحديث المنتج بنجاح")
                    .data(product)
                    .statusCode(HttpStatus.OK.value())
                    .requestId(generateRequestId(httpRequest))
                    .metadata(createSuccessMetadata("product_updated", product.getDisplayId()))
                    .build();
            
            return new ResponseEntity<>(response, HttpStatus.OK);
            
        } catch (IllegalArgumentException e) {
            // This will be handled by GlobalExceptionHandler
            if (e.getMessage().contains("not found")) {
                throw ResourceNotFoundException.product(id);
            }
            if (e.getMessage().contains("already exists")) {
                throw new DuplicateResourceException(e.getMessage(), "البيانات مكررة");
            }
            throw new BusinessException(e.getMessage(), "خطأ في تحديث المنتج");
        }
    }

    /**
     * تحديث حالة المنتج
     * Update product status
     * 
     * @param id معرف المنتج
     * @param isActive الحالة الجديدة
     * @param httpRequest معلومات الطلب
     * @return ResponseEntity<ApiResponse<ProductResponse>>
     * 
     * @apiNote PUT /api/v1/products/{id}/status?isActive=true
     * @since 2.0
     */
    @PutMapping("/{id}/status")
    @Operation(
        summary = "تحديث حالة المنتج / Update product status",
        description = "تفعيل أو إلغاء تفعيل المنتج / Activate or deactivate product"
    )
    public ResponseEntity<ApiResponse<ProductResponse>> updateProductStatus(
            @Parameter(description = "معرف المنتج / Product ID") @PathVariable UUID id,
            @Parameter(description = "الحالة الجديدة / New status") @RequestParam Boolean isActive,
            HttpServletRequest httpRequest) {
        
        try {
            ProductResponse product = productService.updateProductStatus(id, isActive);
            
            String message = isActive ? "Product activated successfully" : "Product deactivated successfully";
            String messageAr = isActive ? "تم تفعيل المنتج بنجاح" : "تم إلغاء تفعيل المنتج بنجاح";
            
            ApiResponse<ProductResponse> response = ApiResponse.<ProductResponse>builder()
                    .success(true)
                    .message(message)
                    .messageAr(messageAr)
                    .data(product)
                    .statusCode(HttpStatus.OK.value())
                    .requestId(generateRequestId(httpRequest))
                    .metadata(createSuccessMetadata("product_status_updated", 
                                                  Map.of("productId", product.getDisplayId(), 
                                                        "isActive", isActive)))
                    .build();
            
            return new ResponseEntity<>(response, HttpStatus.OK);
            
        } catch (IllegalArgumentException e) {
            throw ResourceNotFoundException.product(id);
        }
    }

    /**
     * تحديث مخزون المنتج
     * Update product stock
     * 
     * @param id معرف المنتج
     * @param quantity الكمية الجديدة
     * @param httpRequest معلومات الطلب
     * @return ResponseEntity<ApiResponse<ProductResponse>>
     * 
     * @apiNote PUT /api/v1/products/{id}/stock?quantity=50
     * @since 2.0
     */
    @PutMapping("/{id}/stock")
    @Operation(
        summary = "تحديث مخزون المنتج / Update product stock",
        description = "تحديث كمية المخزون للمنتج / Update stock quantity for product"
    )
    public ResponseEntity<ApiResponse<ProductResponse>> updateProductStock(
            @Parameter(description = "معرف المنتج / Product ID") @PathVariable UUID id,
            @Parameter(description = "الكمية الجديدة / New quantity") @RequestParam Integer quantity,
            HttpServletRequest httpRequest) {
        
        try {
            ProductResponse product = productService.updateProductStock(id, quantity);
            
            ApiResponse<ProductResponse> response = ApiResponse.<ProductResponse>builder()
                    .success(true)
                    .message("Product stock updated successfully")
                    .messageAr("تم تحديث مخزون المنتج بنجاح")
                    .data(product)
                    .statusCode(HttpStatus.OK.value())
                    .requestId(generateRequestId(httpRequest))
                    .metadata(createSuccessMetadata("product_stock_updated", 
                                                  Map.of("productId", product.getDisplayId(), 
                                                        "newQuantity", quantity)))
                    .build();
            
            return new ResponseEntity<>(response, HttpStatus.OK);
            
        } catch (IllegalArgumentException e) {
            if (e.getMessage().contains("not found")) {
                throw ResourceNotFoundException.product(id);
            }
            throw new BusinessException(e.getMessage(), "خطأ في تحديث المخزون");
        }
    }

    /**
     * حذف المنتج
     * Delete product
     * 
     * @param id معرف المنتج
     * @param httpRequest معلومات الطلب
     * @return ResponseEntity<ApiResponse<Void>>
     * 
     * @apiNote DELETE /api/v1/products/{id}
     * @since 2.0
     */
    @DeleteMapping("/{id}")
    @Operation(
        summary = "حذف المنتج / Delete product",
        description = "حذف منتج من المتجر نهائياً / Permanently delete product from store"
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "تم حذف المنتج بنجاح / Product deleted successfully",
            content = @Content(schema = @Schema(implementation = ApiResponse.class))
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "المنتج غير موجود / Product not found",
            content = @Content(schema = @Schema(implementation = ApiResponse.class))
        )
    })
    public ResponseEntity<ApiResponse<Void>> deleteProduct(
            @Parameter(description = "معرف المنتج / Product ID") @PathVariable UUID id,
            HttpServletRequest httpRequest) {
        
        try {
            productService.deleteProduct(id);
            
            ApiResponse<Void> response = ApiResponse.<Void>builder()
                    .success(true)
                    .message("Product deleted successfully")
                    .messageAr("تم حذف المنتج بنجاح")
                    .statusCode(HttpStatus.OK.value())
                    .requestId(generateRequestId(httpRequest))
                    .metadata(createSuccessMetadata("product_deleted", id))
                    .build();
            
            return new ResponseEntity<>(response, HttpStatus.OK);
            
        } catch (IllegalArgumentException e) {
            throw ResourceNotFoundException.product(id);
        }
    }

    /**
     * الحصول على المنتجات ذات المخزون المنخفض
     * Get products with low stock
     * 
     * @param httpRequest معلومات الطلب
     * @return ResponseEntity<ApiResponse<List<ProductResponse>>>
     * 
     * @apiNote GET /api/v1/products/inventory/low-stock
     * @since 2.0
     */
    @GetMapping("/inventory/low-stock")
    @Operation(
        summary = "المنتجات ذات المخزون المنخفض / Products with low stock",
        description = "الحصول على المنتجات التي تحتاج إلى إعادة تجهيز المخزون / Get products that need stock replenishment"
    )
    public ResponseEntity<ApiResponse<List<ProductResponse>>> getLowStockProducts(
            HttpServletRequest httpRequest) {
        
        List<ProductResponse> products = productService.getLowStockProducts();
        
        ApiResponse<List<ProductResponse>> response = ApiResponse.<List<ProductResponse>>builder()
                .success(true)
                .message("Low stock products retrieved successfully")
                .messageAr("تم الحصول على المنتجات ذات المخزون المنخفض بنجاح")
                .data(products)
                .statusCode(HttpStatus.OK.value())
                .requestId(generateRequestId(httpRequest))
                .metadata(createSuccessMetadata("low_stock_products_count", products.size()))
                .build();
        
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * الحصول على المنتجات غير المتوفرة
     * Get out of stock products
     * 
     * @param httpRequest معلومات الطلب
     * @return ResponseEntity<ApiResponse<List<ProductResponse>>>
     * 
     * @apiNote GET /api/v1/products/inventory/out-of-stock
     * @since 2.0
     */
    @GetMapping("/inventory/out-of-stock")
    @Operation(
        summary = "المنتجات غير المتوفرة / Out of stock products",
        description = "الحصول على المنتجات غير المتوفرة في المخزون / Get products that are out of stock"
    )
    public ResponseEntity<ApiResponse<List<ProductResponse>>> getOutOfStockProducts(
            HttpServletRequest httpRequest) {
        
        List<ProductResponse> products = productService.getOutOfStockProducts();
        
        ApiResponse<List<ProductResponse>> response = ApiResponse.<List<ProductResponse>>builder()
                .success(true)
                .message("Out of stock products retrieved successfully")
                .messageAr("تم الحصول على المنتجات غير المتوفرة بنجاح")
                .data(products)
                .statusCode(HttpStatus.OK.value())
                .requestId(generateRequestId(httpRequest))
                .metadata(createSuccessMetadata("out_of_stock_products_count", products.size()))
                .build();
        
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * فحص صحة نظام المنتجات
     * Product system health check
     * 
     * @param httpRequest معلومات الطلب
     * @return ResponseEntity<ApiResponse<String>>
     * 
     * @apiNote GET /api/v1/products/health
     * @since 2.0
     */
    @GetMapping("/health")
    @Operation(
        summary = "فحص صحة النظام / System health check",
        description = "فحص حالة وصحة نظام المنتجات / Check product system health status"
    )
    public ResponseEntity<ApiResponse<String>> health(HttpServletRequest httpRequest) {
        
        ApiResponse<String> response = ApiResponse.<String>builder()
                .success(true)
                .message("Product service is running")
                .messageAr("خدمة المنتجات تعمل بشكل طبيعي")
                .data("OK")
                .statusCode(HttpStatus.OK.value())
                .requestId(generateRequestId(httpRequest))
                .metadata(createSuccessMetadata("health_check", "OK"))
                .build();
        
        return new ResponseEntity<>(response, HttpStatus.OK);
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
        return "REQ-" + System.currentTimeMillis() + "-" + 
               Integer.toHexString(request.hashCode()).toUpperCase();
    }

    /**
     * إنشاء معلومات إضافية للنجاح
     * Create success metadata
     * 
     * @param operation العملية
     * @param data البيانات الإضافية
     * @return Map<String, Object>
     */
    private Map<String, Object> createSuccessMetadata(String operation, Object data) {
        Map<String, Object> metadata = new java.util.HashMap<>();
        metadata.put("operation", operation);
        metadata.put("timestamp", java.time.LocalDateTime.now());
        metadata.put("data", data);
        return metadata;
    }

    /**
     * إنشاء معلومات إضافية للصفحات
     * Create pagination metadata
     * 
     * @param page رقم الصفحة
     * @param size حجم الصفحة
     * @param totalElements إجمالي العناصر
     * @return Map<String, Object>
     */
    private Map<String, Object> createPaginationMetadata(int page, int size, long totalElements) {
        Map<String, Object> metadata = new java.util.HashMap<>();
        metadata.put("operation", "pagination");
        metadata.put("currentPage", page);
        metadata.put("pageSize", size);
        metadata.put("totalElements", totalElements);
        metadata.put("totalPages", (int) Math.ceil((double) totalElements / size));
        metadata.put("timestamp", java.time.LocalDateTime.now());
        return metadata;
    }

    /**
     * إنشاء معلومات إضافية للبحث
     * Create search metadata
     * 
     * @param searchTerm مصطلح البحث
     * @param totalResults إجمالي النتائج
     * @return Map<String, Object>
     */
    private Map<String, Object> createSearchMetadata(String searchTerm, long totalResults) {
        Map<String, Object> metadata = new java.util.HashMap<>();
        metadata.put("operation", "search");
        metadata.put("searchTerm", searchTerm);
        metadata.put("totalResults", totalResults);
        metadata.put("timestamp", java.time.LocalDateTime.now());
        return metadata;
    }

    /**
     * إنشاء معلومات إضافية للفئة
     * Create category metadata
     * 
     * @param categoryId معرف الفئة
     * @param totalProducts إجمالي المنتجات
     * @return Map<String, Object>
     */
    private Map<String, Object> createCategoryMetadata(UUID categoryId, long totalProducts) {
        Map<String, Object> metadata = new java.util.HashMap<>();
        metadata.put("operation", "filter_by_category");
        metadata.put("categoryId", categoryId);
        metadata.put("totalProducts", totalProducts);
        metadata.put("timestamp", java.time.LocalDateTime.now());
        return metadata;
    }

    /**
     * إنشاء معلومات إضافية للعلامة التجارية
     * Create brand metadata
     * 
     * @param brandId معرف العلامة التجارية
     * @param totalProducts إجمالي المنتجات
     * @return Map<String, Object>
     */
    private Map<String, Object> createBrandMetadata(UUID brandId, long totalProducts) {
        Map<String, Object> metadata = new java.util.HashMap<>();
        metadata.put("operation", "filter_by_brand");
        metadata.put("brandId", brandId);
        metadata.put("totalProducts", totalProducts);
        metadata.put("timestamp", java.time.LocalDateTime.now());
        return metadata;
    }

    /**
     * إنشاء معلومات إضافية للنطاق السعري
     * Create price range metadata
     * 
     * @param minPrice أقل سعر
     * @param maxPrice أعلى سعر
     * @param totalProducts إجمالي المنتجات
     * @return Map<String, Object>
     */
    private Map<String, Object> createPriceRangeMetadata(BigDecimal minPrice, BigDecimal maxPrice, long totalProducts) {
        Map<String, Object> metadata = new java.util.HashMap<>();
        metadata.put("operation", "filter_by_price_range");
        metadata.put("minPrice", minPrice);
        metadata.put("maxPrice", maxPrice);
        metadata.put("totalProducts", totalProducts);
        metadata.put("timestamp", java.time.LocalDateTime.now());
        return metadata;
    }
}
