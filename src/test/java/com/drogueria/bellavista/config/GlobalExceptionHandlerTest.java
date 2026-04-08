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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Tests unitarios para GlobalExceptionHandler
 *
 * Estos tests verifican que las excepciones se manejen correctamente
 * y retornen las respuestas HTTP apropiadas con formato consistente.
 */
@WebMvcTest
@ContextConfiguration(classes = {GlobalExceptionHandler.class, TestMailConfig.class})
@DisplayName("GlobalExceptionHandler Tests")
class GlobalExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private GlobalExceptionHandler exceptionHandler;

    @Test
    @DisplayName("Debe manejar ResourceNotFoundException correctamente")
    void shouldHandleResourceNotFoundException() throws Exception {
        // Simular una excepción ResourceNotFoundException
        // Nota: En un test real de integración, esto se probaría con un endpoint que lance la excepción

        // Verificar que el handler existe y funciona
        ResourceNotFoundException ex = new ResourceNotFoundException("Usuario no encontrado con ID: 123");

        var response = exceptionHandler.handleResourceNotFound(ex);

        assert response.getStatusCode().is4xxClientError();
        assert response.getBody() != null;
        assert "Not Found".equals(response.getBody().getError());
        assert response.getBody().getMessage().contains("Usuario no encontrado");
    }

    @Test
    @DisplayName("Debe manejar BusinessException correctamente")
    void shouldHandleBusinessException() throws Exception {
        BusinessException ex = new BusinessException("Stock insuficiente para el producto solicitado");

        var response = exceptionHandler.handleBusinessException(ex);

        assert response.getStatusCode().is4xxClientError();
        assert response.getBody() != null;
        assert "Business Error".equals(response.getBody().getError());
        assert response.getBody().getMessage().contains("Stock insuficiente");
    }

    @Test
    @DisplayName("Debe manejar AuthenticationException correctamente")
    void shouldHandleAuthenticationException() throws Exception {
        AuthenticationException ex = new AuthenticationException("Token JWT expirado");

        var response = exceptionHandler.handleAuthenticationException(ex);

        assert response.getStatusCode().is4xxClientError();
        assert response.getBody() != null;
        assert "Unauthorized".equals(response.getBody().getError());
        assert response.getBody().getMessage().contains("Token JWT expirado");
    }

    @Test
    @DisplayName("Debe manejar AccessDeniedException correctamente")
    void shouldHandleAccessDeniedException() throws Exception {
        AccessDeniedException ex = new AccessDeniedException("Acceso denegado");

        var response = exceptionHandler.handleAccessDeniedException(ex);

        assert response.getStatusCode().is4xxClientError();
        assert response.getBody() != null;
        assert "Forbidden".equals(response.getBody().getError());
        assert response.getBody().getMessage().contains("No tiene permisos");
    }

    @Test
    @DisplayName("Debe manejar IllegalArgumentException correctamente")
    void shouldHandleIllegalArgumentException() throws Exception {
        IllegalArgumentException ex = new IllegalArgumentException("El rol especificado no es válido");

        var response = exceptionHandler.handleIllegalArgument(ex);

        assert response.getStatusCode().is4xxClientError();
        assert response.getBody() != null;
        assert "Invalid Argument".equals(response.getBody().getError());
        assert response.getBody().getMessage().contains("rol especificado no es válido");
    }

    @Test
    @DisplayName("Debe manejar IllegalStateException correctamente")
    void shouldHandleIllegalStateException() throws Exception {
        IllegalStateException ex = new IllegalStateException("La orden ya fue completada");

        var response = exceptionHandler.handleIllegalState(ex);

        assert response.getStatusCode().is4xxClientError();
        assert response.getBody() != null;
        assert "Illegal State".equals(response.getBody().getError());
        assert response.getBody().getMessage().contains("orden ya fue completada");
    }

    @Test
    @DisplayName("Debe manejar errores de validación correctamente")
    void shouldHandleValidationErrors() throws Exception {
        // Crear un mock de MethodArgumentNotValidException
        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);

        FieldError fieldError = new FieldError("user", "email", "El email no es válido");
        when(ex.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getAllErrors()).thenReturn(List.of(fieldError));

        var response = exceptionHandler.handleValidationErrors(ex);

        assert response.getStatusCode().is4xxClientError();
        assert response.getBody() != null;
        assert "Validation Error".equals(response.getBody().getError());
        assert response.getBody().getDetails() != null;
        assert response.getBody().getDetails().containsKey("email");
    }

    @Test
    @DisplayName("Debe manejar excepciones genéricas correctamente")
    void shouldHandleGenericException() throws Exception {
        RuntimeException ex = new RuntimeException("Error interno del servidor");

        var response = exceptionHandler.handleGenericException(ex);

        assert response.getStatusCode().is5xxServerError();
        assert response.getBody() != null;
        assert "Internal Server Error".equals(response.getBody().getError());
        assert response.getBody().getMessage().contains("Error interno del servidor");
    }

    @Test
    @DisplayName("ErrorResponse debe tener estructura correcta")
    void errorResponseShouldHaveCorrectStructure() {
        var errorResponse = new GlobalExceptionHandler.ErrorResponse(400, "Bad Request", "Mensaje de error");

        assert errorResponse.getStatus() == 400;
        assert "Bad Request".equals(errorResponse.getError());
        assert "Mensaje de error".equals(errorResponse.getMessage());
        assert errorResponse.getTimestamp() != null;
    }

    @Test
    @DisplayName("ErrorResponse con detalles debe funcionar correctamente")
    void errorResponseWithDetailsShouldWork() {
        var errorResponse = new GlobalExceptionHandler.ErrorResponse(400, "Validation Error", "Errores de validación");
        var details = java.util.Map.of("field1", "error1", "field2", "error2");
        errorResponse.setDetails(details);

        assert errorResponse.getDetails() != null;
        assert errorResponse.getDetails().size() == 2;
        assert "error1".equals(errorResponse.getDetails().get("field1"));
    }
}</content>
<parameter name="filePath">C:\Users\DANIEL-PC\Documents\software 3\softwareDrogueria\src\test\java\com\drogueria\bellavista\config\GlobalExceptionHandlerTest.java
