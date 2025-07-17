package com.ecommerce.multistore.product.application.service;

import com.ecommerce.multistore.product.application.dto.CreateProductRequest;
import com.ecommerce.multistore.product.application.dto.ProductResponse;
import com.ecommerce.multistore.product.domain.Product;
import com.ecommerce.multistore.product.infrastructure.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * خدمة إدارة المنتجات
 * تحتوي على جميع العمليات المتعلقة بالمنتجات مثل الإنشاء والتحديث والبحث
 * 
 * Product Management Service
 * Contains all product-related operations such as creation, update, and search
 * 
 * @author Multi-Store Team
 * @version 1.0
 * @since 2024-01-01
 */
@Service
@Transactional
public class ProductService {

    private final ProductRepository productRepository;

    /**
     * Constructor لحقن Dependencies
     * Constructor for dependency injection
     * 
     * @param productRepository مستودع المنتجات للتعامل مع قاعدة البيانات
     */
    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    /**
     * إنشاء منتج جديد
     * Creates a new product with validation
     * 
     * @param request بيانات المنتج الجديد
     * @return ProductResponse بيانات المنتج المُنشأ
     * @throws IllegalArgumentException إذا كان SKU أو Slug مُستخدم مسبقاً
     * 
     * @see CreateProductRequest
     * @see ProductResponse
     */
    public ProductResponse createProduct(CreateProductRequest request) {
        // التحقق من عدم تكرار SKU
        validateProductUniqueness(request.getSku(), request.getSlug());

        // إنشاء كائن المنتج
        Product product = buildProductFromRequest(request);
        
        // توليد Slug إذا لم يُحدد
        if (product.getSlug() == null || product.getSlug().trim().isEmpty()) {
            product.setSlug(generateSlug(product.getName()));
        }
        
        // حفظ المنتج
        Product savedProduct = productRepository.save(product);
        
        // تحويل إلى Response وإرجاع النتيجة
        return convertToResponse(savedProduct);
    }

    /**
     * البحث عن منتج بواسطة UUID
     * Finds a product by UUID
     * 
     * @param id المعرف الفريد للمنتج
     * @return Optional<ProductResponse> المنتج إذا وُجد، أو Optional.empty()
     */
    @Transactional(readOnly = true)
    public Optional<ProductResponse> findById(UUID id) {
        return productRepository.findById(id)
                .map(this::convertToResponse);
    }

    /**
     * البحث عن منتج بواسطة Display ID
     * Finds a product by display ID
     * 
     * @param displayId المعرف المعروض للمنتج (مثل PRD-000001)
     * @return Optional<ProductResponse> المنتج إذا وُجد، أو Optional.empty()
     */
    @Transactional(readOnly = true)
    public Optional<ProductResponse> findByDisplayId(String displayId) {
        return productRepository.findByDisplayId(displayId)
                .map(this::convertToResponse);
    }

    /**
     * البحث عن منتج بواسطة SKU
     * Finds a product by SKU
     * 
     * @param sku رمز المنتج
     * @return Optional<ProductResponse> المنتج إذا وُجد، أو Optional.empty()
     */
    @Transactional(readOnly = true)
    public Optional<ProductResponse> findBySku(String sku) {
        return productRepository.findBySku(sku)
                .map(this::convertToResponse);
    }

    /**
     * البحث عن منتج بواسطة Slug
     * Finds a product by slug
     * 
     * @param slug الرابط الودود
     * @return Optional<ProductResponse> المنتج إذا وُجد، أو Optional.empty()
     */
    @Transactional(readOnly = true)
    public Optional<ProductResponse> findBySlug(String slug) {
        return productRepository.findBySlug(slug)
                .map(this::convertToResponse);
    }

