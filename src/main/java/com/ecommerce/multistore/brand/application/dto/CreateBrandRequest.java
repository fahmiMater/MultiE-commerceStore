
/* ---- File: src/main/java/com/ecommerce/multistore/brand/application/dto/CreateBrandRequest.java ---- */

package com.ecommerce.multistore.brand.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;

/**
 * طلب إنشاء علامة تجارية
 * Create Brand Request DTO
 */
public class CreateBrandRequest {

    @NotBlank(message = "Brand name is required")
    @Size(max = 255, message = "Brand name cannot exceed 255 characters")
    private String name;

    @Size(max = 255, message = "Arabic name cannot exceed 255 characters")
    private String nameAr;

    @Size(max = 5000, message = "Description cannot exceed 5000 characters")
    private String description;

    @Size(max = 5000, message = "Arabic description cannot exceed 5000 characters")
    private String descriptionAr;

    private String logoUrl;
    private String websiteUrl;

    @Min(value = 0, message = "Sort order cannot be negative")
    @Max(value = 9999, message = "Sort order cannot exceed 9999")
    private Integer sortOrder = 0;

    private Boolean isActive = true;

    // Constructors
    public CreateBrandRequest() {}

    public CreateBrandRequest(String name) {
        this.name = name;
    }

    // Getters and Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getNameAr() { return nameAr; }
    public void setNameAr(String nameAr) { this.nameAr = nameAr; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getDescriptionAr() { return descriptionAr; }
    public void setDescriptionAr(String descriptionAr) { this.descriptionAr = descriptionAr; }

    public String getLogoUrl() { return logoUrl; }
    public void setLogoUrl(String logoUrl) { this.logoUrl = logoUrl; }

    public String getWebsiteUrl() { return websiteUrl; }
    public void setWebsiteUrl(String websiteUrl) { this.websiteUrl = websiteUrl; }

    public Integer getSortOrder() { return sortOrder; }
    public void setSortOrder(Integer sortOrder) { this.sortOrder = sortOrder; }

    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }
}
