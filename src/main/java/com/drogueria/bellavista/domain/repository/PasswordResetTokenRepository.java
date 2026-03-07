package com.drogueria.bellavista.domain.repository;

import com.drogueria.bellavista.domain.model.PasswordResetToken;

import java.util.Optional;

/**
 * Repository interface for password reset token operations.
 */
public interface PasswordResetTokenRepository {
    PasswordResetToken save(PasswordResetToken token);
    Optional<PasswordResetToken> findByToken(String token);
    void deleteByUserId(Long userId);
}
