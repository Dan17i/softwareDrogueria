package com.drogueria.bellavista.infrastructure.scheduler;

import com.drogueria.bellavista.application.service.NotificationApplicationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Scheduled task for generating inventory alerts.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class InventoryAlertScheduler {

    private final NotificationApplicationService notificationService;

    /**
     * Generate inventory alerts every 6 hours.
     * This runs at 00:00, 06:00, 12:00, and 18:00 every day.
     */
    @Scheduled(cron = "0 0 */6 * * *")
    public void generateInventoryAlerts() {
        try {
            log.info("Starting scheduled inventory alert generation");
            notificationService.generateInventoryAlerts();
            log.info("Completed scheduled inventory alert generation");
        } catch (Exception e) {
            log.error("Error during scheduled inventory alert generation", e);
        }
    }

    /**
     * Generate inventory alerts daily at midnight.
     * This is a backup schedule in case the 6-hour schedule fails.
     */
    @Scheduled(cron = "0 0 0 * * *")
    public void generateDailyInventoryAlerts() {
        try {
            log.info("Starting daily inventory alert generation");
            notificationService.generateInventoryAlerts();
            log.info("Completed daily inventory alert generation");
        } catch (Exception e) {
            log.error("Error during daily inventory alert generation", e);
        }
    }
}