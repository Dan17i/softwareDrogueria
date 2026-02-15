package com.drogueria.bellavista.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidad de dominio - Recepción de Mercancía
 * Vincula un Order con un Supplier para recibir mercancía
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GoodsReceipt {
    
    private Long id;
    private String receiptNumber; // Único, ej: GR-2024-001
    private Long orderId;
    private String orderNumber;
    private Long supplierId;
    private String supplierCode;
    private String supplierName;
    private String status; // PENDING, RECEIVED, PARTIALLY_RECEIVED, REJECTED
    @Builder.Default
    private List<GoodsReceiptItem> items = new ArrayList<>();
    private String notes;
    private LocalDateTime receiptDate;
    private LocalDateTime expectedDeliveryDate;
    private LocalDateTime actualDeliveryDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    /**
     * Agregar línea a la recepción
     */
    public void addItem(GoodsReceiptItem item) {
        if (item != null && item.isValidQuantity()) {
            this.items.add(item);
        }
    }
    
    /**
     * Validar que todas las cantidades recibidas son válidas
     */
    public boolean isValidReceipt() {
        return items != null && items.stream().allMatch(GoodsReceiptItem::isValidQuantity);
    }
    
    /**
     * Validar si fue completamente recibida (todas las cantidades coinciden)
     */
    public boolean isFullyReceived() {
        return items != null && items.stream()
                .allMatch(item -> item.getReceivedQuantity() != null && 
                        item.getReceivedQuantity().equals(item.getOrderedQuantity()));
    }
    
    /**
     * Validar si fue parcialmente recibida
     */
    public boolean isPartiallyReceived() {
        return items != null && items.stream().anyMatch(item -> 
                item.getReceivedQuantity() != null && item.getReceivedQuantity() > 0 && 
                !item.getReceivedQuantity().equals(item.getOrderedQuantity()));
    }
    
    /**
     * Marcar como recibida
     */
    public void receive() {
        if (isFullyReceived()) {
            this.status = "RECEIVED";
            this.actualDeliveryDate = LocalDateTime.now();
        } else if (isPartiallyReceived()) {
            this.status = "PARTIALLY_RECEIVED";
            this.actualDeliveryDate = LocalDateTime.now();
        }
    }
    
    /**
     * Obtener total de productos diferentes
     */
    public Integer getTotalLineItems() {
        return items != null ? items.size() : 0;
    }
    
    /**
     * Obtener cantidad total recibida
     */
    public Integer getTotalReceivedQuantity() {
        return items != null ? items.stream()
                .mapToInt(item -> item.getReceivedQuantity() != null ? item.getReceivedQuantity() : 0)
                .sum() : 0;
    }
}
