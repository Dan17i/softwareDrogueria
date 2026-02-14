package com.drogueria.bellavista.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidad de dominio - Orden de Compra
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    
    private Long id;
    private String orderNumber;
    private Long customerId;
    private String customerCode;
    private String customerName;
    private Long supplierId;
    private String supplierCode;
    private String supplierName;
    private String status; // PENDING, COMPLETED, CANCELLED
    private BigDecimal total;
    @Builder.Default
    private List<OrderItem> items = new ArrayList<>();
    private String notes;
    private LocalDateTime orderDate;
    private LocalDateTime expectedDeliveryDate;
    private LocalDateTime actualDeliveryDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    /**
     * Agregar línea a la orden
     */
    public void addItem(OrderItem item) {
        if (item != null) {
            item.calculateSubtotal();
            this.items.add(item);
            recalculateTotal();
        }
    }
    
    /**
     * Recalcular total
     */
    public void recalculateTotal() {
        this.total = this.items.stream()
            .map(OrderItem::getSubtotal)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    
    /**
     * Validar si orden puede ser completada
     */
    public boolean canBeCompleted() {
        return "PENDING".equals(this.status) && this.total.signum() > 0 && !this.items.isEmpty();
    }
    
    /**
     * Marcar como completada
     */
    public void complete() {
        if (!canBeCompleted()) {
            throw new IllegalStateException("La orden no puede ser completada en su estado actual");
        }
        this.status = "COMPLETED";
        this.actualDeliveryDate = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    /**
     * Cancelar orden
     */
    public void cancel() {
        if ("COMPLETED".equals(this.status)) {
            throw new IllegalStateException("No se pueden cancelar órdenes completadas");
        }
        this.status = "CANCELLED";
        this.updatedAt = LocalDateTime.now();
    }
}
