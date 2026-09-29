package com.drogueria.bellavista.application.dto;

import java.time.LocalDateTime;

/**
 * DTO para listar usuarios (información resumida)
 * Métrica 3.3: Optimización de consultas con datos mínimos necesarios
 */
public record UserListDTO(
        Long id,
        String username,
        String email,
        String firstName,
        String lastName,
        String role,
        Boolean active,
        LocalDateTime createdAt,
        LocalDateTime lastLogin) {

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getRole() {
        return role;
    }

    public Boolean getActive() {
        return active;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getLastLogin() {
        return lastLogin;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long id;
        private String username;
        private String email;
        private String firstName;
        private String lastName;
        private String role;
        private Boolean active;
        private LocalDateTime createdAt;
        private LocalDateTime lastLogin;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder username(String username) {
            this.username = username;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder firstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public Builder lastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public Builder role(String role) {
            this.role = role;
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

        public Builder lastLogin(LocalDateTime lastLogin) {
            this.lastLogin = lastLogin;
            return this;
        }

        public UserListDTO build() {
            return new UserListDTO(id, username, email, firstName, lastName, role, active, createdAt, lastLogin);
        }
    }
}
