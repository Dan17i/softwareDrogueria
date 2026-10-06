package com.drogueria.bellavista.integration;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Guarda contra deriva: el esquema creado SOLO por Flyway debe pasar la validación de Hibernate
 * (ddl-auto=validate). Si alguien cambia una entidad sin añadir su migración V{n}, este test falla.
 */
@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@org.springframework.context.annotation.Import(com.drogueria.bellavista.config.TestMailConfig.class)
@DisplayName("Esquema Flyway == entidades JPA")
class SchemaMatchesEntitiesTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine")
            .withDatabaseName("schema_test")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void props(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.flyway.enabled", () -> "true");
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "validate");
        registry.add("app.jwt.secret", () -> "test-secret-key-with-at-least-32-characters-for-testing");
    }

    @Autowired
    private ApplicationContext context;

    @Test
    @DisplayName("el contexto arranca con ddl-auto=validate sobre el esquema de Flyway")
    void contextLoadsWithValidate() {
        assertThat(context).isNotNull();
    }
}
