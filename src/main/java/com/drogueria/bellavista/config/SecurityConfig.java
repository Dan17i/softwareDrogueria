package com.drogueria.bellavista.config;

import com.drogueria.bellavista.application.service.AuthService;
import com.drogueria.bellavista.infrastructure.security.JwtUtils;
import com.drogueria.bellavista.infrastructure.security.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Collections;
/**
 * <h2>SecurityConfig</h2>
 *
 * <p>
 * Clase de configuración principal de seguridad de la aplicación.
 * Define la política de autenticación y autorización basada en JWT
 * utilizando Spring Security.
 * </p>
 *
 * <p>
 * Características principales:
 * </p>
 * <ul>
 *     <li>Autenticación basada en JSON Web Token (JWT).</li>
 *     <li>Sesiones sin estado (STATELESS).</li>
 *     <li>Configuración de CORS.</li>
 *     <li>Protección de endpoints mediante autorización declarativa.</li>
 *     <li>Habilitación de seguridad a nivel de métodos (@PreAuthorize).</li>
 * </ul>
 *
 * @since 1.0
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {
    /**
     * Servicio de autenticación del dominio de aplicación.
     */
    private final AuthService authService;
    /**
     * Utilidad para generación y validación de JWT.
     */
    private final JwtUtils jwtUtils;
    /**
     * Orígenes permitidos para CORS.
     * Configurable mediante propiedad:
     * app.cors.allowed-origins
     */
    @Value("${app.cors.allowed-origins:http://localhost:5173}")
    private String allowedOrigins;
    /**
     * Constructor con inyección de dependencias.
     *
     * @param authService servicio de autenticación
     * @param jwtUtils utilidades JWT
     */
    public SecurityConfig(AuthService authService, JwtUtils jwtUtils) {
        this.authService = authService;
        this.jwtUtils = jwtUtils;
    }
    /**
     * Define el filtro personalizado de autenticación JWT.
     *
     * <p>
     * Este filtro intercepta las solicitudes HTTP, valida el token
     * JWT presente en el header Authorization y establece el contexto
     * de seguridad si el token es válido.
     * </p>
     *
     * @return instancia del filtro JWT
     */
    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(jwtUtils, authService);
    }
    /**
     * Configuración principal del filtro de seguridad.
     *
     * <p>
     * Configura:
     * </p>
     * <ul>
     *     <li>CORS con orígenes permitidos.</li>
     *     <li>Desactivación de CSRF (adecuado para APIs REST stateless).</li>
     *     <li>Política de sesión sin estado (JWT).</li>
     *     <li>Rutas públicas y protegidas.</li>
     *     <li>Registro del filtro JWT antes del filtro estándar
     *         UsernamePasswordAuthenticationFilter.</li>
     * </ul>
     *
     * @param http objeto de configuración HttpSecurity
     * @return cadena de filtros de seguridad configurada
     * @throws Exception en caso de error de configuración
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // Configuración CORS
                .cors(cors -> cors.configurationSource(request -> {
                    var config = new org.springframework.web.cors.CorsConfiguration();
                    config.setAllowedOrigins(Collections.singletonList(allowedOrigins));
                    config.setAllowedMethods(Collections.singletonList("*"));
                    config.setAllowedHeaders(Collections.singletonList("*"));
                    config.setAllowCredentials(true);
                    return config;
                }))
                // API REST sin CSRF
                .csrf(csrf -> csrf.disable())
                // Política sin sesión (JWT)
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // Configuración de autorización
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**", "/h2-console/**", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
                        .anyRequest().authenticated()
                )
                // Registro del filtro JWT antes del filtro estándar
                .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                // Habilita autenticación HTTP Basic (opcional)
                .httpBasic(Customizer.withDefaults());


        return http.build();
    }
}