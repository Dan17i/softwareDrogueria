package com.drogueria.bellavista.application.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * DTO utilizado para transportar las credenciales de autenticación
 * enviadas por el cliente durante el proceso de login.
 *
 * Este objeto pertenece a la capa Application y funciona como contrato
 * de entrada para los endpoints de autenticación.
 *
 * Consideraciones de seguridad:
 * - No debe serializarse en logs
 * - No debe almacenarse en caché
 * - El password siempre debe enviarse por HTTPS
 * - No contiene información del usuario autenticado (solo credenciales)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginRequestDTO {
    /**
     * Nombre de usuario utilizado para autenticación.
     *
     * Reglas:
     * - Obligatorio
     * - Longitud mínima: 3
     * - Longitud máxima: 50
     *
     * Puede representar:
     * - username tradicional
     * - email (si el sistema lo permite)
     * - código de usuario interno
     */
    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    private String username;
    /**
     * Contraseña del usuario.
     *
     * Reglas:
     * - Obligatoria
     * - Longitud mínima: 8 caracteres
     *
     * Notas de seguridad:
     * - Nunca se almacena en texto plano
     * - Se compara contra un hash en el backend
     * - No debe devolverse en responses
     */
    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters")
    private String password;
}
