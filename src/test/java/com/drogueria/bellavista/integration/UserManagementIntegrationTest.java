package com.drogueria.bellavista.integration;

import com.drogueria.bellavista.application.dto.LoginRequestDTO;
import com.drogueria.bellavista.application.dto.RegisterRequestDTO;
import com.drogueria.bellavista.application.dto.UpdateRoleRequestDTO;
import com.drogueria.bellavista.application.dto.UpdateStatusRequestDTO;
import com.drogueria.bellavista.domain.model.Role;
import com.drogueria.bellavista.domain.repository.UserRepository;
import com.drogueria.bellavista.domain.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Tests de integración para gestión de usuarios
 * 
 * MÉTRICAS DE CALIDAD:
 * - Métrica 4.3: Control de acceso - Solo ADMIN puede gestionar usuarios
 * - Métrica 4.4: Mensajes claros en respuestas de error
 * - Métrica 3.3: Tiempo de respuesta de consultas
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Testcontainers
@DisplayName("User Management Integration Tests")
class UserManagementIntegrationTest {
    
    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine")
        .withDatabaseName("testdb")
        .withUsername("test")
        .withPassword("test");
    
    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }
    
    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private UserService userService;
    
    private String adminToken;
    private String userToken;
    
    @BeforeEach
    void setUp() throws Exception {
        // Limpiar usuarios existentes
        userRepository.findByUsername("admin").ifPresent(user -> userRepository.delete(user));
        userRepository.findByUsername("testuser").ifPresent(user -> userRepository.delete(user));
        
        // Crear admin usando el servicio
        userService.createUser("admin", "admin@bellavista.com", "admin123", "Admin", "Sistema", Role.ADMIN);
        
        // Login como admin
        LoginRequestDTO adminLogin = new LoginRequestDTO();
        adminLogin.setUsername("admin");
        adminLogin.setPassword("admin123");
        
        MvcResult adminResult = mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(adminLogin)))
            .andExpect(status().isOk())
            .andReturn();
        
        String adminResponse = adminResult.getResponse().getContentAsString();
        adminToken = objectMapper.readTree(adminResponse).get("token").asText();
        
        // Crear usuario regular usando el servicio
        userService.createUser("testuser", "testuser@test.com", "password123", "Test", "User", Role.USER);
        
        // Login como usuario regular
        LoginRequestDTO userLogin = new LoginRequestDTO();
        userLogin.setUsername("testuser");
        userLogin.setPassword("password123");
        
        MvcResult userResult = mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userLogin)))
            .andExpect(status().isOk())
            .andReturn();
        
        String userResponse = userResult.getResponse().getContentAsString();
        userToken = objectMapper.readTree(userResponse).get("token").asText();
    }
    
    @Test
    @DisplayName("GET /users - Admin debe poder listar usuarios")
    void getAllUsers_asAdmin_shouldSucceed() throws Exception {
        mockMvc.perform(get("/users")
                .header("Authorization", "Bearer " + adminToken))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$.length()").value(org.hamcrest.Matchers.greaterThanOrEqualTo(2)))
            .andExpect(jsonPath("$[0].username").exists())
            .andExpect(jsonPath("$[0].role").exists());
    }
    
    @Test
    @DisplayName("GET /users - Usuario regular no debe poder listar usuarios")
    void getAllUsers_asUser_shouldFail() throws Exception {
        mockMvc.perform(get("/users")
                .header("Authorization", "Bearer " + userToken))
            .andExpect(status().isForbidden());
    }
    
    @Test
    @DisplayName("GET /users - Sin token debe fallar")
    void getAllUsers_withoutToken_shouldFail() throws Exception {
        mockMvc.perform(get("/users"))
            .andExpect(status().isUnauthorized());
    }
    
    @Test
    @DisplayName("GET /users/{id} - Admin debe poder ver usuario específico")
    void getUserById_asAdmin_shouldSucceed() throws Exception {
        // Obtener el ID del testuser desde la lista de usuarios
        MvcResult result = mockMvc.perform(get("/users")
                .header("Authorization", "Bearer " + adminToken))
            .andExpect(status().isOk())
            .andReturn();
        
        String response = result.getResponse().getContentAsString();
        com.fasterxml.jackson.databind.JsonNode usersArray = objectMapper.readTree(response);
        
        // Buscar el usuario testuser en la respuesta
        Long testUserId = null;
        for (com.fasterxml.jackson.databind.JsonNode user : usersArray) {
            if ("testuser".equals(user.get("username").asText())) {
                testUserId = user.get("id").asLong();
                break;
            }
        }
        
        mockMvc.perform(get("/users/" + testUserId)
                .header("Authorization", "Bearer " + adminToken))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.username").value("testuser"))
            .andExpect(jsonPath("$.role").value("USER"));
    }
    
    @Test
    @DisplayName("PATCH /users/{id}/role - Admin debe poder cambiar rol")
    void updateUserRole_asAdmin_shouldSucceed() throws Exception {
        // Obtener el ID del testuser
        MvcResult listResult = mockMvc.perform(get("/users")
                .header("Authorization", "Bearer " + adminToken))
            .andReturn();
        
        String listResponse = listResult.getResponse().getContentAsString();
        com.fasterxml.jackson.databind.JsonNode users = objectMapper.readTree(listResponse);
        Long testUserId = null;
        for (com.fasterxml.jackson.databind.JsonNode user : users) {
            if ("testuser".equals(user.get("username").asText())) {
                testUserId = user.get("id").asLong();
                break;
            }
        }
        
        UpdateRoleRequestDTO request = UpdateRoleRequestDTO.builder()
            .role(Role.SALES)
            .build();
        
        mockMvc.perform(patch("/users/" + testUserId + "/role")
                .header("Authorization", "Bearer " + adminToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.role").value("SALES"));
    }
    
    @Test
    @DisplayName("PATCH /users/{id}/role - Usuario regular no debe poder cambiar roles")
    void updateUserRole_asUser_shouldFail() throws Exception {
        // Obtener el ID del admin
        MvcResult listResult = mockMvc.perform(get("/users")
                .header("Authorization", "Bearer " + adminToken))
            .andReturn();
        
        String listResponse = listResult.getResponse().getContentAsString();
        com.fasterxml.jackson.databind.JsonNode users = objectMapper.readTree(listResponse);
        Long adminId = null;
        for (com.fasterxml.jackson.databind.JsonNode user : users) {
            if ("admin".equals(user.get("username").asText())) {
                adminId = user.get("id").asLong();
                break;
            }
        }
        
        UpdateRoleRequestDTO request = UpdateRoleRequestDTO.builder()
            .role(Role.ADMIN)
            .build();
        
        mockMvc.perform(patch("/users/" + adminId + "/role")
                .header("Authorization", "Bearer " + userToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isForbidden());
    }
    
    @Test
    @DisplayName("PATCH /users/{id}/role - No debe permitir cambiar rol del único admin")
    void updateUserRole_lastAdmin_shouldFail() throws Exception {
        // Obtener el ID del admin
        MvcResult listResult = mockMvc.perform(get("/users")
                .header("Authorization", "Bearer " + adminToken))
            .andReturn();
        
        String listResponse = listResult.getResponse().getContentAsString();
        com.fasterxml.jackson.databind.JsonNode users = objectMapper.readTree(listResponse);
        Long adminId = null;
        for (com.fasterxml.jackson.databind.JsonNode user : users) {
            if ("admin".equals(user.get("username").asText())) {
                adminId = user.get("id").asLong();
                break;
            }
        }
        
        UpdateRoleRequestDTO request = UpdateRoleRequestDTO.builder()
            .role(Role.USER)
            .build();
        
        mockMvc.perform(patch("/users/" + adminId + "/role")
                .header("Authorization", "Bearer " + adminToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value(org.hamcrest.Matchers.containsString("único administrador")));
    }
    
    @Test
    @DisplayName("PATCH /users/{id}/status - Admin debe poder desactivar usuario")
    void updateUserStatus_asAdmin_shouldSucceed() throws Exception {
        // Obtener el ID del testuser
        MvcResult listResult = mockMvc.perform(get("/users")
                .header("Authorization", "Bearer " + adminToken))
            .andReturn();
        
        String listResponse = listResult.getResponse().getContentAsString();
        com.fasterxml.jackson.databind.JsonNode users = objectMapper.readTree(listResponse);
        Long testUserId = null;
        for (com.fasterxml.jackson.databind.JsonNode user : users) {
            if ("testuser".equals(user.get("username").asText())) {
                testUserId = user.get("id").asLong();
                break;
            }
        }
        
        UpdateStatusRequestDTO request = UpdateStatusRequestDTO.builder()
            .active(false)
            .build();
        
        mockMvc.perform(patch("/users/" + testUserId + "/status")
                .header("Authorization", "Bearer " + adminToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk());
    }
    
    @Test
    @DisplayName("PATCH /users/{id}/status - No debe desactivar único admin")
    void updateUserStatus_lastAdmin_shouldFail() throws Exception {
        // Obtener el ID del admin
        MvcResult listResult = mockMvc.perform(get("/users")
                .header("Authorization", "Bearer " + adminToken))
            .andReturn();
        
        String listResponse = listResult.getResponse().getContentAsString();
        com.fasterxml.jackson.databind.JsonNode users = objectMapper.readTree(listResponse);
        Long adminId = null;
        for (com.fasterxml.jackson.databind.JsonNode user : users) {
            if ("admin".equals(user.get("username").asText())) {
                adminId = user.get("id").asLong();
                break;
            }
        }
        
        UpdateStatusRequestDTO request = UpdateStatusRequestDTO.builder()
            .active(false)
            .build();
        
        mockMvc.perform(patch("/users/" + adminId + "/status")
                .header("Authorization", "Bearer " + adminToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value(org.hamcrest.Matchers.containsString("único administrador activo")));
    }
    
    @Test
    @DisplayName("DELETE /users/{id} - Admin debe poder eliminar usuario")
    void deleteUser_asAdmin_shouldSucceed() throws Exception {
        // Obtener el ID del testuser
        MvcResult listResult = mockMvc.perform(get("/users")
                .header("Authorization", "Bearer " + adminToken))
            .andReturn();
        
        String listResponse = listResult.getResponse().getContentAsString();
        com.fasterxml.jackson.databind.JsonNode users = objectMapper.readTree(listResponse);
        Long testUserId = null;
        for (com.fasterxml.jackson.databind.JsonNode user : users) {
            if ("testuser".equals(user.get("username").asText())) {
                testUserId = user.get("id").asLong();
                break;
            }
        }
        
        mockMvc.perform(delete("/users/" + testUserId)
                .header("Authorization", "Bearer " + adminToken))
            .andExpect(status().isNoContent());
        
        // Verificar que el usuario fue eliminado
        mockMvc.perform(get("/users/" + testUserId)
                .header("Authorization", "Bearer " + adminToken))
            .andExpect(status().isNotFound());
    }
    
    @Test
    @DisplayName("DELETE /users/{id} - No debe eliminar único admin")
    void deleteUser_lastAdmin_shouldFail() throws Exception {
        // Obtener el ID del admin
        MvcResult listResult = mockMvc.perform(get("/users")
                .header("Authorization", "Bearer " + adminToken))
            .andReturn();
        
        String listResponse = listResult.getResponse().getContentAsString();
        com.fasterxml.jackson.databind.JsonNode users = objectMapper.readTree(listResponse);
        Long adminId = null;
        for (com.fasterxml.jackson.databind.JsonNode user : users) {
            if ("admin".equals(user.get("username").asText())) {
                adminId = user.get("id").asLong();
                break;
            }
        }
        
        mockMvc.perform(delete("/users/" + adminId)
                .header("Authorization", "Bearer " + adminToken))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value(org.hamcrest.Matchers.containsString("único administrador")));
    }
    
    @Test
    @DisplayName("DELETE /users/{id} - Usuario regular no debe poder eliminar")
    void deleteUser_asUser_shouldFail() throws Exception {
        // Obtener el ID del admin
        MvcResult listResult = mockMvc.perform(get("/users")
                .header("Authorization", "Bearer " + adminToken))
            .andReturn();
        
        String listResponse = listResult.getResponse().getContentAsString();
        com.fasterxml.jackson.databind.JsonNode users = objectMapper.readTree(listResponse);
        Long adminId = null;
        for (com.fasterxml.jackson.databind.JsonNode user : users) {
            if ("admin".equals(user.get("username").asText())) {
                adminId = user.get("id").asLong();
                break;
            }
        }
        
        mockMvc.perform(delete("/users/" + adminId)
                .header("Authorization", "Bearer " + userToken))
            .andExpect(status().isForbidden());
    }
    
    @Test
    @DisplayName("Flujo completo: Registrar, listar, cambiar rol, desactivar")
    void completeUserManagementFlow() throws Exception {
        // 1. Registrar nuevo usuario
        RegisterRequestDTO newUser = RegisterRequestDTO.builder()
            .username("newuser")
            .email("newuser@test.com")
            .password("password123")
            .firstName("New")
            .lastName("User")
            .build();
        
        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newUser)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.role").value("USER"));
        
        // 2. Admin lista usuarios y ve el nuevo
        MvcResult listResult = mockMvc.perform(get("/users")
                .header("Authorization", "Bearer " + adminToken))
            .andExpect(status().isOk())
            .andReturn();
        
        String listResponse = listResult.getResponse().getContentAsString();
        com.fasterxml.jackson.databind.JsonNode users = objectMapper.readTree(listResponse);
        Long newUserId = null;
        for (com.fasterxml.jackson.databind.JsonNode user : users) {
            if ("newuser".equals(user.get("username").asText())) {
                newUserId = user.get("id").asLong();
                break;
            }
        }
        
        // 3. Admin cambia rol a MANAGER
        UpdateRoleRequestDTO roleUpdate = UpdateRoleRequestDTO.builder()
            .role(Role.MANAGER)
            .build();
        
        mockMvc.perform(patch("/users/" + newUserId + "/role")
                .header("Authorization", "Bearer " + adminToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(roleUpdate)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.role").value("MANAGER"));
        
        // 4. Admin desactiva usuario
        UpdateStatusRequestDTO statusUpdate = UpdateStatusRequestDTO.builder()
            .active(false)
            .build();
        
        mockMvc.perform(patch("/users/" + newUserId + "/status")
                .header("Authorization", "Bearer " + adminToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(statusUpdate)))
            .andExpect(status().isOk());
        
        // 5. Usuario desactivado no puede hacer login
        LoginRequestDTO loginAttempt = new LoginRequestDTO();
        loginAttempt.setUsername("newuser");
        loginAttempt.setPassword("password123");
        
        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginAttempt)))
            .andExpect(status().isUnauthorized());
    }
}
