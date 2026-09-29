package com.drogueria.bellavista.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTOs para Cliente
 */
public class CustomerDTO {

    public record CreateRequest(

            @NotBlank(message = "El código es requerido")
            @Size(min = 1, max = 50, message = "El código debe tener entre 1 y 50 caracteres")
            String code,

            @NotBlank(message = "El nombre es requerido")
            @Size(min = 3, max = 100, message = "El nombre debe tener entre 3 y 100 caracteres")
            String name,

            @Email(message = "El email debe ser válido")
            String email,

            @Size(max = 20, message = "El teléfono no debe exceder 20 caracteres")
            String phone,

            @Size(max = 255, message = "La dirección no debe exceder 255 caracteres")
            String address,

            @Size(max = 100, message = "La ciudad no debe exceder 100 caracteres")
            String city,

            @Size(max = 10, message = "El código postal no debe exceder 10 caracteres")
            String postalCode,

            @Size(max = 50, message = "El documento no debe exceder 50 caracteres")
            String documentNumber,

            @Size(max = 50, message = "El tipo de documento no debe exceder 50 caracteres")
            String documentType,

            @NotBlank(message = "El tipo de cliente es requerido")
            @Size(max = 50, message = "El tipo de cliente no debe exceder 50 caracteres")
            String customerType,

            @DecimalMin(value = "0.00", inclusive = false, message = "El límite de crédito debe ser mayor a 0")
            BigDecimal creditLimit) {

        public String getCode() { return code; }
        public String getName() { return name; }
        public String getEmail() { return email; }
        public String getPhone() { return phone; }
        public String getAddress() { return address; }
        public String getCity() { return city; }
        public String getPostalCode() { return postalCode; }
        public String getDocumentNumber() { return documentNumber; }
        public String getDocumentType() { return documentType; }
        public String getCustomerType() { return customerType; }
        public BigDecimal getCreditLimit() { return creditLimit; }

        public static Builder builder() {
            return new Builder();
        }

        public static class Builder {
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

            public Builder code(String code) { this.code = code; return this; }
            public Builder name(String name) { this.name = name; return this; }
            public Builder email(String email) { this.email = email; return this; }
            public Builder phone(String phone) { this.phone = phone; return this; }
            public Builder address(String address) { this.address = address; return this; }
            public Builder city(String city) { this.city = city; return this; }
            public Builder postalCode(String postalCode) { this.postalCode = postalCode; return this; }
            public Builder documentNumber(String documentNumber) { this.documentNumber = documentNumber; return this; }
            public Builder documentType(String documentType) { this.documentType = documentType; return this; }
            public Builder customerType(String customerType) { this.customerType = customerType; return this; }
            public Builder creditLimit(BigDecimal creditLimit) { this.creditLimit = creditLimit; return this; }

            public CreateRequest build() {
                return new CreateRequest(code, name, email, phone, address, city, postalCode,
                        documentNumber, documentType, customerType, creditLimit);
            }
        }
    }

