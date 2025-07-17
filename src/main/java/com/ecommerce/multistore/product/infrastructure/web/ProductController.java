package com.ecommerce.multistore.product.infrastructure.web;

import com.ecommerce.multistore.product.application.dto.CreateProductRequest;
import com.ecommerce.multistore.product.application.dto.ProductResponse;
import com.ecommerce.multistore.product.application.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * وحدة التحكم في المنتجات - REST API
 * Product Controller - REST API for product management
 * 
 * يوفر APIs لإدارة المنتجات بما في ذلك الإنشاء والبحث والتحديث والحذف
 * Provides APIs for product management including creation, search, update, and deletion
 * 
 * @author Multi-Store Team
 * @version 1.0
 * @since 2024-01-01
 */
@RestController
@RequestMapping("/api/v1/products")
@CrossOrigin(origins = "*")
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
     * @return ResponseEntity<ProductResponse> المنتج المُنشأ أو رسالة خطأ
     * 
     * @apiNote POST /api/v1/products
     * @since 1.0
     */
    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(@Valid @RequestBody CreateProductRequest request) {
        try {
            ProductResponse product = productService.createProduct(request);
            return new ResponseEntity<>(product, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
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
     * @return ResponseEntity<Page<ProductResponse>> صفحة من المنتجات
     * 
     * @apiNote GET /api/v1/products?page=0&size=10&sortBy=name&sortDir=asc
     * @since 1.0
     */
    @GetMapping
    public ResponseEntity<Page<ProductResponse>> getAllProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        
        Page<ProductResponse> products = productService.getAllProducts(page, size, sortBy, sortDir);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    /**
     * الحصول على المنتجات النشطة فقط
     * Get active products only
     * 
     * @param page رقم الصفحة
     * @param size عدد العناصر في الصفحة
     * @return ResponseEntity<Page<ProductResponse>> صفحة من المنتجات النشطة
     * 
     * @apiNote GET /api/v1/products/active?page=0&size=10
     * @since 1.0
     */
    @GetMapping("/active")
    public ResponseEntity<Page<ProductResponse>> getActiveProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Page<ProductResponse> products = productService.getActiveProducts(page, size);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    /**
     * الحصول على المنتجات المميزة
     * Get featured products
     * 
     * @return ResponseEntity<List<ProductResponse>> قائمة بالمنتجات المميزة
     * 
     * @apiNote GET /api/v1/products/featured
     * @since 1.0
     */
    @GetMapping("/featured")
    public ResponseEntity<List<ProductResponse>> getFeaturedProducts() {
        List<ProductResponse> products = productService.getFeaturedProducts();
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    /**
     * البحث عن منتج بواسطة UUID
     * Find product by UUID
     * 
     * @param id المعرف الفريد للمنتج
     * @return ResponseEntity<ProductResponse> المنتج أو 404
     * 
     * @apiNote GET /api/v1/products/{id}
     * @since 1.0
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable UUID id) {
        Optional<ProductResponse> product = productService.findById(id);
        return product.map(productResponse -> new ResponseEntity<>(productResponse, HttpStatus.OK))
                     .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * البحث عن منتج بواسطة Display ID
     * Find product by display ID
     * 
     * @param displayId المعرف المعروض (مثل PRD-000001)
     * @return ResponseEntity<ProductResponse> المنتج أو 404
     * 
     * @apiNote GET /api/v1/products/display/{displayId}
     * @since 1.0
     */
    @GetMapping("/display/{displayId}")
    public ResponseEntity<ProductResponse> getProductByDisplayId(@PathVariable String displayId) {
        Optional<ProductResponse> product = productService.findByDisplayId(displayId);
        return product.map(productResponse -> new ResponseEntity<>(productResponse, HttpStatus.OK))
                     .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * البحث عن منتج بواسطة SKU
     * Find product by SKU
     * 
     * @param sku رمز المنتج
     * @return ResponseEntity<ProductResponse> المنتج أو 404
     * 
     * @apiNote GET /api/v1/products/sku/{sku}
     * @since 1.0
     */
    @GetMapping("/sku/{sku}")
    public ResponseEntity<ProductResponse> getProductBySku(@PathVariable String sku) {
        Optional<ProductResponse> product = productService.findBySku(sku);
        return product.map(productResponse -> new ResponseEntity<>(productResponse, HttpStatus.OK))
                     .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * البحث عن منتج بواسطة Slug
     * Find product by slug
     * 
     * @param slug الرابط الودود
     * @return ResponseEntity<ProductResponse> المنتج أو 404
     * 
     * @apiNote GET /api/v1/products/slug/{slug}
     * @since 1.0
     */
    @GetMapping("/slug/{slug}")
    public ResponseEntity<ProductResponse> getProductBySlug(@PathVariable String slug) {
        Optional<ProductResponse> product = productService.findBySlug(slug);
        return product.map(productResponse -> new ResponseEntity<>(productResponse, HttpStatus.OK))
                     .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * البحث في المنتجات
     * Search products
     * 
     * @param q مصطلح البحث
     * @param page رقم الصفحة
     * @param size عدد العناصر في الصفحة
     * @return ResponseEntity<Page<ProductResponse>> صفحة من نتائج البحث
     * 
     * @apiNote GET /api/v1/products/search?q=smartwatch&page=0&size=10
     * @since 1.0
     */
    @GetMapping("/search")
    public ResponseEntity<Page<ProductResponse>> searchProducts(
            @RequestParam("q") String searchTerm,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Page<ProductResponse> products = productService.searchProducts(searchTerm, page, size);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    /**
     * البحث بواسطة الفئة
     * Find products by category
     * 
     * @param categoryId معرف الفئة
     * @param page رقم الصفحة
     * @param size عدد العناصر في الصفحة
     * @return ResponseEntity<Page<ProductResponse>> صفحة من المنتجات
     * 
     * @apiNote GET /api/v1/products/category/{categoryId}?page=0&size=10
     * @since 1.0
     */
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<Page<ProductResponse>> getProductsByCategory(
            @PathVariable UUID categoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Page<ProductResponse> products = productService.getProductsByCategory(categoryId, page, size);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    /**
     * البحث بواسطة العلامة التجارية
     * Find products by brand
     * 
     * @param brandId معرف العلامة التجارية
     * @param page رقم الصفحة
     * @param size عدد العناصر في الصفحة
     * @return ResponseEntity<Page<ProductResponse>> صفحة من المنتجات
     * 
     * @apiNote GET /api/v1/products/brand/{brandId}?page=0&size=10
     * @since 1.0
     */
    @GetMapping("/brand/{brandId}")
    public ResponseEntity<Page<ProductResponse>> getProductsByBrand(
            @PathVariable UUID brandId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Page<ProductResponse> products = productService.getProductsByBrand(brandId, page, size);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    /**
     * البحث في نطاق سعري
     * Find products by price range
     * 
     * @param minPrice أقل سعر
     * @param maxPrice أعلى سعر
     * @param page رقم الصفحة
     * @param size عدد العناصر في الصفحة
     * @return ResponseEntity<Page<ProductResponse>> صفحة من المنتجات
     * 
     * @apiNote GET /api/v1/products/price-range?minPrice=100&maxPrice=500&page=0&size=10
     * @since 1.0
     */
    @GetMapping("/price-range")
    public ResponseEntity<Page<ProductResponse>> getProductsByPriceRange(
            @RequestParam BigDecimal minPrice,
            @RequestParam BigDecimal maxPrice,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Page<ProductResponse> products = productService.getProductsByPriceRange(minPrice, maxPrice, page, size);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    /**
     * تحديث بيانات المنتج
     * Update product information
     * 
     * @param id معرف المنتج
     * @param request البيانات الجديدة
     * @return ResponseEntity<ProductResponse> المنتج المحدث أو رسالة خطأ
     * 
     * @apiNote PUT /api/v1/products/{id}
     * @since 1.0
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> updateProduct(
            @PathVariable UUID id,
            @Valid @RequestBody CreateProductRequest request) {
        try {
            ProductResponse product = productService.updateProduct(id, request);
            return new ResponseEntity<>(product, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * تحديث حالة المنتج
     * Update product status
     * 
     * @param id معرف المنتج
     * @param isActive الحالة الجديدة
     * @return ResponseEntity<ProductResponse> المنتج المحدث أو رسالة خطأ
     * 
     * @apiNote PUT /api/v1/products/{id}/status?isActive=true
     * @since 1.0
     */
    @PutMapping("/{id}/status")
    public ResponseEntity<ProductResponse> updateProductStatus(
            @PathVariable UUID id,
            @RequestParam Boolean isActive) {
        try {
            ProductResponse product = productService.updateProductStatus(id, isActive);
            return new ResponseEntity<>(product, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    /**
     * تحديث مخزون المنتج
     * Update product stock
     * 
     * @param id معرف المنتج
     * @param quantity الكمية الجديدة
     * @return ResponseEntity<ProductResponse> المنتج المحدث أو رسالة خطأ
     * 
     * @apiNote PUT /api/v1/products/{id}/stock?quantity=50
     * @since 1.0
     */
    @PutMapping("/{id}/stock")
    public ResponseEntity<ProductResponse> updateProductStock(
            @PathVariable UUID id,
            @RequestParam Integer quantity) {
        try {
            ProductResponse product = productService.updateProductStock(id, quantity);
            return new ResponseEntity<>(product, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * حذف المنتج
     * Delete product
     * 
     * @param id معرف المنتج
     * @return ResponseEntity<Void> استجابة فارغة أو رسالة خطأ
     * 
     * @apiNote DELETE /api/v1/products/{id}
     * @since 1.0
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable UUID id) {
        try {
            productService.deleteProduct(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * الحصول على المنتجات ذات المخزون المنخفض
     * Get products with low stock
     * 
     * @return ResponseEntity<List<ProductResponse>> قائمة بالمنتجات ذات المخزون المنخفض
     * 
     * @apiNote GET /api/v1/products/inventory/low-stock
     * @since 1.0
     */
    @GetMapping("/inventory/low-stock")
    public ResponseEntity<List<ProductResponse>> getLowStockProducts() {
        List<ProductResponse> products = productService.getLowStockProducts();
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    /**
     * الحصول على المنتجات غير المتوفرة
     * Get out of stock products
     * 
     * @return ResponseEntity<List<ProductResponse>> قائمة بالمنتجات غير المتوفرة
     * 
     * @apiNote GET /api/v1/products/inventory/out-of-stock
     * @since 1.0
     */
    @GetMapping("/inventory/out-of-stock")
    public ResponseEntity<List<ProductResponse>> getOutOfStockProducts() {
        List<ProductResponse> products = productService.getOutOfStockProducts();
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    /**
     * فحص صحة نظام المنتجات
     * Product system health check
     * 
     * @return ResponseEntity<String> حالة النظام
     * 
     * @apiNote GET /api/v1/products/health
     * @since 1.0
     */
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return new ResponseEntity<>("Product Service is running", HttpStatus.OK);
    }
}
