package com.drogueria.bellavista.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Domain model for password reset tokens.
 * Tokens expire after a configurable time period (default: 1 hour).
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PasswordResetToken {
    private Long id;
    private String token;
    private Long userId;
    private LocalDateTime expiryDate;
    private Boolean used;
    private LocalDateTime createdAt;

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expiryDate);
    }

    public boolean isUsed() {
        return Boolean.TRUE.equals(used);
    }
}
