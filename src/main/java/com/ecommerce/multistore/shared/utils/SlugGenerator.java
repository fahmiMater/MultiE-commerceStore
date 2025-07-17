package com.ecommerce.multistore.shared.utils;

import java.text.Normalizer;
import java.util.function.Function;
import java.util.regex.Pattern;

/**
 * مولد الـ Slug - تحويل النصوص إلى URLs صديقة للمتصفح
 * Slug Generator - Convert text to URL-friendly format
 * 
 * @author Multi-Store Team
 * @version 1.0
 * @since 2024-01-01
 */
public class SlugGenerator {
    
    // Pattern للأحرف غير اللاتينية - مُصحح
    private static final Pattern NON_LATIN = Pattern.compile("[^\\w-]");
    
    // Pattern للمسافات المتعددة - مُصحح
    private static final Pattern WHITESPACE = Pattern.compile("[\\s]+");
    
    // Pattern للشرطات المتعددة - مُصحح
    private static final Pattern MULTIPLE_DASHES = Pattern.compile("-{2,}");
    
    // Pattern للشرطات في البداية والنهاية - مُصحح
    private static final Pattern EDGE_DASHES = Pattern.compile("^-|-$");
    
    /**
     * تحويل النص إلى slug
     * Convert text to slug
     */
    public static String generateSlug(String input) {
        if (input == null || input.trim().isEmpty()) {
            return "";
        }
        
        String slug = input.trim().toLowerCase();
        
        // تطبيع النص لإزالة العلامات التشكيلية - مُصحح
        slug = Normalizer.normalize(slug, Normalizer.Form.NFD);
        slug = slug.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
        
        // استبدال المسافات بشرطات
        slug = WHITESPACE.matcher(slug).replaceAll("-");
        
        // إزالة الأحرف غير المسموحة
        slug = NON_LATIN.matcher(slug).replaceAll("");
        
        // إزالة الشرطات المتعددة
        slug = MULTIPLE_DASHES.matcher(slug).replaceAll("-");
        
        // إزالة الشرطات من البداية والنهاية
        slug = EDGE_DASHES.matcher(slug).replaceAll("");
        
        return slug;
    }
    

    /**
 * إنشاء slug فريد باستخدام checker function
 * Generate unique slug using checker function
 * 
 * @param input النص الأصلي
 * @param existsChecker دالة للتحقق من وجود الـ slug
 * @return slug فريد
 */
public static String generateUniqueSlug(String input, Function<String, Boolean> existsChecker) {
    String baseSlug = generateSlug(input);
    
    if (!existsChecker.apply(baseSlug)) {
        return baseSlug;
    }
    
    int counter = 1;
    String uniqueSlug;
    do {
        counter++;
        uniqueSlug = baseSlug + "-" + counter;
    } while (existsChecker.apply(uniqueSlug));
    
    return uniqueSlug;
}

    /**
     * تحويل النص العربي إلى slug
     */
    public static String generateArabicSlug(String arabicText) {
        if (arabicText == null || arabicText.trim().isEmpty()) {
            return "";
        }
        
        String transliterated = transliterateArabic(arabicText);
        return generateSlug(transliterated);
    }
    
    /**
     * تحويل الأحرف العربية إلى لاتينية
     */
    private static String transliterateArabic(String arabic) {
        return arabic
            .replaceAll("ا", "a")
            .replaceAll("ب", "b")
            .replaceAll("ت", "t")
            .replaceAll("ث", "th")
            .replaceAll("ج", "j")
            .replaceAll("ح", "h")
            .replaceAll("خ", "kh")
            .replaceAll("د", "d")
            .replaceAll("ذ", "dh")
            .replaceAll("ر", "r")
            .replaceAll("ز", "z")
            .replaceAll("س", "s")
            .replaceAll("ش", "sh")
            .replaceAll("ص", "s")
            .replaceAll("ض", "d")
            .replaceAll("ط", "t")
            .replaceAll("ظ", "z")
            .replaceAll("ع", "a")
            .replaceAll("غ", "gh")
            .replaceAll("ف", "f")
            .replaceAll("ق", "q")
            .replaceAll("ك", "k")
            .replaceAll("ل", "l")
            .replaceAll("م", "m")
            .replaceAll("ن", "n")
            .replaceAll("ه", "h")
            .replaceAll("و", "w")
            .replaceAll("ي", "y")
            .replaceAll("ى", "a")
            .replaceAll("ة", "h")
            .replaceAll("ء", "");
    }
    
    /**
     * التحقق من صحة الـ slug
     */
    public static boolean isValidSlug(String slug) {
        if (slug == null || slug.isEmpty()) {
            return false;
        }
        
        Pattern validSlugPattern = Pattern.compile("^[a-z0-9]+(?:-[a-z0-9]+)*$");
        return validSlugPattern.matcher(slug).matches();
    }
    
    /**
     * إنشاء slug فريد بإضافة رقم
     */
    public static String generateUniqueSlug(String baseSlug, int counter) {
        if (counter <= 1) {
            return baseSlug;
        }
        return baseSlug + "-" + counter;
    }
    
    /**
     * تقصير الـ slug إلى طول محدد
     */
    public static String truncateSlug(String slug, int maxLength) {
        if (slug == null || slug.length() <= maxLength) {
            return slug;
        }
        
        String truncated = slug.substring(0, maxLength);
        
        if (truncated.endsWith("-")) {
            truncated = truncated.substring(0, truncated.length() - 1);
        }
        
        return truncated;
    }
}