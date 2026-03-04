package com.drogueria.bellavista.application.dto;

import com.drogueria.bellavista.domain.model.Role;
import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterWithRoleRequestDTO {

    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 50)
    private String username;

    @NotBlank(message = "Email is required")
    @Email
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 8)
    private String password;

    private String firstName;
    private String lastName;

    @NotNull(message = "Role is required")
    private Role role;
}