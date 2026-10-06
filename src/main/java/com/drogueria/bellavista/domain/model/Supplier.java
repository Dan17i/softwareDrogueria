package com.drogueria.bellavista.domain.model;

import java.util.Objects;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entidad de dominio - Proveedor
 */
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

    public Supplier() {
    }

    public Supplier(Long id, String code, String name, String email, String phone, String address, String city, String postalCode, String documentNumber, String documentType, Integer leadTimeDays, BigDecimal averagePaymentDelay, Boolean active, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.city = city;
        this.postalCode = postalCode;
        this.documentNumber = documentNumber;
        this.documentType = documentType;
        this.leadTimeDays = leadTimeDays;
        this.averagePaymentDelay = averagePaymentDelay;
        this.active = active;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getDocumentNumber() {
        return documentNumber;
    }

    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    public Integer getLeadTimeDays() {
        return leadTimeDays;
    }

    public void setLeadTimeDays(Integer leadTimeDays) {
        this.leadTimeDays = leadTimeDays;
    }

    public BigDecimal getAveragePaymentDelay() {
        return averagePaymentDelay;
    }

    public void setAveragePaymentDelay(BigDecimal averagePaymentDelay) {
        this.averagePaymentDelay = averagePaymentDelay;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Supplier other)) return false;
        return Objects.equals(id, other.id) && Objects.equals(code, other.code) && Objects.equals(name, other.name) && Objects.equals(email, other.email) && Objects.equals(phone, other.phone) && Objects.equals(address, other.address) && Objects.equals(city, other.city) && Objects.equals(postalCode, other.postalCode) && Objects.equals(documentNumber, other.documentNumber) && Objects.equals(documentType, other.documentType) && Objects.equals(leadTimeDays, other.leadTimeDays) && Objects.equals(averagePaymentDelay, other.averagePaymentDelay) && Objects.equals(active, other.active) && Objects.equals(createdAt, other.createdAt) && Objects.equals(updatedAt, other.updatedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, code, name, email, phone, address, city, postalCode, documentNumber, documentType, leadTimeDays, averagePaymentDelay, active, createdAt, updatedAt);
    }

    @Override
    public String toString() {
        return "Supplier(" + "id=" + id + ", code=" + code + ", name=" + name + ", email=" + email + ", phone=" + phone + ", address=" + address + ", city=" + city + ", postalCode=" + postalCode + ", documentNumber=" + documentNumber + ", documentType=" + documentType + ", leadTimeDays=" + leadTimeDays + ", averagePaymentDelay=" + averagePaymentDelay + ", active=" + active + ", createdAt=" + createdAt + ", updatedAt=" + updatedAt + ")";
    }

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

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder code(String code) {
            this.code = code;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder phone(String phone) {
            this.phone = phone;
            return this;
        }

        public Builder address(String address) {
            this.address = address;
            return this;
        }

        public Builder city(String city) {
            this.city = city;
            return this;
        }

        public Builder postalCode(String postalCode) {
            this.postalCode = postalCode;
            return this;
        }

        public Builder documentNumber(String documentNumber) {
            this.documentNumber = documentNumber;
            return this;
        }

        public Builder documentType(String documentType) {
            this.documentType = documentType;
            return this;
        }

        public Builder leadTimeDays(Integer leadTimeDays) {
            this.leadTimeDays = leadTimeDays;
            return this;
        }

        public Builder averagePaymentDelay(BigDecimal averagePaymentDelay) {
            this.averagePaymentDelay = averagePaymentDelay;
            return this;
        }

        public Builder active(Boolean active) {
            this.active = active;
            return this;
        }

        public Builder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Builder updatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public Supplier build() {
            return new Supplier(id, code, name, email, phone, address, city, postalCode, documentNumber, documentType, leadTimeDays, averagePaymentDelay, active, createdAt, updatedAt);
        }
    }

    
    /**
     * Validar si proveedor está disponible
     */
    public boolean isAvailable() {
        return this.active;
    }
}
