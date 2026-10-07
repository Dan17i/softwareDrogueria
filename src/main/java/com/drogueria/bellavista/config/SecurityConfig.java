package com.drogueria.bellavista.config;

import com.drogueria.bellavista.application.service.AuthService;
import com.drogueria.bellavista.infrastructure.security.JwtUtils;
import com.drogueria.bellavista.infrastructure.security.JwtAuthenticationFilter;
import com.drogueria.bellavista.infrastructure.security.RateLimitFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Arrays;
import java.util.Map;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private final AuthService authService;
    private final JwtUtils jwtUtils;
    private final Environment environment;

    public SecurityConfig(AuthService authService, JwtUtils jwtUtils, Environment environment) {
        this.authService = authService;
        this.jwtUtils = jwtUtils;
        this.environment = environment;
    }

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(Arrays.asList("https://inventoryrs.online","https://invetoryrx.onrender.com","http://localhost:5173"));
        config.setAllowedMethods(Arrays.asList("GET","POST","PUT","DELETE","PATCH","OPTIONS"));
        config.setAllowedHeaders(Arrays.asList("Authorization","Content-Type","Accept","X-Requested-With","ngrok-skip-browser-warning"));
        config.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(jwtUtils, authService);
    }

    /**
     * Límites por IP en endpoints públicos de autenticación. Configurable con app.rate-limit.*
     * (login: 10/min, forgot-password: 5/15min, register y reset-password: 10/15min por defecto).
     */
    private RateLimitFilter rateLimitFilter() {
        long minute = 60_000L;
        int loginMax = environment.getProperty("app.rate-limit.login-max", Integer.class, 10);
        int forgotMax = environment.getProperty("app.rate-limit.forgot-password-max", Integer.class, 5);
        int otherMax = environment.getProperty("app.rate-limit.register-max", Integer.class, 10);
        return new RateLimitFilter(Map.of(
                "/auth/login", new RateLimitFilter.Rule(loginMax, minute),
                "/auth/forgot-password", new RateLimitFilter.Rule(forgotMax, 15 * minute),
                "/auth/register", new RateLimitFilter.Rule(otherMax, 15 * minute),
                "/auth/reset-password", new RateLimitFilter.Rule(otherMax, 15 * minute)));
    }

    /** Acceso libre si {@code allowed}; en caso contrario solo usuarios con ROLE_ADMIN. */
    private static AuthorizationManager<RequestAuthorizationContext> adminOrAllowed(boolean allowed) {
        return (authentication, context) -> new AuthorizationDecision(
                allowed || authentication.get().getAuthorities().stream()
                        .anyMatch(a -> "ROLE_ADMIN".equals(a.getAuthority())));
    }

    @Bean
    @SuppressWarnings("java:S4502") // Safe: stateless JWT API — no session cookies, CSRF vector does not apply
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        boolean rateLimitEnabled = environment.getProperty("app.rate-limit.enabled", Boolean.class, true);
        boolean dev = environment.acceptsProfiles(Profiles.of("dev", "test"));
        // Swagger UI: abierto en dev/test o si app.docs.public=true (demos); si no, solo ADMIN
        boolean docsPublic = dev || environment.getProperty("app.docs.public", Boolean.class, false);
        http
                .cors(Customizer.withDefaults())
                // CSRF disabled: API is stateless (SessionCreationPolicy.STATELESS) and authenticates via Bearer JWT tokens,
                // not browser session cookies. No CSRF attack vector exists in this configuration.
                .csrf(csrf -> csrf.disable())

                // frameOptions off solo para la consola H2 (dev/test)
                .headers(headers -> {
                    if (dev) {
                        headers.frameOptions(frame -> frame.disable());
                    }
                })

                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // Sin httpBasic en prod, Spring respondería 403; el cliente espera 401 para no autenticados
                .exceptionHandling(ex -> ex.authenticationEntryPoint(
                        new org.springframework.security.web.authentication.HttpStatusEntryPoint(org.springframework.http.HttpStatus.UNAUTHORIZED)))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(org.springframework.http.HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers("/auth/register", "/auth/login", "/auth/forgot-password",
                                "/auth/reset-password", "/auth/dev-create-admin").permitAll()
                        .requestMatchers("/auth/admin/**").hasRole("ADMIN")
                        .requestMatchers("/actuator/health/**", "/actuator/info").permitAll()
                        .requestMatchers("/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**")
                                .access(adminOrAllowed(docsPublic))
                        .requestMatchers("/h2-console/**", "/actuator/**")
                                .access(adminOrAllowed(dev))
                        // Sin permitAll para /orders/**, /customers/** ni /api/notifications/**:
                        // requieren autenticación + el @PreAuthorize por rol de cada endpoint.
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                ;
        if (rateLimitEnabled) {
            http.addFilterBefore(rateLimitFilter(), JwtAuthenticationFilter.class);
        }
        if (dev) {
            http.httpBasic(Customizer.withDefaults());
        }

        return http.build();
    }
}
