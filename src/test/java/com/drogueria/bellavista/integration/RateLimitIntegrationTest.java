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
@DisplayName("Rate limit en /auth/login")
class RateLimitIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine")
            .withDatabaseName("ratelimit_test")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void props(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
        registry.add("app.jwt.secret", () -> "test-secret-key-with-at-least-32-characters-for-testing");
        registry.add("app.rate-limit.enabled", () -> "true");
        registry.add("app.rate-limit.login-max", () -> "3");
    }

    @LocalServerPort
    int port;

    @Test
    @DisplayName("tras 3 intentos, el 4º recibe 429 con Retry-After")
    void shouldReturn429AfterTooManyLogins() throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder(URI.create("http://localhost:" + port + "/api/auth/login"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString("{\"username\":\"nadie\",\"password\":\"mala-clave-1\"}"))
                .build();

        for (int i = 0; i < 3; i++) {
            HttpResponse<String> r = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertThat(r.statusCode()).isNotEqualTo(429);
        }
        HttpResponse<String> blocked = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertThat(blocked.statusCode()).isEqualTo(429);
        assertThat(blocked.headers().firstValue("Retry-After")).isPresent();
        assertThat(blocked.body()).contains("Demasiados intentos");
    }
}