    /**
     * الحصول على جميع المنتجات مع الصفحات
     * Retrieves all products with pagination
     * 
     * @param page رقم الصفحة (يبدأ من 0)
     * @param size عدد العناصر في الصفحة
     * @param sortBy الحقل المطلوب ترتيبه
     * @param sortDir اتجاه الترتيب (asc أو desc)
     * @return Page<ProductResponse> صفحة من المنتجات
     */
    @Transactional(readOnly = true)
    public Page<ProductResponse> getAllProducts(int page, int size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
                   Sort.by(sortBy).descending() : 
                   Sort.by(sortBy).ascending();
        
        Pageable pageable = PageRequest.of(page, size, sort);
        
        return productRepository.findAll(pageable)
                .map(this::convertToResponse);
    }

    /**
     * الحصول على المنتجات النشطة فقط
     * Retrieves active products only
     * 
     * @param page رقم الصفحة
     * @param size عدد العناصر في الصفحة
     * @return Page<ProductResponse> صفحة من المنتجات النشطة
     */
    @Transactional(readOnly = true)
    public Page<ProductResponse> getActiveProducts(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return productRepository.findByIsActiveTrue(pageable)
                .map(this::convertToResponse);
    }

    /**
     * الحصول على المنتجات المميزة
     * Retrieves featured products
     * 
     * @return List<ProductResponse> قائمة بالمنتجات المميزة
     */
    @Transactional(readOnly = true)
    public List<ProductResponse> getFeaturedProducts() {
        return productRepository.findByIsFeaturedTrueAndIsActiveTrue()
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    /**
     * البحث في المنتجات بواسطة النص
     * Search products by text
     * 
     * @param searchTerm مصطلح البحث
     * @param page رقم الصفحة
     * @param size عدد العناصر في الصفحة
     * @return Page<ProductResponse> صفحة من نتائج البحث
     */
    @Transactional(readOnly = true)
    public Page<ProductResponse> searchProducts(String searchTerm, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("name").ascending());
        return productRepository.searchProducts(searchTerm, pageable)
                .map(this::convertToResponse);
    }

