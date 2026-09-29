package com.drogueria.bellavista.application.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record ForgotPasswordRequestDTO(

        @NotBlank(message = "El email es obligatorio")
        @Email(message = "El email debe ser válido")
        String email) {

    public String getEmail() {
        return email;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String email;

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public ForgotPasswordRequestDTO build() {
            return new ForgotPasswordRequestDTO(email);
        }
    }
}
