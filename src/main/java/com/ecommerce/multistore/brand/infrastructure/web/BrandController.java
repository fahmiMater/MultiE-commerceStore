package com.ecommerce.multistore.brand.infrastructure.web;

import com.ecommerce.multistore.brand.application.dto.BrandResponse;
import com.ecommerce.multistore.brand.application.dto.CreateBrandRequest;
import com.ecommerce.multistore.brand.application.service.BrandService;
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
@RequestMapping(AppConstants.API_BASE_PATH + "/brands")
@Tag(name = "Brands", description = "إدارة العلامات التجارية - Brand Management")
public class BrandController {

    private final BrandService brandService;

    @Autowired
    public BrandController(BrandService brandService) {
        this.brandService = brandService;
    }

    @PostMapping
    @Operation(summary = "إنشاء علامة تجارية جديدة", description = "Create a new brand")
    public ResponseEntity<ApiResponse<BrandResponse>> createBrand(
            @Valid @RequestBody CreateBrandRequest request) {
        
        BrandResponse brand = brandService.createBrand(request);
        
        ApiResponse<BrandResponse> response = ApiResponse.<BrandResponse>success(
            brand, 
            "Brand created successfully", 
            201
        );
        response.setMessageAr("تم إنشاء العلامة التجارية بنجاح");
        
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get Brand by ID", description = "الحصول على علامة تجارية بواسطة المعرف")
    public ResponseEntity<ApiResponse<BrandResponse>> getBrandById(
            @Parameter(description = "Brand ID") @PathVariable UUID id) {

        return brandService.findById(id)
                .map(brand -> {
                    ApiResponse<BrandResponse> response = ApiResponse.<BrandResponse>success(
                        brand, 
                        "Brand retrieved successfully"
                    );
                    response.setMessageAr("تم استرجاع العلامة التجارية بنجاح");
                    return ResponseEntity.ok(response);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/display/{displayId}")
    @Operation(summary = "Get Brand by Display ID", description = "الحصول على علامة تجارية بواسطة معرف العرض")
    public ResponseEntity<ApiResponse<BrandResponse>> getBrandByDisplayId(
            @Parameter(description = "Brand Display ID") @PathVariable String displayId) {

        return brandService.findByDisplayId(displayId)
                .map(brand -> {
                    ApiResponse<BrandResponse> response = ApiResponse.<BrandResponse>success(
                        brand, 
                        "Brand retrieved successfully"
                    );
                    response.setMessageAr("تم استرجاع العلامة التجارية بنجاح");
                    return ResponseEntity.ok(response);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/slug/{slug}")
    @Operation(summary = "Get Brand by Slug", description = "الحصول على علامة تجارية بواسطة الرابط الودي")
    public ResponseEntity<ApiResponse<BrandResponse>> getBrandBySlug(
            @Parameter(description = "Brand Slug") @PathVariable String slug) {

        return brandService.findBySlug(slug)
                .map(brand -> {
                    ApiResponse<BrandResponse> response = ApiResponse.<BrandResponse>success(
                        brand, 
                        "Brand retrieved successfully"
                    );
                    response.setMessageAr("تم استرجاع العلامة التجارية بنجاح");
                    return ResponseEntity.ok(response);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    @Operation(summary = "Get All Brands", description = "الحصول على جميع العلامات التجارية")
    public ResponseEntity<ApiResponse<PaginatedResponse<BrandResponse>>> getAllBrands(
            @Parameter(description = "Page number") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Sort by field") @RequestParam(defaultValue = "name") String sortBy,
            @Parameter(description = "Sort direction") @RequestParam(defaultValue = "asc") String sortDir) {

        Pageable pageable = PaginationUtils.createPageable(page, size, sortBy, sortDir);
        Page<BrandResponse> brands = brandService.getAllBrands(pageable);

        PaginatedResponse<BrandResponse> paginatedResponse = PaginatedResponse.from(brands);

        ApiResponse<PaginatedResponse<BrandResponse>> response = ApiResponse.<PaginatedResponse<BrandResponse>>success(
            paginatedResponse,
            "Brands retrieved successfully"
        );
        response.setMessageAr("تم استرجاع العلامات التجارية بنجاح");

        return ResponseEntity.ok(response);
    }

    @GetMapping("/active")
    @Operation(summary = "Get Active Brands", description = "الحصول على العلامات التجارية النشطة")
    public ResponseEntity<ApiResponse<List<BrandResponse>>> getActiveBrands() {

        List<BrandResponse> brands = brandService.getActiveBrands();
        ApiResponse<List<BrandResponse>> response = ApiResponse.<List<BrandResponse>>success(
            brands, 
            "Active brands retrieved successfully"
        );
        response.setMessageAr("تم استرجاع العلامات التجارية النشطة بنجاح");

        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    @Operation(summary = "Search Brands", description = "البحث في العلامات التجارية")
    public ResponseEntity<ApiResponse<PaginatedResponse<BrandResponse>>> searchBrands(
            @Parameter(description = "Search query") @RequestParam String query,
            @Parameter(description = "Page number") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Sort by field") @RequestParam(defaultValue = "name") String sortBy,
            @Parameter(description = "Sort direction") @RequestParam(defaultValue = "asc") String sortDir) {

        Pageable pageable = PaginationUtils.createPageable(page, size, sortBy, sortDir);
        Page<BrandResponse> brands = brandService.searchBrands(query, pageable);

        PaginatedResponse<BrandResponse> paginatedResponse = PaginatedResponse.from(brands);

        ApiResponse<PaginatedResponse<BrandResponse>> response = ApiResponse.<PaginatedResponse<BrandResponse>>success(
            paginatedResponse,
            "Brands search completed successfully"
        );
        response.setMessageAr("تم البحث في العلامات التجارية بنجاح");

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update Brand", description = "تحديث علامة تجارية")
    public ResponseEntity<ApiResponse<BrandResponse>> updateBrand(
            @Parameter(description = "Brand ID") @PathVariable UUID id,
            @Valid @RequestBody CreateBrandRequest request) {

        BrandResponse brand = brandService.updateBrand(id, request);
        ApiResponse<BrandResponse> response = ApiResponse.<BrandResponse>success(
            brand, 
            "Brand updated successfully"
        );
        response.setMessageAr("تم تحديث العلامة التجارية بنجاح");

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/activate")
    @Operation(summary = "Activate Brand", description = "تفعيل علامة تجارية")
    public ResponseEntity<ApiResponse<BrandResponse>> activateBrand(
            @Parameter(description = "Brand ID") @PathVariable UUID id) {

        BrandResponse brand = brandService.activateBrand(id);
        ApiResponse<BrandResponse> response = ApiResponse.<BrandResponse>success(
            brand, 
            "Brand activated successfully"
        );
        response.setMessageAr("تم تفعيل العلامة التجارية بنجاح");

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/deactivate")
    @Operation(summary = "Deactivate Brand", description = "إلغاء تفعيل علامة تجارية")
    public ResponseEntity<ApiResponse<BrandResponse>> deactivateBrand(
            @Parameter(description = "Brand ID") @PathVariable UUID id) {

        BrandResponse brand = brandService.deactivateBrand(id);
        ApiResponse<BrandResponse> response = ApiResponse.<BrandResponse>success(
            brand, 
            "Brand deactivated successfully"
        );
        response.setMessageAr("تم إلغاء تفعيل العلامة التجارية بنجاح");

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete Brand", description = "حذف علامة تجارية")
    public ResponseEntity<ApiResponse<Void>> deleteBrand(
            @Parameter(description = "Brand ID") @PathVariable UUID id) {

        brandService.deleteBrand(id);
        ApiResponse<Void> response = ApiResponse.<Void>success(
            null, 
            "Brand deleted successfully"
        );
        response.setMessageAr("تم حذف العلامة التجارية بنجاح");

        return ResponseEntity.ok(response);
    }
}
