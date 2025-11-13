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

    /**
     * Defines security rules and integrates the JwtAuthFilter so that
     * tokens are validated before any controller logic executes.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Disable CSRF
                .csrf(AbstractHttpConfigurer::disable)

                // Define access control rules for URL patterns
                .authorizeHttpRequests(auth -> auth
                        // Public endpoints (authentication)
                        .requestMatchers("/api/auth/**").permitAll()

                        // Role-protected routes (backed by JWT roles)
                        .requestMatchers("/admin/**").hasAuthority("ROLE_ADMIN")
                        .requestMatchers("/employer/**").hasAuthority("ROLE_EMPLOYER")
                        .requestMatchers("/student/**").hasAuthority("ROLE_STUDENT")

                        // Any other request must be authenticated
                        .anyRequest().authenticated()
                )

                // Use stateless session policy as JWT manages auth
                .sessionManagement(sm ->
                        sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // Inserts JWT validation before username/password login filter
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)

                // Disable legacy login options
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable);

        return http.build();
    }

    /**
     * Provides a BCryptPasswordEncoder bean for hashing passwords
     * during user registration and authentication.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Exposes the AuthenticationManager to support authentication
     * in the AuthService.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration cfg) throws Exception {
        return cfg.getAuthenticationManager();
    }
}