    /**
     * البحث بواسطة الفئة
     * Find products by category
     * 
     * @param categoryId معرف الفئة
     * @param page رقم الصفحة
     * @param size عدد العناصر في الصفحة
     * @return Page<ProductResponse> صفحة من المنتجات
     */
    @Transactional(readOnly = true)
    public Page<ProductResponse> getProductsByCategory(UUID categoryId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("name").ascending());
        return productRepository.findByCategoryIdAndIsActiveTrue(categoryId, pageable)
                .map(this::convertToResponse);
    }

    /**
     * البحث بواسطة العلامة التجارية
     * Find products by brand
     * 
     * @param brandId معرف العلامة التجارية
     * @param page رقم الصفحة
     * @param size عدد العناصر في الصفحة
     * @return Page<ProductResponse> صفحة من المنتجات
     */
    @Transactional(readOnly = true)
    public Page<ProductResponse> getProductsByBrand(UUID brandId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("name").ascending());
        return productRepository.findByBrandIdAndIsActiveTrue(brandId, pageable)
                .map(this::convertToResponse);
    }

    /**
     * البحث في نطاق سعري
     * Find products by price range
     * 
     * @param minPrice أقل سعر
     * @param maxPrice أعلى سعر
     * @param page رقم الصفحة
     * @param size عدد العناصر في الصفحة
     * @return Page<ProductResponse> صفحة من المنتجات
     */
    @Transactional(readOnly = true)
    public Page<ProductResponse> getProductsByPriceRange(
            BigDecimal minPrice, BigDecimal maxPrice, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("price").ascending());
        return productRepository.findByPriceBetweenAndIsActiveTrue(minPrice, maxPrice, pageable)
                .map(this::convertToResponse);
    }

    /**
     * تحديث بيانات المنتج
     * Update product information
     * 
     * @param productId معرف المنتج
     * @param request البيانات الجديدة
     * @return ProductResponse المنتج المحدث
     * @throws IllegalArgumentException إذا لم يوجد المنتج
     */
    public ProductResponse updateProduct(UUID productId, CreateProductRequest request) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found with ID: " + productId));

        // التحقق من تكرار SKU إذا تم تغييره
        if (!product.getSku().equals(request.getSku()) && 
            productRepository.existsBySku(request.getSku())) {
            throw new IllegalArgumentException("SKU already exists: " + request.getSku());
        }

        // تحديث البيانات
        updateProductFromRequest(product, request);
        product.setUpdatedAt(LocalDateTime.now());

        Product updatedProduct = productRepository.save(product);
        return convertToResponse(updatedProduct);
    }

    /**
     * تحديث حالة المنتج (نشط/غير نشط)
     * Update product status (active/inactive)
     * 
     * @param productId معرف المنتج
     * @param isActive الحالة الجديدة
     * @return ProductResponse المنتج المحدث
     * @throws IllegalArgumentException إذا لم يوجد المنتج
     */
    public ProductResponse updateProductStatus(UUID productId, Boolean isActive) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found with ID: " + productId));

        product.setIsActive(isActive);
        product.setUpdatedAt(LocalDateTime.now());

        Product updatedProduct = productRepository.save(product);
        return convertToResponse(updatedProduct);
    }

    /**
     * تحديث مخزون المنتج
     * Update product stock
     * 
     * @param productId معرف المنتج
     * @param newQuantity الكمية الجديدة
     * @return ProductResponse المنتج المحدث
     * @throws IllegalArgumentException إذا لم يوجد المنتج أو كانت الكمية سالبة
     */
    public ProductResponse updateProductStock(UUID productId, Integer newQuantity) {
        if (newQuantity < 0) {
            throw new IllegalArgumentException("Stock quantity cannot be negative");
        }

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found with ID: " + productId));

        product.updateStock(newQuantity);
        Product updatedProduct = productRepository.save(product);
        return convertToResponse(updatedProduct);
    }

    /**
     * حذف المنتج
     * Delete product
     * 
     * @param productId معرف المنتج
     * @throws IllegalArgumentException إذا لم يوجد المنتج
     */
    public void deleteProduct(UUID productId) {
        if (!productRepository.existsById(productId)) {
            throw new IllegalArgumentException("Product not found with ID: " + productId);
        }
        productRepository.deleteById(productId);
    }

    /**
     * الحصول على المنتجات ذات المخزون المنخفض
     * Get products with low stock
     * 
     * @return List<ProductResponse> قائمة بالمنتجات ذات المخزون المنخفض
     */
    @Transactional(readOnly = true)
    public List<ProductResponse> getLowStockProducts() {
        return productRepository.findLowStockProducts()
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    /**
     * الحصول على المنتجات غير المتوفرة
     * Get out of stock products
     * 
     * @return List<ProductResponse> قائمة بالمنتجات غير المتوفرة
     */
    @Transactional(readOnly = true)
    public List<ProductResponse> getOutOfStockProducts() {
        return productRepository.findOutOfStockProducts()
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    // ===============================
    // Private Helper Methods
    // ===============================

    /**
     * التحقق من عدم تكرار SKU و Slug
     * Validates that SKU and slug are unique
     * 
     * @param sku رمز المنتج
     * @param slug الرابط الودود
     * @throws IllegalArgumentException إذا كان SKU أو Slug مُستخدم مسبقاً
     */
    private void validateProductUniqueness(String sku, String slug) {
        if (productRepository.existsBySku(sku)) {
            throw new IllegalArgumentException("SKU already exists: " + sku);
        }
        
        if (slug != null && !slug.trim().isEmpty() && productRepository.existsBySlug(slug)) {
            throw new IllegalArgumentException("Slug already exists: " + slug);
        }
    }

    /**
     * بناء كائن المنتج من طلب الإنشاء
     * Builds Product entity from CreateProductRequest
     * 
     * @param request بيانات المنتج الجديد
     * @return Product كائن المنتج الجديد
     */
    private Product buildProductFromRequest(CreateProductRequest request) {
        Product product = new Product();
        updateProductFromRequest(product, request);
        return product;
    }

    /**
     * تحديث كائن المنتج من الطلب
     * Updates Product entity from request
     * 
     * @param product كائن المنتج
     * @param request بيانات التحديث
     */
    private void updateProductFromRequest(Product product, CreateProductRequest request) {
        product.setName(request.getName());
        product.setNameAr(request.getNameAr());
        product.setSku(request.getSku());
        product.setSlug(request.getSlug());
        product.setDescription(request.getDescription());
        product.setDescriptionAr(request.getDescriptionAr());
        product.setShortDescription(request.getShortDescription());
        product.setShortDescriptionAr(request.getShortDescriptionAr());
        product.setPrice(request.getPrice());
        product.setComparePrice(request.getComparePrice());
        product.setCostPrice(request.getCostPrice());
        product.setWeight(request.getWeight());
        product.setStockQuantity(request.getStockQuantity());
        product.setMinStockLevel(request.getMinStockLevel());
        product.setCategoryId(request.getCategoryId());
        product.setBrandId(request.getBrandId());
        product.setBarcode(request.getBarcode());
        product.setTrackInventory(request.getTrackInventory());
        product.setIsActive(request.getIsActive());
        product.setIsFeatured(request.getIsFeatured());
        product.setIsDigital(request.getIsDigital());
        product.setRequiresShipping(request.getRequiresShipping());
        product.setAttributes(request.getAttributes());
        product.setTags(request.getTags());
    }

    /**
     * توليد Slug من اسم المنتج
     * Generates slug from product name
     * 
     * @param name اسم المنتج
     * @return String الرابط الودود
     */
    private String generateSlug(String name) {
        if (name == null || name.trim().isEmpty()) {
            return "product-" + System.currentTimeMillis();
        }
        
        String slug = name.toLowerCase()
                         .replaceAll("[^a-z0-9\\s-]", "")
                         .replaceAll("\\s+", "-")
                         .replaceAll("-+", "-")
                         .replaceAll("^-|-$", "");
        
        // التأكد من عدم تكرار الـ slug
        String originalSlug = slug;
        int counter = 1;
        while (productRepository.existsBySlug(slug)) {
            slug = originalSlug + "-" + counter++;
        }
        
        return slug;
    }

    /**
     * تحويل كائن Product إلى ProductResponse
     * Converts Product entity to ProductResponse DTO
     * 
     * @param product كائن المنتج
     * @return ProductResponse DTO للاستجابة
     */
    private ProductResponse convertToResponse(Product product) {
        ProductResponse response = new ProductResponse();
        response.setId(product.getId());
        response.setDisplayId(product.getDisplayId());
        response.setName(product.getName());
        response.setNameAr(product.getNameAr());
        response.setSlug(product.getSlug());
        response.setDescription(product.getDescription());
        response.setDescriptionAr(product.getDescriptionAr());
        response.setShortDescription(product.getShortDescription());
        response.setShortDescriptionAr(product.getShortDescriptionAr());
        response.setSku(product.getSku());
        response.setBarcode(product.getBarcode());
        response.setCategoryId(product.getCategoryId());
        response.setBrandId(product.getBrandId());
        response.setPrice(product.getPrice());
        response.setComparePrice(product.getComparePrice());
        response.setCostPrice(product.getCostPrice());
        response.setWeight(product.getWeight());
        response.setStockQuantity(product.getStockQuantity());
        response.setMinStockLevel(product.getMinStockLevel());
        response.setTrackInventory(product.getTrackInventory());
        response.setIsActive(product.getIsActive());
        response.setIsFeatured(product.getIsFeatured());
        response.setIsDigital(product.getIsDigital());
        response.setRequiresShipping(product.getRequiresShipping());
        response.setAttributes(product.getAttributes());
        response.setTags(product.getTags());
        response.setCreatedAt(product.getCreatedAt());
        response.setUpdatedAt(product.getUpdatedAt());
        
        // Business Logic Fields
        response.setIsAvailable(product.isAvailable());
        response.setIsLowStock(product.isLowStock());
        response.setDiscountPercentage(product.getDiscountPercentage());
        
        return response;
    }
}
