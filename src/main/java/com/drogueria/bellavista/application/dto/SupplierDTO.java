package com.drogueria.bellavista.application.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTOs para Proveedor
 */
public class SupplierDTO {
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateRequest {
        
        @NotBlank(message = "El código es requerido")
        @Size(min = 1, max = 50)
        private String code;
        
        @NotBlank(message = "El nombre es requerido")
        @Size(min = 3, max = 100)
        private String name;
        
        @Email(message = "Email debe ser válido")
        private String email;
        
        @Size(max = 20)
        private String phone;
        
        @Size(max = 255)
        private String address;
        
        @Size(max = 100)
        private String city;
        
        @Size(max = 10)
        private String postalCode;
        
        @Size(max = 50)
        private String documentNumber;
        
        @Size(max = 50)
        private String documentType;
        
        @Min(value = 0, message = "Lead time no puede ser negativo")
        private Integer leadTimeDays;
        
        @DecimalMin(value = "0.00")
        private BigDecimal averagePaymentDelay;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateRequest {
        
        @NotBlank(message = "El código es requerido")
        @Size(min = 1, max = 50)
        private String code;
        
        @NotBlank(message = "El nombre es requerido")
        @Size(min = 3, max = 100)
        private String name;
        
        @Email
        private String email;
        
        @Size(max = 20)
        private String phone;
        
        @Size(max = 255)
        private String address;
        
        @Size(max = 100)
        private String city;
        
        @Size(max = 10)
        private String postalCode;
        
        @Size(max = 50)
        private String documentNumber;
        
        @Size(max = 50)
        private String documentType;
        
        @Min(value = 0)
        private Integer leadTimeDays;
        
        @DecimalMin(value = "0.00")
        private BigDecimal averagePaymentDelay;
        
        private Boolean active;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
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
        private Integer leadTimeDays;
        private BigDecimal averagePaymentDelay;
        private Boolean active;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
    }
}
