/* ---- File: src/main/java/com/ecommerce/multistore/category/application/dto/CategoryTreeResponse.java ---- */

package com.ecommerce.multistore.category.application.dto;

import java.util.List;

/**
 * استجابة شجرة الفئات
 * Category Tree Response DTO
 */
public class CategoryTreeResponse {

    private List<CategoryResponse> categories;
    private Integer totalCategories;
    private Integer activeCategories;
    private Integer parentCategories;
    private Integer childCategories;

    // Constructors
    public CategoryTreeResponse() {}

    public CategoryTreeResponse(List<CategoryResponse> categories) {
        this.categories = categories;
        this.totalCategories = categories.size();
        this.activeCategories = (int) categories.stream().filter(c -> c.getIsActive()).count();
        this.parentCategories = (int) categories.stream().filter(c -> c.getParentId() == null).count();
        this.childCategories = (int) categories.stream().filter(c -> c.getParentId() != null).count();
    }

    // Getters and Setters
    public List<CategoryResponse> getCategories() { return categories; }
    public void setCategories(List<CategoryResponse> categories) { this.categories = categories; }

    public Integer getTotalCategories() { return totalCategories; }
    public void setTotalCategories(Integer totalCategories) { this.totalCategories = totalCategories; }

    public Integer getActiveCategories() { return activeCategories; }
    public void setActiveCategories(Integer activeCategories) { this.activeCategories = activeCategories; }

    public Integer getParentCategories() { return parentCategories; }
    public void setParentCategories(Integer parentCategories) { this.parentCategories = parentCategories; }

    public Integer getChildCategories() { return childCategories; }
    public void setChildCategories(Integer childCategories) { this.childCategories = childCategories; }
}
