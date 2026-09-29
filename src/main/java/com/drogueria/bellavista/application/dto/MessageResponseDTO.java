package com.drogueria.bellavista.application.dto;

public record MessageResponseDTO(String message) {

    public String getMessage() {
        return message;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String message;

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public MessageResponseDTO build() {
            return new MessageResponseDTO(message);
        }
    }
}
