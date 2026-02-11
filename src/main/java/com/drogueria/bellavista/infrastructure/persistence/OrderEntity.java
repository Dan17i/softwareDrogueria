package com.drogueria.bellavista.infrastructure.persistence;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidad JPA - Orden
 */
@Entity
@Table(name = "orders", indexes = {
    @Index(name = "idx_order_number", columnList = "order_number", unique = true),
    @Index(name = "idx_customer_id", columnList = "customer_id"),
    @Index(name = "idx_status", columnList = "status"),
    @Index(name = "idx_order_date", columnList = "order_date")
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "order_number", nullable = false, unique = true, length = 50)
    private String orderNumber;
    
    @Column(name = "customer_id", nullable = false)
    private Long customerId;
    
    @Column(name = "customer_code", length = 50)
    private String customerCode;
    
    @Column(name = "customer_name", length = 100)
    private String customerName;
    
    @Column(name = "supplier_id")
    private Long supplierId;
    
    @Column(name = "supplier_code", length = 50)
    private String supplierCode;
    
    @Column(name = "supplier_name", length = 100)
    private String supplierName;
    
    @Column(nullable = false, length = 20)
    private String status; // PENDING, COMPLETED, CANCELLED
    
    @Column(precision = 14, scale = 2)
    private BigDecimal total;
    
    @Column(columnDefinition = "TEXT")
    private String notes;
    
    @Column(name = "order_date")
    private LocalDateTime orderDate;
    
    @Column(name = "expected_delivery_date")
    private LocalDateTime expectedDeliveryDate;
    
    @Column(name = "actual_delivery_date")
    private LocalDateTime actualDeliveryDate;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    // No usar @OneToMany porque queremos mantener la simplicidad
    // Los items se cargan por separado en el repositorio
}
