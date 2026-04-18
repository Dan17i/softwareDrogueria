package com.drogueria.bellavista.domain.service;

import com.drogueria.bellavista.domain.model.Notification;
import com.drogueria.bellavista.domain.model.Product;
import com.drogueria.bellavista.domain.repository.NotificationRepository;
import com.drogueria.bellavista.domain.repository.ProductRepository;
import com.drogueria.bellavista.exception.ResourceNotFoundException;
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

class NotificationServiceTest {

    @Mock private NotificationRepository notificationRepository;
    @Mock private ProductRepository productRepository;
    @InjectMocks private NotificationService notificationService;

    private Notification notification;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        notification = Notification.builder()
            .id(1L).title("Alerta").message("msg")
            .type(Notification.NotificationType.INVENTORY_ALERT)
            .isRead(false).requiredRole("WAREHOUSE")
            .createdAt(LocalDateTime.now()).build();
    }

    @Test
    @DisplayName("✅ getNotificationsForRole - delega a repository")
    void shouldGetNotificationsForRole() {
        when(notificationRepository.findByRequiredRole("ADMIN")).thenReturn(List.of(notification));

        List<Notification> result = notificationService.getNotificationsForRole("ADMIN");

        assertEquals(1, result.size());
        verify(notificationRepository).findByRequiredRole("ADMIN");
    }

    @Test
    @DisplayName("✅ getUnreadNotificationsForRole - delega a repository")
    void shouldGetUnreadForRole() {
        when(notificationRepository.findUnreadByRequiredRole("USER")).thenReturn(List.of(notification));

        List<Notification> result = notificationService.getUnreadNotificationsForRole("USER");

        assertEquals(1, result.size());
        verify(notificationRepository).findUnreadByRequiredRole("USER");
    }

    @Test
    @DisplayName("✅ getAllNotifications - retorna todas")
    void shouldGetAllNotifications() {
        when(notificationRepository.findAll()).thenReturn(List.of(notification));

        List<Notification> result = notificationService.getAllNotifications();

        assertEquals(1, result.size());
        verify(notificationRepository).findAll();
    }

    @Test
    @DisplayName("✅ markAsRead - notificación existe → invoca markAsRead en repository")
    void shouldMarkAsReadWhenExists() {
        when(notificationRepository.findById(1L)).thenReturn(notification);

        notificationService.markAsRead(1L);

        verify(notificationRepository).markAsRead(1L);
    }

    @Test
    @DisplayName("❌ markAsRead - no existe → lanza ResourceNotFoundException")
    void shouldThrowWhenNotificationNotFoundOnMarkAsRead() {
        when(notificationRepository.findById(99L)).thenReturn(null);

        assertThrows(ResourceNotFoundException.class, () -> notificationService.markAsRead(99L));
        verify(notificationRepository, never()).markAsRead(any());
    }

    @Test
    @DisplayName("✅ deleteNotification - existe → elimina del repository")
    void shouldDeleteNotification() {
        when(notificationRepository.findById(1L)).thenReturn(notification);

        notificationService.deleteNotification(1L);

        verify(notificationRepository).deleteById(1L);
    }

    @Test
    @DisplayName("❌ deleteNotification - no existe → lanza ResourceNotFoundException")
    void shouldThrowWhenNotFoundOnDelete() {
        when(notificationRepository.findById(99L)).thenReturn(null);

        assertThrows(ResourceNotFoundException.class, () -> notificationService.deleteNotification(99L));
        verify(notificationRepository, never()).deleteById(any());
    }

    @Test
    @DisplayName("✅ generateInventoryAlerts - productos que necesitan restock → crea alertas")
    void shouldGenerateAlertsForProductsNeedingRestock() {
        Product product = Product.builder()
            .id(10L).name("Acetaminofén").code("MED-001").stock(3).build();
        when(productRepository.findProductsNeedingRestock()).thenReturn(List.of(product));
        when(notificationRepository.findUnreadByRequiredRole("WAREHOUSE")).thenReturn(List.of());
        when(notificationRepository.save(any())).thenReturn(notification);

        notificationService.generateInventoryAlerts();

        verify(notificationRepository).save(any(Notification.class));
    }

    @Test
    @DisplayName("✅ generateInventoryAlerts - alerta activa ya existe → no crea duplicado")
    void shouldNotDuplicateAlertWhenActiveAlertExists() {
        Product product = Product.builder()
            .id(10L).name("Ibuprofeno").code("MED-002").stock(2).build();
        Notification existingAlert = Notification.builder()
            .type(Notification.NotificationType.INVENTORY_ALERT)
            .message("Quedan 2 unidades en inventario.")
            .isRead(false).requiredRole("WAREHOUSE").build();
        when(productRepository.findProductsNeedingRestock()).thenReturn(List.of(product));
        when(notificationRepository.findUnreadByRequiredRole("WAREHOUSE")).thenReturn(List.of(existingAlert));

        notificationService.generateInventoryAlerts();

        verify(notificationRepository, never()).save(any());
    }

    @Test
    @DisplayName("✅ generateInventoryAlerts - sin productos → no crea alertas")
    void shouldNotGenerateAlertsWhenNoProducts() {
        when(productRepository.findProductsNeedingRestock()).thenReturn(List.of());

        notificationService.generateInventoryAlerts();

        verify(notificationRepository, never()).save(any());
    }

    @Test
    @DisplayName("✅ resolveInventoryAlert - no lanza excepción")
    void shouldResolveInventoryAlertWithoutError() {
        assertDoesNotThrow(() -> notificationService.resolveInventoryAlert(1L));
    }
}
