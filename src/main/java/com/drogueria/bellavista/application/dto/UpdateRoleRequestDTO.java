package com.drogueria.bellavista.application.dto;

import com.drogueria.bellavista.domain.model.Role;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para actualizar el rol de un usuario
 * Métrica 2.2: Validación clara de datos de entrada
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateRoleRequestDTO {
    
    @NotNull(message = "El rol es obligatorio")
    private Role role;
}
