package com.drogueria.bellavista.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO para listar usuarios (información resumida)
 * Métrica 3.3: Optimización de consultas con datos mínimos necesarios
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserListDTO {
    private Long id;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String role;
    private Boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime lastLogin;
}
