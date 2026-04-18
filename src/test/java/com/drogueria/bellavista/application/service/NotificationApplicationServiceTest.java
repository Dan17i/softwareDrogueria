package com.drogueria.bellavista.application.service;

import com.drogueria.bellavista.application.dto.NotificationDTO;
import com.drogueria.bellavista.application.mapper.NotificationMapper;
import com.drogueria.bellavista.domain.model.Notification;
import com.drogueria.bellavista.domain.service.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class NotificationApplicationServiceTest {

    @Mock private NotificationService notificationService;
    @Mock private NotificationMapper mapper;
    @InjectMocks private NotificationApplicationService service;

    private Notification notification;
    private NotificationDTO.Response dto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        notification = Notification.builder()
            .id(1L).title("Alerta").message("msg")
            .type(Notification.NotificationType.INVENTORY_ALERT)
            .isRead(false).requiredRole("WAREHOUSE")
            .createdAt(LocalDateTime.now()).build();

        dto = NotificationDTO.Response.builder()
            .id(1L).title("Alerta").message("msg")
            .type("INVENTORY_ALERT").isRead(false)
            .requiredRole("WAREHOUSE").createdAt(LocalDateTime.now())
            .build();
    }

    @Test
    @DisplayName("✅ getNotificationsForRole - mapea lista a DTOs")
    void shouldGetNotificationsForRole() {
        when(notificationService.getNotificationsForRole("ADMIN")).thenReturn(List.of(notification));
        when(mapper.toResponse(notification)).thenReturn(dto);

        List<NotificationDTO.Response> result = service.getNotificationsForRole("ADMIN");

        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getId());
        verify(notificationService).getNotificationsForRole("ADMIN");
        verify(mapper).toResponse(notification);
    }

    @Test
    @DisplayName("✅ getUnreadNotificationsForRole - mapea lista no leídas")
    void shouldGetUnreadNotifications() {
        when(notificationService.getUnreadNotificationsForRole("USER")).thenReturn(List.of(notification));
        when(mapper.toResponse(notification)).thenReturn(dto);

        List<NotificationDTO.Response> result = service.getUnreadNotificationsForRole("USER");

        assertEquals(1, result.size());
        verify(notificationService).getUnreadNotificationsForRole("USER");
    }

    @Test
    @DisplayName("✅ getAllNotifications - retorna todas mapeadas")
    void shouldGetAllNotifications() {
        when(notificationService.getAllNotifications()).thenReturn(List.of(notification));
        when(mapper.toResponse(notification)).thenReturn(dto);

        List<NotificationDTO.Response> result = service.getAllNotifications();

        assertEquals(1, result.size());
        verify(notificationService).getAllNotifications();
    }

    @Test
    @DisplayName("✅ markAsRead - delega a notificationService")
    void shouldMarkAsRead() {
        doNothing().when(notificationService).markAsRead(1L);

        service.markAsRead(1L);

        verify(notificationService).markAsRead(1L);
    }

    @Test
    @DisplayName("✅ generateInventoryAlerts - delega a notificationService")
    void shouldGenerateInventoryAlerts() {
        doNothing().when(notificationService).generateInventoryAlerts();

        service.generateInventoryAlerts();

        verify(notificationService).generateInventoryAlerts();
    }

    @Test
    @DisplayName("✅ deleteNotification - delega a notificationService")
    void shouldDeleteNotification() {
        doNothing().when(notificationService).deleteNotification(1L);

        service.deleteNotification(1L);

        verify(notificationService).deleteNotification(1L);
    }

    @Test
    @DisplayName("✅ getNotificationsForRole - lista vacía → retorna vacía")
    void shouldReturnEmptyListWhenNoNotifications() {
        when(notificationService.getNotificationsForRole("GUEST")).thenReturn(List.of());

        List<NotificationDTO.Response> result = service.getNotificationsForRole("GUEST");

        assertTrue(result.isEmpty());
    }
}
