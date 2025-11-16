package com.example.CampusJobBoard.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Configures global security settings for the Campus Job Board system.
 *
 * <p>This class uses JWT authentication, disables unnecessary
 * session-based login mechanisms, and defines role-based access rules
 * for all protected endpoints.</p>
 *
 * <p>The EnableMethodSecurity annotation allows the use of {@code @PreAuthorize}
 *  * in controllers for detailed role enforcement.</p>
 */
@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    public SecurityConfig(JwtAuthFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)

                .authorizeHttpRequests(auth -> auth
                        // Allow static assets and login page
                        .requestMatchers("/", "/login", "/css/**", "/js/**", "/images/**").permitAll()

                        // Allow all dashboard HTML pages (frontend routing only)
                        .requestMatchers("/student/dashboard").permitAll()
                        .requestMatchers("/employer/dashboard").permitAll()
                        .requestMatchers("/admin/dashboard").permitAll()
                        .requestMatchers("/superadmin/dashboard").permitAll()

                        // Auth API
                        .requestMatchers("/api/auth/**").permitAll()

                        // Protected APIs
                        .requestMatchers("/api/student/**").hasRole("STUDENT")
                        .requestMatchers("/api/employer/**").hasRole("EMPLOYER")
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")
                        .requestMatchers("/api/superadmin/**").hasRole("SUPER_ADMIN")

                        .anyRequest().permitAll() // optional: kill all backend blocking
                )



                // API is stateless
                .sessionManagement(sm ->
                        sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // JWT before Spring Security checks
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)

                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable);

        return http.build();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration cfg) throws Exception {
        return cfg.getAuthenticationManager();
    }
}
