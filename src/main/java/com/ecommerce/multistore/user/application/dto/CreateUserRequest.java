package com.ecommerce.multistore.user.application.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CreateUserRequest {
    
    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;
    
    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password should be at least 6 characters")
    private String password;
    
    @NotBlank(message = "First name is required")
    private String firstName;
    
    @NotBlank(message = "Last name is required")
    private String lastName;
    
    private String firstNameAr;
    private String lastNameAr;
    private String phone;
    
    // Constructors
    public CreateUserRequest() {}
    
    // Getters and Setters
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    
    public String getFirstNameAr() { return firstNameAr; }
    public void setFirstNameAr(String firstNameAr) { this.firstNameAr = firstNameAr; }
    
    public String getLastNameAr() { return lastNameAr; }
    public void setLastNameAr(String lastNameAr) { this.lastNameAr = lastNameAr; }
    
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
}
