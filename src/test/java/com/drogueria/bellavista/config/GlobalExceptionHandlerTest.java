package com.drogueria.bellavista.config;

import com.drogueria.bellavista.exception.AuthenticationException;
import com.drogueria.bellavista.exception.BusinessException;
import com.drogueria.bellavista.exception.ResourceNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@WebMvcTest
@ContextConfiguration(classes = {GlobalExceptionHandler.class, TestMailConfig.class})
@DisplayName("GlobalExceptionHandler Tests")
class GlobalExceptionHandlerTest {

    @Autowired
    private GlobalExceptionHandler exceptionHandler;

    @Test
    @DisplayName("Debe manejar ResourceNotFoundException correctamente")
    void shouldHandleResourceNotFoundException() {
        ResourceNotFoundException ex = new ResourceNotFoundException("Usuario no encontrado con ID: 123");

        var response = exceptionHandler.handleResourceNotFound(ex);

        assertTrue(response.getStatusCode().is4xxClientError());
        assertNotNull(response.getBody());
        assertEquals("Not Found", response.getBody().getError());
        assertTrue(response.getBody().getMessage().contains("Usuario no encontrado"));
    }

    @Test
    @DisplayName("Debe manejar BusinessException correctamente")
    void shouldHandleBusinessException() {
        BusinessException ex = new BusinessException("Stock insuficiente para el producto solicitado");

        var response = exceptionHandler.handleBusinessException(ex);

        assertTrue(response.getStatusCode().is4xxClientError());
        assertNotNull(response.getBody());
        assertEquals("Business Error", response.getBody().getError());
        assertTrue(response.getBody().getMessage().contains("Stock insuficiente"));
    }

    @Test
    @DisplayName("Debe manejar AuthenticationException correctamente")
    void shouldHandleAuthenticationException() {
        AuthenticationException ex = new AuthenticationException("Token JWT expirado");

        var response = exceptionHandler.handleAuthenticationException(ex);

        assertTrue(response.getStatusCode().is4xxClientError());
        assertNotNull(response.getBody());
        assertEquals("Unauthorized", response.getBody().getError());
        assertTrue(response.getBody().getMessage().contains("Token JWT expirado"));
    }

    @Test
    @DisplayName("Debe manejar AccessDeniedException correctamente")
    void shouldHandleAccessDeniedException() {
        AccessDeniedException ex = new AccessDeniedException("Acceso denegado");

        var response = exceptionHandler.handleAccessDeniedException(ex);

        assertTrue(response.getStatusCode().is4xxClientError());
        assertNotNull(response.getBody());
        assertEquals("Forbidden", response.getBody().getError());
        assertTrue(response.getBody().getMessage().contains("No tiene permisos"));
    }

    @Test
    @DisplayName("Debe manejar IllegalArgumentException correctamente")
    void shouldHandleIllegalArgumentException() {
        IllegalArgumentException ex = new IllegalArgumentException("El rol especificado no es válido");

        var response = exceptionHandler.handleIllegalArgument(ex);

        assertTrue(response.getStatusCode().is4xxClientError());
        assertNotNull(response.getBody());
        assertEquals("Invalid Argument", response.getBody().getError());
        assertTrue(response.getBody().getMessage().contains("rol especificado no es válido"));
    }

    @Test
    @DisplayName("Debe manejar IllegalStateException correctamente")
    void shouldHandleIllegalStateException() {
        IllegalStateException ex = new IllegalStateException("La orden ya fue completada");

        var response = exceptionHandler.handleIllegalState(ex);

        assertTrue(response.getStatusCode().is4xxClientError());
        assertNotNull(response.getBody());
        assertEquals("Illegal State", response.getBody().getError());
        assertTrue(response.getBody().getMessage().contains("orden ya fue completada"));
    }

    @Test
    @DisplayName("Debe manejar errores de validación correctamente")
    void shouldHandleValidationErrors() {
        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);

        FieldError fieldError = new FieldError("user", "email", "El email no es válido");
        when(ex.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getAllErrors()).thenReturn(List.of(fieldError));

        var response = exceptionHandler.handleValidationErrors(ex);

        assertTrue(response.getStatusCode().is4xxClientError());
        assertNotNull(response.getBody());
        assertEquals("Validation Error", response.getBody().getError());
        assertNotNull(response.getBody().getDetails());
        assertTrue(response.getBody().getDetails().containsKey("email"));
    }

    @Test
    @DisplayName("Debe manejar excepciones genéricas correctamente")
    void shouldHandleGenericException() {
        RuntimeException ex = new RuntimeException("Error interno del servidor");

        var response = exceptionHandler.handleGenericException(ex);

        assertTrue(response.getStatusCode().is5xxServerError());
        assertNotNull(response.getBody());
        assertEquals("Internal Server Error", response.getBody().getError());
        assertTrue(response.getBody().getMessage().contains("Error interno del servidor"));
    }

    @Test
    @DisplayName("ErrorResponse debe tener estructura correcta")
    void errorResponseShouldHaveCorrectStructure() {
        var errorResponse = new GlobalExceptionHandler.ErrorResponse(400, "Bad Request", "Mensaje de error");

        assertEquals(400, errorResponse.getStatus());
        assertEquals("Bad Request", errorResponse.getError());
        assertEquals("Mensaje de error", errorResponse.getMessage());
        assertNotNull(errorResponse.getTimestamp());
    }

    @Test
    @DisplayName("ErrorResponse con detalles debe funcionar correctamente")
    void errorResponseWithDetailsShouldWork() {
        var errorResponse = new GlobalExceptionHandler.ErrorResponse(400, "Validation Error", "Errores de validación");
        var details = java.util.Map.of("field1", "error1", "field2", "error2");
        errorResponse.setDetails(details);

        assertNotNull(errorResponse.getDetails());
        assertEquals(2, errorResponse.getDetails().size());
        assertEquals("error1", errorResponse.getDetails().get("field1"));
    }
}
