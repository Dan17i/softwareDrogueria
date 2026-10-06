package com.drogueria.bellavista.config;

import com.drogueria.bellavista.application.service.AuthService;
import com.drogueria.bellavista.infrastructure.security.JwtUtils;
import com.drogueria.bellavista.infrastructure.security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Arrays;
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

    @Bean
    @SuppressWarnings("java:S4502") // Safe: stateless JWT API — no session cookies, CSRF vector does not apply
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        boolean dev = environment.acceptsProfiles(Profiles.of("dev", "test"));
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
                        .requestMatchers("/h2-console/**", "/swagger-ui/**", "/v3/api-docs/**", "/actuator/**")
                                .access((authentication, ctx) -> new org.springframework.security.authorization.AuthorizationDecision(
                                        dev || authentication.get().getAuthorities().stream()
                                                .anyMatch(a -> "ROLE_ADMIN".equals(a.getAuthority()))))
                        // Sin permitAll para /orders/**, /customers/** ni /api/notifications/**:
                        // requieren autenticación + el @PreAuthorize por rol de cada endpoint.
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                ;
        if (dev) {
            http.httpBasic(Customizer.withDefaults());
        }

        return http.build();
    }
}
