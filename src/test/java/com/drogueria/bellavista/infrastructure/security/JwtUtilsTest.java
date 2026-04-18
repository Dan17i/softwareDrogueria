package com.drogueria.bellavista.infrastructure.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JwtUtilsTest {

    private JwtUtils jwtUtils;

    // 64 hex chars = 32 bytes, satisfies MIN_SECRET_BYTES
    private static final String SECRET =
        "a8f3d2e1b9c4f7a6e0d5c8b2a1f4e7d3c6b9a2f5e8d1c4b7a0f3e6d9c2b5a8f1";
    private static final long EXPIRATION_MS = 3600000L;

    @Mock private Authentication authentication;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        jwtUtils = new JwtUtils();
        ReflectionTestUtils.setField(jwtUtils, "jwtSecret", SECRET);
        ReflectionTestUtils.setField(jwtUtils, "jwtExpirationMs", EXPIRATION_MS);
        // manually invoke @PostConstruct
        ReflectionTestUtils.invokeMethod(jwtUtils, "validateConfiguration");
    }

    @Test
    @DisplayName("✅ generateTokenFromUsername - retorna token no vacío")
    void shouldGenerateTokenFromUsername() {
        String token = jwtUtils.generateTokenFromUsername("daniel");
        assertNotNull(token);
        assertFalse(token.isBlank());
    }

    @Test
    @DisplayName("✅ generateToken - retorna token con subject correcto")
    void shouldGenerateTokenFromAuthentication() {
        when(authentication.getName()).thenReturn("admin");

        String token = jwtUtils.generateToken(authentication);

        assertNotNull(token);
        assertEquals("admin", jwtUtils.getUsernameFromToken(token));
    }

    @Test
    @DisplayName("✅ getUsernameFromToken - extrae username del token")
    void shouldExtractUsernameFromToken() {
        String token = jwtUtils.generateTokenFromUsername("testuser");
        assertEquals("testuser", jwtUtils.getUsernameFromToken(token));
    }

    @Test
    @DisplayName("✅ validateToken - token válido → true")
    void shouldValidateValidToken() {
        String token = jwtUtils.generateTokenFromUsername("user");
        assertTrue(jwtUtils.validateToken(token));
    }

    @Test
    @DisplayName("❌ validateToken - token malformado → false")
    void shouldReturnFalseForMalformedToken() {
        assertFalse(jwtUtils.validateToken("not.a.valid.jwt"));
    }

    @Test
    @DisplayName("❌ validateToken - token vacío/null-like → false")
    void shouldReturnFalseForBlankToken() {
        assertFalse(jwtUtils.validateToken(""));
    }

    @Test
    @DisplayName("✅ isTokenExpired - token vigente → false")
    void shouldReturnFalseForNonExpiredToken() {
        String token = jwtUtils.generateTokenFromUsername("user");
        assertFalse(jwtUtils.isTokenExpired(token));
    }

    @Test
    @DisplayName("❌ isTokenExpired - token inválido → true (excepción capturada)")
    void shouldReturnTrueForInvalidTokenOnExpiredCheck() {
        assertTrue(jwtUtils.isTokenExpired("garbage.token.here"));
    }

    @Test
    @DisplayName("✅ getIdFromToken - token sin jti → retorna fallback subject_issuedAt")
    void shouldReturnFallbackIdWhenNoJti() {
        String token = jwtUtils.generateTokenFromUsername("daniel");
        String id = jwtUtils.getIdFromToken(token);
        assertNotNull(id);
        assertTrue(id.startsWith("daniel_"));
    }

    @Test
    @DisplayName("❌ getIdFromToken - token inválido → retorna null")
    void shouldReturnNullForInvalidTokenOnGetId() {
        assertNull(jwtUtils.getIdFromToken("invalid.token"));
    }

    @Test
    @DisplayName("✅ getExpirationFromToken - retorna fecha futura")
    void shouldReturnFutureExpiration() {
        String token = jwtUtils.generateTokenFromUsername("user");
        Date expiration = jwtUtils.getExpirationFromToken(token);
        assertNotNull(expiration);
        assertTrue(expiration.after(new Date()));
    }

    @Test
    @DisplayName("❌ getExpirationFromToken - token inválido → retorna null")
    void shouldReturnNullForInvalidTokenOnGetExpiration() {
        assertNull(jwtUtils.getExpirationFromToken("bad.token"));
    }

    @Test
    @DisplayName("✅ extractTokenFromBearerHeader - extrae token del header")
    void shouldExtractTokenFromBearerHeader() {
        String token = jwtUtils.generateTokenFromUsername("user");
        String extracted = jwtUtils.extractTokenFromBearerHeader("Bearer " + token);
        assertEquals(token, extracted);
    }

    @Test
    @DisplayName("❌ extractTokenFromBearerHeader - header null → retorna null")
    void shouldReturnNullForNullHeader() {
        assertNull(jwtUtils.extractTokenFromBearerHeader(null));
    }

    @Test
    @DisplayName("❌ extractTokenFromBearerHeader - sin Bearer → retorna null")
    void shouldReturnNullForNonBearerHeader() {
        assertNull(jwtUtils.extractTokenFromBearerHeader("Basic dXNlcjpwYXNz"));
    }

    @Test
    @DisplayName("❌ validateConfiguration - secret null → lanza IllegalStateException")
    void shouldThrowWhenSecretNull() {
        JwtUtils utils = new JwtUtils();
        ReflectionTestUtils.setField(utils, "jwtSecret", null);
        ReflectionTestUtils.setField(utils, "jwtExpirationMs", EXPIRATION_MS);

        assertThrows(Exception.class,
            () -> ReflectionTestUtils.invokeMethod(utils, "validateConfiguration"));
    }

    @Test
    @DisplayName("❌ validateConfiguration - secret muy corto → lanza IllegalStateException")
    void shouldThrowWhenSecretTooShort() {
        JwtUtils utils = new JwtUtils();
        ReflectionTestUtils.setField(utils, "jwtSecret", "short");
        ReflectionTestUtils.setField(utils, "jwtExpirationMs", EXPIRATION_MS);

        assertThrows(Exception.class,
            () -> ReflectionTestUtils.invokeMethod(utils, "validateConfiguration"));
    }

    @Test
    @DisplayName("✅ validateConfiguration - secret UTF-8 largo → inicializa correctamente")
    void shouldInitializeWithUtf8Secret() {
        JwtUtils utils = new JwtUtils();
        // 32+ chars non-hex string → UTF-8 path
        ReflectionTestUtils.setField(utils, "jwtSecret", "ThisIsAVeryLongSecretKeyForJWT!!");
        ReflectionTestUtils.setField(utils, "jwtExpirationMs", EXPIRATION_MS);

        assertDoesNotThrow(() -> ReflectionTestUtils.invokeMethod(utils, "validateConfiguration"));
    }
}
