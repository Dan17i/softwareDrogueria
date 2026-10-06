package com.drogueria.bellavista.infrastructure.persistence;

import java.util.Objects;
import jakarta.persistence.*;

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

    public CustomerEntity() {
    }

    public CustomerEntity(Long id, String code, String name, String email, String phone, String address, String city, String postalCode, String documentNumber, String documentType, String customerType, BigDecimal creditLimit, BigDecimal pendingBalance, Boolean active, LocalDateTime createdAt, LocalDateTime updatedAt) {
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
        this.customerType = customerType;
        this.creditLimit = creditLimit;
        this.pendingBalance = pendingBalance;
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

    public String getCustomerType() {
        return customerType;
    }

    public void setCustomerType(String customerType) {
        this.customerType = customerType;
    }

    public BigDecimal getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(BigDecimal creditLimit) {
        this.creditLimit = creditLimit;
    }

    public BigDecimal getPendingBalance() {
        return pendingBalance;
    }

    public void setPendingBalance(BigDecimal pendingBalance) {
        this.pendingBalance = pendingBalance;
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
        if (!(o instanceof CustomerEntity other)) return false;
        return Objects.equals(id, other.id) && Objects.equals(code, other.code) && Objects.equals(name, other.name) && Objects.equals(email, other.email) && Objects.equals(phone, other.phone) && Objects.equals(address, other.address) && Objects.equals(city, other.city) && Objects.equals(postalCode, other.postalCode) && Objects.equals(documentNumber, other.documentNumber) && Objects.equals(documentType, other.documentType) && Objects.equals(customerType, other.customerType) && Objects.equals(creditLimit, other.creditLimit) && Objects.equals(pendingBalance, other.pendingBalance) && Objects.equals(active, other.active) && Objects.equals(createdAt, other.createdAt) && Objects.equals(updatedAt, other.updatedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, code, name, email, phone, address, city, postalCode, documentNumber, documentType, customerType, creditLimit, pendingBalance, active, createdAt, updatedAt);
    }

    @Override
    public String toString() {
        return "CustomerEntity(" + "id=" + id + ", code=" + code + ", name=" + name + ", email=" + email + ", phone=" + phone + ", address=" + address + ", city=" + city + ", postalCode=" + postalCode + ", documentNumber=" + documentNumber + ", documentType=" + documentType + ", customerType=" + customerType + ", creditLimit=" + creditLimit + ", pendingBalance=" + pendingBalance + ", active=" + active + ", createdAt=" + createdAt + ", updatedAt=" + updatedAt + ")";
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
        private String customerType;
        private BigDecimal creditLimit;
        private BigDecimal pendingBalance;
        private Boolean active = true;
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

        public Builder customerType(String customerType) {
            this.customerType = customerType;
            return this;
        }

        public Builder creditLimit(BigDecimal creditLimit) {
            this.creditLimit = creditLimit;
            return this;
        }

        public Builder pendingBalance(BigDecimal pendingBalance) {
            this.pendingBalance = pendingBalance;
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

        public CustomerEntity build() {
            return new CustomerEntity(id, code, name, email, phone, address, city, postalCode, documentNumber, documentType, customerType, creditLimit, pendingBalance, active, createdAt, updatedAt);
        }
    }

}
