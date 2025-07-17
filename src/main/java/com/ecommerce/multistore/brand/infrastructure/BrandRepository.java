

/* ---- File: src/main/java/com/ecommerce/multistore/brand/infrastructure/BrandRepository.java ---- */

package com.ecommerce.multistore.brand.infrastructure;

import com.ecommerce.multistore.brand.domain.Brand;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * مستودع العلامات التجارية
 * Brand Repository
 */
@Repository
public interface BrandRepository extends JpaRepository<Brand, UUID> {

    /**
     * البحث بواسطة Display ID
     */
    Optional<Brand> findByDisplayId(String displayId);

    /**
     * البحث بواسطة Slug
     */
    Optional<Brand> findBySlug(String slug);

    /**
     * البحث بواسطة الاسم
     */
    Optional<Brand> findByName(String name);

    /**
     * التحقق من وجود Slug
     */
    boolean existsBySlug(String slug);

    /**
     * التحقق من وجود اسم
     */
    boolean existsByName(String name);

    /**
     * الحصول على العلامات النشطة
     */
    List<Brand> findByIsActiveTrueOrderBySortOrderAsc();

    /**
     * الحصول على العلامات النشطة مع التصفح
     */
    Page<Brand> findByIsActiveTrue(Pageable pageable);

    /**
     * البحث في العلامات التجارية
     */
    @Query("SELECT b FROM Brand b WHERE " +
           "(LOWER(b.name) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(b.nameAr) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(b.description) LIKE LOWER(CONCAT('%', :query, '%'))) AND " +
           "b.isActive = true")
    Page<Brand> searchBrands(@Param("query") String query, Pageable pageable);

    /**
     * الحصول على العلامات مرتبة حسب الترتيب
     */
    List<Brand> findAllByOrderBySortOrderAsc();

    /**
     * عدد المنتجات لكل علامة تجارية
     */
    @Query("SELECT b.id, COUNT(p.id) FROM Brand b LEFT JOIN Product p ON b.id = p.brand.id GROUP BY b.id")
    List<Object[]> countProductsByBrand();
}
