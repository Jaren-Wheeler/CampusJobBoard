package com.example.CampusJobBoard.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * SecurityConfig defines the baseline security rules
 * for the application using Spring Security.
 * Key points:
 * - Disables CSRF for easier API testing with Postman.
 * - Allows open access to authentication endpoints (/api/auth/**).
 * - Requires authentication for all other routes.
 * - Disables session storage (stateless setup for token-based auth).
 * - Disables default form and popup login prompts.
 * Later will integrate with custom
 * authentication and role-based access control.
 */
@Configuration
public class SecurityConfig {

    /**
     * Sets security rules for HTTP requests.
     * Defines which endpoints are public and how sessions are managed.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Disable CSRF protection to simplify API testing
                .csrf(csrf -> csrf.disable())

                // Set authorization rules
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll() // open routes for registration & login
                        .anyRequest().authenticated() // protect everything else
                )

                // Disable session storage (use stateless JWT later)
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // Remove default login page and browser login popup
                .formLogin(form -> form.disable())
                .httpBasic(httpBasic -> httpBasic.disable());

        return http.build();
    }

    /**
     * Provides a password encoder bean for encrypting user passwords.
     * Uses BCrypt
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
