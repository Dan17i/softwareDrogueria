package com.drogueria.bellavista.application.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para activar/desactivar un usuario
 * Métrica 2.2: Validación clara de datos de entrada
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateStatusRequestDTO {
    
    @NotNull(message = "El estado activo es obligatorio")
    private Boolean active;
}
