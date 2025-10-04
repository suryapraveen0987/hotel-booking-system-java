package com.hotel.service;

import com.hotel.model.User;
import com.hotel.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Handles user registration: checks for existing user and hashes the password.
     */
    public User registerNewUser(User user) {
        // 1. Check if the username is already taken
        if (userRepository.existsByUsername(user.getUsername())) {
            return null; // Registration failed: user already exists
        }

        // 2. Hash the plaintext password before saving
        String hashedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashedPassword);

        // 3. Set default role and save
        if (user.getRole() == null) {
            user.setRole(User.Role.CUSTOMER);
        }

        return userRepository.save(user);
    }

    /**
     * Finds a user by username for login or profile lookup.
     */
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}