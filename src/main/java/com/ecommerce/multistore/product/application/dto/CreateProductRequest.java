package com.ecommerce.multistore.product.application.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

/**
 * طلب إنشاء منتج جديد
 * Create product request DTO
 * 
 * @author Multi-Store Team
 * @version 1.0
 * @since 2024-01-01
 */
public class CreateProductRequest {
    
    /**
     * اسم المنتج (مطلوب)
     * Product name (required)
     */
    @NotBlank(message = "Product name is required")
    @Size(max = 255, message = "Product name cannot exceed 255 characters")
    private String name;
    
    /**
     * اسم المنتج بالعربية
     * Product name in Arabic
     */
    @Size(max = 255, message = "Arabic name cannot exceed 255 characters")
    private String nameAr;
    
    /**
     * رمز المنتج (مطلوب وفريد)
     * Product SKU (required and unique)
     */
    @NotBlank(message = "SKU is required")
    @Size(max = 100, message = "SKU cannot exceed 100 characters")
    private String sku;
    
    /**
     * الرابط الودود (سيتم توليده تلقائياً إذا لم يُحدد)
     * URL slug (auto-generated if not provided)
     */
    private String slug;
    
    /**
     * وصف المنتج
     * Product description
     */
    private String description;
    
    /**
     * وصف المنتج بالعربية
     * Product description in Arabic
     */
    private String descriptionAr;
    
    /**
     * وصف مختصر
     * Short description
     */
    private String shortDescription;
    
    /**
     * وصف مختصر بالعربية
     * Short description in Arabic
     */
    private String shortDescriptionAr;
    
    /**
     * سعر المنتج (مطلوب)
     * Product price (required)
     */
    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    @Digits(integer = 8, fraction = 2, message = "Price format is invalid")
    private BigDecimal price;
    
    /**
     * سعر المقارنة
     * Compare price
     */
    @DecimalMin(value = "0.0", message = "Compare price cannot be negative")
    @Digits(integer = 8, fraction = 2, message = "Compare price format is invalid")
    private BigDecimal comparePrice;
    
    /**
     * سعر التكلفة
     * Cost price
     */
    @DecimalMin(value = "0.0", message = "Cost price cannot be negative")
    @Digits(integer = 8, fraction = 2, message = "Cost price format is invalid")
    private BigDecimal costPrice;
    
    /**
     * الوزن بالكيلوغرام
     * Weight in kilograms
     */
    @DecimalMin(value = "0.0", message = "Weight cannot be negative")
    private BigDecimal weight;
    
    /**
     * كمية المخزون
     * Stock quantity
     */
    @Min(value = 0, message = "Stock quantity cannot be negative")
    private Integer stockQuantity = 0;
    
    /**
     * الحد الأدنى للمخزون
     * Minimum stock level
     */
    @Min(value = 0, message = "Minimum stock level cannot be negative")
    private Integer minStockLevel = 5;
    
    /**
     * معرف الفئة
     * Category ID
     */
    private UUID categoryId;
    
    /**
     * معرف العلامة التجارية
     * Brand ID
     */
    private UUID brandId;
    
    /**
     * الباركود
     * Barcode
     */
    private String barcode;
    
    /**
     * تتبع المخزون
     * Track inventory
     */
    private Boolean trackInventory = true;
    
    /**
     * منتج نشط
     * Is active
     */
    private Boolean isActive = true;
    
    /**
     * منتج مميز
     * Is featured
     */
    private Boolean isFeatured = false;
    
    /**
     * منتج رقمي
     * Is digital
     */
    private Boolean isDigital = false;
    
    /**
     * يتطلب شحن
     * Requires shipping
     */
    private Boolean requiresShipping = true;
    
    /**
     * خصائص إضافية (JSON)
     * Additional attributes (JSON)
     */
    private String attributes;
    
    /**
     * العلامات
     * Tags
     */
    private List<String> tags;
    
    // ===============================
    // Constructors
    // ===============================
    
    public CreateProductRequest() {}
    
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
}
