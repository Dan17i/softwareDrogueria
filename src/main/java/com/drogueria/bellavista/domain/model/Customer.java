package com.drogueria.bellavista.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entidad de dominio - Cliente
 * Representa el modelo de negocio puro de un cliente
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Customer {
    
    private Long id;
    private String code;
    private String name;
    private String email;
    private String phone;
    private String address;
    private String city;
    private String postalCode;
    private String documentNumber;
    private String documentType; // RUT, CC, etc.
    private String customerType; // MAYORISTA, MINORISTA
    private BigDecimal creditLimit;
    private BigDecimal pendingBalance;
    private Boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    /**
     * Lógica de negocio: Validar si tiene crédito disponible
     */
    public boolean hasCreditAvailable(BigDecimal amount) {
        if (!this.active || this.creditLimit == null) {
            return false;
        }
        BigDecimal availableCredit = this.creditLimit.subtract(this.pendingBalance != null ? this.pendingBalance : BigDecimal.ZERO);
        return amount.compareTo(availableCredit) <= 0;
    }
    
    /**
     * Lógica de negocio: Aumentar saldo pendiente
     */
    public void increasePendingBalance(BigDecimal amount) {
        if (amount == null || amount.signum() <= 0) {
            throw new IllegalArgumentException("El monto debe ser mayor a 0");
        }
        this.pendingBalance = (this.pendingBalance != null ? this.pendingBalance : BigDecimal.ZERO).add(amount);
        this.updatedAt = LocalDateTime.now();
    }
    
    /**
     * Lógica de negocio: Disminuir saldo pendiente (pago)
     */
    public void reducePendingBalance(BigDecimal amount) {
        if (amount == null || amount.signum() <= 0) {
            throw new IllegalArgumentException("El monto debe ser mayor a 0");
        }
        if (this.pendingBalance == null) {
            this.pendingBalance = BigDecimal.ZERO;
        }
        if (this.pendingBalance.compareTo(amount) < 0) {
            throw new IllegalStateException("El pago excede el saldo pendiente");
        }
        this.pendingBalance = this.pendingBalance.subtract(amount);
        this.updatedAt = LocalDateTime.now();
    }
    
    /**
     * Lógica de negocio: Validar si está moroso
     */
    public boolean isMoroso() {
        return this.active && this.pendingBalance != null && this.pendingBalance.signum() > 0;
    }
}
