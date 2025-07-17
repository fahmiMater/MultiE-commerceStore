package com.ecommerce.multistore.user.application.service;

import com.ecommerce.multistore.user.application.dto.CreateUserRequest;
import com.ecommerce.multistore.user.application.dto.UserResponse;
import com.ecommerce.multistore.user.domain.User;
import com.ecommerce.multistore.user.domain.UserRole;
import com.ecommerce.multistore.user.infrastructure.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * خدمة إدارة المستخدمين
 * تحتوي على جميع العمليات المتعلقة بالمستخدمين مثل الإنشاء والتحديث والبحث
 * 
 * User Management Service
 * Contains all user-related operations such as creation, update, and search
 * 
 * @author Multi-Store Team
 * @version 1.0
 * @since 2024-01-01
 */
@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Constructor لحقن Dependencies
     * Constructor for dependency injection
     * 
     * @param userRepository مستودع المستخدمين للتعامل مع قاعدة البيانات
     * @param passwordEncoder مشفر كلمات المرور
     */
    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * إنشاء مستخدم جديد
     * Creates a new user with validation and password encryption
     * 
     * @param request بيانات المستخدم الجديد
     * @return UserResponse بيانات المستخدم المُنشأ
     * @throws IllegalArgumentException إذا كان الإيميل أو الهاتف مُستخدم مسبقاً
     * 
     * @see CreateUserRequest
     * @see UserResponse
     */
    public UserResponse createUser(CreateUserRequest request) {
        // التحقق من عدم وجود المستخدم مسبقاً
        validateUserUniqueness(request.getEmail(), request.getPhone());

        // إنشاء كائن المستخدم
        User user = buildUserFromRequest(request);
        
        // حفظ المستخدم
        User savedUser = userRepository.save(user);
        
        // إجبار JPA على كتابة البيانات في قاعدة البيانات
        userRepository.flush();
        
        // إعادة تحميل المستخدم للحصول على display_id المُولد من الـ trigger
        savedUser = userRepository.findById(savedUser.getId())
                .orElse(savedUser);
        
        // تحويل إلى Response وإرجاع النتيجة
        return convertToResponse(savedUser);
    }

    /**
     * البحث عن مستخدم بواسطة الإيميل
     * Finds a user by email address
     * 
     * @param email عنوان البريد الإلكتروني
     * @return Optional<UserResponse> المستخدم إذا وُجد، أو Optional.empty()
     */
    @Transactional(readOnly = true)
    public Optional<UserResponse> findByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(this::convertToResponse);
    }

    /**
     * البحث عن مستخدم بواسطة Display ID
     * Finds a user by display ID
     * 
     * @param displayId المعرف المعروض للمستخدم (مثل USR-000001)
     * @return Optional<UserResponse> المستخدم إذا وُجد، أو Optional.empty()
     */
    @Transactional(readOnly = true)
    public Optional<UserResponse> findByDisplayId(String displayId) {
        return userRepository.findByDisplayId(displayId)
                .map(this::convertToResponse);
    }

    /**
     * البحث عن مستخدم بواسطة UUID
     * Finds a user by UUID
     * 
     * @param id المعرف الفريد للمستخدم
     * @return Optional<UserResponse> المستخدم إذا وُجد، أو Optional.empty()
     */
    @Transactional(readOnly = true)
    public Optional<UserResponse> findById(UUID id) {
        return userRepository.findById(id)
                .map(this::convertToResponse);
    }

    /**
     * الحصول على جميع المستخدمين
     * Retrieves all users from the database
     * 
     * @return List<UserResponse> قائمة بجميع المستخدمين
     */
    @Transactional(readOnly = true)
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    /**
     * تفعيل أو إلغاء تفعيل المستخدم
     * Activates or deactivates a user account
     * 
     * @param userId معرف المستخدم
     * @param isActive حالة التفعيل المطلوبة
     * @return UserResponse بيانات المستخدم المحدثة
     * @throws IllegalArgumentException إذا لم يوجد المستخدم
     */
    public UserResponse updateUserStatus(UUID userId, Boolean isActive) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));
        
        user.setIsActive(isActive);
        user.setUpdatedAt(LocalDateTime.now());
        
        User updatedUser = userRepository.save(user);
        return convertToResponse(updatedUser);
    }

    /**
     * تأكيد البريد الإلكتروني للمستخدم
     * Verifies user's email address
     * 
     * @param userId معرف المستخدم
     * @return UserResponse بيانات المستخدم المحدثة
     * @throws IllegalArgumentException إذا لم يوجد المستخدم
     */
    public UserResponse verifyUserEmail(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));
        
        user.setIsVerified(true);
        user.setUpdatedAt(LocalDateTime.now());
        
        User updatedUser = userRepository.save(user);
        return convertToResponse(updatedUser);
    }

    /**
     * التحقق من صحة بيانات تسجيل الدخول
     * Validates user login credentials
     * 
     * @param email البريد الإلكتروني
     * @param password كلمة المرور
     * @return Optional<UserResponse> المستخدم إذا كانت البيانات صحيحة
     */
    @Transactional(readOnly = true)
    public Optional<UserResponse> validateLogin(String email, String password) {
        return userRepository.findByEmail(email)
                .filter(user -> passwordEncoder.matches(password, user.getPasswordHash()))
                .filter(User::getIsActive)
                .map(this::convertToResponse);
    }

    // ===============================
    // Private Helper Methods
    // ===============================

    /**
     * التحقق من عدم تكرار الإيميل أو الهاتف
     * Validates that email and phone are unique
     * 
     * @param email البريد الإلكتروني
     * @param phone رقم الهاتف
     * @throws IllegalArgumentException إذا كان الإيميل أو الهاتف مُستخدم مسبقاً
     */
    private void validateUserUniqueness(String email, String phone) {
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email already exists: " + email);
        }
        
        if (phone != null && userRepository.existsByPhone(phone)) {
            throw new IllegalArgumentException("Phone number already exists: " + phone);
        }
    }

    /**
     * بناء كائن المستخدم من طلب الإنشاء
     * Builds User entity from CreateUserRequest
     * 
     * @param request بيانات المستخدم الجديد
     * @return User كائن المستخدم الجديد
     */
    private User buildUserFromRequest(CreateUserRequest request) {
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setFirstNameAr(request.getFirstNameAr());
        user.setLastNameAr(request.getLastNameAr());
        user.setPhone(request.getPhone());
        user.setRole(UserRole.CUSTOMER);
        user.setIsActive(true);
        user.setIsVerified(false);
        
        return user;
    }

    /**
     * تحويل كائن User إلى UserResponse
     * Converts User entity to UserResponse DTO
     * 
     * @param user كائن المستخدم
     * @return UserResponse DTO للاستجابة
     */
    private UserResponse convertToResponse(User user) {
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setDisplayId(user.getDisplayId());
        response.setEmail(user.getEmail());
        response.setPhone(user.getPhone());
        response.setFirstName(user.getFirstName());
        response.setLastName(user.getLastName());
        response.setFirstNameAr(user.getFirstNameAr());
        response.setLastNameAr(user.getLastNameAr());
        response.setIsActive(user.getIsActive());
        response.setIsVerified(user.getIsVerified());
        response.setRole(user.getRole());
        response.setCreatedAt(user.getCreatedAt());
        
        return response;
    }
}
