package com.drogueria.bellavista.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * DTO de respuesta de autenticación.
 * <p>
 * Se devuelve al cliente después de un login exitoso y contiene
 * la información necesaria para autenticarse en las siguientes
 * peticiones al sistema.
 *
 * <p><b>Responsabilidad:</b>
 * Transportar el token JWT generado y datos básicos del usuario
 * autenticado hacia la capa de presentación (API REST).
 *
 * <p><b>Uso típico:</b>
 * Es retornado por el endpoint de login del módulo de autenticación.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthResponseDTO {
    /**
     * Token JWT generado por el sistema para el usuario autenticado.
     * Debe enviarse en el header Authorization de las siguientes peticiones.
     */
    private String token;
    /**
     * Tipo de token utilizado en el esquema de autorización HTTP.
     * Por defecto es "Bearer".
     */
    @Builder.Default
    private String tokenType = "Bearer";
    /**
     * Identificador único del usuario autenticado.
     */
    private Long userId;
    /**
     * Nombre de usuario utilizado para iniciar sesión.
     */
    private String username;
    /**
     * Rol del usuario dentro del sistema (ej. ADMIN, USER).
     */
    private String role;
}
