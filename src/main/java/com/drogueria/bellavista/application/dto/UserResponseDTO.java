package com.drogueria.bellavista.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * DTO de respuesta para la información de usuario.
 *
 * <p>Este objeto se utiliza para transferir datos del usuario desde la capa
 * de aplicación hacia la capa de presentación (controladores REST).</p>
 *
 * <p>Contiene únicamente la información pública del usuario necesaria para
 * las respuestas del sistema, evitando exponer datos sensibles como la
 * contraseña.</p>
 *
 * <p>Se usa comúnmente en respuestas de endpoints de:
 * <ul>
 *     <li>Consulta de usuarios</li>
 *     <li>Autenticación</li>
 *     <li>Gestión de perfil</li>
 * </ul>
 * </p>
 *
 * @author Droguería Bellavista Team
 * @version 1.0
 * @since 2026
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponseDTO {
    /**
     * Identificador único del usuario en la base de datos.
     */
    private Long id;
    /**
     * Nombre de usuario utilizado para autenticación en el sistema.
     */
    private String username;
    /**
     * Correo electrónico del usuario.
     */
    private String email;
    /**
     * Nombre del usuario.
     */
    private String firstName;
    /**
     * Apellido del usuario.
     */
    private String lastName;
    /**
     * Rol asignado al usuario dentro del sistema
     * (por ejemplo: ADMIN, USER, MANAGER, etc.).
     */
    private String role;
}
