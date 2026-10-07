package com.drogueria.bellavista.integration;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@org.springframework.context.annotation.Import(com.drogueria.bellavista.config.TestMailConfig.class)
@DisplayName("OpenAPI / Swagger UI con app.docs.public=true")
class OpenApiDocsIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine")
            .withDatabaseName("openapi_test")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void props(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
        registry.add("app.jwt.secret", () -> "test-secret-key-with-at-least-32-characters-for-testing");
        registry.add("app.docs.public", () -> "true");
    }

    @LocalServerPort
    int port;

    private HttpResponse<String> get(String path) throws Exception {
        HttpRequest request = HttpRequest.newBuilder(URI.create("http://localhost:" + port + "/api" + path)).GET().build();
        return HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
    }

    @Test
    @DisplayName("api-docs es público e incluye título y esquema JWT bearerAuth")
    void apiDocsShouldDescribeApiAndJwtScheme() throws Exception {
        HttpResponse<String> response = get("/v3/api-docs");

        assertThat(response.statusCode()).isEqualTo(200);
        assertThat(response.body())
                .contains("Droguería Bellavista API")
                .contains("bearerAuth")
                .contains("\"/auth/login\"")
                .contains("\"/products\"");
    }

    @Test
    @DisplayName("Swagger UI es accesible sin autenticación")
    void swaggerUiShouldBePublic() throws Exception {
        assertThat(get("/swagger-ui/index.html").statusCode()).isEqualTo(200);
    }

    @Test
    @DisplayName("los endpoints de negocio siguen protegidos")
    void businessEndpointsShouldStayProtected() throws Exception {
        assertThat(get("/products").statusCode()).isEqualTo(401);
    }
}
