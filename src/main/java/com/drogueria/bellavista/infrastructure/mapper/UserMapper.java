package com.drogueria.bellavista.infrastructure.mapper;

import com.drogueria.bellavista.domain.model.User;
import com.drogueria.bellavista.infrastructure.persistence.UserEntity;
import org.springframework.stereotype.Component;

/**
 * Mapper for converting between User domain model and UserEntity JPA entity.
 */
@Component
public class UserMapper {

    public User toDomain(UserEntity entity) {
        if (entity == null) {
            return null;
        }
        return User.builder()
            .id(entity.getId())
            .username(entity.getUsername())
            .email(entity.getEmail())
            .password(entity.getPassword())
            .firstName(entity.getFirstName())
            .lastName(entity.getLastName())
            .role(entity.getRole())
            .active(entity.getActive())
            .createdAt(entity.getCreatedAt())
            .updatedAt(entity.getUpdatedAt())
            .lastLogin(entity.getLastLogin())
            .build();
    }

    public UserEntity toEntity(User domain) {
        if (domain == null) {
            return null;
        }
        return UserEntity.builder()
            .id(domain.getId())
            .username(domain.getUsername())
            .email(domain.getEmail())
            .password(domain.getPassword())
            .firstName(domain.getFirstName())
            .lastName(domain.getLastName())
            .role(domain.getRole())
            .active(domain.getActive())
            .createdAt(domain.getCreatedAt())
            .updatedAt(domain.getUpdatedAt())
            .lastLogin(domain.getLastLogin())
            .build();
    }
}
