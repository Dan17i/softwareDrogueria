package com.drogueria.bellavista.infrastructure.persistence;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * JPA Entity - Línea de Recepción de Mercancía
 */
@Entity
@Table(name = "goods_receipt_items")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GoodsReceiptItemEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "goods_receipt_id", nullable = false)
    private Long goodsReceiptId;
    
    @Column(name = "product_id", nullable = false)
    private Long productId;
    
    @Column(name = "product_code", nullable = false, length = 50)
    private String productCode;
    
    @Column(name = "product_name", nullable = false, length = 255)
    private String productName;
    
    @Column(name = "ordered_quantity", nullable = false)
    private Integer orderedQuantity;
    
    @Column(name = "received_quantity")
    private Integer receivedQuantity;
    
    @Column(name = "unit_price", precision = 19, scale = 2)
    private BigDecimal unitPrice;
    
    @Column(name = "notes", length = 500)
    private String notes;
}
