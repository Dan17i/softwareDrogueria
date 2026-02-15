package com.drogueria.bellavista.exception;
/**
 * <h2>BusinessException</h2>
 *
 * <p>
 * Excepción personalizada utilizada para representar
 * errores relacionados con las reglas de negocio del sistema.
 * </p>
 *
 * <p>
 * Se lanza cuando una operación no puede completarse debido
 * a una violación de una regla funcional o restricción del dominio,
 * por ejemplo:
 * </p>
 * <ul>
 *     <li>Intentar registrar un cliente con un código duplicado.</li>
 *     <li>Confirmar una orden en estado inválido.</li>
 *     <li>Procesar una recepción con cantidades incorrectas.</li>
 * </ul>
 *
 * <p>
 * Extiende de {@link RuntimeException}, por lo que es una
 * excepción no verificada (unchecked). Esto permite que sea
 * capturada y transformada en una respuesta HTTP adecuada
 * por el manejador global de excepciones.
 * </p>
 * @author Daniel Jurado & equipo de desarrollo
 * @since 1.0
 */
public class BusinessException extends RuntimeException {
    /**
     * Constructor que recibe un mensaje descriptivo del error de negocio.
     *
     * @param message descripción del error funcional
     */
    public BusinessException(String message) {
        super(message);
    }
    /**
     * Constructor que recibe un mensaje y la causa raíz del error.
     *
     * @param message descripción del error
     * @param cause excepción original que provocó el problema
     */
    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }
}
