package com.drogueria.bellavista.application.mapper;

import com.drogueria.bellavista.application.dto.NotificationDTO;
import com.drogueria.bellavista.domain.model.Notification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class NotificationMapperTest {

    private NotificationMapper mapper;
    private LocalDateTime now;

    @BeforeEach
    void setUp() {
        mapper = new NotificationMapper();
        now = LocalDateTime.now();
    }

    @Test
    @DisplayName("✅ toResponse - Notification completa → Response DTO correcto")
    void shouldMapNotificationToResponse() {
        Notification notification = Notification.builder()
            .id(1L).title("Alerta stock").message("Producto bajo")
            .type(Notification.NotificationType.INVENTORY_ALERT)
            .isRead(false).requiredRole("ADMIN")
            .createdAt(now).readAt(null)
            .relatedEntityId("prod-42").relatedEntityType("PRODUCT")
            .build();

        NotificationDTO.Response result = mapper.toResponse(notification);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Alerta stock", result.getTitle());
        assertEquals("Producto bajo", result.getMessage());
        assertEquals("INVENTORY_ALERT", result.getType());
        assertFalse(result.getIsRead());
        assertEquals("ADMIN", result.getRequiredRole());
        assertEquals(now, result.getCreatedAt());
        assertNull(result.getReadAt());
        assertEquals("prod-42", result.getRelatedEntityId());
        assertEquals("PRODUCT", result.getRelatedEntityType());
    }

    @Test
    @DisplayName("✅ toResponse - null → null")
    void shouldReturnNullWhenNotificationIsNull() {
        assertNull(mapper.toResponse(null));
    }

    @Test
    @DisplayName("✅ toResponse - notificación leída incluye readAt")
    void shouldMapReadNotification() {
        Notification notification = Notification.builder()
            .id(2L).title("Leída").message("msg")
            .type(Notification.NotificationType.SYSTEM_ALERT)
            .isRead(true).createdAt(now).readAt(now)
            .requiredRole("USER")
            .build();

        NotificationDTO.Response result = mapper.toResponse(notification);

        assertTrue(result.getIsRead());
        assertEquals(now, result.getReadAt());
    }

    @Test
    @DisplayName("✅ fromCreateRequest - CreateRequest → Notification de dominio")
    void shouldMapCreateRequestToNotification() {
        NotificationDTO.CreateRequest request = NotificationDTO.CreateRequest.builder()
            .title("Nueva alerta").message("Stock crítico")
            .type("ORDER_ALERT").requiredRole("ADMIN")
            .build();

        Notification result = mapper.fromCreateRequest(request);

        assertNotNull(result);
        assertEquals("Nueva alerta", result.getTitle());
        assertEquals("Stock crítico", result.getMessage());
        assertEquals(Notification.NotificationType.ORDER_ALERT, result.getType());
        assertEquals("ADMIN", result.getRequiredRole());
        assertFalse(result.getIsRead());
        assertNotNull(result.getCreatedAt());
    }

    @Test
    @DisplayName("✅ fromCreateRequest - null → null")
    void shouldReturnNullWhenRequestIsNull() {
        assertNull(mapper.fromCreateRequest(null));
    }

    @Test
    @DisplayName("🧪 NotificationDTO.Response - builder y getters funcionan")
    void shouldExerciseNotificationDTOResponse() {
        NotificationDTO.Response dto = NotificationDTO.Response.builder()
            .id(10L).title("t").message("m").type("SYSTEM_ALERT")
            .isRead(true).requiredRole("USER").createdAt(now).readAt(now)
            .relatedEntityId("e1").relatedEntityType("ORDER")
            .build();

        assertEquals(10L, dto.getId());
        assertEquals("t", dto.getTitle());
        assertEquals("m", dto.getMessage());
        assertEquals("SYSTEM_ALERT", dto.getType());
        assertTrue(dto.getIsRead());

        dto.setTitle("updated");
        assertEquals("updated", dto.getTitle());
    }

    @Test
    @DisplayName("🧪 NotificationDTO.CreateRequest - builder y getters funcionan")
    void shouldExerciseNotificationDTOCreateRequest() {
        NotificationDTO.CreateRequest req = new NotificationDTO.CreateRequest("t", "m", "USER_ALERT", "ADMIN");

        assertEquals("t", req.getTitle());
        assertEquals("m", req.getMessage());
        assertEquals("USER_ALERT", req.getType());
        assertEquals("ADMIN", req.getRequiredRole());

        req.setMessage("updated");
        assertEquals("updated", req.getMessage());
    }

    @Test
    @DisplayName("🧪 Notification.isForRole - null requiredRole siempre retorna true")
    void shouldReturnTrueForAnyRoleWhenRequiredRoleIsNull() {
        Notification n = Notification.builder().requiredRole(null).build();
        assertTrue(n.isForRole("ADMIN"));
        assertTrue(n.isForRole("USER"));
    }

    @Test
    @DisplayName("🧪 Notification.isForRole - requiredRole específico verifica igualdad")
    void shouldCheckRoleEquality() {
        Notification n = Notification.builder().requiredRole("ADMIN").build();
        assertTrue(n.isForRole("ADMIN"));
        assertFalse(n.isForRole("USER"));
    }

    @Test
    @DisplayName("🧪 Notification.markAsRead - marca isRead=true y setea readAt")
    void shouldMarkNotificationAsRead() {
        Notification n = Notification.builder().isRead(false).build();
        n.markAsRead();
        assertTrue(n.getIsRead());
        assertNotNull(n.getReadAt());
    }

    @Test
    @DisplayName("🧪 NotificationDTO - outer class constructor ejecutable")
    void shouldInstantiateNotificationDTO() {
        NotificationDTO dto = new NotificationDTO();
        assertNotNull(dto);
    }
}
