package com.senkiv.carsharing.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import javax.crypto.SecretKey;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

class JwtUtilTest {
    private static final String SECRET = "testSecretKeyForJwtTokenGenerationAndValidation";
    private static final long VALIDITY_TIME = 3600000;
    private static final String TEST_USERNAME = "test@example.com";

    private JwtUtil jwtUtil;
    private SecretKey secretKey;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();
        secretKey = Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));

        ReflectionTestUtils.setField(jwtUtil, "secret", SECRET);
        ReflectionTestUtils.setField(jwtUtil, "validityTime", VALIDITY_TIME);
        ReflectionTestUtils.setField(jwtUtil, "secretKey", secretKey);
    }

    @Test
    void generateToken_ShouldCreateValidToken() {
        String token = jwtUtil.generateToken(TEST_USERNAME);

        assertNotNull(token);
        assertFalse(token.isEmpty());

        JwtParser parser = Jwts.parser().verifyWith(secretKey).build();
        Jws<Claims> claims = parser.parseSignedClaims(token);

        assertEquals(TEST_USERNAME, claims.getPayload().getSubject());
        assertNotNull(claims.getPayload().getIssuedAt());
        assertNotNull(claims.getPayload().getExpiration());
    }

    @Test
    void getUsername_ShouldReturnCorrectUsername() {
        String token = jwtUtil.generateToken(TEST_USERNAME);

        String username = jwtUtil.getUsername(token);

        assertEquals(TEST_USERNAME, username);
    }

    @Test
    void isValid_ShouldReturnTrueForValidToken() {
        String token = jwtUtil.generateToken(TEST_USERNAME);

        boolean isValid = jwtUtil.isValid(token);

        assertTrue(isValid);
    }

    @Test
    void isValid_ShouldReturnFalseForExpiredToken() throws Exception {
        JwtUtil expiredJwtUtil = new JwtUtil();
        ReflectionTestUtils.setField(expiredJwtUtil, "secret", SECRET);
        ReflectionTestUtils.setField(expiredJwtUtil, "validityTime", -10000L);
        ReflectionTestUtils.setField(expiredJwtUtil, "secretKey", secretKey);

        String expiredToken = expiredJwtUtil.generateToken(TEST_USERNAME);

        boolean isValid = jwtUtil.isValid(expiredToken);

        assertFalse(isValid);
    }
}
