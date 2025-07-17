
/* ---- File: src/main/java/com/ecommerce/multistore/category/infrastructure/CategoryRepository.java ---- */

package com.ecommerce.multistore.category.infrastructure;

import com.ecommerce.multistore.category.domain.Category;
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
 * مستودع الفئات
 * Category Repository
 */
@Repository
public interface CategoryRepository extends JpaRepository<Category, UUID> {

    /**
     * البحث بواسطة Display ID
     */
    Optional<Category> findByDisplayId(String displayId);

    /**
     * البحث بواسطة Slug
     */
    Optional<Category> findBySlug(String slug);

    /**
     * البحث بواسطة الاسم
     */
    Optional<Category> findByName(String name);

    /**
     * التحقق من وجود Slug
     */
    boolean existsBySlug(String slug);

    /**
     * التحقق من وجود اسم
     */
    boolean existsByName(String name);

    /**
     * الحصول على الفئات الرئيسية
     */
    List<Category> findByParentIdIsNullAndIsActiveTrueOrderBySortOrderAsc();

    /**
     * الحصول على الفئات الفرعية
     */
    List<Category> findByParentIdAndIsActiveTrueOrderBySortOrderAsc(UUID parentId);
// في CategoryRepository.java
boolean existsByParentIdAndIsActive(UUID parentId, boolean isActive);

    /**
     * الحصول على الفئات النشطة
     */
    List<Category> findByIsActiveTrueOrderBySortOrderAsc();

    /**
     * الحصول على الفئات النشطة مع التصفح
     */
    Page<Category> findByIsActiveTrue(Pageable pageable);

    /**
     * البحث في الفئات
     */
    @Query("SELECT c FROM Category c WHERE " +
           "(LOWER(c.name) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(c.nameAr) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(c.description) LIKE LOWER(CONCAT('%', :query, '%'))) AND " +
           "c.isActive = true")
    Page<Category> searchCategories(@Param("query") String query, Pageable pageable);

    /**
     * عدد المنتجات لكل فئة
     */
    @Query("SELECT c.id, COUNT(p.id) FROM Category c LEFT JOIN Product p ON c.id = p.category.id GROUP BY c.id")
    List<Object[]> countProductsByCategory();

    /**
     * الحصول على شجرة الفئات
     */
    @Query("SELECT c FROM Category c WHERE c.isActive = true ORDER BY c.parentId NULLS FIRST, c.sortOrder ASC")
    List<Category> findCategoryTree();
}
