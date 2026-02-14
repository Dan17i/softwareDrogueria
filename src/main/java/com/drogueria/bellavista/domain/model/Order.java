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
 * <h2>Entidad de Dominio - Order</h2>
 *
 * <p>
 * Representa una orden de compra dentro del sistema.
 * Una orden agrupa múltiples líneas de productos y
 * establece la relación comercial entre un cliente y/o proveedor.
 * </p>
 *
 * <p>
 * Esta entidad encapsula reglas de negocio relacionadas con:
 * </p>
 * <ul>
 *     <li>Gestión de líneas de orden</li>
 *     <li>Cálculo del total</li>
 *     <li>Control de estados (PENDING, COMPLETED, CANCELLED)</li>
 * </ul>
 *
 * <p>
 * Forma parte del núcleo del dominio y no contiene lógica
 * de persistencia ni dependencias de infraestructura.
 * </p>
 * @author Daniel Jurado & equipo de desarrollo
 * @since 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    /**
     * Identificador único de la orden.
     */
    private Long id;
    /**
     * Número único de la orden.
     */
    private String orderNumber;
    /**
     * Identificador del cliente asociado.
     */
    private Long customerId;
    /**
     * Código interno del cliente.
     */
    private String customerCode;
    /**
     * Nombre del cliente.
     */
    private String customerName;
    /**
     * Identificador del proveedor asociado.
     */
    private Long supplierId;
    /**
     * Código interno del proveedor.
     */
    private String supplierCode;
    /**
     * Nombre del proveedor.
     */
    private String supplierName;
    /**
     * Estado actual de la orden.
     *
     * <p>Valores posibles:</p>
     * <ul>
     *     <li>PENDING</li>
     *     <li>COMPLETED</li>
     *     <li>CANCELLED</li>
     * </ul>
     */
    private String status;
    /**
     * Valor total acumulado de la orden.
     */
    private BigDecimal total;
    /**
     * Lista de líneas asociadas a la orden.
     */
    @Builder.Default
    private List<OrderItem> items = new ArrayList<>();
    /**
     * Observaciones adicionales.
     */
    private String notes;
    /**
     * Fecha en que se realizó la orden.
     */
    private LocalDateTime orderDate;
    /**
     * Fecha estimada de entrega.
     */
    private LocalDateTime expectedDeliveryDate;
    /**
     * Fecha real de entrega o finalización.
     */
    private LocalDateTime actualDeliveryDate;
    /**
     * Fecha y hora de creación del registro.
     */
    private LocalDateTime createdAt;
    /**
     * Fecha y hora de la última actualización.
     */
    private LocalDateTime updatedAt;
    /**
     * Agrega una línea a la orden.
     *
     * <p>
     * Antes de agregarla, calcula su subtotal y posteriormente
     * recalcula el total general de la orden.
     * </p>
     *
     * @param item línea de orden a agregar
     */
    public void addItem(OrderItem item) {
        if (item != null) {
            item.calculateSubtotal();
            this.items.add(item);
            recalculateTotal();
        }
    }
    /**
     * Recalcula el total general de la orden
     * sumando los subtotales de todas sus líneas.
     */
    public void recalculateTotal() {
        this.total = this.items.stream()
            .map(OrderItem::getSubtotal)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    /**
     * Determina si la orden puede ser completada.
     *
     * <p>
     * Condiciones:
     * </p>
     * <ul>
     *     <li>Debe estar en estado PENDING.</li>
     *     <li>Debe tener al menos una línea.</li>
     *     <li>El total debe ser mayor a cero.</li>
     * </ul>
     *
     * @return {@code true} si puede completarse,
     *         {@code false} en caso contrario.
     */
    public boolean canBeCompleted() {
        return "PENDING".equals(this.status) && this.total.signum() > 0 && !this.items.isEmpty();
    }
    /**
     * Marca la orden como completada.
     *
     * @throws IllegalStateException si la orden no cumple
     * las condiciones necesarias para completarse.
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
     * Cancela la orden.
     *
     * <p>
     * No es posible cancelar una orden que ya fue completada.
     * </p>
     *
     * @throws IllegalStateException si la orden ya está completada.
     */

    public void cancel() {
        if ("COMPLETED".equals(this.status)) {
            throw new IllegalStateException("No se pueden cancelar órdenes completadas");
        }
        this.status = "CANCELLED";
        this.updatedAt = LocalDateTime.now();
    }
}
