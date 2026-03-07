package com.drogueria.bellavista.domain.service;

import com.drogueria.bellavista.domain.model.PasswordResetToken;
import com.drogueria.bellavista.domain.model.User;
import com.drogueria.bellavista.domain.repository.PasswordResetTokenRepository;
import com.drogueria.bellavista.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Service for password reset operations.
 * Handles token generation, validation, and password reset flow.
 * 
 * MÉTRICAS DE CALIDAD:
 * - Métrica 4.2: Seguridad en recuperación de contraseña
 * - Métrica 2.2: Mensajes claros y específicos
 */
@Service
@Transactional
@RequiredArgsConstructor
public class PasswordResetService {
    
    private final PasswordResetTokenRepository tokenRepository;
    private final UserService userService;
    
    private static final int TOKEN_EXPIRY_HOURS = 1;
    
    /**
     * Create password reset token for user.
     * Deletes any existing tokens for the user first.
     */
    public PasswordResetToken createPasswordResetToken(String email) {
        User user = userService.getUserByEmail(email);
        
        // Delete any existing tokens for this user
        tokenRepository.deleteByUserId(user.getId());
        
        // Generate new token
        String token = UUID.randomUUID().toString();
        LocalDateTime expiryDate = LocalDateTime.now().plusHours(TOKEN_EXPIRY_HOURS);
        
        PasswordResetToken resetToken = PasswordResetToken.builder()
            .token(token)
            .userId(user.getId())
            .expiryDate(expiryDate)
            .used(false)
            .createdAt(LocalDateTime.now())
            .build();
        
        return tokenRepository.save(resetToken);
    }
    
    /**
     * Validate and use password reset token.
     * Métrica 2.2: Validación con mensajes claros
     */
    public void resetPassword(String token, String newPassword) {
        if (token == null || token.trim().isEmpty()) {
            throw new BusinessException("El token de recuperación es obligatorio");
        }
        
        PasswordResetToken resetToken = tokenRepository.findByToken(token)
            .orElseThrow(() -> new BusinessException("Token de recuperación inválido o expirado"));
        
        // Validate token
        if (resetToken.isUsed()) {
            throw new BusinessException("Este token ya ha sido utilizado");
        }
        
        if (resetToken.isExpired()) {
            throw new BusinessException("El token ha expirado. Por favor solicita uno nuevo");
        }
        
        // Update password
        userService.updatePassword(resetToken.getUserId(), newPassword);
        
        // Mark token as used
        resetToken.setUsed(true);
        tokenRepository.save(resetToken);
    }
    
    /**
     * Validate if token is valid (not used and not expired).
     */
    public boolean isTokenValid(String token) {
        return tokenRepository.findByToken(token)
            .map(t -> !t.isUsed() && !t.isExpired())
            .orElse(false);
    }
}
