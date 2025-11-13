package com.example.CampusJobBoard.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

/**
 * Intercepts incoming requests to validate the JWT token.
 * If the token is valid, the user's authentication is set in the SecurityContext.
 */
@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    public JwtAuthFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // Extract Authorization header from the incoming request
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String username;

        // Only continues if header starts with Bearer
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Extracts the token and username from JWT
        jwt = authHeader.substring(7);
        username = jwtService.extractUsername(jwt);

        // Ensures no one is already authenticated
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // Validate the token
            User userDetails = new User(username, "", java.util.List.of());

            if (jwtService.validateToken(jwt, userDetails)) {
                // Build an authentication object and attach it to the context
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // Continue the filter chain
        filterChain.doFilter(request, response);
    }
}
