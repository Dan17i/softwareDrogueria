package com.drogueria.bellavista.infrastructure.persistence;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entidad JPA - Cliente
 * Mapea a la tabla "customers" en la base de datos
 */
@Entity
@Table(name = "customers", indexes = {
    @Index(name = "idx_code", columnList = "code", unique = true),
    @Index(name = "idx_email", columnList = "email", unique = true),
    @Index(name = "idx_document", columnList = "document_number", unique = true),
    @Index(name = "idx_active", columnList = "active"),
    @Index(name = "idx_customer_type", columnList = "customer_type")
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true, length = 50)
    private String code;
    
    @Column(nullable = false, length = 100)
    private String name;
    
    @Column(unique = true, length = 100)
    private String email;
    
    @Column(length = 20)
    private String phone;
    
    @Column(length = 255)
    private String address;
    
    @Column(length = 100)
    private String city;
    
    @Column(name = "postal_code", length = 10)
    private String postalCode;
    
    @Column(name = "document_number", unique = true, length = 50)
    private String documentNumber;
    
    @Column(name = "document_type", length = 50)
    private String documentType;
    
    @Column(name = "customer_type", length = 50)
    private String customerType;
    
    @Column(name = "credit_limit", precision = 12, scale = 2)
    private BigDecimal creditLimit;
    
    @Column(name = "pending_balance", precision = 12, scale = 2)
    private BigDecimal pendingBalance;
    
    @Column(nullable = false)
    private Boolean active = true;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
