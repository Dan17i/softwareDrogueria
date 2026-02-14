package com.drogueria.bellavista.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
/**
 * <h2>Entidad de Dominio - User</h2>
 *
 * <p>
 * Representa un usuario autenticado dentro del sistema.
 * Esta entidad pertenece al núcleo del dominio y es independiente
 * de cualquier implementación de persistencia o framework de seguridad.
 * </p>
 *
 * <p>
 * Un usuario posee un rol que determina sus permisos y nivel
 * de acceso dentro del sistema.
 * </p>
 *
 * <p>
 * Incluye información básica de identificación, estado
 * y control de auditoría.
 * </p>
 * @author Daniel Jurado & equipo de desarrollo
 * @since 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    /**
     * Identificador único del usuario.
     */
    private Long id;
    /**
     * Nombre de usuario utilizado para autenticación.
     */
    private String username;
    /**
     * Correo electrónico del usuario.
     */
    private String email;
    /**
     * Contraseña del usuario.
     *
     * <p>
     * Debe almacenarse de forma encriptada o hasheada.
     * Nunca debe guardarse en texto plano.
     * </p>
     */
    private String password; // Should be hashed
    /**
     * Nombre del usuario.
     */
    private String firstName;
    /**
     * Apellido del usuario.
     */
    private String lastName;
    /**
     * Rol asignado al usuario.
     * Define los permisos dentro del sistema.
     */
    private Role role;
    /**
     * Indica si el usuario se encuentra activo.
     */
    private Boolean active;
    /**
     * Fecha y hora de creación del registro.
     */
    private LocalDateTime createdAt;
    /**
     * Fecha y hora de la última actualización.
     */
    private LocalDateTime updatedAt;
    /**
     * Fecha y hora del último inicio de sesión.
     */
    private LocalDateTime lastLogin;
    /**
     * Obtiene el nombre completo del usuario.
     *
     * @return nombre y apellido concatenados.
     */
    public String getFullName() {
        return firstName + " " + lastName;
    }
    /**
     * Verifica si el usuario está activo.
     *
     * @return {@code true} si el usuario está activo,
     *         {@code false} en caso contrario.
     */
    public boolean isActive() {
        return Boolean.TRUE.equals(active);
    }
}
