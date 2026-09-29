package com.drogueria.bellavista.application.dto;

import com.drogueria.bellavista.domain.model.Role;
import jakarta.validation.constraints.NotNull;

/**
 * DTO para actualizar el rol de un usuario
 * Métrica 2.2: Validación clara de datos de entrada
 */
public record UpdateRoleRequestDTO(

        @NotNull(message = "El rol es obligatorio")
        Role role) {

    public Role getRole() {
        return role;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Role role;

        public Builder role(Role role) {
            this.role = role;
            return this;
        }

        public UpdateRoleRequestDTO build() {
            return new UpdateRoleRequestDTO(role);
        }
    }
}
