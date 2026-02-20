package com.drogueria.bellavista.infrastructure.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import jakarta.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * Utility component for JWT token generation, validation, and extraction.
 * Implements best practices for JWT handling including:
 * - Token expiration
 * - Signature verification
 * - Claims extraction
 */
@Component
public class JwtUtils {
    private static final Logger log = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${app.jwt.secret:your-secret-key-must-be-at-least-32-characters-for-hs256}")
    private String jwtSecret;

    @Value("${app.jwt.expiration:86400000}")  // 24 hours in ms
    private long jwtExpirationMs;

    // Minimum secret length for HS256 is 32 bytes recommended
    private static final int MIN_SECRET_BYTES = 32;

    // Cached signing key to avoid recreating it on every call
    private SecretKey signingKey;

    /**
     * Generate JWT token from authentication.
     * Token contains username as subject and expiration time.
     */
    public String generateToken(Authentication authentication) {
        String username = authentication.getName();
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationMs);

        return Jwts.builder()
            .subject(username)
            .issuedAt(now)
            .expiration(expiryDate)
            .signWith(getSigningKey(), SignatureAlgorithm.HS256)
            .compact();
    }

    /**
     * Generate JWT token from username (for custom flows).
     */
    public String generateTokenFromUsername(String username) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationMs);

        return Jwts.builder()
            .subject(username)
            .issuedAt(now)
            .expiration(expiryDate)
            .signWith(getSigningKey(), SignatureAlgorithm.HS256)
            .compact();
    }

    /**
     * Extract username from JWT token.
     */
    public String getUsernameFromToken(String token) {
        Claims claims = getAllClaimsFromToken(token);
        return claims.getSubject();
    }

    /**
     * Validate JWT token.
     * Checks signature and expiration.
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token);
            return true;
        } catch (ExpiredJwtException e) {
            log.warn("JWT token expired: {}", e.getMessage());
            return false;
        } catch (MalformedJwtException e) {
            log.warn("Malformed JWT token: {}", e.getMessage());
            return false;
        } catch (SecurityException e) {
            log.warn("Invalid JWT signature: {}", e.getMessage());
            return false;
        } catch (UnsupportedJwtException e) {
            log.warn("Unsupported JWT token: {}", e.getMessage());
            return false;
        } catch (IllegalArgumentException e) {
            log.warn("JWT token argument is invalid: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Check if token is expired.
     */
    public boolean isTokenExpired(String token) {
        try {
            Claims claims = getAllClaimsFromToken(token);
            return claims.getExpiration().before(new Date());
        } catch (Exception e) {
            return true;
        }
    }

    /**
     * Extract all claims from token.
     */
    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
            .verifyWith(getSigningKey())
            .build()
            .parseSignedClaims(token)
            .getPayload();
    }

    /**
     * Get signing key for JWT operations.
     */
    private SecretKey getSigningKey() {
        return signingKey;
    }

    @PostConstruct
    private void validateConfiguration() {
        if (jwtSecret == null || jwtSecret.trim().isEmpty()) {
            String msg = "JWT secret is not configured. Set 'app.jwt.secret' with a secure value.";
            log.error(msg);
            throw new IllegalStateException(msg);
        }
        byte[] secretBytes = decodeSecret(jwtSecret);

        if (secretBytes.length < MIN_SECRET_BYTES) {
            String msg = String.format("JWT secret is too short (%d bytes). Must be at least %d bytes.", secretBytes.length, MIN_SECRET_BYTES);
            log.error(msg);
            throw new IllegalStateException(msg);
        }

        // Initialize and cache signing key
        this.signingKey = Keys.hmacShaKeyFor(secretBytes);

        log.debug("JWT configuration validated: secret length={} bytes, expirationMs={}", secretBytes.length, jwtExpirationMs);
    }

    /**
     * Extract token from Bearer header.
     * Expected format: "Bearer <token>"
     */
    public String extractTokenFromBearerHeader(String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7);
        }
        return null;
    }

    /**
     * Decode secret string which may be provided as a hex string or as a raw string.
     * If the value contains only hex chars and has even length, decode hex; otherwise return UTF-8 bytes.
     */
    private byte[] decodeSecret(String secret) {
        String s = secret.trim();
        if (s.matches("^[0-9a-fA-F]+$") && (s.length() % 2 == 0)) {
            try {
                int len = s.length();
                byte[] data = new byte[len / 2];
                for (int i = 0; i < len; i += 2) {
                    data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                            + Character.digit(s.charAt(i+1), 16));
                }
                return data;
            } catch (Exception e) {
                log.debug("Failed to decode JWT secret as hex, falling back to UTF-8 bytes: {}", e.getMessage());
            }
        }
        return secret.getBytes(StandardCharsets.UTF_8);
    }
}
