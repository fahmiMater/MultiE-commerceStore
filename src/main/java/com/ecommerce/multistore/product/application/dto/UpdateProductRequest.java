package com.ecommerce.multistore.product.application.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * طلب تحديث بيانات المنتج
 * Product update request DTO
 * 
 * يسمح بتحديث مرن للمنتجات مع إمكانية تحديث حقول محددة فقط
 * Allows flexible product updates with the ability to update specific fields only
 * 
 * @author Multi-Store Team
 * @version 1.0
 * @since 2024-01-01
 */
@Schema(description = "طلب تحديث بيانات المنتج / Product update request")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UpdateProductRequest {
    
    /**
     * اسم المنتج الجديد
     * New product name
     */
    @Schema(description = "اسم المنتج الجديد / New product name", example = "Smart Watch Pro Updated")
    @Size(max = 255, message = "Product name cannot exceed 255 characters")
    private String name;
    
    /**
     * اسم المنتج الجديد بالعربية
     * New product name in Arabic
     */
    @Schema(description = "اسم المنتج الجديد بالعربية / New product name in Arabic", example = "ساعة ذكية برو محدثة")
    @Size(max = 255, message = "Arabic name cannot exceed 255 characters")
    private String nameAr;
    
    /**
     * رمز المنتج الجديد
     * New product SKU
     */
    @Schema(description = "رمز المنتج الجديد / New product SKU", example = "SW-PRO-001-V2")
    @Size(max = 100, message = "SKU cannot exceed 100 characters")
    private String sku;
    
    /**
     * الرابط الودود الجديد
     * New URL slug
     */
    @Schema(description = "الرابط الودود الجديد / New URL slug", example = "smart-watch-pro-updated")
    @Size(max = 255, message = "Slug cannot exceed 255 characters")
    private String slug;
    
    /**
     * الوصف الجديد
     * New description
     */
    @Schema(description = "الوصف الجديد / New description")
    private String description;
    
    /**
     * الوصف الجديد بالعربية
     * New description in Arabic
     */
    @Schema(description = "الوصف الجديد بالعربية / New description in Arabic")
    private String descriptionAr;
    
    /**
     * الوصف المختصر الجديد
     * New short description
     */
    @Schema(description = "الوصف المختصر الجديد / New short description")
    private String shortDescription;
    
    /**
     * الوصف المختصر الجديد بالعربية
     * New short description in Arabic
     */
    @Schema(description = "الوصف المختصر الجديد بالعربية / New short description in Arabic")
    private String shortDescriptionAr;
    
    /**
     * السعر الجديد
     * New price
     */
    @Schema(description = "السعر الجديد / New price", example = "249.99")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    @Digits(integer = 8, fraction = 2, message = "Price format is invalid")
    private BigDecimal price;
    
    /**
     * سعر المقارنة الجديد
     * New compare price
     */
    @Schema(description = "سعر المقارنة الجديد / New compare price", example = "299.99")
    @DecimalMin(value = "0.0", message = "Compare price cannot be negative")
    @Digits(integer = 8, fraction = 2, message = "Compare price format is invalid")
    private BigDecimal comparePrice;
    
    /**
     * سعر التكلفة الجديد
     * New cost price
     */
    @Schema(description = "سعر التكلفة الجديد / New cost price", example = "199.99")
    @DecimalMin(value = "0.0", message = "Cost price cannot be negative")
    @Digits(integer = 8, fraction = 2, message = "Cost price format is invalid")
    private BigDecimal costPrice;
    
    /**
     * الوزن الجديد
     * New weight
     */
    @Schema(description = "الوزن الجديد / New weight", example = "0.250")
    @DecimalMin(value = "0.0", message = "Weight cannot be negative")
    private BigDecimal weight;
    
    /**
     * كمية المخزون الجديدة
     * New stock quantity
     */
    @Schema(description = "كمية المخزون الجديدة / New stock quantity", example = "100")
    @Min(value = 0, message = "Stock quantity cannot be negative")
    private Integer stockQuantity;
    
    /**
     * الحد الأدنى للمخزون الجديد
     * New minimum stock level
     */
    @Schema(description = "الحد الأدنى للمخزون الجديد / New minimum stock level", example = "10")
    @Min(value = 0, message = "Minimum stock level cannot be negative")
    private Integer minStockLevel;
    
    /**
     * معرف الفئة الجديد
     * New category ID
     */
    @Schema(description = "معرف الفئة الجديد / New category ID")
    private UUID categoryId;
    
    /**
     * معرف العلامة التجارية الجديد
     * New brand ID
     */
    @Schema(description = "معرف العلامة التجارية الجديد / New brand ID")
    private UUID brandId;
    
    /**
     * الباركود الجديد
     * New barcode
     */
    @Schema(description = "الباركود الجديد / New barcode")
    private String barcode;
    
    /**
     * تتبع المخزون
     * Track inventory
     */
    @Schema(description = "تتبع المخزون / Track inventory", example = "true")
    private Boolean trackInventory;
    
    /**
     * المنتج نشط
     * Is product active
     */
    @Schema(description = "المنتج نشط / Is product active", example = "true")
    private Boolean isActive;
    
    /**
     * المنتج مميز
     * Is product featured
     */
    @Schema(description = "المنتج مميز / Is product featured", example = "false")
    private Boolean isFeatured;
    
    /**
     * المنتج رقمي
     * Is product digital
     */
    @Schema(description = "المنتج رقمي / Is product digital", example = "false")
    private Boolean isDigital;
    
    /**
     * يتطلب شحن
     * Requires shipping
     */
    @Schema(description = "يتطلب شحن / Requires shipping", example = "true")
    private Boolean requiresShipping;
    
    /**
     * الخصائص الجديدة
     * New attributes
     */
    @Schema(description = "الخصائص الجديدة / New attributes")
    private String attributes;
    
    /**
     * العلامات الجديدة
     * New tags
     */
    @Schema(description = "العلامات الجديدة / New tags", example = "[\"smartwatch\", \"fitness\", \"updated\"]")
    private List<String> tags;
    
    /**
     * الحقول المطلوب تحديثها
     * Fields to update
     */
    @Schema(description = "الحقول المطلوب تحديثها / Fields to update", 
            example = "[\"name\", \"price\", \"stockQuantity\"]")
    private List<String> fieldsToUpdate;
    
    /**
     * خيارات التحديث
     * Update options
     */
    @Schema(description = "خيارات التحديث / Update options")
    private UpdateOptions updateOptions;
    
    /**
     * تحديثات الخصائص المرنة
     * Flexible attribute updates
     */
    @Schema(description = "تحديثات الخصائص المرنة / Flexible attribute updates")
    private Map<String, Object> attributeUpdates;
    
    /**
     * تحديثات الأسعار المجمعة
     * Bulk price updates
     */
    @Schema(description = "تحديثات الأسعار المجمعة / Bulk price updates")
    private PriceUpdateInfo priceUpdateInfo;
    
    /**
     * تحديثات المخزون المجمعة
     * Bulk inventory updates
     */
    @Schema(description = "تحديثات المخزون المجمعة / Bulk inventory updates")
    private InventoryUpdateInfo inventoryUpdateInfo;
    
    /**
     * إعدادات SEO
     * SEO settings
     */
    @Schema(description = "إعدادات SEO / SEO settings")
    private SeoSettings seoSettings;
    
    /**
     * معلومات التدقيق
     * Audit information
     */
    @Schema(description = "معلومات التدقيق / Audit information")
    private AuditInfo auditInfo;
    
    // ===============================
    // Constructors
    // ===============================
    
    /**
     * Constructor افتراضي
     * Default constructor
     */
    public UpdateProductRequest() {}
    
    /**
     * Constructor مع الحقول الأساسية
     * Constructor with basic fields
     * 
     * @param name اسم المنتج
     * @param price السعر
     * @param stockQuantity كمية المخزون
     */
    public UpdateProductRequest(String name, BigDecimal price, Integer stockQuantity) {
        this.name = name;
        this.price = price;
        this.stockQuantity = stockQuantity;
    }
    
    // ===============================
    // Builder Pattern
    // ===============================
    
    /**
     * بناء طلب تحديث المنتج
     * Build product update request
     * 
     * @return Builder
     */
    public static Builder builder() {
        return new Builder();
    }
    
    /**
     * فئة البناء لطلب تحديث المنتج
     * Builder class for product update request
     */
    public static class Builder {
        private final UpdateProductRequest request;
        
        public Builder() {
            this.request = new UpdateProductRequest();
        }
        
        public Builder name(String name) {
            request.name = name;
            return this;
        }
        
        public Builder nameAr(String nameAr) {
            request.nameAr = nameAr;
            return this;
        }
        
        public Builder sku(String sku) {
            request.sku = sku;
            return this;
        }
        
        public Builder slug(String slug) {
            request.slug = slug;
            return this;
        }
        
        public Builder description(String description) {
            request.description = description;
            return this;
        }
        
        public Builder descriptionAr(String descriptionAr) {
            request.descriptionAr = descriptionAr;
            return this;
        }
        
        public Builder shortDescription(String shortDescription) {
            request.shortDescription = shortDescription;
            return this;
        }
        
        public Builder shortDescriptionAr(String shortDescriptionAr) {
            request.shortDescriptionAr = shortDescriptionAr;
            return this;
        }
        
        public Builder price(BigDecimal price) {
            request.price = price;
            return this;
        }
        
        public Builder comparePrice(BigDecimal comparePrice) {
            request.comparePrice = comparePrice;
            return this;
        }
        
        public Builder costPrice(BigDecimal costPrice) {
            request.costPrice = costPrice;
            return this;
        }
        
        public Builder weight(BigDecimal weight) {
            request.weight = weight;
            return this;
        }
        
        public Builder stockQuantity(Integer stockQuantity) {
            request.stockQuantity = stockQuantity;
            return this;
        }
        
        public Builder minStockLevel(Integer minStockLevel) {
            request.minStockLevel = minStockLevel;
            return this;
        }
        
        public Builder categoryId(UUID categoryId) {
            request.categoryId = categoryId;
            return this;
        }
        
        public Builder brandId(UUID brandId) {
            request.brandId = brandId;
            return this;
        }
        
        public Builder barcode(String barcode) {
            request.barcode = barcode;
            return this;
        }
        
        public Builder trackInventory(Boolean trackInventory) {
            request.trackInventory = trackInventory;
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
        
        public Builder attributes(String attributes) {
            request.attributes = attributes;
            return this;
        }
        
        public Builder tags(List<String> tags) {
            request.tags = tags;
            return this;
        }
        
        public Builder fieldsToUpdate(List<String> fieldsToUpdate) {
            request.fieldsToUpdate = fieldsToUpdate;
            return this;
        }
        
        public Builder updateOptions(UpdateOptions updateOptions) {
            request.updateOptions = updateOptions;
            return this;
        }
        
        public Builder attributeUpdates(Map<String, Object> attributeUpdates) {
            request.attributeUpdates = attributeUpdates;
            return this;
        }
        
        public Builder priceUpdateInfo(PriceUpdateInfo priceUpdateInfo) {
            request.priceUpdateInfo = priceUpdateInfo;
            return this;
        }
        
        public Builder inventoryUpdateInfo(InventoryUpdateInfo inventoryUpdateInfo) {
            request.inventoryUpdateInfo = inventoryUpdateInfo;
            return this;
        }
        
        public Builder seoSettings(SeoSettings seoSettings) {
            request.seoSettings = seoSettings;
            return this;
        }
        
        public Builder auditInfo(AuditInfo auditInfo) {
            request.auditInfo = auditInfo;
            return this;
        }
        
        public UpdateProductRequest build() {
            return request;
        }
    }
    
    // ===============================
    // Inner Classes
    // ===============================
    
    /**
     * خيارات التحديث
     * Update options
     */
    @Schema(description = "خيارات التحديث / Update options")
    public static class UpdateOptions {
        
        /**
         * تحديث جزئي فقط
         * Partial update only
         */
        @Schema(description = "تحديث جزئي فقط / Partial update only", example = "true")
        private Boolean partialUpdate = true;
        
        /**
         * تحديث الطوابع الزمنية
         * Update timestamps
         */
        @Schema(description = "تحديث الطوابع الزمنية / Update timestamps", example = "true")
        private Boolean updateTimestamps = true;
        
        /**
         * التحقق من التكرار
         * Check for duplicates
         */
        @Schema(description = "التحقق من التكرار / Check for duplicates", example = "true")
        private Boolean checkForDuplicates = true;
        
        /**
         * التحقق من صحة البيانات
         * Validate data
         */
        @Schema(description = "التحقق من صحة البيانات / Validate data", example = "true")
        private Boolean validateData = true;
        
        /**
         * إشعار بالتحديث
         * Send update notification
         */
        @Schema(description = "إشعار بالتحديث / Send update notification", example = "false")
        private Boolean sendNotification = false;
        
        /**
         * تحديث المؤشرات
         * Update indexes
         */
        @Schema(description = "تحديث المؤشرات / Update indexes", example = "true")
        private Boolean updateIndexes = true;
        
        /**
         * تحديث الذاكرة المؤقتة
         * Update cache
         */
        @Schema(description = "تحديث الذاكرة المؤقتة / Update cache", example = "true")
        private Boolean updateCache = true;
        
        /**
         * تسجيل التغييرات
         * Log changes
         */
        @Schema(description = "تسجيل التغييرات / Log changes", example = "true")
        private Boolean logChanges = true;
        
        // Constructors
        public UpdateOptions() {}
        
        // Builder pattern
        public static Builder builder() {
            return new Builder();
        }
        
        public static class Builder {
            private final UpdateOptions options;
            
            public Builder() {
                this.options = new UpdateOptions();
            }
            
            public Builder partialUpdate(Boolean partialUpdate) {
                options.partialUpdate = partialUpdate;
                return this;
            }
            
            public Builder updateTimestamps(Boolean updateTimestamps) {
                options.updateTimestamps = updateTimestamps;
                return this;
            }
            
            public Builder checkForDuplicates(Boolean checkForDuplicates) {
                options.checkForDuplicates = checkForDuplicates;
                return this;
            }
            
            public Builder validateData(Boolean validateData) {
                options.validateData = validateData;
                return this;
            }
            
            public Builder sendNotification(Boolean sendNotification) {
                options.sendNotification = sendNotification;
                return this;
            }
            
            public Builder updateIndexes(Boolean updateIndexes) {
                options.updateIndexes = updateIndexes;
                return this;
            }
            
            public Builder updateCache(Boolean updateCache) {
                options.updateCache = updateCache;
                return this;
            }
            
            public Builder logChanges(Boolean logChanges) {
                options.logChanges = logChanges;
                return this;
            }
            
            public UpdateOptions build() {
                return options;
            }
        }
        
        // Getters and Setters
        public Boolean getPartialUpdate() { return partialUpdate; }
        public void setPartialUpdate(Boolean partialUpdate) { this.partialUpdate = partialUpdate; }
        
        public Boolean getUpdateTimestamps() { return updateTimestamps; }
        public void setUpdateTimestamps(Boolean updateTimestamps) { this.updateTimestamps = updateTimestamps; }
        
        public Boolean getCheckForDuplicates() { return checkForDuplicates; }
        public void setCheckForDuplicates(Boolean checkForDuplicates) { this.checkForDuplicates = checkForDuplicates; }
        
        public Boolean getValidateData() { return validateData; }
        public void setValidateData(Boolean validateData) { this.validateData = validateData; }
        
        public Boolean getSendNotification() { return sendNotification; }
        public void setSendNotification(Boolean sendNotification) { this.sendNotification = sendNotification; }
        
        public Boolean getUpdateIndexes() { return updateIndexes; }
        public void setUpdateIndexes(Boolean updateIndexes) { this.updateIndexes = updateIndexes; }
        
        public Boolean getUpdateCache() { return updateCache; }
        public void setUpdateCache(Boolean updateCache) { this.updateCache = updateCache; }
        
        public Boolean getLogChanges() { return logChanges; }
        public void setLogChanges(Boolean logChanges) { this.logChanges = logChanges; }
    }
    
    /**
     * معلومات تحديث الأسعار
     * Price update information
     */
    @Schema(description = "معلومات تحديث الأسعار / Price update information")
    public static class PriceUpdateInfo {
        
        /**
         * نوع تحديث السعر
         * Price update type
         */
        @Schema(description = "نوع تحديث السعر / Price update type", 
                allowableValues = {"FIXED", "PERCENTAGE", "INCREASE", "DECREASE"})
        private String updateType;
        
        /**
         * القيمة أو النسبة
         * Value or percentage
         */
        @Schema(description = "القيمة أو النسبة / Value or percentage", example = "10.5")
        private BigDecimal updateValue;
        
        /**
         * تحديث سعر المقارنة أيضاً
         * Update compare price as well
         */
        @Schema(description = "تحديث سعر المقارنة أيضاً / Update compare price as well", example = "true")
        private Boolean updateComparePrice = false;
        
        /**
         * الحد الأدنى للسعر
         * Minimum price limit
         */
        @Schema(description = "الحد الأدنى للسعر / Minimum price limit", example = "1.0")
        private BigDecimal minPriceLimit;
        
        /**
         * الحد الأقصى للسعر
         * Maximum price limit
         */
        @Schema(description = "الحد الأقصى للسعر / Maximum price limit", example = "1000.0")
        private BigDecimal maxPriceLimit;
        
        /**
         * تقريب السعر
         * Round price
         */
        @Schema(description = "تقريب السعر / Round price", example = "true")
        private Boolean roundPrice = true;
        
        /**
         * عدد الأرقام العشرية
         * Decimal places
         */
        @Schema(description = "عدد الأرقام العشرية / Decimal places", example = "2")
        private Integer decimalPlaces = 2;
        
        // Constructors
        public PriceUpdateInfo() {}
        
        public PriceUpdateInfo(String updateType, BigDecimal updateValue) {
            this.updateType = updateType;
            this.updateValue = updateValue;
        }
        
        // Builder pattern
        public static Builder builder() {
            return new Builder();
        }
        
        public static class Builder {
            private final PriceUpdateInfo info;
            
            public Builder() {
                this.info = new PriceUpdateInfo();
            }
            
            public Builder updateType(String updateType) {
                info.updateType = updateType;
                return this;
            }
            
            public Builder updateValue(BigDecimal updateValue) {
                info.updateValue = updateValue;
                return this;
            }
            
            public Builder updateComparePrice(Boolean updateComparePrice) {
                info.updateComparePrice = updateComparePrice;
                return this;
            }
            
            public Builder minPriceLimit(BigDecimal minPriceLimit) {
                info.minPriceLimit = minPriceLimit;
                return this;
            }
            
            public Builder maxPriceLimit(BigDecimal maxPriceLimit) {
                info.maxPriceLimit = maxPriceLimit;
                return this;
            }
            
            public Builder roundPrice(Boolean roundPrice) {
                info.roundPrice = roundPrice;
                return this;
            }
            
            public Builder decimalPlaces(Integer decimalPlaces) {
                info.decimalPlaces = decimalPlaces;
                return this;
            }
            
            public PriceUpdateInfo build() {
                return info;
            }
        }
        
        // Getters and Setters
        public String getUpdateType() { return updateType; }
        public void setUpdateType(String updateType) { this.updateType = updateType; }
        
        public BigDecimal getUpdateValue() { return updateValue; }
        public void setUpdateValue(BigDecimal updateValue) { this.updateValue = updateValue; }
        
        public Boolean getUpdateComparePrice() { return updateComparePrice; }
        public void setUpdateComparePrice(Boolean updateComparePrice) { this.updateComparePrice = updateComparePrice; }
        
        public BigDecimal getMinPriceLimit() { return minPriceLimit; }
        public void setMinPriceLimit(BigDecimal minPriceLimit) { this.minPriceLimit = minPriceLimit; }
        
        public BigDecimal getMaxPriceLimit() { return maxPriceLimit; }
        public void setMaxPriceLimit(BigDecimal maxPriceLimit) { this.maxPriceLimit = maxPriceLimit; }
        
        public Boolean getRoundPrice() { return roundPrice; }
        public void setRoundPrice(Boolean roundPrice) { this.roundPrice = roundPrice; }
        
        public Integer getDecimalPlaces() { return decimalPlaces; }
        public void setDecimalPlaces(Integer decimalPlaces) { this.decimalPlaces = decimalPlaces; }
    }
    
    /**
     * معلومات تحديث المخزون
     * Inventory update information
     */
    @Schema(description = "معلومات تحديث المخزون / Inventory update information")
    public static class InventoryUpdateInfo {
        
        /**
         * نوع تحديث المخزون
         * Inventory update type
         */
        @Schema(description = "نوع تحديث المخزون / Inventory update type", 
                allowableValues = {"SET", "INCREASE", "DECREASE", "ADJUSTMENT"})
        private String updateType;
        
        /**
         * الكمية
         * Quantity
         */
        @Schema(description = "الكمية / Quantity", example = "50")
        private Integer quantity;
        
        /**
         * سبب التحديث
         * Update reason
         */
        @Schema(description = "سبب التحديث / Update reason", example = "Stock replenishment")
        private String reason;
        
        /**
         * سبب التحديث بالعربية
         * Update reason in Arabic
         */
        @Schema(description = "سبب التحديث بالعربية / Update reason in Arabic", example = "إعادة تجهيز المخزون")
        private String reasonAr;
        
        /**
         * تحديث الحد الأدنى للمخزون
         * Update minimum stock level
         */
        @Schema(description = "تحديث الحد الأدنى للمخزون / Update minimum stock level", example = "true")
        private Boolean updateMinStockLevel = false;
        
        /**
         * الحد الأدنى الجديد للمخزون
         * New minimum stock level
         */
        @Schema(description = "الحد الأدنى الجديد للمخزون / New minimum stock level", example = "10")
        private Integer newMinStockLevel;
        
        /**
         * تسجيل حركة المخزون
         * Record inventory movement
         */
        @Schema(description = "تسجيل حركة المخزون / Record inventory movement", example = "true")
        private Boolean recordMovement = true;
        
        /**
         * معرف المرجع
         * Reference ID
         */
        @Schema(description = "معرف المرجع / Reference ID")
        private String referenceId;
        
        /**
         * نوع المرجع
         * Reference type
         */
        @Schema(description = "نوع المرجع / Reference type", example = "manual_adjustment")
        private String referenceType;
        
        // Constructors
        public InventoryUpdateInfo() {}
        
        public InventoryUpdateInfo(String updateType, Integer quantity, String reason) {
            this.updateType = updateType;
            this.quantity = quantity;
            this.reason = reason;
        }
        
        // Builder pattern
        public static Builder builder() {
            return new Builder();
        }
        
        public static class Builder {
            private final InventoryUpdateInfo info;
            
            public Builder() {
                this.info = new InventoryUpdateInfo();
            }
            
            public Builder updateType(String updateType) {
                info.updateType = updateType;
                return this;
            }
            
            public Builder quantity(Integer quantity) {
                info.quantity = quantity;
                return this;
            }
            
            public Builder reason(String reason, String reasonAr) {
                info.reason = reason;
                info.reasonAr = reasonAr;
                return this;
            }
            
            public Builder updateMinStockLevel(Boolean updateMinStockLevel) {
                info.updateMinStockLevel = updateMinStockLevel;
                return this;
            }
            
            public Builder newMinStockLevel(Integer newMinStockLevel) {
                info.newMinStockLevel = newMinStockLevel;
                return this;
            }
            
            public Builder recordMovement(Boolean recordMovement) {
                info.recordMovement = recordMovement;
                return this;
            }
            
            public Builder reference(String referenceId, String referenceType) {
                info.referenceId = referenceId;
                info.referenceType = referenceType;
                return this;
            }
            
            public InventoryUpdateInfo build() {
                return info;
            }
        }
        
        // Getters and Setters
        public String getUpdateType() { return updateType; }
        public void setUpdateType(String updateType) { this.updateType = updateType; }
        
        public Integer getQuantity() { return quantity; }
        public void setQuantity(Integer quantity) { this.quantity = quantity; }
        
        public String getReason() { return reason; }
        public void setReason(String reason) { this.reason = reason; }
        
        public String getReasonAr() { return reasonAr; }
        public void setReasonAr(String reasonAr) { this.reasonAr = reasonAr; }
        
        public Boolean getUpdateMinStockLevel() { return updateMinStockLevel; }
        public void setUpdateMinStockLevel(Boolean updateMinStockLevel) { this.updateMinStockLevel = updateMinStockLevel; }
        
        public Integer getNewMinStockLevel() { return newMinStockLevel; }
        public void setNewMinStockLevel(Integer newMinStockLevel) { this.newMinStockLevel = newMinStockLevel; }
        
        public Boolean getRecordMovement() { return recordMovement; }
        public void setRecordMovement(Boolean recordMovement) { this.recordMovement = recordMovement; }
        
        public String getReferenceId() { return referenceId; }
        public void setReferenceId(String referenceId) { this.referenceId = referenceId; }
        
        public String getReferenceType() { return referenceType; }
        public void setReferenceType(String referenceType) { this.referenceType = referenceType; }
    }
    
    /**
     * إعدادات SEO
     * SEO settings
     */
    @Schema(description = "إعدادات SEO / SEO settings")
    public static class SeoSettings {
        
        /**
         * عنوان SEO
         * SEO title
         */
        @Schema(description = "عنوان SEO / SEO title", example = "Smart Watch Pro - Best Fitness Tracker")
        private String seoTitle;
        
        /**
         * عنوان SEO بالعربية
         * SEO title in Arabic
         */
        @Schema(description = "عنوان SEO بالعربية / SEO title in Arabic", example = "ساعة ذكية برو - أفضل متتبع للياقة البدنية")
        private String seoTitleAr;
        
        /**
         * وصف SEO
         * SEO description
         */
        @Schema(description = "وصف SEO / SEO description")
        private String seoDescription;
        
        /**
         * وصف SEO بالعربية
         * SEO description in Arabic
         */
        @Schema(description = "وصف SEO بالعربية / SEO description in Arabic")
        private String seoDescriptionAr;
        
        /**
         * كلمات مفتاحية
         * Keywords
         */
        @Schema(description = "كلمات مفتاحية / Keywords", example = "[\"smartwatch\", \"fitness\", \"health\"]")
        private List<String> keywords;
        
        /**
         * كلمات مفتاحية بالعربية
         * Keywords in Arabic
         */
        @Schema(description = "كلمات مفتاحية بالعربية / Keywords in Arabic", example = "[\"ساعة ذكية\", \"لياقة\", \"صحة\"]")
        private List<String> keywordsAr;
        
        /**
         * تحديث تلقائي للـ SEO
         * Auto update SEO
         */
        @Schema(description = "تحديث تلقائي للـ SEO / Auto update SEO", example = "true")
        private Boolean autoUpdateSeo = true;
        
        // Constructors
        public SeoSettings() {}
        
        // Builder pattern
        public static Builder builder() {
            return new Builder();
        }
        
        public static class Builder {
            private final SeoSettings settings;
            
            public Builder() {
                this.settings = new SeoSettings();
            }
            
            public Builder seoTitle(String seoTitle, String seoTitleAr) {
                settings.seoTitle = seoTitle;
                settings.seoTitleAr = seoTitleAr;
                return this;
            }
            
            public Builder seoDescription(String seoDescription, String seoDescriptionAr) {
                settings.seoDescription = seoDescription;
                settings.seoDescriptionAr = seoDescriptionAr;
                return this;
            }
            
            public Builder keywords(List<String> keywords, List<String> keywordsAr) {
                settings.keywords = keywords;
                settings.keywordsAr = keywordsAr;
                return this;
            }
            
            public Builder autoUpdateSeo(Boolean autoUpdateSeo) {
                settings.autoUpdateSeo = autoUpdateSeo;
                return this;
            }
            
            public SeoSettings build() {
                return settings;
            }
        }
        
        // Getters and Setters
        public String getSeoTitle() { return seoTitle; }
        public void setSeoTitle(String seoTitle) { this.seoTitle = seoTitle; }
        
        public String getSeoTitleAr() { return seoTitleAr; }
        public void setSeoTitleAr(String seoTitleAr) { this.seoTitleAr = seoTitleAr; }
        
        public String getSeoDescription() { return seoDescription; }
        public void setSeoDescription(String seoDescription) { this.seoDescription = seoDescription; }
        
        public String getSeoDescriptionAr() { return seoDescriptionAr; }
        public void setSeoDescriptionAr(String seoDescriptionAr) { this.seoDescriptionAr = seoDescriptionAr; }
        
        public List<String> getKeywords() { return keywords; }
        public void setKeywords(List<String> keywords) { this.keywords = keywords; }
        
        public List<String> getKeywordsAr() { return keywordsAr; }
        public void setKeywordsAr(List<String> keywordsAr) { this.keywordsAr = keywordsAr; }
        
        public Boolean getAutoUpdateSeo() { return autoUpdateSeo; }
        public void setAutoUpdateSeo(Boolean autoUpdateSeo) { this.autoUpdateSeo = autoUpdateSeo; }
    }
    
    /**
     * معلومات التدقيق
     * Audit information
     */
    @Schema(description = "معلومات التدقيق / Audit information")
    public static class AuditInfo {
        
        /**
         * معرف المستخدم المحدث
         * Updated by user ID
         */
        @Schema(description = "معرف المستخدم المحدث / Updated by user ID")
        private UUID updatedBy;
        
        /**
         * سبب التحديث
         * Update reason
         */
        @Schema(description = "سبب التحديث / Update reason", example = "Price adjustment")
        private String updateReason;
        
        /**
         * سبب التحديث بالعربية
         * Update reason in Arabic
         */
        @Schema(description = "سبب التحديث بالعربية / Update reason in Arabic", example = "تعديل السعر")
        private String updateReasonAr;
        
        /**
         * ملاحظات التحديث
         * Update notes
         */
        @Schema(description = "ملاحظات التحديث / Update notes")
        private String updateNotes;
        
        /**
         * ملاحظات التحديث بالعربية
         * Update notes in Arabic
         */
        @Schema(description = "ملاحظات التحديث بالعربية / Update notes in Arabic")
        private String updateNotesAr;
        
        /**
         * نوع التحديث
         * Update type
         */
        @Schema(description = "نوع التحديث / Update type", example = "manual")
        private String updateType = "manual";
        
        /**
         * مصدر التحديث
         * Update source
         */
        @Schema(description = "مصدر التحديث / Update source", example = "admin_dashboard")
        private String updateSource;
        
        /**
         * بيانات إضافية
         * Additional data
         */
        @Schema(description = "بيانات إضافية / Additional data")
        private Map<String, Object> additionalData;
        
        // Constructors
        public AuditInfo() {}
        
        public AuditInfo(UUID updatedBy, String updateReason, String updateReasonAr) {
            this.updatedBy = updatedBy;
            this.updateReason = updateReason;
            this.updateReasonAr = updateReasonAr;
        }
        
        // Builder pattern
        public static Builder builder() {
            return new Builder();
        }
        
        public static class Builder {
            private final AuditInfo info;
            
            public Builder() {
                this.info = new AuditInfo();
            }
            
            public Builder updatedBy(UUID updatedBy) {
                info.updatedBy = updatedBy;
                return this;
            }
            
            public Builder updateReason(String updateReason, String updateReasonAr) {
                info.updateReason = updateReason;
                info.updateReasonAr = updateReasonAr;
                return this;
            }
            
            public Builder updateNotes(String updateNotes, String updateNotesAr) {
                info.updateNotes = updateNotes;
                info.updateNotesAr = updateNotesAr;
                return this;
            }
            
            public Builder updateType(String updateType) {
                info.updateType = updateType;
                return this;
            }
            
            public Builder updateSource(String updateSource) {
                info.updateSource = updateSource;
                return this;
            }
            
            public Builder additionalData(Map<String, Object> additionalData) {
                info.additionalData = additionalData;
                return this;
            }
            
            public AuditInfo build() {
                return info;
            }
        }
        
        // Getters and Setters
        public UUID getUpdatedBy() { return updatedBy; }
        public void setUpdatedBy(UUID updatedBy) { this.updatedBy = updatedBy; }
        
        public String getUpdateReason() { return updateReason; }
        public void setUpdateReason(String updateReason) { this.updateReason = updateReason; }
        
        public String getUpdateReasonAr() { return updateReasonAr; }
        public void setUpdateReasonAr(String updateReasonAr) { this.updateReasonAr = updateReasonAr; }
        
        public String getUpdateNotes() { return updateNotes; }
        public void setUpdateNotes(String updateNotes) { this.updateNotes = updateNotes; }
        
        public String getUpdateNotesAr() { return updateNotesAr; }
        public void setUpdateNotesAr(String updateNotesAr) { this.updateNotesAr = updateNotesAr; }
        
        public String getUpdateType() { return updateType; }
        public void setUpdateType(String updateType) { this.updateType = updateType; }
        
        public String getUpdateSource() { return updateSource; }
        public void setUpdateSource(String updateSource) { this.updateSource = updateSource; }
        
        public Map<String, Object> getAdditionalData() { return additionalData; }
        public void setAdditionalData(Map<String, Object> additionalData) { this.additionalData = additionalData; }
    }
    
    // ===============================
    // Utility Methods
    // ===============================
    
    /**
     * التحقق من وجود تحديثات
     * Check if has updates
     * 
     * @return boolean
     */
    public boolean hasUpdates() {
        return name != null || nameAr != null || sku != null || slug != null ||
               description != null || descriptionAr != null || 
               shortDescription != null || shortDescriptionAr != null ||
               price != null || comparePrice != null || costPrice != null ||
               weight != null || stockQuantity != null || minStockLevel != null ||
               categoryId != null || brandId != null || barcode != null ||
               trackInventory != null || isActive != null || isFeatured != null ||
               isDigital != null || requiresShipping != null ||
               attributes != null || tags != null;
    }
    
    /**
     * التحقق من وجود تحديثات أساسية
     * Check if has basic updates
     * 
     * @return boolean
     */
    public boolean hasBasicUpdates() {
        return name != null || price != null || stockQuantity != null || isActive != null;
    }
    
    /**
     * التحقق من وجود تحديثات متقدمة
     * Check if has advanced updates
     * 
     * @return boolean
     */
    public boolean hasAdvancedUpdates() {
        return priceUpdateInfo != null || inventoryUpdateInfo != null ||
               seoSettings != null || attributeUpdates != null;
    }
    
    /**
     * التحقق من وجود حقول محددة للتحديث
     * Check if has specific fields to update
     * 
     * @return boolean
     */
    public boolean hasSpecificFields() {
        return fieldsToUpdate != null && !fieldsToUpdate.isEmpty();
    }
    
    /**
     * التحقق من تحديث حقل محدد
     * Check if updating specific field
     * 
     * @param fieldName اسم الحقل
     * @return boolean
     */
    public boolean isUpdatingField(String fieldName) {
        return fieldsToUpdate != null && fieldsToUpdate.contains(fieldName);
    }
    
    /**
     * إضافة حقل للتحديث
     * Add field to update
     * 
     * @param fieldName اسم الحقل
     */
    public void addFieldToUpdate(String fieldName) {
        if (fieldsToUpdate == null) {
            fieldsToUpdate = new java.util.ArrayList<>();
        }
        if (!fieldsToUpdate.contains(fieldName)) {
            fieldsToUpdate.add(fieldName);
        }
    }
    
    /**
     * إزالة حقل من التحديث
     * Remove field from update
     * 
     * @param fieldName اسم الحقل
     */
    public void removeFieldFromUpdate(String fieldName) {
        if (fieldsToUpdate != null) {
            fieldsToUpdate.remove(fieldName);
        }
    }
    
    /**
     * الحصول على عدد الحقول المطلوب تحديثها
     * Get number of fields to update
     * 
     * @return int
     */
    public int getUpdateFieldsCount() {
        if (fieldsToUpdate != null) {
            return fieldsToUpdate.size();
        }
        
        int count = 0;
        if (name != null) count++;
        if (nameAr != null) count++;
        if (sku != null) count++;
        if (slug != null) count++;
        if (description != null) count++;
        if (descriptionAr != null) count++;
        if (shortDescription != null) count++;
        if (shortDescriptionAr != null) count++;
        if (price != null) count++;
        if (comparePrice != null) count++;
        if (costPrice != null) count++;
        if (weight != null) count++;
        if (stockQuantity != null) count++;
        if (minStockLevel != null) count++;
        if (categoryId != null) count++;
        if (brandId != null) count++;
        if (barcode != null) count++;
        if (trackInventory != null) count++;
        if (isActive != null) count++;
        if (isFeatured != null) count++;
        if (isDigital != null) count++;
        if (requiresShipping != null) count++;
        if (attributes != null) count++;
        if (tags != null) count++;
        
        return count;
    }
    
    // ===============================
    // Getters and Setters
    // ===============================
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getNameAr() { return nameAr; }
    public void setNameAr(String nameAr) { this.nameAr = nameAr; }
    
    public String getSku() { return sku; }
    public void setSku(String sku) { this.sku = sku; }
    
    public String getSlug() { return slug; }
    public void setSlug(String slug) { this.slug = slug; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public String getDescriptionAr() { return descriptionAr; }
    public void setDescriptionAr(String descriptionAr) { this.descriptionAr = descriptionAr; }
    
    public String getShortDescription() { return shortDescription; }
    public void setShortDescription(String shortDescription) { this.shortDescription = shortDescription; }
    
    public String getShortDescriptionAr() { return shortDescriptionAr; }
    public void setShortDescriptionAr(String shortDescriptionAr) { this.shortDescriptionAr = shortDescriptionAr; }
    
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
    
    public BigDecimal getComparePrice() { return comparePrice; }
    public void setComparePrice(BigDecimal comparePrice) { this.comparePrice = comparePrice; }
    
    public BigDecimal getCostPrice() { return costPrice; }
    public void setCostPrice(BigDecimal costPrice) { this.costPrice = costPrice; }
    
    public BigDecimal getWeight() { return weight; }
    public void setWeight(BigDecimal weight) { this.weight = weight; }
    
    public Integer getStockQuantity() { return stockQuantity; }
    public void setStockQuantity(Integer stockQuantity) { this.stockQuantity = stockQuantity; }
    
    public Integer getMinStockLevel() { return minStockLevel; }
    public void setMinStockLevel(Integer minStockLevel) { this.minStockLevel = minStockLevel; }
    
    public UUID getCategoryId() { return categoryId; }
    public void setCategoryId(UUID categoryId) { this.categoryId = categoryId; }
    
    public UUID getBrandId() { return brandId; }
    public void setBrandId(UUID brandId) { this.brandId = brandId; }
    
    public String getBarcode() { return barcode; }
    public void setBarcode(String barcode) { this.barcode = barcode; }
    
    public Boolean getTrackInventory() { return trackInventory; }
    public void setTrackInventory(Boolean trackInventory) { this.trackInventory = trackInventory; }
    
    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }
    
    public Boolean getIsFeatured() { return isFeatured; }
    public void setIsFeatured(Boolean isFeatured) { this.isFeatured = isFeatured; }
    
    public Boolean getIsDigital() { return isDigital; }
    public void setIsDigital(Boolean isDigital) { this.isDigital = isDigital; }
    
    public Boolean getRequiresShipping() { return requiresShipping; }
    public void setRequiresShipping(Boolean requiresShipping) { this.requiresShipping = requiresShipping; }
    
    public String getAttributes() { return attributes; }
    public void setAttributes(String attributes) { this.attributes = attributes; }
    
    public List<String> getTags() { return tags; }
    public void setTags(List<String> tags) { this.tags = tags; }
    
    public List<String> getFieldsToUpdate() { return fieldsToUpdate; }
    public void setFieldsToUpdate(List<String> fieldsToUpdate) { this.fieldsToUpdate = fieldsToUpdate; }
    
    public UpdateOptions getUpdateOptions() { return updateOptions; }
    public void setUpdateOptions(UpdateOptions updateOptions) { this.updateOptions = updateOptions; }
    
    public Map<String, Object> getAttributeUpdates() { return attributeUpdates; }
    public void setAttributeUpdates(Map<String, Object> attributeUpdates) { this.attributeUpdates = attributeUpdates; }
    
    public PriceUpdateInfo getPriceUpdateInfo() { return priceUpdateInfo; }
    public void setPriceUpdateInfo(PriceUpdateInfo priceUpdateInfo) { this.priceUpdateInfo = priceUpdateInfo; }
    
    public InventoryUpdateInfo getInventoryUpdateInfo() { return inventoryUpdateInfo; }
    public void setInventoryUpdateInfo(InventoryUpdateInfo inventoryUpdateInfo) { this.inventoryUpdateInfo = inventoryUpdateInfo; }
    
    public SeoSettings getSeoSettings() { return seoSettings; }
    public void setSeoSettings(SeoSettings seoSettings) { this.seoSettings = seoSettings; }
    
    public AuditInfo getAuditInfo() { return auditInfo; }
    public void setAuditInfo(AuditInfo auditInfo) { this.auditInfo = auditInfo; }
    
    // ===============================
    // toString Method
    // ===============================
    
    @Override
    public String toString() {
        return "UpdateProductRequest{" +
                "name='" + name + '\'' +
                ", nameAr='" + nameAr + '\'' +
                ", sku='" + sku + '\'' +
                ", slug='" + slug + '\'' +
                ", price=" + price +
                ", comparePrice=" + comparePrice +
                ", stockQuantity=" + stockQuantity +
                ", categoryId=" + categoryId +
                ", brandId=" + brandId +
                ", isActive=" + isActive +
                ", isFeatured=" + isFeatured +
                ", fieldsToUpdate=" + fieldsToUpdate +
                ", updateOptions=" + updateOptions +
                ", hasAdvancedUpdates=" + hasAdvancedUpdates() +
                '}';
    }
}
