package com.ecommerce.multistore.product.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.DecimalMin;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

/**
 * طلب البحث المتقدم في المنتجات
 * Advanced product search request
 * 
 * يحتوي على جميع معايير البحث والفلترة المتقدمة للمنتجات
 * Contains all advanced search and filtering criteria for products
 * 
 * @author Multi-Store Team
 * @version 1.0
 * @since 2024-01-01
 */
@Schema(description = "طلب البحث المتقدم في المنتجات / Advanced product search request")
public class ProductSearchRequest {
    
    /**
     * كلمة البحث العامة
     * General search term
     */
    @Schema(description = "كلمة البحث العامة / General search term", example = "ساعة ذكية")
    private String searchTerm;
    
    /**
     * البحث في الاسم تحديداً
     * Search in name specifically
     */
    @Schema(description = "البحث في الاسم تحديداً / Search in name specifically", example = "Smart Watch")
    private String name;
    
    /**
     * البحث في الاسم العربي
     * Search in Arabic name
     */
    @Schema(description = "البحث في الاسم العربي / Search in Arabic name", example = "ساعة ذكية")
    private String nameAr;
    
    /**
     * البحث في الوصف
     * Search in description
     */
    @Schema(description = "البحث في الوصف / Search in description")
    private String description;
    
    /**
     * البحث في رمز المنتج
     * Search in product SKU
     */
    @Schema(description = "البحث في رمز المنتج / Search in product SKU", example = "SW-PRO-001")
    private String sku;
    
    /**
     * البحث في الباركود
     * Search in barcode
     */
    @Schema(description = "البحث في الباركود / Search in barcode")
    private String barcode;
    
    /**
     * فئات المنتجات المطلوبة
     * Required product categories
     */
    @Schema(description = "فئات المنتجات المطلوبة / Required product categories")
    private List<UUID> categoryIds;
    
    /**
     * العلامات التجارية المطلوبة
     * Required brands
     */
    @Schema(description = "العلامات التجارية المطلوبة / Required brands")
    private List<UUID> brandIds;
    
    /**
     * أقل سعر
     * Minimum price
     */
    @Schema(description = "أقل سعر / Minimum price", example = "100.00")
    @DecimalMin(value = "0.0", message = "Minimum price cannot be negative")
    private BigDecimal minPrice;
    
    /**
     * أعلى سعر
     * Maximum price
     */
    @Schema(description = "أعلى سعر / Maximum price", example = "1000.00")
    @DecimalMin(value = "0.0", message = "Maximum price cannot be negative")
    private BigDecimal maxPrice;
    
    /**
     * أقل كمية في المخزون
     * Minimum stock quantity
     */
    @Schema(description = "أقل كمية في المخزون / Minimum stock quantity", example = "1")
    @Min(value = 0, message = "Minimum stock quantity cannot be negative")
    private Integer minStockQuantity;
    
    /**
     * أعلى كمية في المخزون
     * Maximum stock quantity
     */
    @Schema(description = "أعلى كمية في المخزون / Maximum stock quantity", example = "100")
    @Min(value = 0, message = "Maximum stock quantity cannot be negative")
    private Integer maxStockQuantity;
    
    /**
     * المنتجات النشطة فقط
     * Active products only
     */
    @Schema(description = "المنتجات النشطة فقط / Active products only", example = "true")
    private Boolean isActive;
    
    /**
     * المنتجات المميزة فقط
     * Featured products only
     */
    @Schema(description = "المنتجات المميزة فقط / Featured products only", example = "false")
    private Boolean isFeatured;
    
    /**
     * المنتجات الرقمية فقط
     * Digital products only
     */
    @Schema(description = "المنتجات الرقمية فقط / Digital products only", example = "false")
    private Boolean isDigital;
    
    /**
     * المنتجات التي تحتاج شحن
     * Products that require shipping
     */
    @Schema(description = "المنتجات التي تحتاج شحن / Products that require shipping", example = "true")
    private Boolean requiresShipping;
    
    /**
     * المنتجات ذات المخزون المنخفض
     * Products with low stock
     */
    @Schema(description = "المنتجات ذات المخزون المنخفض / Products with low stock", example = "false")
    private Boolean isLowStock;
    
