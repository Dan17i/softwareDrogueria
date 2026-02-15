package com.drogueria.bellavista.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
/**
 * <h2>PasswordEncoderConfig</h2>
 *
 * <p>
 * Clase de configuración encargada de definir el bean de
 * {@link PasswordEncoder} utilizado en la aplicación.
 * </p>
 *
 * <p>
 * Se utiliza {@link BCryptPasswordEncoder} como mecanismo de
 * encriptación de contraseñas, ya que implementa hashing seguro
 * con salt automático y es recomendado por Spring Security
 * para el almacenamiento de credenciales.
 * </p>
 *
 * <p>
 * Esta configuración permite que el encoder sea inyectado
 * en servicios de autenticación o registro de usuarios
 * mediante inyección de dependencias.
 * </p>
 * @author Daniel Jurado & equipo de desarrollo
 * @since 1.0
 */
@Configuration
public class PasswordEncoderConfig {
    /**
     * Define el bean {@link PasswordEncoder} que será utilizado
     * para codificar y verificar contraseñas en el sistema.
     *
     * <p>
     * BCrypt aplica un algoritmo de hashing fuerte que incluye:
     * </p>
     * <ul>
     *     <li>Salt automático.</li>
     *     <li>Protección contra ataques de diccionario.</li>
     *     <li>Configuración de nivel de complejidad (work factor).</li>
     * </ul>
     *
     * @return implementación segura de {@link PasswordEncoder}
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}