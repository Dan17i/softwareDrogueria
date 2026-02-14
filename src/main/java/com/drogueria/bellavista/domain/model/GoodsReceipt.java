package com.drogueria.bellavista.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
/**
 * <h2>Entidad de Dominio - GoodsReceipt</h2>
 *
 * <p>
 * Representa la recepción de mercancía proveniente de un proveedor,
 * asociada a una orden de compra previamente generada.
 * </p>
 *
 * <p>
 * Esta entidad forma parte del núcleo del dominio y encapsula
 * la lógica de negocio relacionada con:
 * </p>
 * <ul>
 *     <li>Validación de cantidades recibidas</li>
 *     <li>Determinación del estado de recepción</li>
 *     <li>Cálculo de totales</li>
 * </ul>
 *
 * <p>
 * No contiene lógica de infraestructura ni persistencia,
 * respetando los principios de Clean Architecture y DDD.
 * </p>
 * @author Daniel Jurado
 * @since 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GoodsReceipt {
    /**
     * Identificador único de la recepción.
     */
    private Long id;
    /**
     * Número único de recepción.
     * Ejemplo: GR-2024-001
     */
    private String receiptNumber; // Único, ej: GR-2024-001
    /**
     * Identificador de la orden de compra asociada.
     */
    private Long orderId;
    /**
     * Número de la orden de compra.
     */
    private String orderNumber;
    /**
     * Identificador del proveedor.
     */
    private Long supplierId;
    /**
     * Código interno del proveedor.
     */
    private String supplierCode;
    /**
     * Nombre o razón social del proveedor.
     */
    private String supplierName;
    /**
     * Estado actual de la recepción.
     *
     * <p>Valores posibles:</p>
     * <ul>
     *     <li>PENDING</li>
     *     <li>RECEIVED</li>
     *     <li>PARTIALLY_RECEIVED</li>
     *     <li>REJECTED</li>
     * </ul>
     */
    private String status;
    /**
     * Lista de líneas o ítems asociados a la recepción.
     */
    @Builder.Default
    private List<GoodsReceiptItem> items = new ArrayList<>();
    /**
     * Observaciones adicionales sobre la recepción.
     */
    private String notes;
    /**
     * Fecha programada de recepción.
     */
    private LocalDateTime receiptDate;
    /**
     * Fecha estimada de entrega por parte del proveedor.
     */
    private LocalDateTime expectedDeliveryDate;
    /**
     * Fecha real en la que se recibió la mercancía.
     */
    private LocalDateTime actualDeliveryDate;
    /**
     * Fecha y hora de creación del registro.
     */
    private LocalDateTime createdAt;
    /**
     * Fecha y hora de la última actualización del registro.
     */
    private LocalDateTime updatedAt;
    /**
     * Agrega una línea a la recepción.
     *
     * <p>
     * Solo se agrega si el ítem no es nulo y su cantidad es válida.
     * </p>
     *
     * @param item línea de recepción a agregar
     */
    public void addItem(GoodsReceiptItem item) {
        if (item != null && item.isValidQuantity()) {
            this.items.add(item);
        }
    }
    /**
     * Verifica que todas las líneas tengan cantidades válidas.
     *
     * @return {@code true} si todas las cantidades recibidas son válidas,
     *         {@code false} en caso contrario.
     */
    public boolean isValidReceipt() {
        return items != null && items.stream().allMatch(GoodsReceiptItem::isValidQuantity);
    }
    /**
     * Determina si la recepción fue completada totalmente.
     *
     * <p>
     * Se considera completa cuando todas las cantidades recibidas
     * coinciden exactamente con las cantidades ordenadas.
     * </p>
     *
     * @return {@code true} si la recepción es total,
     *         {@code false} en caso contrario.
     */
    public boolean isFullyReceived() {
        return items != null && items.stream()
                .allMatch(item -> item.getReceivedQuantity() != null && 
                        item.getReceivedQuantity().equals(item.getOrderedQuantity()));
    }
    /**
     * Determina si la recepción fue parcial.
     *
     * <p>
     * Se considera parcial cuando al menos un ítem fue recibido
     * en cantidad mayor a cero pero diferente a la ordenada.
     * </p>
     *
     * @return {@code true} si la recepción es parcial,
     *         {@code false} en caso contrario.
     */
    public boolean isPartiallyReceived() {
        return items != null && items.stream().anyMatch(item -> 
                item.getReceivedQuantity() != null && item.getReceivedQuantity() > 0 && 
                !item.getReceivedQuantity().equals(item.getOrderedQuantity()));
    }
    /**
     * Marca la recepción según el estado de las cantidades recibidas.
     *
     * <ul>
     *     <li>RECEIVED → Si todas las cantidades coinciden.</li>
     *     <li>PARTIALLY_RECEIVED → Si existe al menos una diferencia.</li>
     * </ul>
     *
     * <p>
     * También establece la fecha real de entrega.
     * </p>
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
     * Obtiene el total de líneas diferentes en la recepción.
     *
     * @return número total de ítems distintos.
     */
    public Integer getTotalLineItems() {
        return items != null ? items.size() : 0;
    }
    /**
     * Calcula la cantidad total de unidades recibidas.
     *
     * @return suma total de cantidades recibidas.
     */
    public Integer getTotalReceivedQuantity() {
        return items != null ? items.stream()
                .mapToInt(item -> item.getReceivedQuantity() != null ? item.getReceivedQuantity() : 0)
                .sum() : 0;
    }
}
