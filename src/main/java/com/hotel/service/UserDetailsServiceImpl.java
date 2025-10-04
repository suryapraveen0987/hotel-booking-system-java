package com.hotel.service;

import com.hotel.model.User;
import com.hotel.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;
import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 1. Load User from your database
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        // 2. Map the User's role to Spring Security's GrantedAuthority
        // Ensure the role is never null
        if (user.getRole() == null) {
            throw new UsernameNotFoundException("User role not defined for: " + username);
        }
        
        // Convert the Enum role to a Spring Security GrantedAuthority List
        List<GrantedAuthority> authorities = Collections.singletonList(
            new SimpleGrantedAuthority(user.getRole().name())
        );

        // 3. Return the Spring Security UserDetails object
        // user.getPassword() MUST be the BCrypt HASH from the database.
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(), 
                authorities
        );
    }
}