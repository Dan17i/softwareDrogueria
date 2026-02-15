package com.drogueria.bellavista.exception;
/**
 * <h2>AuthenticationException</h2>
 *
 * <p>
 * Excepción personalizada lanzada cuando ocurre un error
 * durante el proceso de autenticación.
 * </p>
 *
 * <p>
 * Generalmente se produce cuando:
 * </p>
 * <ul>
 *     <li>Las credenciales proporcionadas son inválidas.</li>
 *     <li>El usuario no existe.</li>
 *     <li>La contraseña no coincide.</li>
 *     <li>El token de autenticación es inválido o ha expirado.</li>
 * </ul>
 *
 * <p>
 * Extiende de {@link RuntimeException}, por lo que es una
 * excepción no verificada (unchecked). Esto permite que
 * sea manejada de forma centralizada por el
 * {@code GlobalExceptionHandler}.
 * </p>
 * @author Daniel Jurado & equipo de desarrollo
 * @since 1.0
 */
public class AuthenticationException extends RuntimeException {
    /**
     * Constructor que recibe un mensaje descriptivo del error.
     *
     * @param message descripción del motivo de la falla de autenticación
     */
    public AuthenticationException(String message) {
        super(message);
    }
    /**
     * Constructor que recibe un mensaje y la causa raíz del error.
     *
     * @param message descripción del error
     * @param cause excepción original que provocó el fallo
     */
    public AuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }
}
