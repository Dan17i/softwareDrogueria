package com.drogueria.bellavista.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
/**
 * <h2>Entidad de Dominio - GoodsReceiptItem</h2>
 *
 * <p>
 * Representa una línea individual dentro de una recepción de mercancía.
 * Cada instancia corresponde a un producto específico incluido en la orden
 * de compra y recibido por parte del proveedor.
 * </p>
 *
 * <p>
 * Esta entidad encapsula reglas básicas de validación relacionadas con:
 * </p>
 * <ul>
 *     <li>Control de cantidades recibidas</li>
 *     <li>Cálculo de diferencias entre lo ordenado y lo recibido</li>
 * </ul>
 *
 * <p>
 * Forma parte del modelo de dominio y no contiene lógica de persistencia
 * ni dependencias externas.
 * </p>
 * @author Daniel Jurado & equipo de desarrollo
 * @since 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GoodsReceiptItem {
    /**
     * Identificador único de la línea de recepción.
     */
    private Long id;
    /**
     * Identificador del producto asociado.
     */
    private Long productId;
    /**
     * Código interno del producto.
     */
    private String productCode;
    /**
     * Nombre descriptivo del producto.
     */
    private String productName;
    /**
     * Cantidad solicitada en la orden de compra.
     */
    private Integer orderedQuantity; // Cantidad ordenada
    /**
     * Cantidad efectivamente recibida del proveedor.
     */
    private Integer receivedQuantity; // Cantidad recibida
    /**
     * Precio unitario del producto en el momento de la recepción.
     */
    private BigDecimal unitPrice;
    /**
     * Observaciones adicionales sobre la línea de recepción
     * (daños, faltantes, inconsistencias, etc.).
     */
    private String notes;
    /**
     * Valida que la cantidad recibida sea coherente con la cantidad ordenada.
     *
     * <p>
     * Una cantidad es válida cuando:
     * </p>
     * <ul>
     *     <li>No es nula.</li>
     *     <li>No supera la cantidad ordenada.</li>
     * </ul>
     *
     * @return {@code true} si la cantidad recibida es válida,
     *         {@code false} en caso contrario.
     */
    public boolean isValidQuantity() {
        return receivedQuantity != null && receivedQuantity <= orderedQuantity;
    }
    /**
     * Calcula la diferencia entre la cantidad ordenada y la recibida.
     *
     * <p>
     * Si la cantidad recibida es nula, se considera como cero.
     * </p>
     *
     * @return cantidad faltante por recibir.
     */
    public Integer getDifference() {
        if (receivedQuantity == null) return 0;
        return orderedQuantity - receivedQuantity;
    }
}
