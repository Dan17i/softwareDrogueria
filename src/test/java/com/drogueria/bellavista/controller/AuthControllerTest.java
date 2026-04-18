package com.drogueria.bellavista.controller;

import com.drogueria.bellavista.application.dto.*;
import com.drogueria.bellavista.application.service.AuthService;
import com.drogueria.bellavista.domain.model.Role;
import com.drogueria.bellavista.domain.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthControllerTest {

    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController controller;

    private User testUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        testUser = User.builder()
            .id(1L)
            .username("daniel")
            .email("daniel@bellavista.com")
            .firstName("Daniel")
            .lastName("Jurado")
            .role(Role.USER)
            .active(true)
            .build();
    }

    @Test
    @DisplayName("✅ register - retorna 200 con UserResponseDTO")
    void shouldRegisterUser() {
        RegisterRequestDTO req = RegisterRequestDTO.builder()
            .username("daniel").email("daniel@bellavista.com")
            .password("pass1234").firstName("Daniel").lastName("Jurado")
            .build();
        when(authService.registerUser("daniel", "daniel@bellavista.com", "pass1234", "Daniel", "Jurado"))
            .thenReturn(testUser);

        ResponseEntity<UserResponseDTO> response = controller.register(req);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("daniel", response.getBody().getUsername());
        assertEquals("USER", response.getBody().getRole());
    }

    @Test
    @DisplayName("✅ register - rol null no lanza excepción")
    void shouldRegisterUserWithNullRole() {
        User noRoleUser = User.builder().id(2L).username("test").email("t@t.com").role(null).build();
        RegisterRequestDTO req = RegisterRequestDTO.builder()
            .username("test").email("t@t.com").password("pass1234").build();
        when(authService.registerUser(any(), any(), any(), any(), any())).thenReturn(noRoleUser);

        ResponseEntity<UserResponseDTO> response = controller.register(req);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNull(response.getBody().getRole());
    }

    @Test
    @DisplayName("✅ login - retorna 200 con token y datos de usuario")
    void shouldLoginUser() {
        LoginRequestDTO req = LoginRequestDTO.builder()
            .username("daniel").password("pass1234").build();
        when(authService.authenticateUser("daniel", "pass1234")).thenReturn("jwt-token-abc");
        when(authService.getUserByUsername("daniel")).thenReturn(testUser);

        ResponseEntity<AuthResponseDTO> response = controller.login(req);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("jwt-token-abc", response.getBody().getToken());
        assertEquals("daniel", response.getBody().getUsername());
        assertEquals("USER", response.getBody().getRole());
    }

    @Test
    @DisplayName("✅ login - rol null en usuario no lanza excepción")
    void shouldLoginUserWithNullRole() {
        User noRoleUser = User.builder().id(2L).username("daniel").role(null).build();
        LoginRequestDTO req = LoginRequestDTO.builder().username("daniel").password("pass").build();
        when(authService.authenticateUser("daniel", "pass")).thenReturn("token");
        when(authService.getUserByUsername("daniel")).thenReturn(noRoleUser);

        ResponseEntity<AuthResponseDTO> response = controller.login(req);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNull(response.getBody().getRole());
    }

    @Test
    @DisplayName("✅ forgotPassword - retorna 200 con mensaje genérico")
    void shouldProcessForgotPassword() {
        ForgotPasswordRequestDTO req = ForgotPasswordRequestDTO.builder()
            .email("daniel@bellavista.com").build();
        doNothing().when(authService).requestPasswordReset("daniel@bellavista.com");

        ResponseEntity<MessageResponseDTO> response = controller.forgotPassword(req);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().getMessage().contains("Si el email existe"));
    }

    @Test
    @DisplayName("✅ resetPassword - retorna 200 tras restablecer")
    void shouldResetPassword() {
        ResetPasswordRequestDTO req = ResetPasswordRequestDTO.builder()
            .token("reset-token").newPassword("nuevaPass123").build();
        doNothing().when(authService).resetPassword("reset-token", "nuevaPass123");

        ResponseEntity<MessageResponseDTO> response = controller.resetPassword(req);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().getMessage().contains("exitosamente"));
    }

    @Test
    @DisplayName("✅ createAdmin - admin ya existe → retorna 200 con mensaje")
    void shouldReturnExistingAdminMessage() {
        when(authService.getUserByUsername("admin")).thenReturn(
            User.builder().id(99L).username("admin").build());

        ResponseEntity<?> response = controller.createAdmin();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("ya existe"));
    }

    @Test
    @DisplayName("✅ createAdmin - admin no existe → crea y retorna 200")
    void shouldCreateNewAdmin() {
        when(authService.getUserByUsername("admin"))
            .thenThrow(new RuntimeException("not found"));
        when(authService.registerUserWithRole(eq("admin"), any(), any(), any(), any(), eq(Role.ADMIN)))
            .thenReturn(User.builder().id(1L).username("admin").build());

        ResponseEntity<?> response = controller.createAdmin();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("creado"));
    }

    @Test
    @DisplayName("❌ createAdmin - excepción en registerUserWithRole → retorna 500")
    void shouldReturn500WhenCreateAdminFails() {
        when(authService.getUserByUsername("admin"))
            .thenThrow(new RuntimeException("not found"));
        when(authService.registerUserWithRole(any(), any(), any(), any(), any(), any()))
            .thenThrow(new RuntimeException("DB error"));

        ResponseEntity<?> response = controller.createAdmin();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    @DisplayName("✅ createUserWithRole - ADMIN crea usuario con rol → retorna 200")
    void shouldCreateUserWithRole() {
        User adminUser = User.builder().id(2L).username("nuevo")
            .email("nuevo@b.com").firstName("Nuevo").lastName("User").role(Role.ADMIN).build();
        RegisterWithRoleRequestDTO req = RegisterWithRoleRequestDTO.builder()
            .username("nuevo").email("nuevo@b.com").password("pass1234")
            .firstName("Nuevo").lastName("User").role(Role.ADMIN).build();
        when(authService.registerUserWithRole("nuevo", "nuevo@b.com", "pass1234", "Nuevo", "User", Role.ADMIN))
            .thenReturn(adminUser);

        ResponseEntity<UserResponseDTO> response = controller.createUserWithRole(req);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("ADMIN", response.getBody().getRole());
    }

    @Test
    @DisplayName("✅ logout - header Bearer válido → retorna 200")
    void shouldLogoutWithValidBearer() {
        doNothing().when(authService).logout("valid.jwt.token");

        ResponseEntity<MessageResponseDTO> response = controller.logout("Bearer valid.jwt.token");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().getMessage().contains("invalidado"));
    }

    @Test
    @DisplayName("❌ logout - sin header Bearer → retorna 401")
    void shouldReturn401WhenNoBearerToken() {
        ResponseEntity<MessageResponseDTO> response = controller.logout("InvalidHeader");

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    @DisplayName("❌ logout - header null → retorna 401")
    void shouldReturn401WhenHeaderIsNull() {
        ResponseEntity<MessageResponseDTO> response = controller.logout(null);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    @DisplayName("❌ logout - authService lanza excepción → retorna 500")
    void shouldReturn500WhenLogoutThrows() {
        doThrow(new RuntimeException("Redis down")).when(authService).logout(any());

        ResponseEntity<MessageResponseDTO> response = controller.logout("Bearer valid.jwt.token");

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue(response.getBody().getMessage().contains("Error al cerrar sesión"));
    }
}
