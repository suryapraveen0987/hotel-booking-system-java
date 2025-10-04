package com.hotel.model;

import jakarta.persistence.*;

@Entity
@Table(name = "users") 
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    
    @Column(unique = true, nullable = false)
    private String username;
    
    @Column(nullable = false)
    private String password; // Stores the hashed password
    
    @Column(unique = true)
    private String email;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    // Define the Role Enum (used for security and authorization)
    public enum Role {
        CUSTOMER,
        ADMIN
    }

    // --- Constructors ---
    
    // Default constructor required by JPA
    public User() {} 
    
    // Constructor for registration (when userId is not yet known)
    public User(String username, String password, String email, Role role) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
    }

    // --- Getters and Setters ---
    
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    // Note: Password setter is used when creating/updating the user, 
    // it should be called with an ALREADY HASHED password in the service layer.
    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}