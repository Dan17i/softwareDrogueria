package com.drogueria.bellavista.domain.service;

import com.drogueria.bellavista.domain.model.Notification;
import com.drogueria.bellavista.domain.model.Product;
import com.drogueria.bellavista.domain.repository.NotificationRepository;
import com.drogueria.bellavista.domain.repository.ProductRepository;
import com.drogueria.bellavista.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Servicio de dominio - Casos de uso de Notificaciones
 * Contiene toda la lógica de negocio relacionada con notificaciones del sistema
 */
@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final ProductRepository productRepository;

    /**
     * Obtener todas las notificaciones para un rol específico.
     */
    public List<Notification> getNotificationsForRole(String role) {
        return notificationRepository.findByRequiredRole(role);
    }

    /**
     * Obtener notificaciones no leídas para un rol específico.
     */
    public List<Notification> getUnreadNotificationsForRole(String role) {
        return notificationRepository.findUnreadByRequiredRole(role);
    }

    /**
     * Obtener todas las notificaciones.
     */
    public List<Notification> getAllNotifications() {
        return notificationRepository.findAll();
    }

    /**
     * Marcar notificación como leída.
     */
    public void markAsRead(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId);
        if (notification == null) {
            throw new ResourceNotFoundException("Notificación", "id", notificationId);
        }
        notificationRepository.markAsRead(notificationId);
        log.info("Notificación marcada como leída: ID={}", notificationId);
    }

    /**
     * Generar notificaciones de inventario bajo.
     * Se ejecuta periódicamente para alertar sobre productos con stock crítico.
     */
    public void generateInventoryAlerts() {
        List<Product> productsNeedingRestock = productRepository.findProductsNeedingRestock();

        for (Product product : productsNeedingRestock) {
            // Verificar si ya existe una notificación activa para este producto
            if (!hasActiveAlertForProduct(product.getId())) {
                createInventoryAlert(product);
            }
        }

        log.info("Generadas {} alertas de inventario", productsNeedingRestock.size());
    }

    /**
     * Crear alerta de inventario para un producto.
     */
    private void createInventoryAlert(Product product) {
        Notification alert = Notification.builder()
            .title("Stock Crítico: " + product.getName())
            .message("Quedan solo " + product.getStock() + " unidades en inventario.")
            .type(Notification.NotificationType.INVENTORY_ALERT)
            .createdAt(LocalDateTime.now())
            .isRead(false)
            .requiredRole("WAREHOUSE")
            .relatedEntityId(product.getCode())
            .relatedEntityType("PRODUCT")
            .build();

        notificationRepository.save(alert);
        log.info("Alerta de inventario creada para producto: {}", product.getName());
    }

    /**
     * Verificar si ya existe una alerta activa para un producto.
     */
    private boolean hasActiveAlertForProduct(Long productId) {
        // Esta es una simplificación. En un sistema real, mantendríamos
        // una relación entre notificaciones y productos
        List<Notification> unreadAlerts = notificationRepository.findUnreadByRequiredRole("WAREHOUSE");
        return unreadAlerts.stream()
            .anyMatch(n -> n.getType() == Notification.NotificationType.INVENTORY_ALERT &&
                          n.getMessage().contains("unidades en inventario"));
    }

    /**
     * Resolver alerta de inventario (cuando se reabastece el producto).
     */
    public void resolveInventoryAlert(Long productId) {
        // En una implementación completa, buscaríamos y marcaríamos como resueltas
        // las alertas relacionadas con este producto
        log.info("Resolviendo alertas de inventario para producto: {}", productId);
    }

    /**
     * Eliminar notificación.
     */
    public void deleteNotification(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId);
        if (notification == null) {
            throw new ResourceNotFoundException("Notificación", "id", notificationId);
        }
        notificationRepository.deleteById(notificationId);
        log.info("Notificación eliminada: ID={}", notificationId);
    }
}