    /**
     * المنتجات غير المتوفرة
     * Out of stock products
     */
    @Schema(description = "المنتجات غير المتوفرة / Out of stock products", example = "false")
    private Boolean isOutOfStock;
    
    /**
     * العلامات المطلوبة
     * Required tags
     */
    @Schema(description = "العلامات المطلوبة / Required tags", example = "[\"smartwatch\", \"fitness\"]")
    private List<String> tags;
    
    /**
     * خصائص المنتج
     * Product attributes
     */
    @Schema(description = "خصائص المنتج / Product attributes")
    private String attributes;
    
    /**
     * تاريخ الإنشاء من
     * Created from date
     */
    @Schema(description = "تاريخ الإنشاء من / Created from date", example = "2024-01-01")
    private String createdFrom;
    
    /**
     * تاريخ الإنشاء إلى
     * Created to date
     */
    @Schema(description = "تاريخ الإنشاء إلى / Created to date", example = "2024-12-31")
    private String createdTo;
    
    /**
     * حقل الترتيب
     * Sort field
     */
    @Schema(description = "حقل الترتيب / Sort field", example = "createdAt", 
            allowableValues = {"name", "price", "createdAt", "updatedAt", "stockQuantity"})
    private String sortBy = "createdAt";
    
    /**
     * اتجاه الترتيب
     * Sort direction
     */
    @Schema(description = "اتجاه الترتيب / Sort direction", example = "desc", 
            allowableValues = {"asc", "desc"})
    private String sortDirection = "desc";
    
    /**
     * رقم الصفحة
     * Page number
     */
    @Schema(description = "رقم الصفحة / Page number", example = "0")
    @Min(value = 0, message = "Page number cannot be negative")
    private Integer page = 0;
    
    /**
     * حجم الصفحة
     * Page size
     */
    @Schema(description = "حجم الصفحة / Page size", example = "20")
    @Min(value = 1, message = "Page size must be at least 1")
    @Max(value = 100, message = "Page size cannot exceed 100")
    private Integer size = 20;
    
    /**
     * تضمين المنتجات المحذوفة
     * Include deleted products
     */
    @Schema(description = "تضمين المنتجات المحذوفة / Include deleted products", example = "false")
    private Boolean includeDeleted = false;
    
    /**
     * البحث الضبابي
     * Fuzzy search
     */
    @Schema(description = "البحث الضبابي / Fuzzy search", example = "false")
    private Boolean fuzzySearch = false;
    
    /**
     * البحث في جميع الحقول
     * Search in all fields
     */
    @Schema(description = "البحث في جميع الحقول / Search in all fields", example = "true")
    private Boolean searchInAllFields = true;
    
    /**
     * حساسية الأحرف
     * Case sensitive
     */
    @Schema(description = "حساسية الأحرف / Case sensitive", example = "false")
    private Boolean caseSensitive = false;
    
    /**
     * البحث الدقيق
     * Exact match
     */
    @Schema(description = "البحث الدقيق / Exact match", example = "false")
    private Boolean exactMatch = false;
    
    // ===============================
    // Constructors
    // ===============================
    
    /**
     * Constructor افتراضي
     * Default constructor
     */
    public ProductSearchRequest() {}
    
    /**
     * Constructor مع كلمة البحث
     * Constructor with search term
     * 
     * @param searchTerm كلمة البحث
     */
    public ProductSearchRequest(String searchTerm) {
        this.searchTerm = searchTerm;
    }
    
    // ===============================
    // Builder Pattern
    // ===============================
    
    /**
     * بناء طلب البحث
     * Build search request
     * 
     * @return Builder
     */
    public static Builder builder() {
        return new Builder();
    }
    
    /**
     * فئة البناء لطلب البحث
     * Builder class for search request
     */
    public static class Builder {
        private final ProductSearchRequest request;
        
        public Builder() {
            this.request = new ProductSearchRequest();
        }
        
        public Builder searchTerm(String searchTerm) {
            request.searchTerm = searchTerm;
            return this;
        }
        
        public Builder name(String name) {
            request.name = name;
            return this;
        }
        
