package com.drogueria.bellavista.application.service;

import com.drogueria.bellavista.domain.model.PasswordResetToken;
import com.drogueria.bellavista.domain.model.Role;
import com.drogueria.bellavista.domain.model.User;
import com.drogueria.bellavista.domain.service.PasswordResetService;
import com.drogueria.bellavista.domain.service.UserService;
import com.drogueria.bellavista.exception.AuthenticationException;
import com.drogueria.bellavista.exception.ResourceNotFoundException;
import com.drogueria.bellavista.infrastructure.security.JwtUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthServiceTest {

    @Mock private UserService userService;
    @Mock private JwtUtils jwtUtils;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private EmailService emailService;
    @Mock private PasswordResetService passwordResetService;
    @Mock private RedisTemplate<String, String> redisTemplate;
    @Mock private ValueOperations<String, String> valueOperations;

    @InjectMocks
    private AuthService authService;

    private User activeUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        activeUser = User.builder()
            .id(1L).username("daniel").email("daniel@b.com")
            .password("encoded").role(Role.USER).active(true).build();
    }

    // registerUser

    @Test
    @DisplayName("✅ registerUser - crea usuario y envía welcome email")
    void shouldRegisterUserAndSendEmail() {
        when(userService.createUser("daniel", "daniel@b.com", "pass", "D", "J", Role.USER))
            .thenReturn(activeUser);

        User result = authService.registerUser("daniel", "daniel@b.com", "pass", "D", "J");

        assertEquals("daniel", result.getUsername());
        verify(emailService).sendWelcomeEmail("daniel@b.com", "daniel");
    }

    // registerUserWithRole

    @Test
    @DisplayName("✅ registerUserWithRole - crea usuario con rol específico")
    void shouldRegisterUserWithRole() {
        User adminUser = User.builder().id(2L).username("admin").role(Role.ADMIN).build();
        when(userService.createUser("admin", "admin@b.com", "pass", "A", "S", Role.ADMIN))
            .thenReturn(adminUser);

        User result = authService.registerUserWithRole("admin", "admin@b.com", "pass", "A", "S", Role.ADMIN);

        assertEquals(Role.ADMIN, result.getRole());
        verifyNoInteractions(emailService);
    }

    // authenticateUser

    @Test
    @DisplayName("✅ authenticateUser - credenciales válidas retorna token")
    void shouldAuthenticateAndReturnToken() {
        when(userService.getUserByUsername("daniel")).thenReturn(activeUser);
        when(userService.verifyPassword("pass", "encoded")).thenReturn(true);
        when(jwtUtils.generateTokenFromUsername("daniel")).thenReturn("jwt-token");

        String token = authService.authenticateUser("daniel", "pass");

        assertEquals("jwt-token", token);
        verify(userService).updateLastLogin(1L);
    }

    @Test
    @DisplayName("❌ authenticateUser - usuario inactivo lanza AuthenticationException")
    void shouldThrowWhenUserInactive() {
        activeUser.setActive(false);
        when(userService.getUserByUsername("daniel")).thenReturn(activeUser);

        assertThrows(AuthenticationException.class, () -> authService.authenticateUser("daniel", "pass"));
    }

    @Test
    @DisplayName("❌ authenticateUser - contraseña incorrecta lanza AuthenticationException")
    void shouldThrowWhenPasswordInvalid() {
        when(userService.getUserByUsername("daniel")).thenReturn(activeUser);
        when(userService.verifyPassword("wrong", "encoded")).thenReturn(false);

        assertThrows(AuthenticationException.class, () -> authService.authenticateUser("daniel", "wrong"));
    }

    @Test
    @DisplayName("❌ authenticateUser - usuario no existe lanza AuthenticationException")
    void shouldThrowWhenUserNotFound() {
        when(userService.getUserByUsername("ghost"))
            .thenThrow(new ResourceNotFoundException("User", "username", "ghost"));

        assertThrows(AuthenticationException.class, () -> authService.authenticateUser("ghost", "pass"));
    }

    // validateToken / getUsernameFromToken

    @Test
    @DisplayName("✅ validateToken - delega a jwtUtils")
    void shouldValidateToken() {
        when(jwtUtils.validateToken("token")).thenReturn(true);
        assertTrue(authService.validateToken("token"));
    }

    @Test
    @DisplayName("✅ getUsernameFromToken - delega a jwtUtils")
    void shouldGetUsernameFromToken() {
        when(jwtUtils.getUsernameFromToken("token")).thenReturn("daniel");
        assertEquals("daniel", authService.getUsernameFromToken("token"));
    }

    // logout

    @Test
    @DisplayName("✅ logout - agrega token a blacklist en Redis")
    void shouldAddTokenToBlacklist() {
        when(jwtUtils.getIdFromToken("token")).thenReturn("jti-123");
        when(jwtUtils.getExpirationFromToken("token"))
            .thenReturn(new Date(System.currentTimeMillis() + 60000));
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);

        authService.logout("token");

        verify(valueOperations).set(eq("blacklist:jti-123"), eq("true"), anyLong(), eq(TimeUnit.MILLISECONDS));
    }

    @Test
    @DisplayName("✅ logout - token ya expirado no agrega a blacklist")
    void shouldSkipBlacklistWhenTokenAlreadyExpired() {
        when(jwtUtils.getIdFromToken("token")).thenReturn("jti-123");
        when(jwtUtils.getExpirationFromToken("token"))
            .thenReturn(new Date(System.currentTimeMillis() - 1000));

        authService.logout("token");

        verifyNoInteractions(redisTemplate);
    }

    @Test
    @DisplayName("✅ logout - jti null no agrega a blacklist")
    void shouldSkipBlacklistWhenJtiIsNull() {
        when(jwtUtils.getIdFromToken("token")).thenReturn(null);

        authService.logout("token");

        verifyNoInteractions(redisTemplate);
    }

    @Test
    @DisplayName("✅ logout - Redis lanza excepción no propaga error")
    void shouldNotFailLogoutWhenRedisThrows() {
        when(jwtUtils.getIdFromToken("token")).thenThrow(new RuntimeException("Redis down"));

        assertDoesNotThrow(() -> authService.logout("token"));
    }

    // isTokenBlacklisted

    @Test
    @DisplayName("✅ isTokenBlacklisted - token en blacklist retorna true")
    void shouldReturnTrueWhenTokenBlacklisted() {
        when(jwtUtils.getIdFromToken("token")).thenReturn("jti-123");
        when(redisTemplate.hasKey("blacklist:jti-123")).thenReturn(true);

        assertTrue(authService.isTokenBlacklisted("token"));
    }

    @Test
    @DisplayName("❌ isTokenBlacklisted - token no en blacklist retorna false")
    void shouldReturnFalseWhenTokenNotBlacklisted() {
        when(jwtUtils.getIdFromToken("token")).thenReturn("jti-123");
        when(redisTemplate.hasKey("blacklist:jti-123")).thenReturn(false);

        assertFalse(authService.isTokenBlacklisted("token"));
    }

    @Test
    @DisplayName("❌ isTokenBlacklisted - jti null retorna false")
    void shouldReturnFalseWhenJtiNull() {
        when(jwtUtils.getIdFromToken("token")).thenReturn(null);

        assertFalse(authService.isTokenBlacklisted("token"));
    }

    @Test
    @DisplayName("✅ isTokenBlacklisted - Redis lanza excepción retorna false")
    void shouldReturnFalseWhenRedisThrows() {
        when(jwtUtils.getIdFromToken("token")).thenThrow(new RuntimeException("error"));

        assertFalse(authService.isTokenBlacklisted("token"));
    }

    // getCurrentUser

    @Test
    @DisplayName("✅ getCurrentUser - usuario autenticado retorna User")
    void shouldGetCurrentUser() {
        Authentication auth = mock(Authentication.class);
        SecurityContext context = mock(SecurityContext.class);
        when(context.getAuthentication()).thenReturn(auth);
        when(auth.isAuthenticated()).thenReturn(true);
        when(auth.getName()).thenReturn("daniel");
        SecurityContextHolder.setContext(context);
        when(userService.getUserByUsername("daniel")).thenReturn(activeUser);

        User result = authService.getCurrentUser();

        assertEquals("daniel", result.getUsername());
        SecurityContextHolder.clearContext();
    }

    @Test
    @DisplayName("❌ getCurrentUser - sin autenticación lanza AuthenticationException")
    void shouldThrowWhenNoAuthentication() {
        SecurityContext context = mock(SecurityContext.class);
        when(context.getAuthentication()).thenReturn(null);
        SecurityContextHolder.setContext(context);

        assertThrows(AuthenticationException.class, () -> authService.getCurrentUser());
        SecurityContextHolder.clearContext();
    }

    // isUsernameAvailable / isEmailAvailable

    @Test
    @DisplayName("✅ isUsernameAvailable - delega a userService")
    void shouldCheckUsernameAvailability() {
        when(userService.isUsernameAvailable("daniel")).thenReturn(false);
        assertFalse(authService.isUsernameAvailable("daniel"));
    }

    @Test
    @DisplayName("✅ isEmailAvailable - delega a userService")
    void shouldCheckEmailAvailability() {
        when(userService.isEmailAvailable("daniel@b.com")).thenReturn(true);
        assertTrue(authService.isEmailAvailable("daniel@b.com"));
    }

    // loadUserByUsername

    @Test
    @DisplayName("✅ loadUserByUsername - retorna UserDetails con rol correcto")
    void shouldLoadUserDetails() {
        when(userService.getUserByUsername("daniel")).thenReturn(activeUser);

        UserDetails details = authService.loadUserByUsername("daniel");

        assertEquals("daniel", details.getUsername());
        assertTrue(details.isEnabled());
        assertTrue(details.getAuthorities().stream()
            .anyMatch(a -> a.getAuthority().equals("ROLE_USER")));
    }

    @Test
    @DisplayName("❌ loadUserByUsername - usuario no existe lanza UsernameNotFoundException")
    void shouldThrowUsernameNotFoundWhenUserMissing() {
        when(userService.getUserByUsername("ghost"))
            .thenThrow(new RuntimeException("not found"));

        assertThrows(org.springframework.security.core.userdetails.UsernameNotFoundException.class,
            () -> authService.loadUserByUsername("ghost"));
    }

    // getUserByUsername

    @Test
    @DisplayName("✅ getUserByUsername - delega a userService")
    void shouldGetUserByUsername() {
        when(userService.getUserByUsername("daniel")).thenReturn(activeUser);
        assertEquals("daniel", authService.getUserByUsername("daniel").getUsername());
    }

    // requestPasswordReset

    @Test
    @DisplayName("✅ requestPasswordReset - email existe → envía email de reset")
    void shouldRequestPasswordReset() {
        PasswordResetToken token = PasswordResetToken.builder()
            .token("reset-token").userId(1L)
            .expiryDate(LocalDateTime.now().plusHours(1)).build();
        when(userService.getUserByEmail("daniel@b.com")).thenReturn(activeUser);
        when(passwordResetService.createPasswordResetToken("daniel@b.com")).thenReturn(token);

        authService.requestPasswordReset("daniel@b.com");

        verify(emailService).sendPasswordResetEmail("daniel@b.com", "daniel", "reset-token");
    }

    @Test
    @DisplayName("✅ requestPasswordReset - email no existe → silencioso (sin excepción)")
    void shouldSilentlyIgnoreUnknownEmail() {
        when(userService.getUserByEmail("ghost@b.com"))
            .thenThrow(new ResourceNotFoundException("User not found"));

        assertDoesNotThrow(() -> authService.requestPasswordReset("ghost@b.com"));
        verifyNoInteractions(emailService);
    }

    // resetPassword

    @Test
    @DisplayName("✅ resetPassword - token válido actualiza password y envía email")
    void shouldResetPassword() {
        when(passwordResetService.resetPassword("reset-token", "newPass123")).thenReturn(activeUser);

        authService.resetPassword("reset-token", "newPass123");

        verify(emailService).sendPasswordChangedEmail("daniel@b.com", "daniel");
    }
}
