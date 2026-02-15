package com.drogueria.bellavista.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Entidad de dominio - Línea de Recepción de Mercancía
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GoodsReceiptItem {
    
    private Long id;
    private Long productId;
    private String productCode;
    private String productName;
    private Integer orderedQuantity; // Cantidad ordenada
    private Integer receivedQuantity; // Cantidad recibida
    private BigDecimal unitPrice;
    private String notes;
    
    /**
     * Validar que la cantidad recibida no supera la ordenada
     */
    public boolean isValidQuantity() {
        return receivedQuantity != null && receivedQuantity <= orderedQuantity;
    }
    
    /**
     * Calcular diferencia entre ordenado y recibido
     */
    public Integer getDifference() {
        if (receivedQuantity == null) return 0;
        return orderedQuantity - receivedQuantity;
    }
}