        public Builder nameAr(String nameAr) {
            request.nameAr = nameAr;
            return this;
        }
        
        public Builder description(String description) {
            request.description = description;
            return this;
        }
        
        public Builder sku(String sku) {
            request.sku = sku;
            return this;
        }
        
        public Builder barcode(String barcode) {
            request.barcode = barcode;
            return this;
        }
        
        public Builder categoryIds(List<UUID> categoryIds) {
            request.categoryIds = categoryIds;
            return this;
        }
        
        public Builder brandIds(List<UUID> brandIds) {
            request.brandIds = brandIds;
            return this;
        }
        
        public Builder priceRange(BigDecimal minPrice, BigDecimal maxPrice) {
            request.minPrice = minPrice;
            request.maxPrice = maxPrice;
            return this;
        }
        
        public Builder stockRange(Integer minStock, Integer maxStock) {
            request.minStockQuantity = minStock;
            request.maxStockQuantity = maxStock;
            return this;
        }
        
        public Builder isActive(Boolean isActive) {
            request.isActive = isActive;
            return this;
        }
        
        public Builder isFeatured(Boolean isFeatured) {
            request.isFeatured = isFeatured;
            return this;
        }
        
        public Builder isDigital(Boolean isDigital) {
            request.isDigital = isDigital;
            return this;
        }
        
        public Builder requiresShipping(Boolean requiresShipping) {
            request.requiresShipping = requiresShipping;
            return this;
        }
        
        public Builder isLowStock(Boolean isLowStock) {
            request.isLowStock = isLowStock;
            return this;
        }
        
        public Builder isOutOfStock(Boolean isOutOfStock) {
            request.isOutOfStock = isOutOfStock;
            return this;
        }
        
        public Builder tags(List<String> tags) {
            request.tags = tags;
            return this;
        }
        
        public Builder attributes(String attributes) {
            request.attributes = attributes;
            return this;
        }
        
        public Builder createdDateRange(String createdFrom, String createdTo) {
            request.createdFrom = createdFrom;
            request.createdTo = createdTo;
            return this;
        }
        
        public Builder sort(String sortBy, String sortDirection) {
            request.sortBy = sortBy;
            request.sortDirection = sortDirection;
            return this;
        }
        
        public Builder pagination(Integer page, Integer size) {
            request.page = page;
            request.size = size;
            return this;
        }
        
        public Builder includeDeleted(Boolean includeDeleted) {
            request.includeDeleted = includeDeleted;
            return this;
        }
        
        public Builder fuzzySearch(Boolean fuzzySearch) {
            request.fuzzySearch = fuzzySearch;
            return this;
        }
        
        public Builder searchInAllFields(Boolean searchInAllFields) {
            request.searchInAllFields = searchInAllFields;
            return this;
        }
        
        public Builder caseSensitive(Boolean caseSensitive) {
            request.caseSensitive = caseSensitive;
            return this;
        }
        
        public Builder exactMatch(Boolean exactMatch) {
            request.exactMatch = exactMatch;
            return this;
        }
        
        public ProductSearchRequest build() {
            return request;
        }
    }
    
    // ===============================
    // Utility Methods
    // ===============================
    
    /**
     * التحقق من وجود معايير بحث
     * Check if has search criteria
     * 
     * @return boolean
     */
    public boolean hasSearchCriteria() {
        return (searchTerm != null && !searchTerm.trim().isEmpty()) ||
               (name != null && !name.trim().isEmpty()) ||
               (nameAr != null && !nameAr.trim().isEmpty()) ||
               (description != null && !description.trim().isEmpty()) ||
               (sku != null && !sku.trim().isEmpty()) ||
               (barcode != null && !barcode.trim().isEmpty());
    }
    
    /**
     * التحقق من وجود فلاتر
     * Check if has filters
     * 
     * @return boolean
     */
    public boolean hasFilters() {
        return (categoryIds != null && !categoryIds.isEmpty()) ||
               (brandIds != null && !brandIds.isEmpty()) ||
               minPrice != null || maxPrice != null ||
               minStockQuantity != null || maxStockQuantity != null ||
               isActive != null || isFeatured != null ||
               isDigital != null || requiresShipping != null ||
               isLowStock != null || isOutOfStock != null ||
               (tags != null && !tags.isEmpty()) ||
               (attributes != null && !attributes.trim().isEmpty());
    }
    
