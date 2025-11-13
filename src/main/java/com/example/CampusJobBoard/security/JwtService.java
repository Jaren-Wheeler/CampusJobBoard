package com.example.CampusJobBoard.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Service responsible for generating and validating JWT tokens.
 * Used by AuthService during login and by filters to verify user sessions.
 */
@Service
public class JwtService {

    // Secret key loaded from application.properties
    // This key signs and verifies tokens.
    @Value("${app.jwt.secret}")
    private String secretKey;

    // Token expiration time — currently set to 1 hour
    private static final long EXPIRATION_TIME = 1000 * 60 * 60;

    /**
     * Converts the secret key into a signing key.
     */
    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    /**
     * Generates a JWT token for a given user.
     * Includes role and username as claims.
     */
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", userDetails.getAuthorities().stream()
                .findFirst().map(Object::toString).orElse("USER"));

        return Jwts.builder()
                .setClaims(claims) // custom data inside token
                .setSubject(userDetails.getUsername()) // the main identifier (email or username)
                .setIssuedAt(new Date(System.currentTimeMillis())) // token creation time
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME)) // expiry time
                .signWith(getSigningKey(), SignatureAlgorithm.HS256) // sign using the secret key
                .compact();
    }

    /**
     * Validates a token by comparing usernames and checking expiration.
     */
    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    /**
     * Extracts the username from the JWT.
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Extracts a specific claim from the token.
     */
    public <T> T extractClaim(String token, Function<Claims, T> resolver) {
        final Claims claims = extractAllClaims(token);
        return resolver.apply(claims);
    }

    /**
     * Parses the token and returns all claims.
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Checks if the token has expired based on its expiration date.
     */
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Retrieves the expiration date from the token claims.
     */
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
}
