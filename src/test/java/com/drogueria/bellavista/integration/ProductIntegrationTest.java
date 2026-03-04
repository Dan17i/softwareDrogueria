package com.drogueria.bellavista.integration;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Product Integration Tests")
public class ProductIntegrationTest {

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

    private static String token;
    private static Long productId;

    private String baseUrl() {
        return "http://localhost:" + port + "/api";
    }

    private HttpHeaders authHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    // ============================================
    // SETUP - Crear usuario y obtener token
    // ============================================

    @Test
    @Order(1)
    @DisplayName("Setup: Registrar usuario y obtener token")
    void setupUserAndGetToken() {
        // Registrar
        Map<String, Object> reg = new HashMap<>();
        reg.put("username", "productuser");
        reg.put("email", "productuser@test.com");
        reg.put("password", "password123");
        reg.put("firstName", "Product");
        reg.put("lastName", "Tester");

        restTemplate.postForEntity(baseUrl() + "/auth/register", reg, Map.class);

        // Login
        Map<String, Object> login = new HashMap<>();
        login.put("username", "productuser");
        login.put("password", "password123");

        ResponseEntity<Map> response = restTemplate.postForEntity(
                baseUrl() + "/auth/login", login, Map.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        token = (String) response.getBody().get("token");
        assertThat(token).isNotNull();
    }

    // ============================================
    // CREATE
    // ============================================

    @Test
    @Order(2)
    @DisplayName("Debe crear producto exitosamente")
    void shouldCreateProduct() {
        Map<String, Object> product = new HashMap<>();
        product.put("code", "PROD-001");
        product.put("name", "Acetaminofén 500mg");
        product.put("description", "Analgésico y antipirético");
        product.put("price", 5000.00);
        product.put("stock", 100);
        product.put("minStock", 10);
        product.put("category", "Medicamentos");

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(product, authHeaders());
        ResponseEntity<Map> response = restTemplate.exchange(
                baseUrl() + "/products", HttpMethod.POST, entity, Map.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).containsKey("id");
        assertThat(response.getBody().get("code")).isEqualTo("PROD-001");
        assertThat(response.getBody().get("active")).isEqualTo(true);

        productId = ((Number) response.getBody().get("id")).longValue();
    }

    @Test
    @Order(3)
    @DisplayName("No debe crear producto con código duplicado")
    void shouldNotCreateDuplicateProduct() {
        Map<String, Object> product = new HashMap<>();
        product.put("code", "PROD-001");
        product.put("name", "Otro Producto");
        product.put("price", 1000.00);
        product.put("stock", 50);
        product.put("minStock", 5);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(product, authHeaders());
        ResponseEntity<Map> response = restTemplate.exchange(
                baseUrl() + "/products", HttpMethod.POST, entity, Map.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    // ============================================
    // READ
    // ============================================

    @Test
    @Order(4)
    @DisplayName("Debe obtener producto por ID")
    void shouldGetProductById() {
        HttpEntity<?> entity = new HttpEntity<>(authHeaders());
        ResponseEntity<Map> response = restTemplate.exchange(
                baseUrl() + "/products/" + productId, HttpMethod.GET, entity, Map.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().get("code")).isEqualTo("PROD-001");
    }

    @Test
    @Order(5)
    @DisplayName("Debe obtener producto por código")
    void shouldGetProductByCode() {
        HttpEntity<?> entity = new HttpEntity<>(authHeaders());
        ResponseEntity<Map> response = restTemplate.exchange(
                baseUrl() + "/products/code/PROD-001", HttpMethod.GET, entity, Map.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().get("name")).isEqualTo("Acetaminofén 500mg");
    }

    @Test
    @Order(6)
    @DisplayName("Debe listar todos los productos")
    void shouldListAllProducts() {
        HttpEntity<?> entity = new HttpEntity<>(authHeaders());
        ResponseEntity<List> response = restTemplate.exchange(
                baseUrl() + "/products", HttpMethod.GET, entity, List.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotEmpty();
    }

    @Test
    @Order(7)
    @DisplayName("Debe retornar 404 para producto inexistente")
    void shouldReturn404ForNonExistentProduct() {
        HttpEntity<?> entity = new HttpEntity<>(authHeaders());
        ResponseEntity<Map> response = restTemplate.exchange(
                baseUrl() + "/products/99999", HttpMethod.GET, entity, Map.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    // ============================================
    // UPDATE
    // ============================================

    @Test
    @Order(8)
    @DisplayName("Debe actualizar producto")
    void shouldUpdateProduct() {
        Map<String, Object> updateData = new HashMap<>();
        updateData.put("code", "PROD-001");
        updateData.put("name", "Acetaminofén 500mg Actualizado");
        updateData.put("description", "Descripción actualizada");
        updateData.put("price", 5500.00);
        updateData.put("stock", 100);
        updateData.put("minStock", 15);
        updateData.put("category", "Medicamentos");
        updateData.put("active", true);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(updateData, authHeaders());
        ResponseEntity<Map> response = restTemplate.exchange(
                baseUrl() + "/products/" + productId, HttpMethod.PUT, entity, Map.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().get("name")).isEqualTo("Acetaminofén 500mg Actualizado");
        assertThat(((Number) response.getBody().get("price")).doubleValue()).isEqualTo(5500.00);
    }

    // ============================================
    // STOCK OPERATIONS
    // ============================================

    @Test
    @Order(9)
    @DisplayName("Debe reducir stock correctamente")
    void shouldReduceStock() {
        Map<String, Object> adjustment = new HashMap<>();
        adjustment.put("quantity", 10);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(adjustment, authHeaders());
        ResponseEntity<Map> response = restTemplate.exchange(
                baseUrl() + "/products/" + productId + "/reduce-stock",
                HttpMethod.POST, entity, Map.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().get("stock")).isEqualTo(90);
    }

    @Test
    @Order(10)
    @DisplayName("Debe aumentar stock correctamente")
    void shouldIncreaseStock() {
        Map<String, Object> adjustment = new HashMap<>();
        adjustment.put("quantity", 50);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(adjustment, authHeaders());
        ResponseEntity<Map> response = restTemplate.exchange(
                baseUrl() + "/products/" + productId + "/increase-stock",
                HttpMethod.POST, entity, Map.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().get("stock")).isEqualTo(140);
    }

    @Test
    @Order(11)
    @DisplayName("No debe reducir stock si es insuficiente")
    void shouldNotReduceStockIfInsufficient() {
        Map<String, Object> adjustment = new HashMap<>();
        adjustment.put("quantity", 1000);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(adjustment, authHeaders());
        ResponseEntity<Map> response = restTemplate.exchange(
                baseUrl() + "/products/" + productId + "/reduce-stock",
                HttpMethod.POST, entity, Map.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    // ============================================
    // SEARCH & FILTER
    // ============================================

    @Test
    @Order(12)
    @DisplayName("Debe buscar productos por nombre")
    void shouldSearchProductsByName() {
        HttpEntity<?> entity = new HttpEntity<>(authHeaders());
        ResponseEntity<List> response = restTemplate.exchange(
                baseUrl() + "/products/search?name=Acetaminofén",
                HttpMethod.GET, entity, List.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotEmpty();
    }

    @Test
    @Order(13)
    @DisplayName("Debe listar productos por categoría")
    void shouldListProductsByCategory() {
        HttpEntity<?> entity = new HttpEntity<>(authHeaders());
        ResponseEntity<List> response = restTemplate.exchange(
                baseUrl() + "/products/category/Medicamentos",
                HttpMethod.GET, entity, List.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotEmpty();
    }

    // ============================================
    // TOGGLE STATUS
    // ============================================

    @Test
    @Order(14)
    @DisplayName("Debe cambiar estado del producto")
    void shouldToggleProductStatus() {
        HttpEntity<?> entity = new HttpEntity<>(authHeaders());
        ResponseEntity<Map> response = restTemplate.exchange(
                baseUrl() + "/products/" + productId + "/toggle-status",
                HttpMethod.PATCH, entity, Map.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().get("active")).isEqualTo(false);
    }

    // ============================================
    // AUTH REQUIRED
    // ============================================

    @Test
    @Order(15)
    @DisplayName("Debe requerir autenticación para crear producto")
    void shouldRequireAuthToCreateProduct() {
        Map<String, Object> product = new HashMap<>();
        product.put("code", "PROD-002");
        product.put("name", "Otro Producto");
        product.put("price", 1000.00);
        product.put("stock", 50);
        product.put("minStock", 5);

        ResponseEntity<Map> response = restTemplate.postForEntity(
                baseUrl() + "/products", product, Map.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }
}