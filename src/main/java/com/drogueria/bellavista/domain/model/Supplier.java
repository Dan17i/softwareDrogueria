package com.drogueria.bellavista.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entidad de dominio - Proveedor
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Supplier {
    
    private Long id;
    private String code;
    private String name;
    private String email;
    private String phone;
    private String address;
    private String city;
    private String postalCode;
    private String documentNumber;
    private String documentType;
    private Integer leadTimeDays; // Días de entrega
    private BigDecimal averagePaymentDelay; // Promedio de retraso en pago
    private Boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    /**
     * Validar si proveedor está disponible
     */
    public boolean isAvailable() {
        return this.active;
    }
}
