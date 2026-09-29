package com.drogueria.bellavista.controller;

import com.drogueria.bellavista.application.dto.MessageResponseDTO;
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

class DevToolsControllerTest {

    @Mock
    private AuthService authService;

    @InjectMocks
    private DevToolsController controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("✅ createAdmin - admin ya existe → retorna 200 sin crear otro")
    void shouldReturnExistingAdminMessage() {
        when(authService.isUsernameAvailable("admin")).thenReturn(false);

        ResponseEntity<MessageResponseDTO> response = controller.createAdmin();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().getMessage().contains("ya existe"));
        verify(authService, never()).registerUserWithRole(any(), any(), any(), any(), any(), any());
    }

    @Test
    @DisplayName("✅ createAdmin - admin no existe → crea con password aleatoria y retorna 200")
    void shouldCreateNewAdmin() {
        when(authService.isUsernameAvailable("admin")).thenReturn(true);
        when(authService.registerUserWithRole(eq("admin"), any(), any(), any(), any(), eq(Role.ADMIN)))
            .thenReturn(User.builder().id(1L).username("admin").build());

        ResponseEntity<MessageResponseDTO> response = controller.createAdmin();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().getMessage().contains("creado"));
        verify(authService).registerUserWithRole(eq("admin"), any(), any(), any(), any(), eq(Role.ADMIN));
    }
}
