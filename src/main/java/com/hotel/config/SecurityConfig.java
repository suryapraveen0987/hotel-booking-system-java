package com.hotel.config;

import com.hotel.service.UserDetailsServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // 1. Inject the custom UserDetails Service
    private final UserDetailsServiceImpl userDetailsService;
    
    // Constructor Injection for the UserDetailsServiceImpl
    public SecurityConfig(UserDetailsServiceImpl userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    /**
     * Defines the password encoder bean used by UserService and Spring Security
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 2. Define the Authentication Provider that uses the DB and BCrypt
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService); // Tells Spring to load users via your DB service
        authProvider.setPasswordEncoder(passwordEncoder());      // Tells Spring to use your BCrypt encoder for comparison
        return authProvider;
    }

    /**
     * Configures the security filter chain (authorization rules and forms)
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // 3. Inject the custom Authentication Provider into the security chain
            .authenticationProvider(authenticationProvider()) 
            
            // Define access rules based on roles
            .authorizeHttpRequests(authorize -> authorize
                // Publicly accessible paths (Home, Register, Login, Static files)
                .requestMatchers("/css/**", "/js/**", "/images/**", "/", "/register", "/login").permitAll()
                
                // Admin-only paths
                .requestMatchers("/admin/**").hasAuthority("ADMIN") 
                
                // All other paths require authentication
                .anyRequest().authenticated() 
            )
            // Configure the login form
            .formLogin(form -> form
                .loginPage("/login") 
                .defaultSuccessUrl("/search", true) 
                .permitAll()
            )
            // Configure the logout behavior
            .logout(logout -> logout
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/")
                .permitAll()
            )
            // Handle access denied scenarios
            .exceptionHandling(exceptions -> exceptions
                .accessDeniedPage("/access-denied")
            );

        return http.build();
    }
}