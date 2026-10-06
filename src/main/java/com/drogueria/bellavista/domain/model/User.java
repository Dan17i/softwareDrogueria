package com.drogueria.bellavista.domain.model;

import java.util.Objects;

import java.time.LocalDateTime;

/**
 * User domain model representing an authenticated user in the system.
 * This is the domain entity, independent of persistence implementation.
 */
public class User {
    private Long id;
    private String username;
    private String email;
    private String password; // Should be hashed
    private String firstName;
    private String lastName;
    private Role role;
    private Boolean active;
    private Boolean emailVerified;
    private String emailVerificationToken;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime lastLogin;

    public User() {
    }

    public User(Long id, String username, String email, String password, String firstName, String lastName, Role role, Boolean active, Boolean emailVerified, String emailVerificationToken, LocalDateTime createdAt, LocalDateTime updatedAt, LocalDateTime lastLogin) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
        this.active = active;
        this.emailVerified = emailVerified;
        this.emailVerificationToken = emailVerificationToken;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.lastLogin = lastLogin;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Boolean getEmailVerified() {
        return emailVerified;
    }

    public void setEmailVerified(Boolean emailVerified) {
        this.emailVerified = emailVerified;
    }

    public String getEmailVerificationToken() {
        return emailVerificationToken;
    }

    public void setEmailVerificationToken(String emailVerificationToken) {
        this.emailVerificationToken = emailVerificationToken;
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

    public LocalDateTime getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(LocalDateTime lastLogin) {
        this.lastLogin = lastLogin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User other)) return false;
        return Objects.equals(id, other.id) && Objects.equals(username, other.username) && Objects.equals(email, other.email) && Objects.equals(password, other.password) && Objects.equals(firstName, other.firstName) && Objects.equals(lastName, other.lastName) && Objects.equals(role, other.role) && Objects.equals(active, other.active) && Objects.equals(emailVerified, other.emailVerified) && Objects.equals(emailVerificationToken, other.emailVerificationToken) && Objects.equals(createdAt, other.createdAt) && Objects.equals(updatedAt, other.updatedAt) && Objects.equals(lastLogin, other.lastLogin);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, email, password, firstName, lastName, role, active, emailVerified, emailVerificationToken, createdAt, updatedAt, lastLogin);
    }

    @Override
    public String toString() {
        return "User(" + "id=" + id + ", username=" + username + ", email=" + email + ", password=" + password + ", firstName=" + firstName + ", lastName=" + lastName + ", role=" + role + ", active=" + active + ", emailVerified=" + emailVerified + ", emailVerificationToken=" + emailVerificationToken + ", createdAt=" + createdAt + ", updatedAt=" + updatedAt + ", lastLogin=" + lastLogin + ")";
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long id;
        private String username;
        private String email;
        private String password;
        private String firstName;
        private String lastName;
        private Role role;
        private Boolean active;
        private Boolean emailVerified;
        private String emailVerificationToken;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
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

        public Builder password(String password) {
            this.password = password;
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

        public Builder role(Role role) {
            this.role = role;
            return this;
        }

        public Builder active(Boolean active) {
            this.active = active;
            return this;
        }

        public Builder emailVerified(Boolean emailVerified) {
            this.emailVerified = emailVerified;
            return this;
        }

        public Builder emailVerificationToken(String emailVerificationToken) {
            this.emailVerificationToken = emailVerificationToken;
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

        public Builder lastLogin(LocalDateTime lastLogin) {
            this.lastLogin = lastLogin;
            return this;
        }

        public User build() {
            return new User(id, username, email, password, firstName, lastName, role, active, emailVerified, emailVerificationToken, createdAt, updatedAt, lastLogin);
        }
    }


    public String getFullName() {
        return firstName + " " + lastName;
    }

    public boolean isActive() {
        return Boolean.TRUE.equals(active);
    }

    public boolean isEmailVerified() {
        return Boolean.TRUE.equals(emailVerified);
    }
}
