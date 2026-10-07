package com.drogueria.bellavista.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Metadatos de la documentación OpenAPI y esquema de seguridad JWT (botón "Authorize" de Swagger UI).
 */
@Configuration
public class OpenApiConfig {

    static final String BEARER_SCHEME = "bearerAuth";

    @Bean
    public OpenAPI drogueriaOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Droguería Bellavista API")
                        .description("""
                                API REST para la gestión de una droguería: inventario, clientes, proveedores, \
                                órdenes, recepción de mercancía, pagos con Stripe y notificaciones.

                                **Autenticación:** obtén un token en `POST /auth/login` y pulsa **Authorize** \
                                (pega solo el token, sin el prefijo `Bearer`).""")
                        .version("v1")
                        .contact(new Contact().name("Daniel Jurado").url("https://github.com/Dan17i")))
                .components(new Components().addSecuritySchemes(BEARER_SCHEME, new SecurityScheme()
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT")))
                .addSecurityItem(new SecurityRequirement().addList(BEARER_SCHEME));
    }
}
