package com.drogueria.bellavista.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ResetPasswordRequestDTO(

        @NotBlank(message = "El token es obligatorio")
        String token,

        @NotBlank(message = "La nueva contraseña es obligatoria")
        @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
        String newPassword) {

    public String getToken() {
        return token;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String token;
        private String newPassword;

        public Builder token(String token) {
            this.token = token;
            return this;
        }

        public Builder newPassword(String newPassword) {
            this.newPassword = newPassword;
            return this;
        }

        public ResetPasswordRequestDTO build() {
            return new ResetPasswordRequestDTO(token, newPassword);
        }
    }
}
