package com.hotel.controller;

import com.hotel.model.User;
import com.hotel.model.User.Role;
import com.hotel.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthController {

    private final UserService userService;

    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    // --- Login Handlers ---
    @GetMapping("/login")
    public String showLoginPage() {
        // Return the name of the Thymeleaf template (login.html)
        return "login";
    }

    // --- Registration Handlers ---
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        // Pass a blank User object to the form for data binding
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") User user, Model model) {
        
        // Ensure the role is set to CUSTOMER during registration
        user.setRole(Role.CUSTOMER);
        
        User registered = userService.registerNewUser(user);

        if (registered == null) {
            // Failed: Username already exists
            model.addAttribute("errorMessage", "Registration failed: Username already exists.");
            return "register"; 
        }

        // Success: Redirect user to the login page
        return "redirect:/login?success";
    }
    
    // Handler for access denied page (redirected from SecurityConfig)
    @GetMapping("/access-denied")
    public String accessDenied() {
        return "access-denied"; // You need to create this template
    }
}