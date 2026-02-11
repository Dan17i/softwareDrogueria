package com.drogueria.bellavista.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTOs para Cliente
 */
public class CustomerDTO {
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateRequest {
        
        @NotBlank(message = "El código es requerido")
        @Size(min = 1, max = 50, message = "El código debe tener entre 1 y 50 caracteres")
        private String code;
        
        @NotBlank(message = "El nombre es requerido")
        @Size(min = 3, max = 100, message = "El nombre debe tener entre 3 y 100 caracteres")
        private String name;
        
        @Email(message = "El email debe ser válido")
        private String email;
        
        @Size(max = 20, message = "El teléfono no debe exceder 20 caracteres")
        private String phone;
        
        @Size(max = 255, message = "La dirección no debe exceder 255 caracteres")
        private String address;
        
        @Size(max = 100, message = "La ciudad no debe exceder 100 caracteres")
        private String city;
        
        @Size(max = 10, message = "El código postal no debe exceder 10 caracteres")
        private String postalCode;
        
        @Size(max = 50, message = "El documento no debe exceder 50 caracteres")
        private String documentNumber;
        
        @Size(max = 50, message = "El tipo de documento no debe exceder 50 caracteres")
        private String documentType;
        
        @NotBlank(message = "El tipo de cliente es requerido")
        @Size(max = 50, message = "El tipo de cliente no debe exceder 50 caracteres")
        private String customerType; // MAYORISTA, MINORISTA
        
        @DecimalMin(value = "0.00", inclusive = false, message = "El límite de crédito debe ser mayor a 0")
        private BigDecimal creditLimit;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateRequest {
        
        @NotBlank(message = "El código es requerido")
        @Size(min = 1, max = 50, message = "El código debe tener entre 1 y 50 caracteres")
        private String code;
        
        @NotBlank(message = "El nombre es requerido")
        @Size(min = 3, max = 100, message = "El nombre debe tener entre 3 y 100 caracteres")
        private String name;
        
        @Email(message = "El email debe ser válido")
        private String email;
        
        @Size(max = 20, message = "El teléfono no debe exceder 20 caracteres")
        private String phone;
        
        @Size(max = 255, message = "La dirección no debe exceder 255 caracteres")
        private String address;
        
        @Size(max = 100, message = "La ciudad no debe exceder 100 caracteres")
        private String city;
        
        @Size(max = 10, message = "El código postal no debe exceder 10 caracteres")
        private String postalCode;
        
        @Size(max = 50, message = "El documento no debe exceder 50 caracteres")
        private String documentNumber;
        
        @Size(max = 50, message = "El tipo de documento no debe exceder 50 caracteres")
        private String documentType;
        
        @Size(max = 50, message = "El tipo de cliente no debe exceder 50 caracteres")
        private String customerType;
        
        @DecimalMin(value = "0.00", message = "El límite de crédito no puede ser negativo")
        private BigDecimal creditLimit;
        
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
        private String customerType;
        private BigDecimal creditLimit;
        
        @JsonProperty("pendingBalance")
        private BigDecimal pendingBalance;
        
        private Boolean active;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
    }
}
