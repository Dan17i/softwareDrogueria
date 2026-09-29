package com.drogueria.bellavista.application.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * NotificationDTO.Response/CreateRequest son record: equals/hashCode/toString
 * los genera el compilador (no hay lógica propia que testear campo por campo,
 * como sí la había con el @Data de Lombok). Estas pruebas verifican el
 * contrato del record (builder, accesores, igualdad por valor) sin repetir
 * exhaustivamente cada combinación de campos.
 */
class NotificationDTOConditionsTest {

    private static final LocalDateTime NOW = LocalDateTime.now();

    private NotificationDTO.Response fullResponse() {
        return NotificationDTO.Response.builder()
            .id(1L).title("T").message("M").type("INVENTORY_ALERT")
            .isRead(true).requiredRole("ADMIN")
            .createdAt(NOW).readAt(NOW)
            .relatedEntityId("e1").relatedEntityType("PRODUCT")
            .build();
    }

    @Test
    @DisplayName("✅ Response.builder - construye con todos los campos")
    void responseBuilderSetsAllFields() {
        NotificationDTO.Response r = fullResponse();

        assertEquals(1L, r.getId());
        assertEquals("T", r.getTitle());
        assertEquals("M", r.getMessage());
        assertEquals("INVENTORY_ALERT", r.getType());
        assertTrue(r.getIsRead());
        assertEquals("ADMIN", r.getRequiredRole());
        assertEquals(NOW, r.getCreatedAt());
        assertEquals(NOW, r.getReadAt());
        assertEquals("e1", r.getRelatedEntityId());
        assertEquals("PRODUCT", r.getRelatedEntityType());
    }

    @Test
    @DisplayName("✅ Response.equals - mismos valores → iguales, mismo hashCode")
    void responseEqualityByValue() {
        assertEquals(fullResponse(), fullResponse());
        assertEquals(fullResponse().hashCode(), fullResponse().hashCode());
    }

    @Test
    @DisplayName("✅ Response.equals - un campo distinto → no iguales")
    void responseNotEqualWhenFieldDiffers() {
        NotificationDTO.Response other = NotificationDTO.Response.builder()
            .id(1L).title("T").message("M").type("INVENTORY_ALERT")
            .isRead(false).requiredRole("ADMIN")
            .createdAt(NOW).readAt(NOW)
            .relatedEntityId("e1").relatedEntityType("PRODUCT")
            .build();

        assertNotEquals(fullResponse(), other);
    }

    @Test
    @DisplayName("✅ Response - constructor canónico crea objeto completo")
    void responseCanonicalConstructor() {
        NotificationDTO.Response r = new NotificationDTO.Response(
            1L, "T", "M", "SYSTEM_ALERT", true, "ADMIN", NOW, NOW, "e1", "ORDER");
        assertEquals(1L, r.getId());
        assertTrue(r.getIsRead());
    }

    private NotificationDTO.CreateRequest fullRequest() {
        return NotificationDTO.CreateRequest.builder()
            .title("T").message("M").type("ORDER_ALERT").requiredRole("ADMIN")
            .build();
    }

    @Test
    @DisplayName("✅ CreateRequest.builder - construye con todos los campos")
    void createRequestBuilderSetsAllFields() {
        NotificationDTO.CreateRequest r = fullRequest();

        assertEquals("T", r.getTitle());
        assertEquals("M", r.getMessage());
        assertEquals("ORDER_ALERT", r.getType());
        assertEquals("ADMIN", r.getRequiredRole());
    }

    @Test
    @DisplayName("✅ CreateRequest.equals - mismos valores → iguales")
    void createRequestEqualityByValue() {
        assertEquals(fullRequest(), fullRequest());
        assertEquals(fullRequest().hashCode(), fullRequest().hashCode());
    }

    @Test
    @DisplayName("✅ CreateRequest - constructor canónico crea objeto completo")
    void createRequestCanonicalConstructor() {
        NotificationDTO.CreateRequest r = new NotificationDTO.CreateRequest("T", "M", "USER_ALERT", "ADMIN");
        assertEquals("T", r.getTitle());
        assertEquals("ADMIN", r.getRequiredRole());
    }

    @Test
    @DisplayName("✅ NotificationDTO outer class - constructor accesible")
    void outerClassInstantiable() {
        assertNotNull(new NotificationDTO());
    }
}
