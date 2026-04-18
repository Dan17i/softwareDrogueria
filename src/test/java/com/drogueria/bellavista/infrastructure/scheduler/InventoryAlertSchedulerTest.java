package com.drogueria.bellavista.infrastructure.scheduler;

import com.drogueria.bellavista.application.service.NotificationApplicationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.*;

class InventoryAlertSchedulerTest {

    @Mock private NotificationApplicationService notificationService;
    @InjectMocks private InventoryAlertScheduler scheduler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("✅ generateInventoryAlerts - delega a notificationService")
    void shouldDelegateToNotificationService() {
        doNothing().when(notificationService).generateInventoryAlerts();

        scheduler.generateInventoryAlerts();

        verify(notificationService).generateInventoryAlerts();
    }

    @Test
    @DisplayName("✅ generateDailyInventoryAlerts - delega a notificationService")
    void shouldDelegateDailyToNotificationService() {
        doNothing().when(notificationService).generateInventoryAlerts();

        scheduler.generateDailyInventoryAlerts();

        verify(notificationService).generateInventoryAlerts();
    }

    @Test
    @DisplayName("❌ generateInventoryAlerts - excepción en service → no propaga error")
    void shouldCatchExceptionFromService() {
        doThrow(new RuntimeException("DB error")).when(notificationService).generateInventoryAlerts();

        assertDoesNotThrow(() -> scheduler.generateInventoryAlerts());
    }

    @Test
    @DisplayName("❌ generateDailyInventoryAlerts - excepción en service → no propaga error")
    void shouldCatchExceptionFromDailyService() {
        doThrow(new RuntimeException("timeout")).when(notificationService).generateInventoryAlerts();

        assertDoesNotThrow(() -> scheduler.generateDailyInventoryAlerts());
    }
}
