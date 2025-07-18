package com.ecommerce.multistore.category.infrastructure.web;

import com.ecommerce.multistore.category.application.dto.CategoryResponse;
import com.ecommerce.multistore.category.application.dto.CategoryTreeResponse;
import com.ecommerce.multistore.category.application.dto.CreateCategoryRequest;
import com.ecommerce.multistore.category.application.service.CategoryService;
import com.ecommerce.multistore.shared.constants.AppConstants;
import com.ecommerce.multistore.shared.dto.ApiResponse;
import com.ecommerce.multistore.shared.dto.PaginatedResponse;
import com.ecommerce.multistore.shared.utils.PaginationUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(AppConstants.API_BASE_PATH + "/categories")
@Tag(name = "Categories", description = "إدارة الفئات - Category Management")
public class CategoryController {

    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    @Operation(summary = "Create Category", description = "إنشاء فئة جديدة")
    public ResponseEntity<ApiResponse<CategoryResponse>> createCategory(
            @Valid @RequestBody CreateCategoryRequest request) {

        CategoryResponse category = categoryService.createCategory(request);
        ApiResponse<CategoryResponse> response = ApiResponse.<CategoryResponse>success(
            category,
            "Category created successfully",
            201
        );
        response.setMessageAr("تم إنشاء الفئة بنجاح");

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get Category by ID", description = "الحصول على فئة بواسطة المعرف")
    public ResponseEntity<ApiResponse<CategoryResponse>> getCategoryById(
            @Parameter(description = "Category ID") @PathVariable UUID id) {

        return categoryService.findById(id)
                .map(category -> {
                    ApiResponse<CategoryResponse> response = ApiResponse.<CategoryResponse>success(
                        category,
                        "Category retrieved successfully"
                    );
                    response.setMessageAr("تم استرجاع الفئة بنجاح");
                    return ResponseEntity.ok(response);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/slug/{slug}")
    @Operation(summary = "Get Category by Slug", description = "الحصول على فئة بواسطة الرابط الودي")
    public ResponseEntity<ApiResponse<CategoryResponse>> getCategoryBySlug(
            @Parameter(description = "Category Slug") @PathVariable String slug) {

        return categoryService.findBySlug(slug)
                .map(category -> {
                    ApiResponse<CategoryResponse> response = ApiResponse.<CategoryResponse>success(
                        category,
                        "Category retrieved successfully"
                    );
                    response.setMessageAr("تم استرجاع الفئة بنجاح");
                    return ResponseEntity.ok(response);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    @Operation(summary = "Get All Categories", description = "الحصول على جميع الفئات")
    public ResponseEntity<ApiResponse<PaginatedResponse<CategoryResponse>>> getAllCategories(
            @Parameter(description = "Page number") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Sort by field") @RequestParam(defaultValue = "name") String sortBy,
            @Parameter(description = "Sort direction") @RequestParam(defaultValue = "asc") String sortDir) {

        Pageable pageable = PaginationUtils.createPageable(page, size, sortBy, sortDir);
        Page<CategoryResponse> categories = categoryService.getAllCategories(pageable);

        PaginatedResponse<CategoryResponse> paginatedResponse = PaginatedResponse.from(categories);

        ApiResponse<PaginatedResponse<CategoryResponse>> response = ApiResponse.<PaginatedResponse<CategoryResponse>>success(
            paginatedResponse,
            "Categories retrieved successfully"
        );
        response.setMessageAr("تم استرجاع الفئات بنجاح");

        return ResponseEntity.ok(response);
    }

    @GetMapping("/parents")
    @Operation(summary = "Get Parent Categories", description = "الحصول على الفئات الرئيسية")
    public ResponseEntity<ApiResponse<List<CategoryResponse>>> getParentCategories() {

        List<CategoryResponse> categories = categoryService.getParentCategories();
        ApiResponse<List<CategoryResponse>> response = ApiResponse.<List<CategoryResponse>>success(
            categories,
            "Parent categories retrieved successfully"
        );
        response.setMessageAr("تم استرجاع الفئات الرئيسية بنجاح");

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{parentId}/children")
    @Operation(summary = "Get Child Categories", description = "الحصول على الفئات الفرعية")
    public ResponseEntity<ApiResponse<List<CategoryResponse>>> getChildCategories(
            @Parameter(description = "Parent Category ID") @PathVariable UUID parentId) {

        List<CategoryResponse> categories = categoryService.getChildCategories(parentId);
        ApiResponse<List<CategoryResponse>> response = ApiResponse.<List<CategoryResponse>>success(
            categories,
            "Child categories retrieved successfully"
        );
        response.setMessageAr("تم استرجاع الفئات الفرعية بنجاح");

        return ResponseEntity.ok(response);
    }

    @GetMapping("/tree")
    @Operation(summary = "Get Category Tree", description = "الحصول على شجرة الفئات")
    public ResponseEntity<ApiResponse<CategoryTreeResponse>> getCategoryTree() {

        CategoryTreeResponse tree = categoryService.getCategoryTree();
        ApiResponse<CategoryTreeResponse> response = ApiResponse.<CategoryTreeResponse>success(
            tree,
            "Category tree retrieved successfully"
        );
        response.setMessageAr("تم استرجاع شجرة الفئات بنجاح");

        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    @Operation(summary = "Search Categories", description = "البحث في الفئات")
    public ResponseEntity<ApiResponse<PaginatedResponse<CategoryResponse>>> searchCategories(
            @Parameter(description = "Search query") @RequestParam String query,
            @Parameter(description = "Page number") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Sort by field") @RequestParam(defaultValue = "name") String sortBy,
            @Parameter(description = "Sort direction") @RequestParam(defaultValue = "asc") String sortDir) {

        Pageable pageable = PaginationUtils.createPageable(page, size, sortBy, sortDir);
        Page<CategoryResponse> categories = categoryService.searchCategories(query, pageable);

        PaginatedResponse<CategoryResponse> paginatedResponse = PaginatedResponse.from(categories);

        ApiResponse<PaginatedResponse<CategoryResponse>> response = ApiResponse.<PaginatedResponse<CategoryResponse>>success(
            paginatedResponse,
            "Categories search completed successfully"
        );
        response.setMessageAr("تم البحث في الفئات بنجاح");

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update Category", description = "تحديث فئة")
    public ResponseEntity<ApiResponse<CategoryResponse>> updateCategory(
            @Parameter(description = "Category ID") @PathVariable UUID id,
            @Valid @RequestBody CreateCategoryRequest request) {

        CategoryResponse category = categoryService.updateCategory(id, request);
        ApiResponse<CategoryResponse> response = ApiResponse.<CategoryResponse>success(
            category,
            "Category updated successfully"
        );
        response.setMessageAr("تم تحديث الفئة بنجاح");

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/activate")
    @Operation(summary = "Activate Category", description = "تفعيل فئة")
    public ResponseEntity<ApiResponse<CategoryResponse>> activateCategory(
            @Parameter(description = "Category ID") @PathVariable UUID id) {

        CategoryResponse category = categoryService.activateCategory(id);
        ApiResponse<CategoryResponse> response = ApiResponse.<CategoryResponse>success(
            category,
            "Category activated successfully"
        );
        response.setMessageAr("تم تفعيل الفئة بنجاح");

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/deactivate")
    @Operation(summary = "Deactivate Category", description = "إلغاء تفعيل فئة")
    public ResponseEntity<ApiResponse<CategoryResponse>> deactivateCategory(
            @Parameter(description = "Category ID") @PathVariable UUID id) {

        CategoryResponse category = categoryService.deactivateCategory(id);
        ApiResponse<CategoryResponse> response = ApiResponse.<CategoryResponse>success(
            category,
            "Category deactivated successfully"
        );
        response.setMessageAr("تم إلغاء تفعيل الفئة بنجاح");

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete Category", description = "حذف فئة")
    public ResponseEntity<ApiResponse<Void>> deleteCategory(
            @Parameter(description = "Category ID") @PathVariable UUID id) {

        categoryService.deleteCategory(id);
        ApiResponse<Void> response = ApiResponse.<Void>success(
            null,
            "Category deleted successfully"
        );
        response.setMessageAr("تم حذف الفئة بنجاح");

        return ResponseEntity.ok(response);
    }
}
