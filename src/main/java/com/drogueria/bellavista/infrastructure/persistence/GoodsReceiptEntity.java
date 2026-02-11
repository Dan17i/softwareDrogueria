package com.drogueria.bellavista.infrastructure.persistence;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * JPA Entity - Recepción de Mercancía
 */
@Entity
@Table(name = "goods_receipts",
        indexes = {
                @Index(name = "idx_receipt_number", columnList = "receipt_number", unique = true),
                @Index(name = "idx_order_id", columnList = "order_id"),
                @Index(name = "idx_supplier_id", columnList = "supplier_id"),
                @Index(name = "idx_status", columnList = "status")
        })
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GoodsReceiptEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "receipt_number", nullable = false, unique = true, length = 100)
    private String receiptNumber;
    
    @Column(name = "order_id", nullable = false)
    private Long orderId;
    
    @Column(name = "order_number", length = 100)
    private String orderNumber;
    
    @Column(name = "supplier_id")
    private Long supplierId;
    
    @Column(name = "supplier_code", length = 50)
    private String supplierCode;
    
    @Column(name = "supplier_name", length = 255)
    private String supplierName;
    
    @Column(name = "status", nullable = false, length = 50)
    private String status;
    
    @Column(name = "notes", length = 1000)
    private String notes;
    
    @Column(name = "receipt_date")
    private LocalDateTime receiptDate;
    
    @Column(name = "expected_delivery_date")
    private LocalDateTime expectedDeliveryDate;
    
    @Column(name = "actual_delivery_date")
    private LocalDateTime actualDeliveryDate;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
