package com.drogueria.bellavista.config;

import com.drogueria.bellavista.exception.BusinessException;
import com.drogueria.bellavista.exception.ResourceNotFoundException;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
/**
 * <h2>GlobalExceptionHandler</h2>
 *
 * <p>
 * Manejador global de excepciones para la aplicación.
 * Centraliza la gestión de errores lanzados en los controladores REST
 * y transforma las excepciones en respuestas HTTP estructuradas.
 * </p>
 *
 * <p>
 * Utiliza la anotación {@link RestControllerAdvice} para interceptar
 * excepciones de manera transversal en toda la aplicación.
 * </p>
 *
 * <p>
 * Su objetivo es:
 * </p>
 * <ul>
 *     <li>Estandarizar el formato de respuesta de error.</li>
 *     <li>Evitar exponer detalles sensibles en producción.</li>
 *     <li>Separar la lógica de manejo de errores de los controladores.</li>
 * </ul>
 *
 * @since 1.0
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    /**
     * <h3>ErrorResponse</h3>
     *
     * <p>
     * Clase interna utilizada para estructurar las respuestas de error
     * enviadas al cliente.
     * </p>
     *
     * <p>
     * Incluye información relevante como:
     * </p>
     * <ul>
     *     <li>Marca de tiempo del error.</li>
     *     <li>Código de estado HTTP.</li>
     *     <li>Tipo o categoría del error.</li>
     *     <li>Mensaje descriptivo.</li>
     *     <li>Detalles adicionales (por ejemplo, errores de validación).</li>
     * </ul>
     */
    @Data
    @AllArgsConstructor
    public static class ErrorResponse {
        /**
         * Fecha y hora en que ocurrió el error.
         */
        private LocalDateTime timestamp;
        /**
         * Código de estado HTTP.
         */
        private int status;
        /**
         * Tipo o nombre del error.
         */
        private String error;
        /**
         * Mensaje descriptivo del error.
         */
        private String message;
        /**
         * Detalles adicionales (por ejemplo, errores de validación por campo).
         */
        private Map<String, String> details;
        /**
         * Constructor simplificado para errores sin detalles adicionales.
         *
         * @param status código HTTP
         * @param error tipo de error
         * @param message mensaje descriptivo
         */
        public ErrorResponse(int status, String error, String message) {
            this.timestamp = LocalDateTime.now();
            this.status = status;
            this.error = error;
            this.message = message;
            this.details = null;
        }
    }
    /**
     * Maneja excepciones relacionadas con reglas de negocio.
     *
     * @param ex excepción de negocio
     * @return respuesta HTTP 400 con mensaje descriptivo
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFound(ResourceNotFoundException ex) {
        ErrorResponse error = new ErrorResponse(
            HttpStatus.NOT_FOUND.value(),
            "Not Found",
            ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }
    /**
     * Maneja errores de argumentos inválidos.
     *
     * @param ex excepción lanzada
     * @return respuesta HTTP 400
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException ex) {
        ErrorResponse error = new ErrorResponse(
            HttpStatus.BAD_REQUEST.value(),
            "Business Error",
            ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }
    /**
     * Maneja estados ilegales del sistema o flujo de negocio.
     *
     * @param ex excepción lanzada
     * @return respuesta HTTP 409 (conflicto)
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalArgumentException ex) {
        ErrorResponse error = new ErrorResponse(
            HttpStatus.BAD_REQUEST.value(),
            "Invalid Argument",
            ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }
    /**
     * Maneja errores de validación generados por anotaciones
     * como {@code @Valid}.
     *
     * @param ex excepción de validación
     * @return respuesta HTTP 400 con detalle de errores por campo
     */
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErrorResponse> handleIllegalState(IllegalStateException ex) {
        ErrorResponse error = new ErrorResponse(
            HttpStatus.CONFLICT.value(),
            "Illegal State",
            ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }
    /**
     * Maneja errores de validación generados por anotaciones
     * como {@code @Valid}.
     *
     * @param ex excepción de validación
     * @return respuesta HTTP 400 con detalle de errores por campo
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationErrors(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        
        ErrorResponse errorResponse = new ErrorResponse(
            HttpStatus.BAD_REQUEST.value(),
            "Validation Error",
            "Error de validación en los datos enviados"
        );
        errorResponse.setDetails(errors);
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }
    /**
     * Maneja cualquier excepción no controlada explícitamente.
     *
     * <p>
     * Devuelve un error 500 sin exponer detalles internos del sistema.
     * Esto es importante para evitar filtración de información sensible.
     * </p>
     *
     * @param ex excepción genérica
     * @return respuesta HTTP 500
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        ErrorResponse error = new ErrorResponse(
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            "Internal Server Error",
            "Ha ocurrido un error interno en el servidor"
        );
        
        // En producción, no mostrar detalles del error
        // error.setMessage(ex.getMessage());
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}
