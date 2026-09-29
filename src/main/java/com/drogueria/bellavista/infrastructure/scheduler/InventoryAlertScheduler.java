package com.drogueria.bellavista.infrastructure.scheduler;

import com.drogueria.bellavista.application.service.NotificationApplicationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Scheduled task for generating inventory alerts.
 */
@Component
public class InventoryAlertScheduler {

    private static final Logger log = LoggerFactory.getLogger(InventoryAlertScheduler.class);

    private final NotificationApplicationService notificationService;

    public InventoryAlertScheduler(NotificationApplicationService notificationService) {
        this.notificationService = notificationService;
    }

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