package com.drogueria.bellavista.infrastructure.persistence;

import java.util.Objects;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "password_reset_tokens")
public class PasswordResetTokenEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true, length = 100)
    private String token;
    
    @Column(name = "user_id", nullable = false)
    private Long userId;
    
    @Column(name = "expiry_date", nullable = false)
    private LocalDateTime expiryDate;
    
    @Column(nullable = false)
    private Boolean used = false;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public PasswordResetTokenEntity() {
    }

    public PasswordResetTokenEntity(Long id, String token, Long userId, LocalDateTime expiryDate, Boolean used, LocalDateTime createdAt) {
        this.id = id;
        this.token = token;
        this.userId = userId;
        this.expiryDate = expiryDate;
        this.used = used;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public LocalDateTime getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDateTime expiryDate) {
        this.expiryDate = expiryDate;
    }

    public Boolean getUsed() {
        return used;
    }

    public void setUsed(Boolean used) {
        this.used = used;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PasswordResetTokenEntity other)) return false;
        return Objects.equals(id, other.id) && Objects.equals(token, other.token) && Objects.equals(userId, other.userId) && Objects.equals(expiryDate, other.expiryDate) && Objects.equals(used, other.used) && Objects.equals(createdAt, other.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, token, userId, expiryDate, used, createdAt);
    }

    @Override
    public String toString() {
        return "PasswordResetTokenEntity(" + "id=" + id + ", token=" + token + ", userId=" + userId + ", expiryDate=" + expiryDate + ", used=" + used + ", createdAt=" + createdAt + ")";
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long id;
        private String token;
        private Long userId;
        private LocalDateTime expiryDate;
        private Boolean used = false;
        private LocalDateTime createdAt;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder token(String token) {
            this.token = token;
            return this;
        }

        public Builder userId(Long userId) {
            this.userId = userId;
            return this;
        }

        public Builder expiryDate(LocalDateTime expiryDate) {
            this.expiryDate = expiryDate;
            return this;
        }

        public Builder used(Boolean used) {
            this.used = used;
            return this;
        }

        public Builder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public PasswordResetTokenEntity build() {
            return new PasswordResetTokenEntity(id, token, userId, expiryDate, used, createdAt);
        }
    }

    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
