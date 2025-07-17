package com.ecommerce.multistore.user.infrastructure.web;

import com.ecommerce.multistore.user.application.dto.CreateUserRequest;
import com.ecommerce.multistore.user.application.dto.UserResponse;
import com.ecommerce.multistore.user.application.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * وحدة التحكم في المستخدمين - REST API
 * User Controller - REST API for user management
 * 
 * يوفر APIs لإدارة المستخدمين بما في ذلك التسجيل والبحث والتحديث
 * Provides APIs for user management including registration, search, and updates
 * 
 * @author Multi-Store Team
 * @version 1.0
 * @since 2024-01-01
 */
@RestController
@RequestMapping("/api/v1/users")
@CrossOrigin(origins = "*")
@Tag(name = "User Management", description = "APIs لإدارة المستخدمين - User Management APIs")
public class UserController {

    private final UserService userService;

    /**
     * Constructor لحقن UserService
     * Constructor for UserService dependency injection
     * 
     * @param userService خدمة المستخدمين
     */
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * تسجيل مستخدم جديد
     * Register a new user
     * 
     * @param request بيانات المستخدم الجديد
     * @return ResponseEntity<UserResponse> المستخدم المُنشأ أو رسالة خطأ
     * 
     * @apiNote POST /api/v1/users/register
     * @since 1.0
     */
    @PostMapping("/register")
    @Operation(
        summary = "تسجيل مستخدم جديد - Register New User",
        description = "إنشاء حساب مستخدم جديد في النظام مع التحقق من صحة البيانات - Create a new user account with data validation"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "تم إنشاء المستخدم بنجاح - User created successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponse.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "بيانات غير صحيحة - Invalid data provided",
            content = @Content(mediaType = "application/json")
        )
    })
    public ResponseEntity<?> registerUser(
            @Parameter(description = "بيانات المستخدم الجديد - New user data", required = true)
            @Valid @RequestBody CreateUserRequest request) {
        try {
            UserResponse user = userService.createUser(request);
            return new ResponseEntity<>(user, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Registration failed");
            errorResponse.put("message", e.getMessage());
            errorResponse.put("status", HttpStatus.BAD_REQUEST.value());
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * الحصول على جميع المستخدمين
     * Get all users
     * 
     * @return ResponseEntity<List<UserResponse>> قائمة بجميع المستخدمين
     * 
     * @apiNote GET /api/v1/users
     * @since 1.0
     */
    @GetMapping
    @Operation(
        summary = "الحصول على جميع المستخدمين - Get All Users",
        description = "استرجاع قائمة بجميع المستخدمين المسجلين في النظام - Retrieve list of all registered users"
    )
    @ApiResponse(
        responseCode = "200",
        description = "تم استرجاع المستخدمين بنجاح - Users retrieved successfully",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponse.class))
    )
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        List<UserResponse> users = userService.getAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    /**
     * البحث عن مستخدم بواسطة Display ID
     * Find user by display ID
     * 
     * @param displayId المعرف المعروض (مثل USR-000001)
     * @return ResponseEntity<UserResponse> المستخدم أو 404
     * 
     * @apiNote GET /api/v1/users/display/{displayId}
     * @since 1.0
     */
    @GetMapping("/display/{displayId}")
    @Operation(
        summary = "البحث بواسطة Display ID - Find by Display ID",
        description = "البحث عن مستخدم باستخدام المعرف المعروض - Find user using display ID"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "تم العثور على المستخدم - User found",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponse.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "المستخدم غير موجود - User not found"
        )
    })
    public ResponseEntity<?> getUserByDisplayId(
            @Parameter(description = "المعرف المعروض للمستخدم - User display ID", example = "USR-000001")
            @PathVariable String displayId) {
        Optional<UserResponse> user = userService.findByDisplayId(displayId);
        if (user.isPresent()) {
            return new ResponseEntity<>(user.get(), HttpStatus.OK);
        } else {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "User not found");
            errorResponse.put("message", "User with display ID '" + displayId + "' not found");
            errorResponse.put("status", HttpStatus.NOT_FOUND.value());
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }
    }

    /**
     * البحث عن مستخدم بواسطة UUID
     * Find user by UUID
     * 
     * @param id المعرف الفريد للمستخدم
     * @return ResponseEntity<UserResponse> المستخدم أو 404
     * 
     * @apiNote GET /api/v1/users/{id}
     * @since 1.0
     */
    @GetMapping("/{id}")
    @Operation(
        summary = "البحث بواسطة UUID - Find by UUID",
        description = "البحث عن مستخدم باستخدام المعرف الفريد - Find user using UUID"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "تم العثور على المستخدم - User found",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponse.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "المستخدم غير موجود - User not found"
        )
    })
    public ResponseEntity<?> getUserById(
            @Parameter(description = "المعرف الفريد للمستخدم - User UUID")
            @PathVariable UUID id) {
        Optional<UserResponse> user = userService.findById(id);
        if (user.isPresent()) {
            return new ResponseEntity<>(user.get(), HttpStatus.OK);
        } else {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "User not found");
            errorResponse.put("message", "User with ID '" + id + "' not found");
            errorResponse.put("status", HttpStatus.NOT_FOUND.value());
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }
    }

    /**
     * البحث عن مستخدم بواسطة الإيميل
     * Find user by email
     * 
     * @param email عنوان البريد الإلكتروني
     * @return ResponseEntity<UserResponse> المستخدم أو 404
     * 
     * @apiNote GET /api/v1/users/email/{email}
     * @since 1.0
     */
    @GetMapping("/email/{email}")
    @Operation(
        summary = "البحث بواسطة الإيميل - Find by Email",
        description = "البحث عن مستخدم باستخدام البريد الإلكتروني - Find user using email address"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "تم العثور على المستخدم - User found",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponse.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "المستخدم غير موجود - User not found"
        )
    })
    public ResponseEntity<?> getUserByEmail(
            @Parameter(description = "البريد الإلكتروني للمستخدم - User email address", example = "user@example.com")
            @PathVariable String email) {
        Optional<UserResponse> user = userService.findByEmail(email);
        if (user.isPresent()) {
            return new ResponseEntity<>(user.get(), HttpStatus.OK);
        } else {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "User not found");
            errorResponse.put("message", "User with email '" + email + "' not found");
            errorResponse.put("status", HttpStatus.NOT_FOUND.value());
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }
    }

    /**
     * تفعيل أو إلغاء تفعيل المستخدم
     * Activate or deactivate user
     * 
     * @param id معرف المستخدم
     * @param isActive حالة التفعيل المطلوبة
     * @return ResponseEntity<UserResponse> المستخدم المحدث أو رسالة خطأ
     * 
     * @apiNote PUT /api/v1/users/{id}/status?isActive=true
     * @since 1.0
     */
    @PutMapping("/{id}/status")
    @Operation(
        summary = "تحديث حالة المستخدم - Update User Status",
        description = "تفعيل أو إلغاء تفعيل حساب المستخدم - Activate or deactivate user account"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "تم تحديث الحالة بنجاح - Status updated successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponse.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "المستخدم غير موجود - User not found"
        )
    })
    public ResponseEntity<?> updateUserStatus(
            @Parameter(description = "المعرف الفريد للمستخدم - User UUID")
            @PathVariable UUID id, 
            @Parameter(description = "حالة التفعيل المطلوبة - Desired activation status", example = "true")
            @RequestParam Boolean isActive) {
        try {
            UserResponse user = userService.updateUserStatus(id, isActive);
            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Update failed");
            errorResponse.put("message", e.getMessage());
            errorResponse.put("status", HttpStatus.NOT_FOUND.value());
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }
    }

    /**
     * تأكيد البريد الإلكتروني للمستخدم
     * Verify user's email address
     * 
     * @param id معرف المستخدم
     * @return ResponseEntity<UserResponse> المستخدم المحدث أو رسالة خطأ
     * 
     * @apiNote PUT /api/v1/users/{id}/verify
     * @since 1.0
     */
    @PutMapping("/{id}/verify")
    @Operation(
        summary = "تأكيد البريد الإلكتروني - Verify Email",
        description = "تأكيد البريد الإلكتروني للمستخدم - Verify user's email address"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "تم تأكيد البريد الإلكتروني بنجاح - Email verified successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponse.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "المستخدم غير موجود - User not found"
        )
    })
    public ResponseEntity<?> verifyUserEmail(
            @Parameter(description = "المعرف الفريد للمستخدم - User UUID")
            @PathVariable UUID id) {
        try {
            UserResponse user = userService.verifyUserEmail(id);
            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Verification failed");
            errorResponse.put("message", e.getMessage());
            errorResponse.put("status", HttpStatus.NOT_FOUND.value());
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }
    }

    /**
     * فحص صحة النظام
     * Health check endpoint
     * 
     * @return ResponseEntity<String> حالة النظام
     * 
     * @apiNote GET /api/v1/users/health
     * @since 1.0
     */
    @GetMapping("/health")
    @Operation(
        summary = "فحص صحة النظام - Health Check",
        description = "فحص حالة خدمة المستخدمين - Check user service health status"
    )
    @ApiResponse(
        responseCode = "200",
        description = "النظام يعمل بشكل طبيعي - System is running normally"
    )
    public ResponseEntity<Map<String, Object>> health() {
        Map<String, Object> healthResponse = new HashMap<>();
        healthResponse.put("status", "UP");
        healthResponse.put("service", "User Service");
        healthResponse.put("message", "User Service is running");
        healthResponse.put("timestamp", System.currentTimeMillis());
        return new ResponseEntity<>(healthResponse, HttpStatus.OK);
    }
}
