package com.ecommerce.multistore.shared.utils;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import com.ecommerce.multistore.shared.constants.AppConstants;

/**
 * أدوات التصفح والترقيم
 * Pagination Utilities
 */
public class PaginationUtils {
    
    /**
     * إنشاء Pageable مع معاملات افتراضية
     */
    public static Pageable createPageable(Integer page, Integer size, String sortBy, String sortDir) {
        // تطبيق القيم الافتراضية
        int pageNumber = (page != null && page >= 0) ? page : 0;
        int pageSize = (size != null && size > 0) ? 
            Math.min(size, AppConstants.MAX_PAGE_SIZE) : AppConstants.DEFAULT_PAGE_SIZE;
        
        String sortField = (sortBy != null && !sortBy.trim().isEmpty()) ? 
            sortBy : AppConstants.DEFAULT_SORT_BY;
        
        Sort.Direction direction = Sort.Direction.DESC;
        if (sortDir != null && sortDir.equalsIgnoreCase("asc")) {
            direction = Sort.Direction.ASC;
        }
        
        Sort sort = Sort.by(direction, sortField);
        
        return PageRequest.of(pageNumber, pageSize, sort);
    }
    
    /**
     * إنشاء Pageable بسيط
     */
    public static Pageable createSimplePageable(int page, int size) {
        return PageRequest.of(page, size);
    }
    
    /**
     * إنشاء Pageable مع ترتيب
     */
    public static Pageable createPageableWithSort(int page, int size, String sortBy) {
        return PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, sortBy));
    }
    
    /**
     * إنشاء Pageable مع ترتيب تصاعدي
     */
    public static Pageable createPageableAsc(int page, int size, String sortBy) {
        return PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, sortBy));
    }
    
    /**
     * إنشاء Pageable مع ترتيب تنازلي
     */
    public static Pageable createPageableDesc(int page, int size, String sortBy) {
        return PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, sortBy));
    }
    
    /**
     * إنشاء Pageable مع ترتيب متعدد
     */
    public static Pageable createPageableWithMultipleSort(int page, int size, String... sortFields) {
        Sort sort = Sort.by(Sort.Direction.DESC, sortFields);
        return PageRequest.of(page, size, sort);
    }
    
    /**
     * التحقق من صحة معاملات التصفح
     */
    public static boolean isValidPaginationParams(Integer page, Integer size) {
        return (page == null || page >= 0) && 
               (size == null || (size > 0 && size <= AppConstants.MAX_PAGE_SIZE));
    }
    
    /**
     * حساب العدد الإجمالي للصفحات
     */
    public static int calculateTotalPages(long totalElements, int pageSize) {
        if (pageSize <= 0) {
            return 0;
        }
        return (int) Math.ceil((double) totalElements / pageSize);
    }
    
    /**
     * حساب رقم العنصر الأول في الصفحة
     */
    public static long calculateFirstElement(int page, int size) {
        return (long) page * size + 1;
    }
    
    /**
     * حساب رقم العنصر الأخير في الصفحة
     */
    public static long calculateLastElement(int page, int size, long totalElements) {
        long lastElement = (long) (page + 1) * size;
        return Math.min(lastElement, totalElements);
    }
    
    /**
     * التحقق من وجود صفحة سابقة
     */
    public static boolean hasPrevious(int page) {
        return page > 0;
    }
    
    /**
     * التحقق من وجود صفحة تالية
     */
    public static boolean hasNext(int page, int size, long totalElements) {
        int totalPages = calculateTotalPages(totalElements, size);
        return page < totalPages - 1;
    }
    
    /**
     * إنشاء معلومات تفصيلية عن التصفح
     */
    public static PaginationInfo createPaginationInfo(int page, int size, long totalElements) {
        int totalPages = calculateTotalPages(totalElements, size);
        long firstElement = calculateFirstElement(page, size);
        long lastElement = calculateLastElement(page, size, totalElements);
        
        return new PaginationInfo(
            page,
            size,
            totalPages,
            totalElements,
            firstElement,
            lastElement,
            hasPrevious(page),
            hasNext(page, size, totalElements)
        );
    }
    
    /**
     * معلومات التصفح
     */
    public static class PaginationInfo {
        private final int currentPage;
        private final int pageSize;
        private final int totalPages;
        private final long totalElements;
        private final long firstElement;
        private final long lastElement;
        private final boolean hasPrevious;
        private final boolean hasNext;
        
        public PaginationInfo(int currentPage, int pageSize, int totalPages, 
                            long totalElements, long firstElement, long lastElement,
                            boolean hasPrevious, boolean hasNext) {
            this.currentPage = currentPage;
            this.pageSize = pageSize;
            this.totalPages = totalPages;
            this.totalElements = totalElements;
            this.firstElement = firstElement;
            this.lastElement = lastElement;
            this.hasPrevious = hasPrevious;
            this.hasNext = hasNext;
        }
        
        // Getters
        public int getCurrentPage() { return currentPage; }
        public int getPageSize() { return pageSize; }
        public int getTotalPages() { return totalPages; }
        public long getTotalElements() { return totalElements; }
        public long getFirstElement() { return firstElement; }
        public long getLastElement() { return lastElement; }
        public boolean isHasPrevious() { return hasPrevious; }
        public boolean isHasNext() { return hasNext; }
    }
}
