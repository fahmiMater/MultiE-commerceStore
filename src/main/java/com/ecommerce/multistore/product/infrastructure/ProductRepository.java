package com.ecommerce.multistore.product.infrastructure;

import com.ecommerce.multistore.product.domain.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * مستودع المنتجات - للتعامل مع قاعدة البيانات
 * Product Repository - For database operations
 * 
 * @author Multi-Store Team
 * @version 1.0
 * @since 2024-01-01
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {
    
    /**
     * البحث بواسطة Display ID
     * Find by display ID
     * 
     * @param displayId معرف العرض
     * @return Optional<Product>
     */
    Optional<Product> findByDisplayId(String displayId);
    
    /**
     * البحث بواسطة SKU
     * Find by SKU
     * 
     * @param sku رمز المنتج
     * @return Optional<Product>
     */
    Optional<Product> findBySku(String sku);
    
    /**
     * البحث بواسطة Slug
     * Find by slug
     * 
     * @param slug الرابط الودود
     * @return Optional<Product>
     */
    Optional<Product> findBySlug(String slug);
    
    /**
     * التحقق من وجود SKU
     * Check if SKU exists
     * 
     * @param sku رمز المنتج
     * @return boolean
     */
    boolean existsBySku(String sku);
    
    /**
     * التحقق من وجود Slug
     * Check if slug exists
     * 
     * @param slug الرابط الودود
     * @return boolean
     */
    boolean existsBySlug(String slug);
    
    /**
     * البحث في المنتجات النشطة فقط
     * Find active products only
     * 
     * @param pageable الصفحات
     * @return Page<Product>
     */
    Page<Product> findByIsActiveTrue(Pageable pageable);
    
    /**
     * البحث في المنتجات المميزة
     * Find featured products
     * 
     * @return List<Product>
     */
    List<Product> findByIsFeaturedTrueAndIsActiveTrue();
    
    /**
     * البحث بواسطة الفئة
     * Find by category
     * 
     * @param categoryId معرف الفئة
     * @param pageable الصفحات
     * @return Page<Product>
     */
    Page<Product> findByCategoryIdAndIsActiveTrue(UUID categoryId, Pageable pageable);
    
    /**
     * البحث بواسطة العلامة التجارية
     * Find by brand
     * 
     * @param brandId معرف العلامة التجارية
     * @param pageable الصفحات
     * @return Page<Product>
     */
    Page<Product> findByBrandIdAndIsActiveTrue(UUID brandId, Pageable pageable);
    
    /**
     * البحث في نطاق سعري
     * Find by price range
     * 
     * @param minPrice أقل سعر
     * @param maxPrice أعلى سعر
     * @param pageable الصفحات
     * @return Page<Product>
     */
    Page<Product> findByPriceBetweenAndIsActiveTrue(
        BigDecimal minPrice, 
        BigDecimal maxPrice, 
        Pageable pageable
    );
    
    /**
     * البحث النصي في اسم المنتج ووصفه
     * Text search in product name and description
     * 
     * @param searchTerm مصطلح البحث
     * @param pageable الصفحات
     * @return Page<Product>
     */
    @Query("SELECT p FROM Product p WHERE " +
           "(LOWER(p.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(p.nameAr) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(p.description) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(p.descriptionAr) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(p.sku) LIKE LOWER(CONCAT('%', :searchTerm, '%'))) " +
           "AND p.isActive = true")
    Page<Product> searchProducts(@Param("searchTerm") String searchTerm, Pageable pageable);
    
    /**
     * المنتجات ذات المخزون المنخفض
     * Products with low stock
     * 
     * @return List<Product>
     */
    @Query("SELECT p FROM Product p WHERE " +
           "p.trackInventory = true AND " +
           "p.stockQuantity <= p.minStockLevel AND " +
           "p.isActive = true")
    List<Product> findLowStockProducts();
    
    /**
     * المنتجات غير المتوفرة
     * Out of stock products
     * 
     * @return List<Product>
     */
    @Query("SELECT p FROM Product p WHERE " +
           "p.trackInventory = true AND " +
           "p.stockQuantity = 0 AND " +
           "p.isActive = true")
    List<Product> findOutOfStockProducts();
    
    /**
     * عدد المنتجات حسب الفئة
     * Count products by category
     * 
     * @param categoryId معرف الفئة
     * @return Long
     */
    long countByCategoryIdAndIsActiveTrue(UUID categoryId);
    
    /**
     * عدد المنتجات حسب العلامة التجارية
     * Count products by brand
     * 
     * @param brandId معرف العلامة التجارية
     * @return Long
     */
    long countByBrandIdAndIsActiveTrue(UUID brandId);
}
