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
 * Handles creation, parsing, and validation of JWT tokens.
 *
 * <p>This service is used to generate
 * secure access tokens and by the security filter to validate them.</p>
 *
 * <p>Tokens contain the user's email and their role
 * (Student, Employer, Admin) as custom claims.</p>
 */
@Service
public class JwtService {

    /** Secret signing key loaded from application.properties */
    @Value("${app.jwt.secret}")
    private String secretKey;

    /** Token validity duration — currently 1 hour */
    private static final long EXPIRATION_TIME = 1000 * 60 * 60;

    /**
     * Creates a key using the secret key string from application.properties.
     */
    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    /**
     * Generates a JWT token for a given authenticated user.
     *
     * <p>Includes custom claims such as the user's role. The role
     * is stored without the `ROLE_` prefix for cleaner usage and
     * easier access across the system.</p>
     *
     * @param userDetails authenticated user information
     * @return signed JWT token as a string
     */
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();

        // --- Extract user role ---
        // Example: "ROLE_ADMIN" becomes "ADMIN".
        String rawRole = userDetails.getAuthorities().stream()
                .findFirst()
                .map(auth -> auth.getAuthority().replace("ROLE_", ""))
                .orElse("STUDENT");

        // Store the user's role inside the token
        claims.put("role", rawRole);

        return Jwts.builder()
                .setClaims(claims) // custom role claim
                .setSubject(userDetails.getUsername()) // user's email
                .setIssuedAt(new Date(System.currentTimeMillis())) // token creation timestamp
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME)) // expiry timestamp
                .signWith(getSigningKey(), SignatureAlgorithm.HS256) // sign with the secret key
                .compact();
    }

    /**
     * Validates the token's subject and ensures it has not expired.
     *
     * @param token       the token being validated
     * @param userDetails expected user details
     * @return true if the token is valid for the user
     */
    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    /**
     * Extracts the username (email) stored as the token subject.
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Extracts the user role stored in the token.
     *
     * <p>This is used in the JwtAuthFilter to build Spring Security
     * authorities for role-based access control.</p>
     */
    public String extractRole(String token) {
        return extractClaim(token, claims -> claims.get("role", String.class));
    }

    /**
     * Extracts a single claim using a resolver function.
     */
    public <T> T extractClaim(String token, Function<Claims, T> resolver) {
        final Claims claims = extractAllClaims(token);
        return resolver.apply(claims);
    }

    /**
     * Parses and returns all claims contained within the JWT.
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey()) // verify signature with secret key
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Determines whether the token has exceeded its expiration time.
     */
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Retrieves the token expiration timestamp.
     */
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
}