    /**
     * التحقق من وجود فلتر تاريخ
     * Check if has date filter
     * 
     * @return boolean
     */
    public boolean hasDateFilter() {
        return (createdFrom != null && !createdFrom.trim().isEmpty()) ||
               (createdTo != null && !createdTo.trim().isEmpty());
    }
    
    /**
     * التحقق من وجود فلتر سعر
     * Check if has price filter
     * 
     * @return boolean
     */
    public boolean hasPriceFilter() {
        return minPrice != null || maxPrice != null;
    }
    
    /**
     * التحقق من وجود فلتر مخزون
     * Check if has stock filter
     * 
     * @return boolean
     */
    public boolean hasStockFilter() {
        return minStockQuantity != null || maxStockQuantity != null ||
               Boolean.TRUE.equals(isLowStock) || Boolean.TRUE.equals(isOutOfStock);
    }
    
    /**
     * التحقق من صحة نطاق السعر
     * Validate price range
     * 
     * @return boolean
     */
    public boolean isValidPriceRange() {
        if (minPrice == null || maxPrice == null) {
            return true;
        }
        return minPrice.compareTo(maxPrice) <= 0;
    }
    
    /**
     * التحقق من صحة نطاق المخزون
     * Validate stock range
     * 
     * @return boolean
     */
    public boolean isValidStockRange() {
        if (minStockQuantity == null || maxStockQuantity == null) {
            return true;
        }
        return minStockQuantity <= maxStockQuantity;
    }
    
    /**
     * الحصول على عدد الفلاتر النشطة
     * Get active filters count
     * 
     * @return int
     */
    public int getActiveFiltersCount() {
        int count = 0;
        if (categoryIds != null && !categoryIds.isEmpty()) count++;
        if (brandIds != null && !brandIds.isEmpty()) count++;
        if (hasPriceFilter()) count++;
        if (hasStockFilter()) count++;
        if (isActive != null) count++;
        if (isFeatured != null) count++;
        if (isDigital != null) count++;
        if (requiresShipping != null) count++;
        if (tags != null && !tags.isEmpty()) count++;
        if (attributes != null && !attributes.trim().isEmpty()) count++;
        if (hasDateFilter()) count++;
        return count;
    }
    
    // ===============================
    // Getters and Setters
    // ===============================
    
