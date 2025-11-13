package com.example.CampusJobBoard.security;

import com.example.CampusJobBoard.security.JwtAuthFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * SecurityConfig defines the application's security behavior.
 * Key Responsibilities:
 * - Integrates JWT authentication with JwtAuthFilter.
 * - Disables CSRF for simpler API testing with Postman.
 * - Allows open access to /api/auth/** (register/login routes).
 * - Requires valid JWT tokens for all other requests.
 * - Disables traditional session management and form logins.
 */
@Configuration
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    public SecurityConfig(JwtAuthFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }

    /**
     * Configures global security filters and request rules.
     * Ensures JWT validation occurs before other authentication logic.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Disable CSRF for API use (safe for token-based security)
                .csrf(AbstractHttpConfigurer::disable)

                // Define access rules
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll() // open for register/login
                        .anyRequest().authenticated() // all other routes require JWT
                )

                // Use stateless session policy (no HTTP sessions stored)
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // Add JWT validation filter before standard username/password filter
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)

                // Disable default browser-based login forms
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable);

        return http.build();
    }

    /**
     * Provides a PasswordEncoder bean.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Provides an AuthenticationManager bean
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration cfg) throws Exception {
        return cfg.getAuthenticationManager();
    }
}
