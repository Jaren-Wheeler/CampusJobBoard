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
                .csrf(AbstractHttpConfigurer::disable)

                .authorizeHttpRequests(auth -> auth

                        // Frontend pages must be PUBLIC
                        .requestMatchers("/", "/login", "/css/**", "/js/**", "/images/**").permitAll()
                        .requestMatchers("/student/**", "/employer/**", "/admin/**").permitAll()

                        // Backend API auth endpoints
                        .requestMatchers("/api/auth/**").permitAll()

                        // Protected backend API routes (DATA + ACTIONS)
                        .requestMatchers("/api/student/**").hasAuthority("STUDENT")
                        .requestMatchers("/api/employer/**").hasAuthority("EMPLOYER")
                        .requestMatchers("/api/admin/**").hasAuthority("ADMIN")

                        // Everything else requires authentication
                        .anyRequest().authenticated()
                )

                .sessionManagement(sm ->
                        sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)

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
