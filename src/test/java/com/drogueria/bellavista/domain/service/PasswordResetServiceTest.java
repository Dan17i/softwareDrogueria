package com.drogueria.bellavista.domain.service;

import com.drogueria.bellavista.domain.model.PasswordResetToken;
import com.drogueria.bellavista.domain.model.Role;
import com.drogueria.bellavista.domain.model.User;
import com.drogueria.bellavista.domain.repository.PasswordResetTokenRepository;
import com.drogueria.bellavista.exception.BusinessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PasswordResetServiceTest {

    @Mock
    private PasswordResetTokenRepository tokenRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private PasswordResetService passwordResetService;

    private User testUser;
    private PasswordResetToken validToken;
    private PasswordResetToken expiredToken;
    private PasswordResetToken usedToken;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        testUser = User.builder()
            .id(1L)
            .username("testuser")
            .email("test@bellavista.com")
            .role(Role.USER)
            .active(true)
            .build();

        validToken = PasswordResetToken.builder()
            .id(1L)
            .token("valid-uuid-token")
            .userId(1L)
            .expiryDate(LocalDateTime.now().plusHours(1))
            .used(false)
            .createdAt(LocalDateTime.now())
            .build();

        expiredToken = PasswordResetToken.builder()
            .id(2L)
            .token("expired-token")
            .userId(1L)
            .expiryDate(LocalDateTime.now().minusHours(2))
            .used(false)
            .createdAt(LocalDateTime.now().minusHours(3))
            .build();

        usedToken = PasswordResetToken.builder()
            .id(3L)
            .token("used-token")
            .userId(1L)
            .expiryDate(LocalDateTime.now().plusHours(1))
            .used(true)
            .createdAt(LocalDateTime.now())
            .build();
    }

    @Test
    @DisplayName("✅ createPasswordResetToken - elimina tokens previos y guarda uno nuevo")
    void shouldCreateTokenAndDeletePrevious() {
        when(userService.getUserByEmail("test@bellavista.com")).thenReturn(testUser);
        when(tokenRepository.save(any(PasswordResetToken.class))).thenReturn(validToken);

        PasswordResetToken result = passwordResetService.createPasswordResetToken("test@bellavista.com");

        assertNotNull(result);
        verify(tokenRepository).deleteByUserId(1L);
        verify(tokenRepository).save(any(PasswordResetToken.class));
    }

    @Test
    @DisplayName("✅ createPasswordResetToken - token expira en ~1 hora y no está usado")
    void shouldCreateTokenWithCorrectExpiry() {
        when(userService.getUserByEmail("test@bellavista.com")).thenReturn(testUser);
        when(tokenRepository.save(any(PasswordResetToken.class))).thenAnswer(inv -> inv.getArgument(0));

        PasswordResetToken result = passwordResetService.createPasswordResetToken("test@bellavista.com");

        assertTrue(result.getExpiryDate().isAfter(LocalDateTime.now().plusMinutes(59)));
        assertFalse(result.isUsed());
        assertNotNull(result.getToken());
    }

    @Test
    @DisplayName("❌ resetPassword - token null lanza BusinessException")
    void shouldThrowWhenTokenIsNull() {
        assertThrows(BusinessException.class,
            () -> passwordResetService.resetPassword(null, "newPass123"));
        verifyNoInteractions(tokenRepository);
    }

    @Test
    @DisplayName("❌ resetPassword - token vacío lanza BusinessException")
    void shouldThrowWhenTokenIsBlank() {
        assertThrows(BusinessException.class,
            () -> passwordResetService.resetPassword("  ", "newPass123"));
        verifyNoInteractions(tokenRepository);
    }

    @Test
    @DisplayName("❌ resetPassword - token no encontrado lanza BusinessException")
    void shouldThrowWhenTokenNotFound() {
        when(tokenRepository.findByToken("invalid-token")).thenReturn(Optional.empty());

        assertThrows(BusinessException.class,
            () -> passwordResetService.resetPassword("invalid-token", "newPass123"));
        verify(userService, never()).updatePassword(anyLong(), anyString());
    }

    @Test
    @DisplayName("❌ resetPassword - token ya usado lanza BusinessException")
    void shouldThrowWhenTokenAlreadyUsed() {
        when(tokenRepository.findByToken("used-token")).thenReturn(Optional.of(usedToken));

        assertThrows(BusinessException.class,
            () -> passwordResetService.resetPassword("used-token", "newPass123"));
        verify(userService, never()).updatePassword(anyLong(), anyString());
    }

    @Test
    @DisplayName("❌ resetPassword - token expirado lanza BusinessException")
    void shouldThrowWhenTokenExpired() {
        when(tokenRepository.findByToken("expired-token")).thenReturn(Optional.of(expiredToken));

        assertThrows(BusinessException.class,
            () -> passwordResetService.resetPassword("expired-token", "newPass123"));
        verify(userService, never()).updatePassword(anyLong(), anyString());
    }

    @Test
    @DisplayName("✅ resetPassword - happy path: actualiza password, marca token usado, retorna User")
    void shouldResetPasswordSuccessfully() {
        when(tokenRepository.findByToken("valid-uuid-token")).thenReturn(Optional.of(validToken));
        when(tokenRepository.save(any())).thenReturn(validToken);
        when(userService.getUserById(1L)).thenReturn(testUser);

        User result = passwordResetService.resetPassword("valid-uuid-token", "newSecurePass");

        verify(userService).updatePassword(1L, "newSecurePass");
        verify(tokenRepository).save(argThat(t -> Boolean.TRUE.equals(t.getUsed())));
        assertNotNull(result);
        assertEquals("testuser", result.getUsername());
    }

    @Test
    @DisplayName("✅ isTokenValid - token válido retorna true")
    void shouldReturnTrueForValidToken() {
        when(tokenRepository.findByToken("valid-uuid-token")).thenReturn(Optional.of(validToken));

        assertTrue(passwordResetService.isTokenValid("valid-uuid-token"));
    }

    @Test
    @DisplayName("❌ isTokenValid - token usado retorna false")
    void shouldReturnFalseForUsedToken() {
        when(tokenRepository.findByToken("used-token")).thenReturn(Optional.of(usedToken));

        assertFalse(passwordResetService.isTokenValid("used-token"));
    }

    @Test
    @DisplayName("❌ isTokenValid - token expirado retorna false")
    void shouldReturnFalseForExpiredToken() {
        when(tokenRepository.findByToken("expired-token")).thenReturn(Optional.of(expiredToken));

        assertFalse(passwordResetService.isTokenValid("expired-token"));
    }

    @Test
    @DisplayName("❌ isTokenValid - token inexistente retorna false")
    void shouldReturnFalseForNonExistentToken() {
        when(tokenRepository.findByToken("ghost-token")).thenReturn(Optional.empty());

        assertFalse(passwordResetService.isTokenValid("ghost-token"));
    }

    @Test
    @DisplayName("🧪 PasswordResetToken.isExpired - fecha pasada true, futura false")
    void tokenIsExpiredWhenDateIsInPast() {
        assertTrue(expiredToken.isExpired());
        assertFalse(validToken.isExpired());
    }

    @Test
    @DisplayName("🧪 PasswordResetToken.isUsed - used=true retorna true")
    void tokenIsUsedWhenFlagIsTrue() {
        assertTrue(usedToken.isUsed());
        assertFalse(validToken.isUsed());
    }

    @Test
    @DisplayName("🧪 PasswordResetToken.isUsed - used=null retorna false")
    void tokenIsNotUsedWhenFlagIsNull() {
        PasswordResetToken nullUsed = PasswordResetToken.builder()
            .token("t").userId(1L).expiryDate(LocalDateTime.now().plusHours(1)).used(null).build();
        assertFalse(nullUsed.isUsed());
    }
}
