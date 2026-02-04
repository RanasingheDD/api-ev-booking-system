package com.ev_booking_system.api.Util;

import com.ev_booking_system.api.model.UserModel; // Import your UserModel
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {

    private final String SECRET = "9a74c825b71c0a82095bc2bff80cdb468aabbf38b7dccf23ffde7e7b88ddaffc";
    private final long EXPIRATION = 1000 * 60 * 60 * 24; // 1 day (Fixed the math: *24 for a day)

    // ✅ CHANGED: Accept UserModel instead of just String userId
    public String generateToken(UserModel user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", user.getId());     // Store ID as a claim
        claims.put("role", user.getRole()); // Store Role as a claim

        return createToken(claims, user.getEmail()); // ✅ FIX: Use EMAIL as Subject
    }

    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject) // This is now the Email!
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(Keys.hmacShaKeyFor(SECRET.getBytes()), SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractEmail(String token) {
        return parseClaims(token).getSubject(); // This will now correctly return the Email
    }

    // ✅ NEW: Helper to get the role if needed
    public String extractRole(String token) {
        return parseClaims(token).get("role", String.class);
    }

    // ✅ FIXED: Extract ID from the 'claims', not the subject
    public String extractUserId(String token) {
        return parseClaims(token).get("id", String.class);
    }

    public boolean validateToken(String token) {
        try {
            return !isTokenExpired(token);
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isTokenExpired(String token) {
        Date expiration = parseClaims(token).getExpiration();
        return expiration.before(new Date());
    }

    private Claims parseClaims(String token) {
        return Jwts.parserBuilder() // Use parserBuilder() it is newer/safer
                .setSigningKey(Keys.hmacShaKeyFor(SECRET.getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}