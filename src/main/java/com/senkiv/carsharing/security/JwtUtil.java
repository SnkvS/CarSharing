package com.senkiv.carsharing.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {
    @Value("${token.validity}")
    private Long validityTime;
    @Value("${token.secret}")
    private String secret;
    private SecretKey secretKey;

    public String generateToken(String username) {
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + validityTime))
                .signWith(secretKey)
                .compact();
    }

    public String getUsername(String token) {
        JwtParser parser = Jwts.parser()
                .verifyWith(secretKey)
                .build();
        Jws<Claims> claims = parser.parseSignedClaims(token);
        return claims.getPayload().getSubject();
    }

    public boolean isValid(String token) {
        try {
            JwtParser parser = Jwts.parser().verifyWith(secretKey).build();
            Date expiration = parser.parseSignedClaims(token).getPayload().getExpiration();
            return expiration.after(new Date(System.currentTimeMillis()));
        } catch (Exception e) {
            return false;
        }
    }

    @PostConstruct
    private void init() {
        secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }
}
