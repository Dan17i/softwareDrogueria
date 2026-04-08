package com.drogueria.bellavista.controller;

import com.drogueria.bellavista.application.dto.NotificationDTO;
import com.drogueria.bellavista.application.service.NotificationApplicationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for Notification operations.
 */
@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
@Tag(name = "Notifications", description = "Notification management API")
public class NotificationController {

    private final NotificationApplicationService notificationService;

    /**
     * Get all notifications for the current user's role.
     */
    @GetMapping
    @Operation(summary = "Get notifications for user role",
               description = "Retrieve all notifications accessible to the current user's role")
    public ResponseEntity<List<NotificationDTO.Response>> getNotifications(
            @Parameter(description = "User role (automatically determined from authentication)")
            @RequestParam(required = false, defaultValue = "USER") String role) {
        List<NotificationDTO.Response> notifications = notificationService.getNotificationsForRole(role);
        return ResponseEntity.ok(notifications);
    }

    /**
     * Get unread notifications for the current user's role.
     */
    @GetMapping("/unread")
    @Operation(summary = "Get unread notifications",
               description = "Retrieve unread notifications for the current user's role")
    public ResponseEntity<List<NotificationDTO.Response>> getUnreadNotifications(
            @Parameter(description = "User role (automatically determined from authentication)")
            @RequestParam(required = false, defaultValue = "USER") String role) {
        List<NotificationDTO.Response> notifications = notificationService.getUnreadNotificationsForRole(role);
        return ResponseEntity.ok(notifications);
    }

    /**
     * Get all notifications (admin only).
     */
    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get all notifications (Admin)",
               description = "Retrieve all notifications in the system (Admin access required)")
    public ResponseEntity<List<NotificationDTO.Response>> getAllNotifications() {
        List<NotificationDTO.Response> notifications = notificationService.getAllNotifications();
        return ResponseEntity.ok(notifications);
    }

    /**
     * Mark notification as read.
     */
    @PutMapping("/{id}/read")
    @Operation(summary = "Mark notification as read",
               description = "Mark a specific notification as read")
    public ResponseEntity<Void> markAsRead(
            @Parameter(description = "Notification ID") @PathVariable Long id) {
        notificationService.markAsRead(id);
        return ResponseEntity.ok().build();
    }

    /**
     * Generate inventory alerts (admin only).
     */
    @PostMapping("/generate-inventory-alerts")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Generate inventory alerts (Admin)",
               description = "Generate notifications for products that need restock (Admin access required)")
    public ResponseEntity<Void> generateInventoryAlerts() {
        notificationService.generateInventoryAlerts();
        return ResponseEntity.ok().build();
    }

    /**
     * Delete notification (admin only).
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete notification (Admin)",
               description = "Delete a specific notification (Admin access required)")
    public ResponseEntity<Void> deleteNotification(
            @Parameter(description = "Notification ID") @PathVariable Long id) {
        notificationService.deleteNotification(id);
        return ResponseEntity.noContent().build();
    }
}