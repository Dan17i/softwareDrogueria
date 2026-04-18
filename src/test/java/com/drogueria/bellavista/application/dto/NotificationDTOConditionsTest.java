package com.drogueria.bellavista.application.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class NotificationDTOConditionsTest {

    private static final LocalDateTime NOW = LocalDateTime.now();
    private static final LocalDateTime OTHER_TIME = NOW.plusHours(1);

    // ═══════════════════════════════════════════════════════════════════════════
    // NotificationDTO.Response — 10 fields
    // ═══════════════════════════════════════════════════════════════════════════

    private NotificationDTO.Response fullResponse() {
        return NotificationDTO.Response.builder()
            .id(1L).title("T").message("M").type("INVENTORY_ALERT")
            .isRead(true).requiredRole("ADMIN")
            .createdAt(NOW).readAt(NOW)
            .relatedEntityId("e1").relatedEntityType("PRODUCT")
            .build();
    }

    // ── Identity / instanceof ─────────────────────────────────────────────────

    @Test
    @DisplayName("🧪 Response.equals - misma referencia → true")
    void responseSameReference() {
        NotificationDTO.Response r = fullResponse();
        assertEquals(r, r);
    }

    @Test
    @DisplayName("🧪 Response.equals - null → false")
    void responseNotEqualNull() {
        assertNotEquals(null, fullResponse());
    }

    @Test
    @DisplayName("🧪 Response.equals - clase diferente → false")
    void responseNotEqualDifferentClass() {
        assertFalse(fullResponse().equals("x"));
    }

    @Test
    @DisplayName("🧪 Response.canEqual - mismo tipo → true")
    void responseCanEqual() {
        assertTrue(fullResponse().canEqual(fullResponse()));
    }

    // ── Branch D ─────────────────────────────────────────────────────────────

    @Test
    @DisplayName("🧪 Response.equals - todos iguales → true")
    void responseShouldBeEqual() {
        assertEquals(fullResponse(), fullResponse());
    }

    // ── Branch C (this!=null, other!=null, different) ─────────────────────────

    @Test
    @DisplayName("🧪 Response.equals - id diferente → false")
    void responseNotEqualDifferentId() {
        NotificationDTO.Response a = fullResponse(); a.setId(2L);
        assertNotEquals(fullResponse(), a);
    }

    @Test
    @DisplayName("🧪 Response.equals - title diferente → false")
    void responseNotEqualDifferentTitle() {
        NotificationDTO.Response a = fullResponse(); a.setTitle("X");
        assertNotEquals(fullResponse(), a);
    }

    @Test
    @DisplayName("🧪 Response.equals - message diferente → false")
    void responseNotEqualDifferentMessage() {
        NotificationDTO.Response a = fullResponse(); a.setMessage("X");
        assertNotEquals(fullResponse(), a);
    }

    @Test
    @DisplayName("🧪 Response.equals - type diferente → false")
    void responseNotEqualDifferentType() {
        NotificationDTO.Response a = fullResponse(); a.setType("ORDER_ALERT");
        assertNotEquals(fullResponse(), a);
    }

    @Test
    @DisplayName("🧪 Response.equals - isRead diferente → false")
    void responseNotEqualDifferentIsRead() {
        NotificationDTO.Response a = fullResponse(); a.setIsRead(false);
        assertNotEquals(fullResponse(), a);
    }

    @Test
    @DisplayName("🧪 Response.equals - requiredRole diferente → false")
    void responseNotEqualDifferentRequiredRole() {
        NotificationDTO.Response a = fullResponse(); a.setRequiredRole("USER");
        assertNotEquals(fullResponse(), a);
    }

    @Test
    @DisplayName("🧪 Response.equals - createdAt diferente → false")
    void responseNotEqualDifferentCreatedAt() {
        NotificationDTO.Response a = fullResponse(); a.setCreatedAt(OTHER_TIME);
        assertNotEquals(fullResponse(), a);
    }

    @Test
    @DisplayName("🧪 Response.equals - readAt diferente → false")
    void responseNotEqualDifferentReadAt() {
        NotificationDTO.Response a = fullResponse(); a.setReadAt(OTHER_TIME);
        assertNotEquals(fullResponse(), a);
    }

    @Test
    @DisplayName("🧪 Response.equals - relatedEntityId diferente → false")
    void responseNotEqualDifferentRelatedEntityId() {
        NotificationDTO.Response a = fullResponse(); a.setRelatedEntityId("e2");
        assertNotEquals(fullResponse(), a);
    }

    @Test
    @DisplayName("🧪 Response.equals - relatedEntityType diferente → false")
    void responseNotEqualDifferentRelatedEntityType() {
        NotificationDTO.Response a = fullResponse(); a.setRelatedEntityType("ORDER");
        assertNotEquals(fullResponse(), a);
    }

    // ── Branch A (this==null, other!=null) ────────────────────────────────────

    @Test
    @DisplayName("🧪 Response.equals - id this=null, other=1L → false")
    void responseNotEqualIdThisNull() {
        NotificationDTO.Response a = fullResponse(); a.setId(null);
        assertNotEquals(a, fullResponse());
    }

    @Test
    @DisplayName("🧪 Response.equals - title this=null → false")
    void responseNotEqualTitleThisNull() {
        NotificationDTO.Response a = fullResponse(); a.setTitle(null);
        assertNotEquals(a, fullResponse());
    }

    @Test
    @DisplayName("🧪 Response.equals - message this=null → false")
    void responseNotEqualMessageThisNull() {
        NotificationDTO.Response a = fullResponse(); a.setMessage(null);
        assertNotEquals(a, fullResponse());
    }

    @Test
    @DisplayName("🧪 Response.equals - type this=null → false")
    void responseNotEqualTypeThisNull() {
        NotificationDTO.Response a = fullResponse(); a.setType(null);
        assertNotEquals(a, fullResponse());
    }

    @Test
    @DisplayName("🧪 Response.equals - isRead this=null → false")
    void responseNotEqualIsReadThisNull() {
        NotificationDTO.Response a = fullResponse(); a.setIsRead(null);
        assertNotEquals(a, fullResponse());
    }

    @Test
    @DisplayName("🧪 Response.equals - requiredRole this=null → false")
    void responseNotEqualRequiredRoleThisNull() {
        NotificationDTO.Response a = fullResponse(); a.setRequiredRole(null);
        assertNotEquals(a, fullResponse());
    }

    @Test
    @DisplayName("🧪 Response.equals - createdAt this=null → false")
    void responseNotEqualCreatedAtThisNull() {
        NotificationDTO.Response a = fullResponse(); a.setCreatedAt(null);
        assertNotEquals(a, fullResponse());
    }

    @Test
    @DisplayName("🧪 Response.equals - readAt this=null → false")
    void responseNotEqualReadAtThisNull() {
        NotificationDTO.Response a = fullResponse(); a.setReadAt(null);
        assertNotEquals(a, fullResponse());
    }

    @Test
    @DisplayName("🧪 Response.equals - relatedEntityId this=null → false")
    void responseNotEqualRelatedEntityIdThisNull() {
        NotificationDTO.Response a = fullResponse(); a.setRelatedEntityId(null);
        assertNotEquals(a, fullResponse());
    }

    @Test
    @DisplayName("🧪 Response.equals - relatedEntityType this=null → false")
    void responseNotEqualRelatedEntityTypeThisNull() {
        NotificationDTO.Response a = fullResponse(); a.setRelatedEntityType(null);
        assertNotEquals(a, fullResponse());
    }

    // ── Branch B (this==null, other==null) ────────────────────────────────────

    @Test
    @DisplayName("🧪 Response.equals - id: ambos null → iguales")
    void responseEqualNullId() {
        NotificationDTO.Response a = fullResponse(); a.setId(null);
        NotificationDTO.Response b = fullResponse(); b.setId(null);
        assertEquals(a, b);
    }

    @Test
    @DisplayName("🧪 Response.equals - title: ambos null → iguales")
    void responseEqualNullTitle() {
        NotificationDTO.Response a = fullResponse(); a.setTitle(null);
        NotificationDTO.Response b = fullResponse(); b.setTitle(null);
        assertEquals(a, b);
    }

    @Test
    @DisplayName("🧪 Response.equals - message: ambos null → iguales")
    void responseEqualNullMessage() {
        NotificationDTO.Response a = fullResponse(); a.setMessage(null);
        NotificationDTO.Response b = fullResponse(); b.setMessage(null);
        assertEquals(a, b);
    }

    @Test
    @DisplayName("🧪 Response.equals - type: ambos null → iguales")
    void responseEqualNullType() {
        NotificationDTO.Response a = fullResponse(); a.setType(null);
        NotificationDTO.Response b = fullResponse(); b.setType(null);
        assertEquals(a, b);
    }

    @Test
    @DisplayName("🧪 Response.equals - isRead: ambos null → iguales")
    void responseEqualNullIsRead() {
        NotificationDTO.Response a = fullResponse(); a.setIsRead(null);
        NotificationDTO.Response b = fullResponse(); b.setIsRead(null);
        assertEquals(a, b);
    }

    @Test
    @DisplayName("🧪 Response.equals - requiredRole: ambos null → iguales")
    void responseEqualNullRequiredRole() {
        NotificationDTO.Response a = fullResponse(); a.setRequiredRole(null);
        NotificationDTO.Response b = fullResponse(); b.setRequiredRole(null);
        assertEquals(a, b);
    }

    @Test
    @DisplayName("🧪 Response.equals - createdAt: ambos null → iguales")
    void responseEqualNullCreatedAt() {
        NotificationDTO.Response a = fullResponse(); a.setCreatedAt(null);
        NotificationDTO.Response b = fullResponse(); b.setCreatedAt(null);
        assertEquals(a, b);
    }

    @Test
    @DisplayName("🧪 Response.equals - readAt: ambos null → iguales")
    void responseEqualNullReadAt() {
        NotificationDTO.Response a = fullResponse(); a.setReadAt(null);
        NotificationDTO.Response b = fullResponse(); b.setReadAt(null);
        assertEquals(a, b);
    }

    @Test
    @DisplayName("🧪 Response.equals - relatedEntityId: ambos null → iguales")
    void responseEqualNullRelatedEntityId() {
        NotificationDTO.Response a = fullResponse(); a.setRelatedEntityId(null);
        NotificationDTO.Response b = fullResponse(); b.setRelatedEntityId(null);
        assertEquals(a, b);
    }

    @Test
    @DisplayName("🧪 Response.equals - relatedEntityType: ambos null → iguales")
    void responseEqualNullRelatedEntityType() {
        NotificationDTO.Response a = fullResponse(); a.setRelatedEntityType(null);
        NotificationDTO.Response b = fullResponse(); b.setRelatedEntityType(null);
        assertEquals(a, b);
    }

    // ── hashCode / toString ───────────────────────────────────────────────────

    @Test
    @DisplayName("🧪 Response.hashCode - iguales → mismo hashCode")
    void responseHashCode() {
        assertEquals(fullResponse().hashCode(), fullResponse().hashCode());
    }

    @Test
    @DisplayName("🧪 Response.hashCode - campos null → no lanza excepción")
    void responseHashCodeNullFields() {
        assertDoesNotThrow(() -> new NotificationDTO.Response().hashCode());
    }

    @Test
    @DisplayName("🧪 Response.toString - no lanza excepción")
    void responseToString() {
        assertNotNull(fullResponse().toString());
    }

    @Test
    @DisplayName("🧪 Response.allArgsConstructor - crea objeto completo")
    void responseAllArgs() {
        NotificationDTO.Response r = new NotificationDTO.Response(
            1L, "T", "M", "SYSTEM_ALERT", true, "ADMIN", NOW, NOW, "e1", "ORDER");
        assertEquals(1L, r.getId());
        assertTrue(r.getIsRead());
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // NotificationDTO.CreateRequest — 4 fields
    // ═══════════════════════════════════════════════════════════════════════════

    private NotificationDTO.CreateRequest fullRequest() {
        return NotificationDTO.CreateRequest.builder()
            .title("T").message("M").type("ORDER_ALERT").requiredRole("ADMIN")
            .build();
    }

    // ── Identity / instanceof ─────────────────────────────────────────────────

    @Test
    @DisplayName("🧪 CreateRequest.equals - misma referencia → true")
    void requestSameReference() {
        NotificationDTO.CreateRequest r = fullRequest();
        assertEquals(r, r);
    }

    @Test
    @DisplayName("🧪 CreateRequest.equals - null → false")
    void requestNotEqualNull() {
        assertNotEquals(null, fullRequest());
    }

    @Test
    @DisplayName("🧪 CreateRequest.equals - clase diferente → false")
    void requestNotEqualDifferentClass() {
        assertFalse(fullRequest().equals("x"));
    }

    @Test
    @DisplayName("🧪 CreateRequest.canEqual - mismo tipo → true")
    void requestCanEqual() {
        assertTrue(fullRequest().canEqual(fullRequest()));
    }

    // ── Branch D ─────────────────────────────────────────────────────────────

    @Test
    @DisplayName("🧪 CreateRequest.equals - todos iguales → true")
    void requestShouldBeEqual() {
        assertEquals(fullRequest(), fullRequest());
    }

    // ── Branch C ─────────────────────────────────────────────────────────────

    @Test
    @DisplayName("🧪 CreateRequest.equals - title diferente → false")
    void requestNotEqualDifferentTitle() {
        NotificationDTO.CreateRequest a = fullRequest(); a.setTitle("X");
        assertNotEquals(fullRequest(), a);
    }

    @Test
    @DisplayName("🧪 CreateRequest.equals - message diferente → false")
    void requestNotEqualDifferentMessage() {
        NotificationDTO.CreateRequest a = fullRequest(); a.setMessage("X");
        assertNotEquals(fullRequest(), a);
    }

    @Test
    @DisplayName("🧪 CreateRequest.equals - type diferente → false")
    void requestNotEqualDifferentType() {
        NotificationDTO.CreateRequest a = fullRequest(); a.setType("SYSTEM_ALERT");
        assertNotEquals(fullRequest(), a);
    }

    @Test
    @DisplayName("🧪 CreateRequest.equals - requiredRole diferente → false")
    void requestNotEqualDifferentRequiredRole() {
        NotificationDTO.CreateRequest a = fullRequest(); a.setRequiredRole("USER");
        assertNotEquals(fullRequest(), a);
    }

    // ── Branch A ─────────────────────────────────────────────────────────────

    @Test
    @DisplayName("🧪 CreateRequest.equals - title this=null → false")
    void requestNotEqualTitleThisNull() {
        NotificationDTO.CreateRequest a = fullRequest(); a.setTitle(null);
        assertNotEquals(a, fullRequest());
    }

    @Test
    @DisplayName("🧪 CreateRequest.equals - message this=null → false")
    void requestNotEqualMessageThisNull() {
        NotificationDTO.CreateRequest a = fullRequest(); a.setMessage(null);
        assertNotEquals(a, fullRequest());
    }

    @Test
    @DisplayName("🧪 CreateRequest.equals - type this=null → false")
    void requestNotEqualTypeThisNull() {
        NotificationDTO.CreateRequest a = fullRequest(); a.setType(null);
        assertNotEquals(a, fullRequest());
    }

    @Test
    @DisplayName("🧪 CreateRequest.equals - requiredRole this=null → false")
    void requestNotEqualRequiredRoleThisNull() {
        NotificationDTO.CreateRequest a = fullRequest(); a.setRequiredRole(null);
        assertNotEquals(a, fullRequest());
    }

    // ── Branch B ─────────────────────────────────────────────────────────────

    @Test
    @DisplayName("🧪 CreateRequest.equals - title ambos null → iguales")
    void requestEqualNullTitle() {
        NotificationDTO.CreateRequest a = fullRequest(); a.setTitle(null);
        NotificationDTO.CreateRequest b = fullRequest(); b.setTitle(null);
        assertEquals(a, b);
    }

    @Test
    @DisplayName("🧪 CreateRequest.equals - message ambos null → iguales")
    void requestEqualNullMessage() {
        NotificationDTO.CreateRequest a = fullRequest(); a.setMessage(null);
        NotificationDTO.CreateRequest b = fullRequest(); b.setMessage(null);
        assertEquals(a, b);
    }

    @Test
    @DisplayName("🧪 CreateRequest.equals - type ambos null → iguales")
    void requestEqualNullType() {
        NotificationDTO.CreateRequest a = fullRequest(); a.setType(null);
        NotificationDTO.CreateRequest b = fullRequest(); b.setType(null);
        assertEquals(a, b);
    }

    @Test
    @DisplayName("🧪 CreateRequest.equals - requiredRole ambos null → iguales")
    void requestEqualNullRequiredRole() {
        NotificationDTO.CreateRequest a = fullRequest(); a.setRequiredRole(null);
        NotificationDTO.CreateRequest b = fullRequest(); b.setRequiredRole(null);
        assertEquals(a, b);
    }

    // ── hashCode / toString ───────────────────────────────────────────────────

    @Test
    @DisplayName("🧪 CreateRequest.hashCode - iguales → mismo hashCode")
    void requestHashCode() {
        assertEquals(fullRequest().hashCode(), fullRequest().hashCode());
    }

    @Test
    @DisplayName("🧪 CreateRequest.hashCode - campos null → no lanza excepción")
    void requestHashCodeNullFields() {
        assertDoesNotThrow(() -> new NotificationDTO.CreateRequest().hashCode());
    }

    @Test
    @DisplayName("🧪 CreateRequest.toString - no lanza excepción")
    void requestToString() {
        assertNotNull(fullRequest().toString());
    }

    @Test
    @DisplayName("🧪 CreateRequest.allArgsConstructor - crea objeto completo")
    void requestAllArgs() {
        NotificationDTO.CreateRequest r = new NotificationDTO.CreateRequest("T", "M", "USER_ALERT", "ADMIN");
        assertEquals("T", r.getTitle());
        assertEquals("ADMIN", r.getRequiredRole());
    }

    // ── NotificationDTO outer class ───────────────────────────────────────────

    @Test
    @DisplayName("🧪 NotificationDTO outer class - constructor accesible")
    void outerClassInstantiable() {
        assertNotNull(new NotificationDTO());
    }
}