    public String getSearchTerm() { return searchTerm; }
    public void setSearchTerm(String searchTerm) { this.searchTerm = searchTerm; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getNameAr() { return nameAr; }
    public void setNameAr(String nameAr) { this.nameAr = nameAr; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public String getSku() { return sku; }
    public void setSku(String sku) { this.sku = sku; }
    
    public String getBarcode() { return barcode; }
    public void setBarcode(String barcode) { this.barcode = barcode; }
    
    public List<UUID> getCategoryIds() { return categoryIds; }
    public void setCategoryIds(List<UUID> categoryIds) { this.categoryIds = categoryIds; }
    
    public List<UUID> getBrandIds() { return brandIds; }
    public void setBrandIds(List<UUID> brandIds) { this.brandIds = brandIds; }
    
    public BigDecimal getMinPrice() { return minPrice; }
    public void setMinPrice(BigDecimal minPrice) { this.minPrice = minPrice; }
    
    public BigDecimal getMaxPrice() { return maxPrice; }
    public void setMaxPrice(BigDecimal maxPrice) { this.maxPrice = maxPrice; }
    
    public Integer getMinStockQuantity() { return minStockQuantity; }
    public void setMinStockQuantity(Integer minStockQuantity) { this.minStockQuantity = minStockQuantity; }
    
    public Integer getMaxStockQuantity() { return maxStockQuantity; }
    public void setMaxStockQuantity(Integer maxStockQuantity) { this.maxStockQuantity = maxStockQuantity; }
    
    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }
    
    public Boolean getIsFeatured() { return isFeatured; }
    public void setIsFeatured(Boolean isFeatured) { this.isFeatured = isFeatured; }
    
    public Boolean getIsDigital() { return isDigital; }
    public void setIsDigital(Boolean isDigital) { this.isDigital = isDigital; }
    
    public Boolean getRequiresShipping() { return requiresShipping; }
    public void setRequiresShipping(Boolean requiresShipping) { this.requiresShipping = requiresShipping; }
    
    public Boolean getIsLowStock() { return isLowStock; }
    public void setIsLowStock(Boolean isLowStock) { this.isLowStock = isLowStock; }
    
    public Boolean getIsOutOfStock() { return isOutOfStock; }
    public void setIsOutOfStock(Boolean isOutOfStock) { this.isOutOfStock = isOutOfStock; }
    
    public List<String> getTags() { return tags; }
    public void setTags(List<String> tags) { this.tags = tags; }
    
    public String getAttributes() { return attributes; }
    public void setAttributes(String attributes) { this.attributes = attributes; }
    
    public String getCreatedFrom() { return createdFrom; }
    public void setCreatedFrom(String createdFrom) { this.createdFrom = createdFrom; }
    
    public String getCreatedTo() { return createdTo; }
    public void setCreatedTo(String createdTo) { this.createdTo = createdTo; }
    
    public String getSortBy() { return sortBy; }
    public void setSortBy(String sortBy) { this.sortBy = sortBy; }
    
    public String getSortDirection() { return sortDirection; }
    public void setSortDirection(String sortDirection) { this.sortDirection = sortDirection; }
    
    public Integer getPage() { return page; }
    public void setPage(Integer page) { this.page = page; }
    
    public Integer getSize() { return size; }
    public void setSize(Integer size) { this.size = size; }
    
    public Boolean getIncludeDeleted() { return includeDeleted; }
    public void setIncludeDeleted(Boolean includeDeleted) { this.includeDeleted = includeDeleted; }
    
    public Boolean getFuzzySearch() { return fuzzySearch; }
    public void setFuzzySearch(Boolean fuzzySearch) { this.fuzzySearch = fuzzySearch; }
    
    public Boolean getSearchInAllFields() { return searchInAllFields; }
    public void setSearchInAllFields(Boolean searchInAllFields) { this.searchInAllFields = searchInAllFields; }
    
    public Boolean getCaseSensitive() { return caseSensitive; }
    public void setCaseSensitive(Boolean caseSensitive) { this.caseSensitive = caseSensitive; }
    
    public Boolean getExactMatch() { return exactMatch; }
    public void setExactMatch(Boolean exactMatch) { this.exactMatch = exactMatch; }
    
    // ===============================
    // toString Method
    // ===============================
    
    @Override
    public String toString() {
        return "ProductSearchRequest{" +
                "searchTerm='" + searchTerm + '\'' +
                ", name='" + name + '\'' +
                ", nameAr='" + nameAr + '\'' +
                ", description='" + description + '\'' +
                ", sku='" + sku + '\'' +
                ", barcode='" + barcode + '\'' +
                ", categoryIds=" + categoryIds +
                ", brandIds=" + brandIds +
                ", minPrice=" + minPrice +
                ", maxPrice=" + maxPrice +
                ", minStockQuantity=" + minStockQuantity +
                ", maxStockQuantity=" + maxStockQuantity +
                ", isActive=" + isActive +
                ", isFeatured=" + isFeatured +
                ", isDigital=" + isDigital +
                ", requiresShipping=" + requiresShipping +
                ", isLowStock=" + isLowStock +
                ", isOutOfStock=" + isOutOfStock +
                ", tags=" + tags +
                ", attributes='" + attributes + '\'' +
                ", createdFrom='" + createdFrom + '\'' +
                ", createdTo='" + createdTo + '\'' +
                ", sortBy='" + sortBy + '\'' +
                ", sortDirection='" + sortDirection + '\'' +
                ", page=" + page +
                ", size=" + size +
                ", includeDeleted=" + includeDeleted +
                ", fuzzySearch=" + fuzzySearch +
                ", searchInAllFields=" + searchInAllFields +
                ", caseSensitive=" + caseSensitive +
                ", exactMatch=" + exactMatch +
                '}';
    }
}
