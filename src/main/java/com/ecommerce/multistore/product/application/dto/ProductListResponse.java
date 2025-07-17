package com.ecommerce.multistore.product.application.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * استجابة قائمة المنتجات محسنة
 * Enhanced product list response
 * 
 * تحتوي على معلومات مبسطة ومحسنة لعرض المنتجات في القوائم
 * Contains simplified and enhanced information for displaying products in lists
 * 
 * @author Multi-Store Team
 * @version 1.0
 * @since 2024-01-01
 */
@Schema(description = "استجابة قائمة المنتجات / Product list response")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductListResponse {
    
    /**
     * المعرف الفريد
     * Unique identifier
     */
    @Schema(description = "المعرف الفريد / Unique identifier")
    private UUID id;
    
    /**
     * المعرف المعروض
     * Display identifier
     */
    @Schema(description = "المعرف المعروض / Display identifier", example = "PRD-000001")
    private String displayId;
    
    /**
     * اسم المنتج
     * Product name
     */
    @Schema(description = "اسم المنتج / Product name", example = "Smart Watch Pro")
    private String name;
    
    /**
     * اسم المنتج بالعربية
     * Product name in Arabic
     */
    @Schema(description = "اسم المنتج بالعربية / Product name in Arabic", example = "ساعة ذكية برو")
    private String nameAr;
    
    /**
     * الرابط الودود
     * Friendly URL slug
     */
    @Schema(description = "الرابط الودود / Friendly URL slug", example = "smart-watch-pro")
    private String slug;
    
    /**
     * الوصف المختصر
     * Short description
     */
    @Schema(description = "الوصف المختصر / Short description")
    private String shortDescription;
    
    /**
     * الوصف المختصر بالعربية
     * Short description in Arabic
     */
    @Schema(description = "الوصف المختصر بالعربية / Short description in Arabic")
    private String shortDescriptionAr;
    
    /**
     * رمز المنتج
     * Product SKU
     */
    @Schema(description = "رمز المنتج / Product SKU", example = "SW-PRO-001")
    private String sku;
    
    /**
     * السعر الحالي
     * Current price
     */
    @Schema(description = "السعر الحالي / Current price", example = "299.99")
    private BigDecimal price;
    
    /**
     * سعر المقارنة
     * Compare price
     */
    @Schema(description = "سعر المقارنة / Compare price", example = "349.99")
    private BigDecimal comparePrice;
    
    /**
     * نسبة الخصم
     * Discount percentage
     */
    @Schema(description = "نسبة الخصم / Discount percentage", example = "15.5")
    private BigDecimal discountPercentage;
    
    /**
     * كمية المخزون
     * Stock quantity
     */
    @Schema(description = "كمية المخزون / Stock quantity", example = "50")
    private Integer stockQuantity;
    
    /**
     * الحد الأدنى للمخزون
     * Minimum stock level
     */
    @Schema(description = "الحد الأدنى للمخزون / Minimum stock level", example = "5")
    private Integer minStockLevel;
    
    /**
     * معرف الفئة
     * Category ID
     */
    @Schema(description = "معرف الفئة / Category ID")
    private UUID categoryId;
    
    /**
     * اسم الفئة
     * Category name
     */
    @Schema(description = "اسم الفئة / Category name", example = "Electronics")
    private String categoryName;
    
    /**
     * اسم الفئة بالعربية
     * Category name in Arabic
     */
    @Schema(description = "اسم الفئة بالعربية / Category name in Arabic", example = "إلكترونيات")
    private String categoryNameAr;
    
    /**
     * معرف العلامة التجارية
     * Brand ID
     */
    @Schema(description = "معرف العلامة التجارية / Brand ID")
    private UUID brandId;
    
    /**
     * اسم العلامة التجارية
     * Brand name
     */
    @Schema(description = "اسم العلامة التجارية / Brand name", example = "TechBrand")
    private String brandName;
    
    /**
     * اسم العلامة التجارية بالعربية
     * Brand name in Arabic
     */
    @Schema(description = "اسم العلامة التجارية بالعربية / Brand name in Arabic", example = "علامة تقنية")
    private String brandNameAr;
    
    /**
     * الصورة الرئيسية
     * Primary image
     */
    @Schema(description = "الصورة الرئيسية / Primary image")
    private String primaryImageUrl;
    
    /**
     * صور المنتج
     * Product images
     */
    @Schema(description = "صور المنتج / Product images")
    private List<ProductImageInfo> images;
    
    /**
     * العلامات
     * Tags
     */
    @Schema(description = "العلامات / Tags", example = "[\"smartwatch\", \"fitness\", \"health\"]")
    private List<String> tags;
    
    /**
     * الخصائص المعروضة
     * Displayed attributes
     */
    @Schema(description = "الخصائص المعروضة / Displayed attributes")
    private List<AttributeInfo> displayedAttributes;
    
    /**
     * حالة المنتج
     * Product status
     */
    @Schema(description = "حالة المنتج / Product status")
    private ProductStatus status;
    
    /**
     * إحصائيات المنتج
     * Product statistics
     */
    @Schema(description = "إحصائيات المنتج / Product statistics")
    private ProductStats stats;
    
    /**
     * تاريخ الإنشاء
     * Created date
     */
    @Schema(description = "تاريخ الإنشاء / Created date")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    
    /**
     * تاريخ آخر تحديث
     * Last updated date
     */
    @Schema(description = "تاريخ آخر تحديث / Last updated date")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;
    
    // ===============================
    // Constructors
    // ===============================
    
    /**
     * Constructor افتراضي
     * Default constructor
     */
    public ProductListResponse() {}
    
    // ===============================
    // Builder Pattern
    // ===============================
    
    /**
     * بناء استجابة قائمة المنتجات
     * Build product list response
     * 
     * @return Builder
     */
    public static Builder builder() {
        return new Builder();
    }
    
    /**
     * فئة البناء لاستجابة قائمة المنتجات
     * Builder class for product list response
     */
    public static class Builder {
        private final ProductListResponse response;
        
        public Builder() {
            this.response = new ProductListResponse();
        }
        
        public Builder id(UUID id) {
            response.id = id;
            return this;
        }
        
        public Builder displayId(String displayId) {
            response.displayId = displayId;
            return this;
        }
        
        public Builder name(String name) {
            response.name = name;
            return this;
        }
        
        public Builder nameAr(String nameAr) {
            response.nameAr = nameAr;
            return this;
        }
        
        public Builder slug(String slug) {
            response.slug = slug;
            return this;
        }
        
        public Builder shortDescription(String shortDescription) {
            response.shortDescription = shortDescription;
            return this;
        }
        
        public Builder shortDescriptionAr(String shortDescriptionAr) {
            response.shortDescriptionAr = shortDescriptionAr;
            return this;
        }
        
        public Builder sku(String sku) {
            response.sku = sku;
            return this;
        }
        
        public Builder price(BigDecimal price) {
            response.price = price;
            return this;
        }
        
        public Builder comparePrice(BigDecimal comparePrice) {
            response.comparePrice = comparePrice;
            return this;
        }
        
        public Builder discountPercentage(BigDecimal discountPercentage) {
            response.discountPercentage = discountPercentage;
            return this;
        }
        
        public Builder stockQuantity(Integer stockQuantity) {
            response.stockQuantity = stockQuantity;
            return this;
        }
        
        public Builder minStockLevel(Integer minStockLevel) {
            response.minStockLevel = minStockLevel;
            return this;
        }
        
        public Builder category(UUID categoryId, String categoryName, String categoryNameAr) {
            response.categoryId = categoryId;
            response.categoryName = categoryName;
            response.categoryNameAr = categoryNameAr;
            return this;
        }
        
        public Builder brand(UUID brandId, String brandName, String brandNameAr) {
            response.brandId = brandId;
            response.brandName = brandName;
            response.brandNameAr = brandNameAr;
            return this;
        }
        
        public Builder primaryImageUrl(String primaryImageUrl) {
            response.primaryImageUrl = primaryImageUrl;
            return this;
        }
        
        public Builder images(List<ProductImageInfo> images) {
            response.images = images;
            return this;
        }
        
        public Builder tags(List<String> tags) {
            response.tags = tags;
            return this;
        }
        
        public Builder displayedAttributes(List<AttributeInfo> displayedAttributes) {
            response.displayedAttributes = displayedAttributes;
            return this;
        }
        
        public Builder status(ProductStatus status) {
            response.status = status;
            return this;
        }
        
        public Builder stats(ProductStats stats) {
            response.stats = stats;
            return this;
        }
        
        public Builder timestamps(LocalDateTime createdAt, LocalDateTime updatedAt) {
            response.createdAt = createdAt;
            response.updatedAt = updatedAt;
            return this;
        }
        
        public ProductListResponse build() {
            return response;
        }
    }
    
    // ===============================
    // Inner Classes
    // ===============================
    
    /**
     * معلومات صورة المنتج
     * Product image information
     */
    @Schema(description = "معلومات صورة المنتج / Product image information")
    public static class ProductImageInfo {
        
        /**
         * معرف الصورة
         * Image ID
         */
        @Schema(description = "معرف الصورة / Image ID")
        private UUID id;
        
        /**
         * رابط الصورة
         * Image URL
         */
        @Schema(description = "رابط الصورة / Image URL")
        private String imageUrl;
        
        /**
         * النص البديل
         * Alternative text
         */
        @Schema(description = "النص البديل / Alternative text")
        private String altText;
        
        /**
         * النص البديل بالعربية
         * Alternative text in Arabic
         */
        @Schema(description = "النص البديل بالعربية / Alternative text in Arabic")
        private String altTextAr;
        
        /**
         * ترتيب الصورة
         * Image sort order
         */
        @Schema(description = "ترتيب الصورة / Image sort order")
        private Integer sortOrder;
        
        /**
         * هل الصورة رئيسية
         * Is primary image
         */
        @Schema(description = "هل الصورة رئيسية / Is primary image")
        private Boolean isPrimary;
        
        // Constructors
        public ProductImageInfo() {}
        
        public ProductImageInfo(UUID id, String imageUrl, String altText, String altTextAr, 
                               Integer sortOrder, Boolean isPrimary) {
            this.id = id;
            this.imageUrl = imageUrl;
            this.altText = altText;
            this.altTextAr = altTextAr;
            this.sortOrder = sortOrder;
            this.isPrimary = isPrimary;
        }
        
        // Getters and Setters
        public UUID getId() { return id; }
        public void setId(UUID id) { this.id = id; }
        
        public String getImageUrl() { return imageUrl; }
        public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
        
        public String getAltText() { return altText; }
        public void setAltText(String altText) { this.altText = altText; }
        
        public String getAltTextAr() { return altTextAr; }
        public void setAltTextAr(String altTextAr) { this.altTextAr = altTextAr; }
        
        public Integer getSortOrder() { return sortOrder; }
        public void setSortOrder(Integer sortOrder) { this.sortOrder = sortOrder; }
        
        public Boolean getIsPrimary() { return isPrimary; }
        public void setIsPrimary(Boolean isPrimary) { this.isPrimary = isPrimary; }
    }
    
    /**
     * معلومات الخاصية
     * Attribute information
     */
    @Schema(description = "معلومات الخاصية / Attribute information")
    public static class AttributeInfo {
        
        /**
         * اسم الخاصية
         * Attribute name
         */
        @Schema(description = "اسم الخاصية / Attribute name", example = "Color")
        private String name;
        
        /**
         * اسم الخاصية بالعربية
         * Attribute name in Arabic
         */
        @Schema(description = "اسم الخاصية بالعربية / Attribute name in Arabic", example = "اللون")
        private String nameAr;
        
        /**
         * قيمة الخاصية
         * Attribute value
         */
        @Schema(description = "قيمة الخاصية / Attribute value", example = "Black")
        private String value;
        
        /**
         * قيمة الخاصية بالعربية
         * Attribute value in Arabic
         */
        @Schema(description = "قيمة الخاصية بالعربية / Attribute value in Arabic", example = "أسود")
        private String valueAr;
        
        /**
         * نوع الخاصية
         * Attribute type
         */
        @Schema(description = "نوع الخاصية / Attribute type", example = "color")
        private String type;
        
        /**
         * هل الخاصية قابلة للبحث
         * Is searchable attribute
         */
        @Schema(description = "هل الخاصية قابلة للبحث / Is searchable attribute")
        private Boolean isSearchable;
        
        /**
         * هل الخاصية مرئية
         * Is visible attribute
         */
        @Schema(description = "هل الخاصية مرئية / Is visible attribute")
        private Boolean isVisible;
        
        // Constructors
        public AttributeInfo() {}
        
        public AttributeInfo(String name, String nameAr, String value, String valueAr, 
                           String type, Boolean isSearchable, Boolean isVisible) {
            this.name = name;
            this.nameAr = nameAr;
            this.value = value;
            this.valueAr = valueAr;
            this.type = type;
            this.isSearchable = isSearchable;
            this.isVisible = isVisible;
        }
        
        // Getters and Setters
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        
        public String getNameAr() { return nameAr; }
        public void setNameAr(String nameAr) { this.nameAr = nameAr; }
        
        public String getValue() { return value; }
        public void setValue(String value) { this.value = value; }
        
        public String getValueAr() { return valueAr; }
        public void setValueAr(String valueAr) { this.valueAr = valueAr; }
        
        public String getType() { return type; }
        public void setType(String type) { this.type = type; }
        
        public Boolean getIsSearchable() { return isSearchable; }
        public void setIsSearchable(Boolean isSearchable) { this.isSearchable = isSearchable; }
        
        public Boolean getIsVisible() { return isVisible; }
        public void setIsVisible(Boolean isVisible) { this.isVisible = isVisible; }
    }
    
    /**
     * حالة المنتج
     * Product status
     */
    @Schema(description = "حالة المنتج / Product status")
    public static class ProductStatus {
        
        /**
         * هل المنتج نشط
         * Is product active
         */
        @Schema(description = "هل المنتج نشط / Is product active")
        private Boolean isActive;
        
        /**
         * هل المنتج مميز
         * Is product featured
         */
        @Schema(description = "هل المنتج مميز / Is product featured")
        private Boolean isFeatured;
        
        /**
         * هل المنتج متوفر
         * Is product available
         */
        @Schema(description = "هل المنتج متوفر / Is product available")
        private Boolean isAvailable;
        
        /**
         * هل المخزون منخفض
         * Is stock low
         */
        @Schema(description = "هل المخزون منخفض / Is stock low")
        private Boolean isLowStock;
        
        /**
         * هل المنتج غير متوفر
         * Is product out of stock
         */
        @Schema(description = "هل المنتج غير متوفر / Is product out of stock")
        private Boolean isOutOfStock;
        
        /**
         * هل المنتج رقمي
         * Is digital product
         */
        @Schema(description = "هل المنتج رقمي / Is digital product")
        private Boolean isDigital;
        
        /**
         * هل يتطلب شحن
         * Requires shipping
         */
        @Schema(description = "هل يتطلب شحن / Requires shipping")
        private Boolean requiresShipping;
        
        /**
         * حالة المنتج النصية
         * Product status text
         */
        @Schema(description = "حالة المنتج النصية / Product status text")
        private String statusText;
        
        /**
         * حالة المنتج النصية بالعربية
         * Product status text in Arabic
         */
        @Schema(description = "حالة المنتج النصية بالعربية / Product status text in Arabic")
        private String statusTextAr;
        
        /**
         * لون الحالة
         * Status color
         */
        @Schema(description = "لون الحالة / Status color", example = "green")
        private String statusColor;
        
        // Constructors
        public ProductStatus() {}
        
        // Builder pattern
        public static Builder builder() {
            return new Builder();
        }
        
        public static class Builder {
            private final ProductStatus status;
            
            public Builder() {
                this.status = new ProductStatus();
            }
            
            public Builder isActive(Boolean isActive) {
                status.isActive = isActive;
                return this;
            }
            
            public Builder isFeatured(Boolean isFeatured) {
                status.isFeatured = isFeatured;
                return this;
            }
            
            public Builder isAvailable(Boolean isAvailable) {
                status.isAvailable = isAvailable;
                return this;
            }
            
            public Builder isLowStock(Boolean isLowStock) {
                status.isLowStock = isLowStock;
                return this;
            }
            
            public Builder isOutOfStock(Boolean isOutOfStock) {
                status.isOutOfStock = isOutOfStock;
                return this;
            }
            
            public Builder isDigital(Boolean isDigital) {
                status.isDigital = isDigital;
                return this;
            }
            
            public Builder requiresShipping(Boolean requiresShipping) {
                status.requiresShipping = requiresShipping;
                return this;
            }
            
            public Builder statusText(String statusText, String statusTextAr) {
                status.statusText = statusText;
                status.statusTextAr = statusTextAr;
                return this;
            }
            
            public Builder statusColor(String statusColor) {
                status.statusColor = statusColor;
                return this;
            }
            
            public ProductStatus build() {
                return status;
            }
        }
        
        // Getters and Setters
        public Boolean getIsActive() { return isActive; }
        public void setIsActive(Boolean isActive) { this.isActive = isActive; }
        
        public Boolean getIsFeatured() { return isFeatured; }
        public void setIsFeatured(Boolean isFeatured) { this.isFeatured = isFeatured; }
        
        public Boolean getIsAvailable() { return isAvailable; }
        public void setIsAvailable(Boolean isAvailable) { this.isAvailable = isAvailable; }
        
        public Boolean getIsLowStock() { return isLowStock; }
        public void setIsLowStock(Boolean isLowStock) { this.isLowStock = isLowStock; }
        
        public Boolean getIsOutOfStock() { return isOutOfStock; }
        public void setIsOutOfStock(Boolean isOutOfStock) { this.isOutOfStock = isOutOfStock; }
        
        public Boolean getIsDigital() { return isDigital; }
        public void setIsDigital(Boolean isDigital) { this.isDigital = isDigital; }
        
        public Boolean getRequiresShipping() { return requiresShipping; }
        public void setRequiresShipping(Boolean requiresShipping) { this.requiresShipping = requiresShipping; }
        
        public String getStatusText() { return statusText; }
        public void setStatusText(String statusText) { this.statusText = statusText; }
        
        public String getStatusTextAr() { return statusTextAr; }
        public void setStatusTextAr(String statusTextAr) { this.statusTextAr = statusTextAr; }
        
        public String getStatusColor() { return statusColor; }
        public void setStatusColor(String statusColor) { this.statusColor = statusColor; }
    }
    
    /**
     * إحصائيات المنتج
     * Product statistics
     */
    @Schema(description = "إحصائيات المنتج / Product statistics")
    public static class ProductStats {
        
        /**
         * عدد المشاهدات
         * View count
         */
        @Schema(description = "عدد المشاهدات / View count")
        private Long viewCount;
        
        /**
         * عدد مرات الشراء
         * Purchase count
         */
        @Schema(description = "عدد مرات الشراء / Purchase count")
        private Long purchaseCount;
        
        /**
         * عدد مرات الإضافة للسلة
         * Add to cart count
         */
        @Schema(description = "عدد مرات الإضافة للسلة / Add to cart count")
        private Long addToCartCount;
        
        /**
         * عدد مرات الإعجاب
         * Like count
         */
        @Schema(description = "عدد مرات الإعجاب / Like count")
        private Long likeCount;
        
        /**
         * التقييم المتوسط
         * Average rating
         */
        @Schema(description = "التقييم المتوسط / Average rating")
        private BigDecimal averageRating;
        
        /**
         * عدد التقييمات
         * Rating count
         */
        @Schema(description = "عدد التقييمات / Rating count")
        private Long ratingCount;
        
        /**
         * معدل التحويل
         * Conversion rate
         */
        @Schema(description = "معدل التحويل / Conversion rate")
        private BigDecimal conversionRate;
        
        /**
         * آخر مشاهدة
         * Last viewed
         */
        @Schema(description = "آخر مشاهدة / Last viewed")
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime lastViewed;
        
        /**
         * آخر شراء
         * Last purchased
         */
        @Schema(description = "آخر شراء / Last purchased")
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime lastPurchased;
        
        // Constructors
        public ProductStats() {}
        
        // Builder pattern
        public static Builder builder() {
            return new Builder();
        }
        
        public static class Builder {
            private final ProductStats stats;
            
            public Builder() {
                this.stats = new ProductStats();
            }
            
            public Builder viewCount(Long viewCount) {
                stats.viewCount = viewCount;
                return this;
            }
            
            public Builder purchaseCount(Long purchaseCount) {
                stats.purchaseCount = purchaseCount;
                return this;
            }
            
            public Builder addToCartCount(Long addToCartCount) {
                stats.addToCartCount = addToCartCount;
                return this;
            }
            
            public Builder likeCount(Long likeCount) {
                stats.likeCount = likeCount;
                return this;
            }
            
            public Builder averageRating(BigDecimal averageRating) {
                stats.averageRating = averageRating;
                return this;
            }
            
            public Builder ratingCount(Long ratingCount) {
                stats.ratingCount = ratingCount;
                return this;
            }
            
            public Builder conversionRate(BigDecimal conversionRate) {
                stats.conversionRate = conversionRate;
                return this;
            }
            
            public Builder lastViewed(LocalDateTime lastViewed) {
                stats.lastViewed = lastViewed;
                return this;
            }
            
            public Builder lastPurchased(LocalDateTime lastPurchased) {
                stats.lastPurchased = lastPurchased;
                return this;
            }
            
            public ProductStats build() {
                return stats;
            }
        }
        
        // Getters and Setters
        public Long getViewCount() { return viewCount; }
        public void setViewCount(Long viewCount) { this.viewCount = viewCount; }
        
        public Long getPurchaseCount() { return purchaseCount; }
        public void setPurchaseCount(Long purchaseCount) { this.purchaseCount = purchaseCount; }
        
        public Long getAddToCartCount() { return addToCartCount; }
        public void setAddToCartCount(Long addToCartCount) { this.addToCartCount = addToCartCount; }
        
        public Long getLikeCount() { return likeCount; }
        public void setLikeCount(Long likeCount) { this.likeCount = likeCount; }
        
        public BigDecimal getAverageRating() { return averageRating; }
        public void setAverageRating(BigDecimal averageRating) { this.averageRating = averageRating; }
        
        public Long getRatingCount() { return ratingCount; }
        public void setRatingCount(Long ratingCount) { this.ratingCount = ratingCount; }
        
        public BigDecimal getConversionRate() { return conversionRate; }
        public void setConversionRate(BigDecimal conversionRate) { this.conversionRate = conversionRate; }
        
        public LocalDateTime getLastViewed() { return lastViewed; }
        public void setLastViewed(LocalDateTime lastViewed) { this.lastViewed = lastViewed; }
        
        public LocalDateTime getLastPurchased() { return lastPurchased; }
        public void setLastPurchased(LocalDateTime lastPurchased) { this.lastPurchased = lastPurchased; }
    }
    
    // ===============================
    // Utility Methods
    // ===============================
    
    /**
     * التحقق من وجود خصم
     * Check if has discount
     * 
     * @return boolean
     */
    public boolean hasDiscount() {
        return comparePrice != null && comparePrice.compareTo(price) > 0;
    }
    
    /**
     * التحقق من وجود صور
     * Check if has images
     * 
     * @return boolean
     */
    public boolean hasImages() {
        return images != null && !images.isEmpty();
    }
    
    /**
     * التحقق من وجود صورة رئيسية
     * Check if has primary image
     * 
     * @return boolean
     */
    public boolean hasPrimaryImage() {
        return primaryImageUrl != null && !primaryImageUrl.trim().isEmpty();
    }
    
    /**
     * التحقق من وجود علامات
     * Check if has tags
     * 
     * @return boolean
     */
    public boolean hasTags() {
        return tags != null && !tags.isEmpty();
    }
    
    /**
     * التحقق من وجود خصائص معروضة
     * Check if has displayed attributes
     * 
     * @return boolean
     */
    public boolean hasDisplayedAttributes() {
        return displayedAttributes != null && !displayedAttributes.isEmpty();
    }
    
    /**
     * الحصول على النص الودود للحالة
     * Get friendly status text
     * 
     * @return String
     */
    public String getFriendlyStatusText() {
        if (status == null) return "Unknown";
        
        if (Boolean.TRUE.equals(status.isOutOfStock)) {
            return "Out of Stock";
        }
        if (Boolean.TRUE.equals(status.isLowStock)) {
            return "Low Stock";
        }
        if (Boolean.TRUE.equals(status.isAvailable)) {
            return "Available";
        }
        if (Boolean.FALSE.equals(status.isActive)) {
            return "Inactive";
        }
        
        return "Available";
    }
    
    /**
     * الحصول على النص الودود للحالة بالعربية
     * Get friendly status text in Arabic
     * 
     * @return String
     */
    public String getFriendlyStatusTextAr() {
        if (status == null) return "غير معروف";
        
        if (Boolean.TRUE.equals(status.isOutOfStock)) {
            return "غير متوفر";
        }
        if (Boolean.TRUE.equals(status.isLowStock)) {
            return "كمية قليلة";
        }
        if (Boolean.TRUE.equals(status.isAvailable)) {
            return "متوفر";
        }
        if (Boolean.FALSE.equals(status.isActive)) {
            return "غير نشط";
        }
        
        return "متوفر";
    }
    
    // ===============================
    // Getters and Setters
    // ===============================
    
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    
    public String getDisplayId() { return displayId; }
    public void setDisplayId(String displayId) { this.displayId = displayId; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getNameAr() { return nameAr; }
    public void setNameAr(String nameAr) { this.nameAr = nameAr; }
    
    public String getSlug() { return slug; }
    public void setSlug(String slug) { this.slug = slug; }
    
    public String getShortDescription() { return shortDescription; }
    public void setShortDescription(String shortDescription) { this.shortDescription = shortDescription; }
    
    public String getShortDescriptionAr() { return shortDescriptionAr; }
    public void setShortDescriptionAr(String shortDescriptionAr) { this.shortDescriptionAr = shortDescriptionAr; }
    
    public String getSku() { return sku; }
    public void setSku(String sku) { this.sku = sku; }
    
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
    
    public BigDecimal getComparePrice() { return comparePrice; }
    public void setComparePrice(BigDecimal comparePrice) { this.comparePrice = comparePrice; }
    
    public BigDecimal getDiscountPercentage() { return discountPercentage; }
    public void setDiscountPercentage(BigDecimal discountPercentage) { this.discountPercentage = discountPercentage; }
    
    public Integer getStockQuantity() { return stockQuantity; }
    public void setStockQuantity(Integer stockQuantity) { this.stockQuantity = stockQuantity; }
    
    public Integer getMinStockLevel() { return minStockLevel; }
    public void setMinStockLevel(Integer minStockLevel) { this.minStockLevel = minStockLevel; }
    
    public UUID getCategoryId() { return categoryId; }
    public void setCategoryId(UUID categoryId) { this.categoryId = categoryId; }
    
    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }
    
    public String getCategoryNameAr() { return categoryNameAr; }
    public void setCategoryNameAr(String categoryNameAr) { this.categoryNameAr = categoryNameAr; }
    
    public UUID getBrandId() { return brandId; }
    public void setBrandId(UUID brandId) { this.brandId = brandId; }
    
    public String getBrandName() { return brandName; }
    public void setBrandName(String brandName) { this.brandName = brandName; }
    
    public String getBrandNameAr() { return brandNameAr; }
    public void setBrandNameAr(String brandNameAr) { this.brandNameAr = brandNameAr; }
    
    public String getPrimaryImageUrl() { return primaryImageUrl; }
    public void setPrimaryImageUrl(String primaryImageUrl) { this.primaryImageUrl = primaryImageUrl; }
    
    public List<ProductImageInfo> getImages() { return images; }
    public void setImages(List<ProductImageInfo> images) { this.images = images; }
    
    public List<String> getTags() { return tags; }
    public void setTags(List<String> tags) { this.tags = tags; }
    
    public List<AttributeInfo> getDisplayedAttributes() { return displayedAttributes; }
    public void setDisplayedAttributes(List<AttributeInfo> displayedAttributes) { this.displayedAttributes = displayedAttributes; }
    
    public ProductStatus getStatus() { return status; }
    public void setStatus(ProductStatus status) { this.status = status; }
    
    public ProductStats getStats() { return stats; }
    public void setStats(ProductStats stats) { this.stats = stats; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    
    // ===============================
    // toString Method
    // ===============================
    
    @Override
    public String toString() {
        return "ProductListResponse{" +
                "id=" + id +
                ", displayId='" + displayId + '\'' +
                ", name='" + name + '\'' +
                ", nameAr='" + nameAr + '\'' +
                ", slug='" + slug + '\'' +
                ", sku='" + sku + '\'' +
                ", price=" + price +
                ", comparePrice=" + comparePrice +
                ", discountPercentage=" + discountPercentage +
                ", stockQuantity=" + stockQuantity +
                ", categoryName='" + categoryName + '\'' +
                ", brandName='" + brandName + '\'' +
                ", status=" + status +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
