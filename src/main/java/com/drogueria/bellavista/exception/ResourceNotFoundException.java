package com.drogueria.bellavista.exception;
/**
 * <h2>ResourceNotFoundException</h2>
 *
 * <p>
 * Excepción personalizada utilizada cuando un recurso solicitado
 * no existe o no puede ser encontrado en el sistema.
 * </p>
 *
 * <p>
 * Se emplea comúnmente en operaciones de consulta cuando:
 * </p>
 * <ul>
 *     <li>No existe un cliente con el ID indicado.</li>
 *     <li>No se encuentra una orden con el número proporcionado.</li>
 *     <li>Un producto o proveedor no está registrado.</li>
 * </ul>
 *
 * <p>
 * Extiende de {@link RuntimeException}, permitiendo que sea
 * manejada de forma centralizada por el manejador global
 * de excepciones y traducida a una respuesta HTTP 404 (Not Found).
 * </p>
 *
 * @since 1.0
 */
public class ResourceNotFoundException extends RuntimeException {
    /**
     * Constructor que recibe un mensaje descriptivo personalizado.
     *
     * @param message mensaje detallado del error
     */
    public ResourceNotFoundException(String message) {
        super(message);
    }
    /**
     * Constructor que construye automáticamente un mensaje
     * indicando el recurso, el campo y el valor utilizado
     * en la búsqueda.
     *
     * <p>
     * Ejemplo de mensaje generado:
     * "Cliente no encontrado con id: 10"
     * </p>
     *
     * @param resource nombre del recurso (ej. "Cliente")
     * @param field campo de búsqueda (ej. "id", "code")
     * @param value valor utilizado en la búsqueda
     */
    public ResourceNotFoundException(String resource, String field, Object value) {
        super(String.format("%s no encontrado con %s: %s", resource, field, value));
    }
}