    public record UpdateRequest(

            @NotBlank(message = "El código es requerido")
            @Size(min = 1, max = 50, message = "El código debe tener entre 1 y 50 caracteres")
            String code,

            @NotBlank(message = "El nombre es requerido")
            @Size(min = 3, max = 100, message = "El nombre debe tener entre 3 y 100 caracteres")
            String name,

            @Email(message = "El email debe ser válido")
            String email,

            @Size(max = 20, message = "El teléfono no debe exceder 20 caracteres")
            String phone,

            @Size(max = 255, message = "La dirección no debe exceder 255 caracteres")
            String address,

            @Size(max = 100, message = "La ciudad no debe exceder 100 caracteres")
            String city,

            @Size(max = 10, message = "El código postal no debe exceder 10 caracteres")
            String postalCode,

            @Size(max = 50, message = "El documento no debe exceder 50 caracteres")
            String documentNumber,

            @Size(max = 50, message = "El tipo de documento no debe exceder 50 caracteres")
            String documentType,

            @Size(max = 50, message = "El tipo de cliente no debe exceder 50 caracteres")
            String customerType,

            @DecimalMin(value = "0.00", message = "El límite de crédito no puede ser negativo")
            BigDecimal creditLimit,

            Boolean active) {

        public String getCode() { return code; }
        public String getName() { return name; }
        public String getEmail() { return email; }
        public String getPhone() { return phone; }
        public String getAddress() { return address; }
        public String getCity() { return city; }
        public String getPostalCode() { return postalCode; }
        public String getDocumentNumber() { return documentNumber; }
        public String getDocumentType() { return documentType; }
        public String getCustomerType() { return customerType; }
        public BigDecimal getCreditLimit() { return creditLimit; }
        public Boolean getActive() { return active; }

        public static Builder builder() {
            return new Builder();
        }

        public static class Builder {
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
            private Boolean active;

            public Builder code(String code) { this.code = code; return this; }
            public Builder name(String name) { this.name = name; return this; }
            public Builder email(String email) { this.email = email; return this; }
            public Builder phone(String phone) { this.phone = phone; return this; }
            public Builder address(String address) { this.address = address; return this; }
            public Builder city(String city) { this.city = city; return this; }
            public Builder postalCode(String postalCode) { this.postalCode = postalCode; return this; }
            public Builder documentNumber(String documentNumber) { this.documentNumber = documentNumber; return this; }
            public Builder documentType(String documentType) { this.documentType = documentType; return this; }
            public Builder customerType(String customerType) { this.customerType = customerType; return this; }
            public Builder creditLimit(BigDecimal creditLimit) { this.creditLimit = creditLimit; return this; }
            public Builder active(Boolean active) { this.active = active; return this; }

            public UpdateRequest build() {
                return new UpdateRequest(code, name, email, phone, address, city, postalCode,
                        documentNumber, documentType, customerType, creditLimit, active);
            }
        }
    }

    public record Response(
            Long id,
            String code,
            String name,
            String email,
            String phone,
            String address,
            String city,
            String postalCode,
            String documentNumber,
            String documentType,
            String customerType,
            BigDecimal creditLimit,

            @JsonProperty("pendingBalance")
            BigDecimal pendingBalance,

            Boolean active,
            LocalDateTime createdAt,
            LocalDateTime updatedAt) {

        public Long getId() { return id; }
        public String getCode() { return code; }
        public String getName() { return name; }
        public String getEmail() { return email; }
        public String getPhone() { return phone; }
        public String getAddress() { return address; }
        public String getCity() { return city; }
        public String getPostalCode() { return postalCode; }
        public String getDocumentNumber() { return documentNumber; }
        public String getDocumentType() { return documentType; }
        public String getCustomerType() { return customerType; }
        public BigDecimal getCreditLimit() { return creditLimit; }
        public BigDecimal getPendingBalance() { return pendingBalance; }
        public Boolean getActive() { return active; }
        public LocalDateTime getCreatedAt() { return createdAt; }
        public LocalDateTime getUpdatedAt() { return updatedAt; }

        public static Builder builder() {
            return new Builder();
        }

        public static class Builder {
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
            private BigDecimal pendingBalance;
            private Boolean active;
            private LocalDateTime createdAt;
            private LocalDateTime updatedAt;

            public Builder id(Long id) { this.id = id; return this; }
            public Builder code(String code) { this.code = code; return this; }
            public Builder name(String name) { this.name = name; return this; }
            public Builder email(String email) { this.email = email; return this; }
            public Builder phone(String phone) { this.phone = phone; return this; }
            public Builder address(String address) { this.address = address; return this; }
            public Builder city(String city) { this.city = city; return this; }
            public Builder postalCode(String postalCode) { this.postalCode = postalCode; return this; }
            public Builder documentNumber(String documentNumber) { this.documentNumber = documentNumber; return this; }
            public Builder documentType(String documentType) { this.documentType = documentType; return this; }
            public Builder customerType(String customerType) { this.customerType = customerType; return this; }
            public Builder creditLimit(BigDecimal creditLimit) { this.creditLimit = creditLimit; return this; }
            public Builder pendingBalance(BigDecimal pendingBalance) { this.pendingBalance = pendingBalance; return this; }
            public Builder active(Boolean active) { this.active = active; return this; }
            public Builder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
            public Builder updatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; return this; }

            public Response build() {
                return new Response(id, code, name, email, phone, address, city, postalCode,
                        documentNumber, documentType, customerType, creditLimit, pendingBalance,
                        active, createdAt, updatedAt);
            }
        }
    }
}
