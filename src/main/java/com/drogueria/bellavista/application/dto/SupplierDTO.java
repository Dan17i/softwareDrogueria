package com.drogueria.bellavista.application.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTOs para Proveedor
 */
public class SupplierDTO {

    public record CreateRequest(

            @NotBlank(message = "El código es requerido")
            @Size(min = 1, max = 50)
            String code,

            @NotBlank(message = "El nombre es requerido")
            @Size(min = 3, max = 100)
            String name,

            @Email(message = "Email debe ser válido")
            String email,

            @Size(max = 20)
            String phone,

            @Size(max = 255)
            String address,

            @Size(max = 100)
            String city,

            @Size(max = 10)
            String postalCode,

            @Size(max = 50)
            String documentNumber,

            @Size(max = 50)
            String documentType,

            @Min(value = 0, message = "Lead time no puede ser negativo")
            Integer leadTimeDays,

            @DecimalMin(value = "0.00")
            BigDecimal averagePaymentDelay) {

        public String getCode() { return code; }
        public String getName() { return name; }
        public String getEmail() { return email; }
        public String getPhone() { return phone; }
        public String getAddress() { return address; }
        public String getCity() { return city; }
        public String getPostalCode() { return postalCode; }
        public String getDocumentNumber() { return documentNumber; }
        public String getDocumentType() { return documentType; }
        public Integer getLeadTimeDays() { return leadTimeDays; }
        public BigDecimal getAveragePaymentDelay() { return averagePaymentDelay; }

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
            private Integer leadTimeDays;
            private BigDecimal averagePaymentDelay;

            public Builder code(String code) { this.code = code; return this; }
            public Builder name(String name) { this.name = name; return this; }
            public Builder email(String email) { this.email = email; return this; }
            public Builder phone(String phone) { this.phone = phone; return this; }
            public Builder address(String address) { this.address = address; return this; }
            public Builder city(String city) { this.city = city; return this; }
            public Builder postalCode(String postalCode) { this.postalCode = postalCode; return this; }
            public Builder documentNumber(String documentNumber) { this.documentNumber = documentNumber; return this; }
            public Builder documentType(String documentType) { this.documentType = documentType; return this; }
            public Builder leadTimeDays(Integer leadTimeDays) { this.leadTimeDays = leadTimeDays; return this; }
            public Builder averagePaymentDelay(BigDecimal averagePaymentDelay) { this.averagePaymentDelay = averagePaymentDelay; return this; }

            public CreateRequest build() {
                return new CreateRequest(code, name, email, phone, address, city, postalCode,
                        documentNumber, documentType, leadTimeDays, averagePaymentDelay);
            }
        }
    }

    public record UpdateRequest(

            @NotBlank(message = "El código es requerido")
            @Size(min = 1, max = 50)
            String code,

            @NotBlank(message = "El nombre es requerido")
            @Size(min = 3, max = 100)
            String name,

            @Email
            String email,

            @Size(max = 20)
            String phone,

            @Size(max = 255)
            String address,

            @Size(max = 100)
            String city,

            @Size(max = 10)
            String postalCode,

            @Size(max = 50)
            String documentNumber,

            @Size(max = 50)
            String documentType,

            @Min(value = 0)
            Integer leadTimeDays,

            @DecimalMin(value = "0.00")
            BigDecimal averagePaymentDelay,

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
        public Integer getLeadTimeDays() { return leadTimeDays; }
        public BigDecimal getAveragePaymentDelay() { return averagePaymentDelay; }
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
            private Integer leadTimeDays;
            private BigDecimal averagePaymentDelay;
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
            public Builder leadTimeDays(Integer leadTimeDays) { this.leadTimeDays = leadTimeDays; return this; }
            public Builder averagePaymentDelay(BigDecimal averagePaymentDelay) { this.averagePaymentDelay = averagePaymentDelay; return this; }
            public Builder active(Boolean active) { this.active = active; return this; }

            public UpdateRequest build() {
                return new UpdateRequest(code, name, email, phone, address, city, postalCode,
                        documentNumber, documentType, leadTimeDays, averagePaymentDelay, active);
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
            Integer leadTimeDays,
            BigDecimal averagePaymentDelay,
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
        public Integer getLeadTimeDays() { return leadTimeDays; }
        public BigDecimal getAveragePaymentDelay() { return averagePaymentDelay; }
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
            private Integer leadTimeDays;
            private BigDecimal averagePaymentDelay;
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
            public Builder leadTimeDays(Integer leadTimeDays) { this.leadTimeDays = leadTimeDays; return this; }
            public Builder averagePaymentDelay(BigDecimal averagePaymentDelay) { this.averagePaymentDelay = averagePaymentDelay; return this; }
            public Builder active(Boolean active) { this.active = active; return this; }
            public Builder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
            public Builder updatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; return this; }

            public Response build() {
                return new Response(id, code, name, email, phone, address, city, postalCode,
                        documentNumber, documentType, leadTimeDays, averagePaymentDelay, active,
                        createdAt, updatedAt);
            }
        }
    }
}
