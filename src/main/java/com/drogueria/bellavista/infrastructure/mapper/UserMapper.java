package com.drogueria.bellavista.infrastructure.mapper;

import com.drogueria.bellavista.domain.model.User;
import com.drogueria.bellavista.infrastructure.persistence.UserEntity;
import org.springframework.stereotype.Component;
/**
 * <h2>UserMapper</h2>
 *
 * <p>
 * Componente encargado de convertir datos entre:
 * </p>
 *
 * <ul>
 *     <li>{@link UserEntity} (capa de persistencia - JPA)</li>
 *     <li>{@link User} (modelo de dominio)</li>
 * </ul>
 *
 * <p>
 * Este mapper pertenece a la capa de infraestructura dentro de la
 * arquitectura hexagonal (Ports & Adapters), permitiendo que el modelo
 * de dominio permanezca completamente desacoplado de los detalles
 * técnicos de la base de datos.
 * </p>
 *
 * <p>
 * Su responsabilidad es exclusivamente la transformación de datos
 * entre ambas representaciones, sin incluir lógica de negocio,
 * respetando el principio de responsabilidad única (SRP).
 * </p>
 * @author Daniel Jurado & equipo de desarrollo
 * @since 1.0
 */
@Component
public class UserMapper {
    /**
     * Convierte una entidad JPA {@link UserEntity}
     * en un modelo de dominio {@link User}.
     *
     * <p>
     * Si la entidad recibida es {@code null}, retorna {@code null}
     * para evitar {@link NullPointerException}.
     * </p>
     *
     * @param entity entidad obtenida desde la base de datos
     * @return modelo de dominio equivalente o {@code null} si la entidad es nula
     */
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
    /**
     * Convierte un modelo de dominio {@link User}
     * en su entidad de persistencia {@link UserEntity}.
     *
     * <p>
     * Este método prepara el objeto para ser almacenado mediante
     * Spring Data JPA u otro mecanismo de persistencia.
     * </p>
     *
     * <p>
     * Si el objeto de dominio es {@code null}, retorna {@code null}.
     * </p>
     *
     * @param domain modelo de dominio a convertir
     * @return entidad JPA lista para persistencia o {@code null} si el dominio es nulo
     */
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
