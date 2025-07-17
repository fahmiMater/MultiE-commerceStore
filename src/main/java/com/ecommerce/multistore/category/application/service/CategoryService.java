
/* ---- File: src/main/java/com/ecommerce/multistore/category/application/service/CategoryService.java ---- */

package com.ecommerce.multistore.category.application.service;

import com.ecommerce.multistore.category.application.dto.CategoryResponse;
import com.ecommerce.multistore.category.application.dto.CategoryTreeResponse;
import com.ecommerce.multistore.category.application.dto.CreateCategoryRequest;
import com.ecommerce.multistore.category.domain.Category;
import com.ecommerce.multistore.category.infrastructure.CategoryRepository;
import com.ecommerce.multistore.shared.exception.BusinessException;
import com.ecommerce.multistore.shared.exception.DuplicateResourceException;
import com.ecommerce.multistore.shared.exception.ResourceNotFoundException;
import com.ecommerce.multistore.shared.utils.SlugGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * خدمة إدارة الفئات
 * Category Management Service
 */
@Service
@Transactional
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    /**
     * إنشاء فئة جديدة
     * Create new category
     */
    @CacheEvict(value = "categories", allEntries = true)
    public CategoryResponse createCategory(CreateCategoryRequest request) {
        // التحقق من عدم وجود اسم مكرر
        if (categoryRepository.existsByName(request.getName())) {
            throw new DuplicateResourceException("Category with name '" + request.getName() + "' already exists");
        }

        // التحقق من وجود الفئة الأب إذا تم تحديدها
        if (request.getParentId() != null) {
            categoryRepository.findById(request.getParentId())
                .orElseThrow(() -> new ResourceNotFoundException("Parent category not found"));
        }

        // توليد slug فريد
        String slug = SlugGenerator.generateUniqueSlug(
            request.getName(), 
            categoryRepository::existsBySlug
        );

        // إنشاء الفئة
        Category category = new Category();
        category.setName(request.getName());
        category.setNameAr(request.getNameAr());
        category.setSlug(slug);
        category.setDescription(request.getDescription());
        category.setDescriptionAr(request.getDescriptionAr());
        category.setParentId(request.getParentId());
        category.setImageUrl(request.getImageUrl());
        category.setIcon(request.getIcon());
        category.setSortOrder(request.getSortOrder());
        category.setIsActive(request.getIsActive());

        Category savedCategory = categoryRepository.save(category);
        return convertToResponse(savedCategory);
    }

    /**
     * الحصول على فئة بواسطة ID
     * Get category by ID
     */
    @Transactional(readOnly = true)
    @Cacheable(value = "categories", key = "#id")
    public Optional<CategoryResponse> findById(UUID id) {
        return categoryRepository.findById(id)
                .map(this::convertToResponse);
    }

    /**
     * الحصول على فئة بواسطة Slug
     * Get category by slug
     */
    @Transactional(readOnly = true)
    @Cacheable(value = "categories", key = "#slug")
    public Optional<CategoryResponse> findBySlug(String slug) {
        return categoryRepository.findBySlug(slug)
                .map(this::convertToResponse);
    }

    /**
     * الحصول على جميع الفئات
     * Get all categories
     */
    @Transactional(readOnly = true)
    public Page<CategoryResponse> getAllCategories(Pageable pageable) {
        return categoryRepository.findAll(pageable)
                .map(this::convertToResponse);
    }

    /**
     * الحصول على الفئات الرئيسية
     * Get parent categories
     */
    @Transactional(readOnly = true)
    @Cacheable(value = "categories", key = "'parents'")
    public List<CategoryResponse> getParentCategories() {
        return categoryRepository.findByParentIdIsNullAndIsActiveTrueOrderBySortOrderAsc()
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    /**
     * الحصول على الفئات الفرعية
     * Get child categories
     */
    @Transactional(readOnly = true)
    @Cacheable(value = "categories", key = "'children_' + #parentId")
    public List<CategoryResponse> getChildCategories(UUID parentId) {
        return categoryRepository.findByParentIdAndIsActiveTrueOrderBySortOrderAsc(parentId)
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    /**
     * الحصول على شجرة الفئات
     * Get category tree
     */
    @Transactional(readOnly = true)
    @Cacheable(value = "categories", key = "'tree'")
    public CategoryTreeResponse getCategoryTree() {
        List<Category> allCategories = categoryRepository.findCategoryTree();

        // تجميع الفئات حسب الأب
        Map<UUID, List<Category>> categoryMap = allCategories.stream()
                .filter(c -> c.getParentId() != null)
                .collect(Collectors.groupingBy(Category::getParentId));

        // بناء شجرة الفئات
        List<CategoryResponse> parentCategories = allCategories.stream()
                .filter(c -> c.getParentId() == null)
                .map(parent -> {
                    CategoryResponse parentResponse = convertToResponse(parent);
                    List<Category> children = categoryMap.get(parent.getId());
                    if (children != null) {
                        List<CategoryResponse> childrenResponses = children.stream()
                                .map(this::convertToResponse)
                                .collect(Collectors.toList());
                        parentResponse.setChildren(childrenResponses);
                    }
                    return parentResponse;
                })
                .collect(Collectors.toList());

        return new CategoryTreeResponse(parentCategories);
    }

    /**
     * البحث في الفئات
     * Search categories
     */
    @Transactional(readOnly = true)
    public Page<CategoryResponse> searchCategories(String query, Pageable pageable) {
        return categoryRepository.searchCategories(query, pageable)
                .map(this::convertToResponse);
    }

    /**
     * تحديث فئة
     * Update category
     */
    @CacheEvict(value = "categories", allEntries = true)
    public CategoryResponse updateCategory(UUID id, CreateCategoryRequest request) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with ID: " + id));

        // التحقق من عدم وجود اسم مكرر (باستثناء الفئة الحالية)
        if (!category.getName().equals(request.getName()) && 
            categoryRepository.existsByName(request.getName())) {
            throw new DuplicateResourceException("Category with name '" + request.getName() + "' already exists");
        }

        // التحقق من الفئة الأب
        if (request.getParentId() != null) {
            if (request.getParentId().equals(id)) {
                throw new IllegalArgumentException("Category cannot be its own parent");
            }
            categoryRepository.findById(request.getParentId())
                .orElseThrow(() -> new ResourceNotFoundException("Parent category not found"));
        }

        // تحديث slug إذا تغير الاسم
        if (!category.getName().equals(request.getName())) {
            String newSlug = SlugGenerator.generateUniqueSlug(
                request.getName(), 
                slug -> !slug.equals(category.getSlug()) && categoryRepository.existsBySlug(slug)
            );
            category.setSlug(newSlug);
        }

        // تحديث البيانات
        category.updateInfo(
            request.getName(),
            request.getNameAr(),
            request.getDescription(),
            request.getDescriptionAr()
        );
        category.setParentId(request.getParentId());
        category.setImageUrl(request.getImageUrl());
        category.setIcon(request.getIcon());
        category.setSortOrder(request.getSortOrder());
        category.setIsActive(request.getIsActive());

        Category updatedCategory = categoryRepository.save(category);
        return convertToResponse(updatedCategory);
    }

    // في CategoryService.java
/**
 * تفعيل فئة
 * Activate category
 */
@Transactional
public CategoryResponse activateCategory(UUID id) {
    Category category = categoryRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Category not found with ID: " + id));
    
    category.setIsActive(true);
    category.setUpdatedAt(LocalDateTime.now());
    
    Category savedCategory = categoryRepository.save(category);
    return convertToResponse(savedCategory);
}

/**
 * إلغاء تفعيل فئة
 * Deactivate category
 */
@Transactional
public CategoryResponse deactivateCategory(UUID id) {
    Category category = categoryRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Category not found with ID: " + id));
    
    // التحقق من وجود فئات فرعية نشطة
    if (categoryRepository.existsByParentIdAndIsActive(id, true)) {
        throw new BusinessException("Cannot deactivate category with active subcategories");
    }
    
    category.setIsActive(false);
    category.setUpdatedAt(LocalDateTime.now());
    
    Category savedCategory = categoryRepository.save(category);
    return convertToResponse(savedCategory);
}


    /**
     * حذف فئة
     * Delete category
     */
    @CacheEvict(value = "categories", allEntries = true)
    public void deleteCategory(UUID id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with ID: " + id));

        // التحقق من عدم وجود فئات فرعية
        List<Category> children = categoryRepository.findByParentIdAndIsActiveTrueOrderBySortOrderAsc(id);
        if (!children.isEmpty()) {
            throw new IllegalStateException("Cannot delete category with active children");
        }

        categoryRepository.delete(category);
    }

    // Helper Methods

    /**
     * تحويل Entity إلى Response DTO
     * Convert entity to response DTO
     */
    private CategoryResponse convertToResponse(Category category) {
        CategoryResponse response = new CategoryResponse();
        response.setId(category.getId());
        response.setDisplayId(category.getDisplayId());
        response.setName(category.getName());
        response.setNameAr(category.getNameAr());
        response.setSlug(category.getSlug());
        response.setDescription(category.getDescription());
        response.setDescriptionAr(category.getDescriptionAr());
        response.setParentId(category.getParentId());
        response.setImageUrl(category.getImageUrl());
        response.setIcon(category.getIcon());
        response.setSortOrder(category.getSortOrder());
        response.setIsActive(category.getIsActive());
        response.setCreatedAt(category.getCreatedAt());
        response.setUpdatedAt(category.getUpdatedAt());
        return response;
    }
}
