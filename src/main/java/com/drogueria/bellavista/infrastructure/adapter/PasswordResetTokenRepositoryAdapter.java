package com.drogueria.bellavista.infrastructure.adapter;

import com.drogueria.bellavista.domain.model.PasswordResetToken;
import com.drogueria.bellavista.domain.repository.PasswordResetTokenRepository;
import com.drogueria.bellavista.infrastructure.persistence.JpaPasswordResetTokenRepository;
import com.drogueria.bellavista.infrastructure.persistence.PasswordResetTokenEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class PasswordResetTokenRepositoryAdapter implements PasswordResetTokenRepository {
    
    private final JpaPasswordResetTokenRepository jpaRepository;
    
    @Override
    @Transactional
    public PasswordResetToken save(PasswordResetToken token) {
        PasswordResetTokenEntity entity = toEntity(token);
        PasswordResetTokenEntity saved = jpaRepository.save(entity);
        return toDomain(saved);
    }
    
    @Override
    public Optional<PasswordResetToken> findByToken(String token) {
        return jpaRepository.findByToken(token)
            .map(this::toDomain);
    }
    
    @Override
    @Transactional
    public void deleteByUserId(Long userId) {
        jpaRepository.deleteByUserId(userId);
    }
    
    private PasswordResetTokenEntity toEntity(PasswordResetToken domain) {
        return PasswordResetTokenEntity.builder()
            .id(domain.getId())
            .token(domain.getToken())
            .userId(domain.getUserId())
            .expiryDate(domain.getExpiryDate())
            .used(domain.getUsed())
            .createdAt(domain.getCreatedAt())
            .build();
    }
    
    private PasswordResetToken toDomain(PasswordResetTokenEntity entity) {
        return PasswordResetToken.builder()
            .id(entity.getId())
            .token(entity.getToken())
            .userId(entity.getUserId())
            .expiryDate(entity.getExpiryDate())
            .used(entity.getUsed())
            .createdAt(entity.getCreatedAt())
            .build();
    }
}
