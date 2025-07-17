
/* ---- File: src/main/java/com/ecommerce/multistore/brand/application/service/BrandService.java ---- */

package com.ecommerce.multistore.brand.application.service;

import com.ecommerce.multistore.brand.application.dto.BrandResponse;
import com.ecommerce.multistore.brand.application.dto.CreateBrandRequest;
import com.ecommerce.multistore.brand.domain.Brand;
import com.ecommerce.multistore.brand.infrastructure.BrandRepository;
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
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * خدمة إدارة العلامات التجارية
 * Brand Management Service
 */
@Service
@Transactional
public class BrandService {

    private final BrandRepository brandRepository;

    @Autowired
    public BrandService(BrandRepository brandRepository) {
        this.brandRepository = brandRepository;
    }

    /**
     * إنشاء علامة تجارية جديدة
     * Create new brand
     */
    @CacheEvict(value = "brands", allEntries = true)
    public BrandResponse createBrand(CreateBrandRequest request) {
        // التحقق من عدم وجود اسم مكرر
        if (brandRepository.existsByName(request.getName())) {
            throw new DuplicateResourceException("Brand with name '" + request.getName() + "' already exists");
        }

        // توليد slug فريد
        String slug = SlugGenerator.generateUniqueSlug(
            request.getName(), 
            brandRepository::existsBySlug
        );

        // إنشاء العلامة التجارية
        Brand brand = new Brand();
        brand.setName(request.getName());
        brand.setNameAr(request.getNameAr());
        brand.setSlug(slug);
        brand.setDescription(request.getDescription());
        brand.setDescriptionAr(request.getDescriptionAr());
        brand.setLogoUrl(request.getLogoUrl());
        brand.setWebsiteUrl(request.getWebsiteUrl());
        brand.setSortOrder(request.getSortOrder());
        brand.setIsActive(request.getIsActive());

        Brand savedBrand = brandRepository.save(brand);
        return convertToResponse(savedBrand);
    }

    /**
     * الحصول على علامة تجارية بواسطة ID
     * Get brand by ID
     */
    @Transactional(readOnly = true)
    @Cacheable(value = "brands", key = "#id")
    public Optional<BrandResponse> findById(UUID id) {
        return brandRepository.findById(id)
                .map(this::convertToResponse);
    }

    /**
     * الحصول على علامة تجارية بواسطة Display ID
     * Get brand by display ID
     */
    @Transactional(readOnly = true)
    @Cacheable(value = "brands", key = "#displayId")
    public Optional<BrandResponse> findByDisplayId(String displayId) {
        return brandRepository.findByDisplayId(displayId)
                .map(this::convertToResponse);
    }

    /**
     * الحصول على علامة تجارية بواسطة Slug
     * Get brand by slug
     */
    @Transactional(readOnly = true)
    @Cacheable(value = "brands", key = "#slug")
    public Optional<BrandResponse> findBySlug(String slug) {
        return brandRepository.findBySlug(slug)
                .map(this::convertToResponse);
    }

    /**
     * الحصول على جميع العلامات التجارية
     * Get all brands
     */
    @Transactional(readOnly = true)
    public Page<BrandResponse> getAllBrands(Pageable pageable) {
        return brandRepository.findAll(pageable)
                .map(this::convertToResponse);
    }

    /**
     * الحصول على العلامات التجارية النشطة
     * Get active brands
     */
    @Transactional(readOnly = true)
    @Cacheable(value = "brands", key = "'active'")
    public List<BrandResponse> getActiveBrands() {
        return brandRepository.findByIsActiveTrueOrderBySortOrderAsc()
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    /**
     * البحث في العلامات التجارية
     * Search brands
     */
    @Transactional(readOnly = true)
    public Page<BrandResponse> searchBrands(String query, Pageable pageable) {
        return brandRepository.searchBrands(query, pageable)
                .map(this::convertToResponse);
    }

    /**
     * تحديث علامة تجارية
     * Update brand
     */
    @CacheEvict(value = "brands", allEntries = true)
    public BrandResponse updateBrand(UUID id, CreateBrandRequest request) {
        Brand brand = brandRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Brand not found with ID: " + id));

        // التحقق من عدم وجود اسم مكرر (باستثناء العلامة الحالية)
        if (!brand.getName().equals(request.getName()) && 
            brandRepository.existsByName(request.getName())) {
            throw new DuplicateResourceException("Brand with name '" + request.getName() + "' already exists");
        }

        // تحديث slug إذا تغير الاسم
        if (!brand.getName().equals(request.getName())) {
            String newSlug = SlugGenerator.generateUniqueSlug(
                request.getName(), 
                slug -> !slug.equals(brand.getSlug()) && brandRepository.existsBySlug(slug)
            );
            brand.setSlug(newSlug);
        }

        // تحديث البيانات
        brand.updateInfo(
            request.getName(),
            request.getNameAr(),
            request.getDescription(),
            request.getDescriptionAr()
        );
        brand.setLogoUrl(request.getLogoUrl());
        brand.setWebsiteUrl(request.getWebsiteUrl());
        brand.setSortOrder(request.getSortOrder());
        brand.setIsActive(request.getIsActive());

        Brand updatedBrand = brandRepository.save(brand);
        return convertToResponse(updatedBrand);
    }

    /**
     * تفعيل علامة تجارية
     * Activate brand
     */
    @CacheEvict(value = "brands", allEntries = true)
    public BrandResponse activateBrand(UUID id) {
        Brand brand = brandRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Brand not found with ID: " + id));

        brand.activate();
        Brand updatedBrand = brandRepository.save(brand);
        return convertToResponse(updatedBrand);
    }

    /**
     * إلغاء تفعيل علامة تجارية
     * Deactivate brand
     */
    @CacheEvict(value = "brands", allEntries = true)
    public BrandResponse deactivateBrand(UUID id) {
        Brand brand = brandRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Brand not found with ID: " + id));

        brand.deactivate();
        Brand updatedBrand = brandRepository.save(brand);
        return convertToResponse(updatedBrand);
    }

    /**
     * حذف علامة تجارية
     * Delete brand
     */
    @CacheEvict(value = "brands", allEntries = true)
    public void deleteBrand(UUID id) {
        Brand brand = brandRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Brand not found with ID: " + id));

        brandRepository.delete(brand);
    }

    // Helper Methods

    /**
     * تحويل Entity إلى Response DTO
     * Convert entity to response DTO
     */
    private BrandResponse convertToResponse(Brand brand) {
        BrandResponse response = new BrandResponse();
        response.setId(brand.getId());
        response.setDisplayId(brand.getDisplayId());
        response.setName(brand.getName());
        response.setNameAr(brand.getNameAr());
        response.setSlug(brand.getSlug());
        response.setDescription(brand.getDescription());
        response.setDescriptionAr(brand.getDescriptionAr());
        response.setLogoUrl(brand.getLogoUrl());
        response.setWebsiteUrl(brand.getWebsiteUrl());
        response.setIsActive(brand.getIsActive());
        response.setSortOrder(brand.getSortOrder());
        response.setCreatedAt(brand.getCreatedAt());
        response.setUpdatedAt(brand.getUpdatedAt());
        return response;
    }
}
