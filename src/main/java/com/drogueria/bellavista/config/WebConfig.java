package com.drogueria.bellavista.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
/**
 * <h2>WebConfig</h2>
 *
 * <p>
 * Clase de configuración web encargada de definir las políticas
 * de CORS (Cross-Origin Resource Sharing) para la aplicación.
 * </p>
 *
 * <p>
 * Permite que el frontend (por ejemplo, una aplicación SPA en React,
 * Angular o Vue) pueda realizar peticiones HTTP al backend cuando
 * se ejecutan en dominios o puertos distintos.
 * </p>
 *
 * <p>
 * Esta configuración aplica únicamente a las rutas que comienzan
 * con <code>/api/**</code>.
 * </p>
 *
 * <p>
 * ⚠ En entornos de producción se recomienda restringir
 * <code>allowedOrigins</code> a dominios específicos en lugar de "*"
 * por motivos de seguridad.
 * </p>
 *
 * @since 1.0
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {
    /**
     * Configura los mapeos CORS para la aplicación.
     *
     * <p>
     * Configuración actual:
     * </p>
     * <ul>
     *     <li>Aplica a rutas <code>/api/**</code>.</li>
     *     <li>Permite cualquier origen ("*").</li>
     *     <li>Permite métodos HTTP: GET, POST, PUT, DELETE, PATCH, OPTIONS.</li>
     *     <li>Permite cualquier encabezado.</li>
     *     <li>Define un tiempo máximo de caché de preflight de 3600 segundos.</li>
     * </ul>
     *
     * @param registry registro de configuraciones CORS
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS")
                .allowedHeaders("*")
                .maxAge(3600);
    }
}
