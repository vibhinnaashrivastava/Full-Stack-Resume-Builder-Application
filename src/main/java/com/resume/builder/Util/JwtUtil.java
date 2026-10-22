
package com.resume.builder.Util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private long jwtExpiration;

    // Generate JWT token
    public String generateToken(String userId) {

        Date now = new Date();

        Date expiryDate = new Date(
                now.getTime() + jwtExpiration
        );

        return Jwts.builder()
                .subject(userId)
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(getSigningKey())
                .compact();
    }

    // Generate the HMAC signing key
    private SecretKey getSigningKey() {

        byte[] keyBytes = jwtSecret.getBytes(
                StandardCharsets.UTF_8
        );

        return Keys.hmacShaKeyFor(keyBytes);
    }

    // Extract user ID from JWT
    public String getUserIdFromToken(String token) {

        Claims claims = Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return claims.getSubject();
    }

    // Validate JWT signature and expiration
    public boolean validateToken(String token) {

        try {
            Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token);

            return true;

        } catch (JwtException | IllegalArgumentException exception) {
            return false;
        }
    }

    // Check whether JWT is expired
    public boolean isTokenExpired(String token) {

        try {
            Claims claims = Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            return claims.getExpiration() == null
                    || claims.getExpiration().before(new Date());

        } catch (JwtException | IllegalArgumentException exception) {
            return true;
        }
    }
}
