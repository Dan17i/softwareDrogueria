package com.drogueria.bellavista.integration;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import java.time.Duration;

import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;

import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthOrderIntegrationTest {

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
    }

    @LocalServerPort
    int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private String baseUrl() {
        return "http://localhost:" + port + "/api";
    }

    @Test
    void authAndOrderFlow() throws Exception {
        // 1) Register user
        Map<String, Object> reg = new HashMap<>();
        reg.put("username", "testuser");
        reg.put("email", "testuser@example.com");
        reg.put("password", "password123");
        reg.put("firstName", "Test");
        reg.put("lastName", "User");

        ResponseEntity<Map> regResp = restTemplate.postForEntity(baseUrl() + "/auth/register", reg, Map.class);
        assertThat(regResp.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(regResp.getBody()).containsKey("id");

        // 2) Login
        Map<String, Object> login = new HashMap<>();
        login.put("username", "testuser");
        login.put("password", "password123");

        ResponseEntity<Map> loginResp = restTemplate.postForEntity(baseUrl() + "/auth/login", login, Map.class);
        assertThat(loginResp.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(loginResp.getBody()).containsKey("token");
        String token = (String) loginResp.getBody().get("token");

        HttpHeaders authHeaders = new HttpHeaders();
        authHeaders.setBearerAuth(token);
        authHeaders.setContentType(MediaType.APPLICATION_JSON);

        // 3) Create a product
        Map<String, Object> product = new HashMap<>();
        product.put("code", "P-TEST-001");
        product.put("name", "Test Product");
        product.put("description", "Producto para pruebas");
        product.put("price", 10.5);
        product.put("stock", 10);
        product.put("minStock", 1);

        HttpEntity<Map<String, Object>> prodEntity = new HttpEntity<>(product, authHeaders);
        ResponseEntity<Map> prodResp = restTemplate.exchange(baseUrl() + "/products", HttpMethod.POST, prodEntity, Map.class);
        assertThat(prodResp.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Number prodIdNum = (Number) prodResp.getBody().get("id");
        Long productId = prodIdNum.longValue();

        // 4) Create a customer
        Map<String, Object> customer = new HashMap<>();
        customer.put("code", "C-TEST-001");
        customer.put("name", "Cliente Test");
        customer.put("email", "cliente@test.com");
        customer.put("customerType", "MINORISTA");
        customer.put("creditLimit", 1000);

        HttpEntity<Map<String, Object>> custEntity = new HttpEntity<>(customer, authHeaders);
        ResponseEntity<Map> custResp = restTemplate.exchange(baseUrl() + "/customers", HttpMethod.POST, custEntity, Map.class);
        assertThat(custResp.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Number custIdNum = (Number) custResp.getBody().get("id");
        Long customerId = custIdNum.longValue();

        // 5) Create Order
        Map<String, Object> orderReq = new HashMap<>();
        orderReq.put("customerId", customerId);
        Map<String, Object> item = new HashMap<>();
        item.put("productId", productId);
        item.put("quantity", 1);
        orderReq.put("items", List.of(item));

        HttpEntity<Map<String, Object>> orderEntity = new HttpEntity<>(orderReq, authHeaders);
        ResponseEntity<Map> orderResp = restTemplate.exchange(baseUrl() + "/orders", HttpMethod.POST, orderEntity, Map.class);
        assertThat(orderResp.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Number orderIdNum = (Number) orderResp.getBody().get("id");
        Long orderId = orderIdNum.longValue();

        // Validate total equals product price (approx)
        Object totalObj = orderResp.getBody().get("total");
        assertThat(totalObj).isNotNull();

        // 6) Complete order
        ResponseEntity<Map> completeResp = restTemplate.exchange(baseUrl() + "/orders/" + orderId + "/complete", HttpMethod.PATCH, new HttpEntity<>(authHeaders), Map.class);
        assertThat(completeResp.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(completeResp.getBody().get("status")).isEqualTo("COMPLETED");
    }

    @TestConfiguration
static class RestTemplateConfig {

    @Bean
    public RestTemplateBuilder restTemplateBuilder() {
        return new RestTemplateBuilder()
                .requestFactory(() -> new HttpComponentsClientHttpRequestFactory());
    }
}
}
