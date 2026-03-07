package com.drogueria.bellavista.integration;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Security Integration Tests")
@org.springframework.context.annotation.Import(com.drogueria.bellavista.config.TestMailConfig.class)
public class SecurityIntegrationTest {

    @Container
    public static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void registerPgProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
        registry.add("app.jwt.secret", () -> "test-secret-key-with-at-least-32-characters-for-testing");
    }

    @LocalServerPort
    int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private static String validToken;

    private String baseUrl() {
        return "http://localhost:" + port + "/api";
    }

    // ============================================
    // REGISTRO
    // ============================================

    @Test
    @Order(1)
    @DisplayName("Debe registrar usuario exitosamente")
    void shouldRegisterUserSuccessfully() {
        Map<String, Object> reg = new HashMap<>();
        reg.put("username", "securityuser");
        reg.put("email", "securityuser@test.com");
        reg.put("password", "password123");
        reg.put("firstName", "Security");
        reg.put("lastName", "Tester");

        ResponseEntity<Map> response = restTemplate.postForEntity(
                baseUrl() + "/auth/register", reg, Map.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).containsKey("id");
        assertThat(response.getBody().get("username")).isEqualTo("securityuser");
    }

    @Test
    @Order(2)
    @DisplayName("No debe registrar usuario con username duplicado")
    void shouldNotRegisterDuplicateUsername() {
        Map<String, Object> reg = new HashMap<>();
        reg.put("username", "securityuser");
        reg.put("email", "otro@test.com");
        reg.put("password", "password123");
        reg.put("firstName", "Otro");
        reg.put("lastName", "Usuario");

        ResponseEntity<Map> response = restTemplate.postForEntity(
                baseUrl() + "/auth/register", reg, Map.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    @Order(3)
    @DisplayName("No debe registrar usuario con password corta")
    void shouldNotRegisterWithShortPassword() {
        Map<String, Object> reg = new HashMap<>();
        reg.put("username", "shortpass");
        reg.put("email", "shortpass@test.com");
        reg.put("password", "123");
        reg.put("firstName", "Short");
        reg.put("lastName", "Pass");

        ResponseEntity<Map> response = restTemplate.postForEntity(
                baseUrl() + "/auth/register", reg, Map.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    // ============================================
    // LOGIN
    // ============================================

    @Test
    @Order(4)
    @DisplayName("Debe hacer login exitosamente")
    void shouldLoginSuccessfully() {
        Map<String, Object> login = new HashMap<>();
        login.put("username", "securityuser");
        login.put("password", "password123");

        ResponseEntity<Map> response = restTemplate.postForEntity(
                baseUrl() + "/auth/login", login, Map.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).containsKey("token");
        assertThat(response.getBody().get("username")).isEqualTo("securityuser");

        validToken = (String) response.getBody().get("token");
    }

    @Test
    @Order(5)
    @DisplayName("No debe hacer login con password incorrecta")
    void shouldNotLoginWithWrongPassword() {
        Map<String, Object> login = new HashMap<>();
        login.put("username", "securityuser");
        login.put("password", "wrongpassword");

        ResponseEntity<Map> response = restTemplate.postForEntity(
                baseUrl() + "/auth/login", login, Map.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    @Order(6)
    @DisplayName("No debe hacer login con usuario inexistente")
    void shouldNotLoginWithNonExistentUser() {
        Map<String, Object> login = new HashMap<>();
        login.put("username", "noexiste");
        login.put("password", "password123");

        ResponseEntity<Map> response = restTemplate.postForEntity(
                baseUrl() + "/auth/login", login, Map.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    // ============================================
    // ACCESO A ENDPOINTS PROTEGIDOS
    // ============================================

    @Test
    @Order(7)
    @DisplayName("Debe acceder a endpoint protegido con token válido")
    void shouldAccessProtectedEndpointWithValidToken() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(validToken);

        HttpEntity<?> entity = new HttpEntity<>(headers);
        ResponseEntity<Object> response = restTemplate.exchange(
                baseUrl() + "/products", HttpMethod.GET, entity, Object.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @Order(8)
    @DisplayName("No debe acceder a endpoint protegido sin token")
    void shouldNotAccessProtectedEndpointWithoutToken() {
        ResponseEntity<Map> response = restTemplate.getForEntity(
                baseUrl() + "/products", Map.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    @Order(9)
    @DisplayName("No debe acceder a endpoint protegido con token inválido")
    void shouldNotAccessProtectedEndpointWithInvalidToken() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth("token.invalido.falso");

        HttpEntity<?> entity = new HttpEntity<>(headers);
        ResponseEntity<Map> response = restTemplate.exchange(
                baseUrl() + "/products", HttpMethod.GET, entity, Map.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    // ============================================
    // ENDPOINTS PÚBLICOS
    // ============================================

    @Test
    @Order(10)
    @DisplayName("Endpoint de registro debe ser público")
    void registerEndpointShouldBePublic() {
        Map<String, Object> reg = new HashMap<>();
        reg.put("username", "publictest");
        reg.put("email", "publictest@test.com");
        reg.put("password", "password123");
        reg.put("firstName", "Public");
        reg.put("lastName", "Test");

        ResponseEntity<Map> response = restTemplate.postForEntity(
                baseUrl() + "/auth/register", reg, Map.class);

        // No debe ser UNAUTHORIZED (puede ser OK o BAD_REQUEST)
        assertThat(response.getStatusCode()).isNotEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    @Order(11)
    @DisplayName("Endpoint de login debe ser público")
    void loginEndpointShouldBePublic() {
        Map<String, Object> login = new HashMap<>();
        login.put("username", "cualquiera");
        login.put("password", "cualquiera");

        ResponseEntity<Map> response = restTemplate.postForEntity(
                baseUrl() + "/auth/login", login, Map.class);

        // Puede ser OK o UNAUTHORIZED, pero no FORBIDDEN
        assertThat(response.getStatusCode()).isIn(HttpStatus.OK, HttpStatus.UNAUTHORIZED);
    }

}