package com.drogueria.bellavista.controller;

import com.drogueria.bellavista.application.dto.NotificationDTO;
import com.drogueria.bellavista.application.service.NotificationApplicationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class NotificationControllerTest {

    @Mock private NotificationApplicationService notificationService;
    @InjectMocks private NotificationController controller;

    private NotificationDTO.Response response;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        response = NotificationDTO.Response.builder()
            .id(1L).title("Alerta").message("Stock bajo")
            .type("INVENTORY_ALERT").isRead(false)
            .requiredRole("WAREHOUSE").createdAt(LocalDateTime.now())
            .build();
    }

    @Test
    @DisplayName("✅ getNotifications - retorna lista y 200")
    void shouldGetNotificationsForRole() {
        when(notificationService.getNotificationsForRole("ADMIN")).thenReturn(List.of(response));

        ResponseEntity<List<NotificationDTO.Response>> result = controller.getNotifications("ADMIN");

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(1, result.getBody().size());
        verify(notificationService).getNotificationsForRole("ADMIN");
    }

    @Test
    @DisplayName("✅ getNotifications - rol USER por defecto")
    void shouldGetNotificationsDefaultRole() {
        when(notificationService.getNotificationsForRole("USER")).thenReturn(List.of());

        ResponseEntity<List<NotificationDTO.Response>> result = controller.getNotifications("USER");

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertTrue(result.getBody().isEmpty());
    }

    @Test
    @DisplayName("✅ getUnreadNotifications - retorna no leídas y 200")
    void shouldGetUnreadNotifications() {
        when(notificationService.getUnreadNotificationsForRole("WAREHOUSE")).thenReturn(List.of(response));

        ResponseEntity<List<NotificationDTO.Response>> result = controller.getUnreadNotifications("WAREHOUSE");

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(1, result.getBody().size());
        verify(notificationService).getUnreadNotificationsForRole("WAREHOUSE");
    }

    @Test
    @DisplayName("✅ getAllNotifications - retorna todas y 200")
    void shouldGetAllNotifications() {
        when(notificationService.getAllNotifications()).thenReturn(List.of(response));

        ResponseEntity<List<NotificationDTO.Response>> result = controller.getAllNotifications();

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(1, result.getBody().size());
        verify(notificationService).getAllNotifications();
    }

    @Test
    @DisplayName("✅ markAsRead - retorna 200 tras marcar")
    void shouldMarkAsRead() {
        doNothing().when(notificationService).markAsRead(1L);

        ResponseEntity<Void> result = controller.markAsRead(1L);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        verify(notificationService).markAsRead(1L);
    }

    @Test
    @DisplayName("✅ generateInventoryAlerts - retorna 200")
    void shouldGenerateInventoryAlerts() {
        doNothing().when(notificationService).generateInventoryAlerts();

        ResponseEntity<Void> result = controller.generateInventoryAlerts();

        assertEquals(HttpStatus.OK, result.getStatusCode());
        verify(notificationService).generateInventoryAlerts();
    }

    @Test
    @DisplayName("✅ deleteNotification - retorna 204")
    void shouldDeleteNotification() {
        doNothing().when(notificationService).deleteNotification(1L);

        ResponseEntity<Void> result = controller.deleteNotification(1L);

        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
        verify(notificationService).deleteNotification(1L);
    }
}
