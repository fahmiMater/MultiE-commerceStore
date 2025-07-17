package com.ecommerce.multistore.shared.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * استجابة الصفحات الموحدة
 * Unified paginated response wrapper
 * 
 * يوفر تنسيق موحد للبيانات المقسمة على صفحات مع معلومات التنقل
 * Provides consistent format for paginated data with navigation info
 * 
 * @param <T> نوع البيانات في الصفحة / Type of data in the page
 * 
 * @author Multi-Store Team
 * @version 1.0
 * @since 2024-01-01
 */
@Schema(description = "استجابة الصفحات الموحدة / Unified paginated response")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PaginatedResponse<T> {
    
    /**
     * البيانات في الصفحة الحالية
     * Data in current page
     */
    @Schema(description = "البيانات في الصفحة الحالية / Data in current page")
    private List<T> content;
    
    /**
     * معلومات الصفحة
     * Page information
     */
    @Schema(description = "معلومات الصفحة / Page information")
    private PageInfo pageInfo;
    
    /**
     * معلومات الفرز
     * Sort information
     */
    @Schema(description = "معلومات الفرز / Sort information")
    private SortInfo sortInfo;
    
    /**
     * معلومات إضافية للفلترة
     * Additional filter information
     */
    @Schema(description = "معلومات الفلترة / Filter information")
    private FilterInfo filterInfo;
    
    // ===============================
    // Constructors
    // ===============================
    
    /**
     * Constructor افتراضي
     * Default constructor
     */
    public PaginatedResponse() {}
    
    /**
     * Constructor مع البيانات ومعلومات الصفحة
     * Constructor with data and page info
     * 
     * @param content البيانات
     * @param pageInfo معلومات الصفحة
     */
    public PaginatedResponse(List<T> content, PageInfo pageInfo) {
        this.content = content;
        this.pageInfo = pageInfo;
    }
    
    /**
     * Constructor كامل
     * Full constructor
     * 
     * @param content البيانات
     * @param pageInfo معلومات الصفحة
     * @param sortInfo معلومات الفرز
     * @param filterInfo معلومات الفلترة
     */
    public PaginatedResponse(List<T> content, PageInfo pageInfo, SortInfo sortInfo, FilterInfo filterInfo) {
        this.content = content;
        this.pageInfo = pageInfo;
        this.sortInfo = sortInfo;
        this.filterInfo = filterInfo;
    }
    
    // ===============================
    // Static Factory Methods
    // ===============================
    
    /**
     * إنشاء من Spring Data Page
     * Create from Spring Data Page
     * 
     * @param <T> نوع البيانات
     * @param page صفحة Spring Data
     * @return PaginatedResponse<T>
     */
    public static <T> PaginatedResponse<T> from(Page<T> page) {
        PageInfo pageInfo = PageInfo.builder()
                .pageNumber(page.getNumber())
                .pageSize(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .hasNext(page.hasNext())
                .hasPrevious(page.hasPrevious())
                .isFirst(page.isFirst())
                .isLast(page.isLast())
                .build();
        
        SortInfo sortInfo = null;
        if (page.getSort().isSorted()) {
            sortInfo = SortInfo.builder()
                    .sorted(true)
                    .sortBy(page.getSort().toString())
                    .build();
        }
        
        return new PaginatedResponse<>(page.getContent(), pageInfo, sortInfo, null);
    }
    
    /**
     * إنشاء من Spring Data Page مع معلومات فلترة
     * Create from Spring Data Page with filter info
     * 
     * @param <T> نوع البيانات
     * @param page صفحة Spring Data
     * @param filterInfo معلومات الفلترة
     * @return PaginatedResponse<T>
     */
    public static <T> PaginatedResponse<T> from(Page<T> page, FilterInfo filterInfo) {
        PaginatedResponse<T> response = from(page);
        response.setFilterInfo(filterInfo);
        return response;
    }
    
    /**
     * إنشاء استجابة فارغة
     * Create empty response
     * 
     * @param <T> نوع البيانات
     * @return PaginatedResponse<T>
     */
    public static <T> PaginatedResponse<T> empty() {
        PageInfo pageInfo = PageInfo.builder()
                .pageNumber(0)
                .pageSize(0)
                .totalElements(0L)
                .totalPages(0)
                .hasNext(false)
                .hasPrevious(false)
                .isFirst(true)
                .isLast(true)
                .build();
        
        return new PaginatedResponse<>(List.of(), pageInfo);
    }
    
    // ===============================
    // Utility Methods
    // ===============================
    
    /**
     * التحقق من وجود بيانات
     * Check if has data
     * 
     * @return boolean
     */
    public boolean hasContent() {
        return content != null && !content.isEmpty();
    }
    
    /**
     * التحقق من وجود صفحة تالية
     * Check if has next page
     * 
     * @return boolean
     */
    public boolean hasNext() {
        return pageInfo != null && pageInfo.hasNext;
    }
    
    /**
     * التحقق من وجود صفحة سابقة
     * Check if has previous page
     * 
     * @return boolean
     */
    public boolean hasPrevious() {
        return pageInfo != null && pageInfo.hasPrevious;
    }
    
    /**
     * الحصول على عدد العناصر في الصفحة الحالية
     * Get current page element count
     * 
     * @return int
     */
    public int getCurrentPageElementCount() {
        return content != null ? content.size() : 0;
    }
    
    // ===============================
    // Getters and Setters
    // ===============================
    
    public List<T> getContent() { return content; }
    public void setContent(List<T> content) { this.content = content; }
    
    public PageInfo getPageInfo() { return pageInfo; }
    public void setPageInfo(PageInfo pageInfo) { this.pageInfo = pageInfo; }
    
    public SortInfo getSortInfo() { return sortInfo; }
    public void setSortInfo(SortInfo sortInfo) { this.sortInfo = sortInfo; }
    
    public FilterInfo getFilterInfo() { return filterInfo; }
    public void setFilterInfo(FilterInfo filterInfo) { this.filterInfo = filterInfo; }
    
    // ===============================
    // Inner Classes
    // ===============================
    
    /**
     * معلومات الصفحة
     * Page information
     */
    @Schema(description = "معلومات الصفحة / Page information")
    public static class PageInfo {
        
        /**
         * رقم الصفحة الحالية (يبدأ من 0)
         * Current page number (starts from 0)
         */
        @Schema(description = "رقم الصفحة الحالية / Current page number", example = "0")
        private int pageNumber;
        
        /**
         * حجم الصفحة
         * Page size
         */
        @Schema(description = "حجم الصفحة / Page size", example = "10")
        private int pageSize;
        
        /**
         * إجمالي عدد العناصر
         * Total number of elements
         */
        @Schema(description = "إجمالي عدد العناصر / Total number of elements", example = "100")
        private long totalElements;
        
        /**
         * إجمالي عدد الصفحات
         * Total number of pages
         */
        @Schema(description = "إجمالي عدد الصفحات / Total number of pages", example = "10")
        private int totalPages;
        
        /**
         * هل توجد صفحة تالية
         * Has next page
         */
        @Schema(description = "هل توجد صفحة تالية / Has next page", example = "true")
        private boolean hasNext;
        
        /**
         * هل توجد صفحة سابقة
         * Has previous page
         */
        @Schema(description = "هل توجد صفحة سابقة / Has previous page", example = "false")
        private boolean hasPrevious;
        
        /**
         * هل هذه الصفحة الأولى
         * Is first page
         */
        @Schema(description = "هل هذه الصفحة الأولى / Is first page", example = "true")
        private boolean isFirst;
        
        /**
         * هل هذه الصفحة الأخيرة
         * Is last page
         */
        @Schema(description = "هل هذه الصفحة الأخيرة / Is last page", example = "false")
        private boolean isLast;
        
        // Constructors
        public PageInfo() {}
        
        // Builder pattern
        public static Builder builder() {
            return new Builder();
        }
        
        public static class Builder {
            private final PageInfo pageInfo;
            
            public Builder() {
                this.pageInfo = new PageInfo();
            }
            
            public Builder pageNumber(int pageNumber) {
                pageInfo.pageNumber = pageNumber;
                return this;
            }
            
            public Builder pageSize(int pageSize) {
                pageInfo.pageSize = pageSize;
                return this;
            }
            
            public Builder totalElements(long totalElements) {
                pageInfo.totalElements = totalElements;
                return this;
            }
            
            public Builder totalPages(int totalPages) {
                pageInfo.totalPages = totalPages;
                return this;
            }
            
            public Builder hasNext(boolean hasNext) {
                pageInfo.hasNext = hasNext;
                return this;
            }
            
            public Builder hasPrevious(boolean hasPrevious) {
                pageInfo.hasPrevious = hasPrevious;
                return this;
            }
            
            public Builder isFirst(boolean isFirst) {
                pageInfo.isFirst = isFirst;
                return this;
            }
            
            public Builder isLast(boolean isLast) {
                pageInfo.isLast = isLast;
                return this;
            }
            
            public PageInfo build() {
                return pageInfo;
            }
        }
        
        // Getters and Setters
        public int getPageNumber() { return pageNumber; }
        public void setPageNumber(int pageNumber) { this.pageNumber = pageNumber; }
        
        public int getPageSize() { return pageSize; }
        public void setPageSize(int pageSize) { this.pageSize = pageSize; }
        
        public long getTotalElements() { return totalElements; }
        public void setTotalElements(long totalElements) { this.totalElements = totalElements; }
        
        public int getTotalPages() { return totalPages; }
        public void setTotalPages(int totalPages) { this.totalPages = totalPages; }
        
        public boolean isHasNext() { return hasNext; }
        public void setHasNext(boolean hasNext) { this.hasNext = hasNext; }
        
        public boolean isHasPrevious() { return hasPrevious; }
        public void setHasPrevious(boolean hasPrevious) { this.hasPrevious = hasPrevious; }
        
        public boolean isFirst() { return isFirst; }
        public void setFirst(boolean first) { isFirst = first; }
        
        public boolean isLast() { return isLast; }
        public void setLast(boolean last) { isLast = last; }
    }
    
    /**
     * معلومات الفرز
     * Sort information
     */
    @Schema(description = "معلومات الفرز / Sort information")
    public static class SortInfo {
        
        /**
         * هل البيانات مرتبة
         * Is data sorted
         */
        @Schema(description = "هل البيانات مرتبة / Is data sorted", example = "true")
        private boolean sorted;
        
        /**
         * الحقل المرتب حسبه
         * Sort field
         */
        @Schema(description = "الحقل المرتب حسبه / Sort field", example = "name")
        private String sortBy;
        
        /**
         * اتجاه الفرز
         * Sort direction
         */
        @Schema(description = "اتجاه الفرز / Sort direction", example = "ASC")
        private String sortDirection;
        
        // Constructors
        public SortInfo() {}
        
        // Builder pattern
        public static Builder builder() {
            return new Builder();
        }
        
        public static class Builder {
            private final SortInfo sortInfo;
            
            public Builder() {
                this.sortInfo = new SortInfo();
            }
            
            public Builder sorted(boolean sorted) {
                sortInfo.sorted = sorted;
                return this;
            }
            
            public Builder sortBy(String sortBy) {
                sortInfo.sortBy = sortBy;
                return this;
            }
            
            public Builder sortDirection(String sortDirection) {
                sortInfo.sortDirection = sortDirection;
                return this;
            }
            
            public SortInfo build() {
                return sortInfo;
            }
        }
        
        // Getters and Setters
        public boolean isSorted() { return sorted; }
        public void setSorted(boolean sorted) { this.sorted = sorted; }
        
        public String getSortBy() { return sortBy; }
        public void setSortBy(String sortBy) { this.sortBy = sortBy; }
        
        public String getSortDirection() { return sortDirection; }
        public void setSortDirection(String sortDirection) { this.sortDirection = sortDirection; }
    }
    
    /**
     * معلومات الفلترة
     * Filter information
     */
    @Schema(description = "معلومات الفلترة / Filter information")
    public static class FilterInfo {
        
        /**
         * هل تم تطبيق فلترة
         * Is filtered
         */
        @Schema(description = "هل تم تطبيق فلترة / Is filtered", example = "true")
        private boolean filtered;
        
        /**
         * عدد الفلاتر المطبقة
         * Number of applied filters
         */
        @Schema(description = "عدد الفلاتر المطبقة / Number of applied filters", example = "2")
        private int appliedFiltersCount;
        
        /**
         * تفاصيل الفلاتر المطبقة
         * Applied filters details
         */
        @Schema(description = "تفاصيل الفلاتر المطبقة / Applied filters details")
        private Object appliedFilters;
        
        // Constructors
        public FilterInfo() {}
        
        // Builder pattern
        public static Builder builder() {
            return new Builder();
        }
        
        public static class Builder {
            private final FilterInfo filterInfo;
            
            public Builder() {
                this.filterInfo = new FilterInfo();
            }
            
            public Builder filtered(boolean filtered) {
                filterInfo.filtered = filtered;
                return this;
            }
            
            public Builder appliedFiltersCount(int appliedFiltersCount) {
                filterInfo.appliedFiltersCount = appliedFiltersCount;
                return this;
            }
            
            public Builder appliedFilters(Object appliedFilters) {
                filterInfo.appliedFilters = appliedFilters;
                return this;
            }
            
            public FilterInfo build() {
                return filterInfo;
            }
        }
        
        // Getters and Setters
        public boolean isFiltered() { return filtered; }
        public void setFiltered(boolean filtered) { this.filtered = filtered; }
        
        public int getAppliedFiltersCount() { return appliedFiltersCount; }
        public void setAppliedFiltersCount(int appliedFiltersCount) { this.appliedFiltersCount = appliedFiltersCount; }
        
        public Object getAppliedFilters() { return appliedFilters; }
        public void setAppliedFilters(Object appliedFilters) { this.appliedFilters = appliedFilters; }
    }
    
    // ===============================
    // toString Method
    // ===============================
    
    @Override
    public String toString() {
        return "PaginatedResponse{" +
                "content=" + content +
                ", pageInfo=" + pageInfo +
                ", sortInfo=" + sortInfo +
                ", filterInfo=" + filterInfo +
                '}';
    }
}
