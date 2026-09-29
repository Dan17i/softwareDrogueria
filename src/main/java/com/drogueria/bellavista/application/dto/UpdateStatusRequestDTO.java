package com.drogueria.bellavista.application.dto;

import jakarta.validation.constraints.NotNull;

/**
 * DTO para activar/desactivar un usuario
 * Métrica 2.2: Validación clara de datos de entrada
 */
public record UpdateStatusRequestDTO(

        @NotNull(message = "El estado activo es obligatorio")
        Boolean active) {

    public Boolean getActive() {
        return active;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Boolean active;

        public Builder active(Boolean active) {
            this.active = active;
            return this;
        }

        public UpdateStatusRequestDTO build() {
            return new UpdateStatusRequestDTO(active);
        }
    }
}
