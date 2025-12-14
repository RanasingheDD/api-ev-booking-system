package com.ev_booking_system.api.Util;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.util.Date;
@Component
public class JwtUtil {

    private final String SECRET = "thisIsASecretKeyWithAtLeast32Characters!";
    private final long EXPIRATION = 1000 * 60 *60*3 ; // 1 day

    public String generateToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(Keys.hmacShaKeyFor(SECRET.getBytes()), SignatureAlgorithm.HS256)
                .compact();
    }


    public String extractEmail(String token) {
        return parseClaims(token).getSubject();
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
        return Jwts.parser()
                .setSigningKey(Keys.hmacShaKeyFor(SECRET.getBytes()))
                .parseClaimsJws(token)
                .getBody();
    }

}
