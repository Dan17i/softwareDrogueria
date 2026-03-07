package com.drogueria.bellavista.controller;

import com.drogueria.bellavista.application.dto.*;
import com.drogueria.bellavista.application.service.AuthService;
import com.drogueria.bellavista.domain.model.Role;
import com.drogueria.bellavista.domain.model.User;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * Authentication controller.
 * 
 * MÉTRICAS DE CALIDAD:
 * - Métrica 4.2: Seguridad en autenticación y recuperación de contraseña
 * - Métrica 2.2: Mensajes claros en respuestas
 */
@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = {"https://invetoryrx.onrender.com", "http://localhost:5173"})
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> register(@Valid @RequestBody RegisterRequestDTO req) {
        User created = authService.registerUser(req.getUsername(), req.getEmail(), req.getPassword(), req.getFirstName(), req.getLastName());

        UserResponseDTO resp = UserResponseDTO.builder()
            .id(created.getId())
            .username(created.getUsername())
            .email(created.getEmail())
            .firstName(created.getFirstName())
            .lastName(created.getLastName())
            .role(created.getRole() != null ? created.getRole().name() : null)
            .build();

        return ResponseEntity.ok(resp);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@Valid @RequestBody LoginRequestDTO req) {
        String token = authService.authenticateUser(req.getUsername(), req.getPassword());
        User user = authService.getUserByUsername(req.getUsername());

        AuthResponseDTO resp = AuthResponseDTO.builder()
            .token(token)
            .userId(user.getId())
            .username(user.getUsername())
            .role(user.getRole() != null ? user.getRole().name() : null)
            .build();

        return ResponseEntity.ok(resp);
    }

    /**
     * Request password reset - sends email with reset link.
     * POST /auth/forgot-password
     */
    @PostMapping("/forgot-password")
    public ResponseEntity<MessageResponseDTO> forgotPassword(@Valid @RequestBody ForgotPasswordRequestDTO req) {
        authService.requestPasswordReset(req.getEmail());
        return ResponseEntity.ok(MessageResponseDTO.builder()
            .message("Si el email existe, recibirás instrucciones para restablecer tu contraseña.")
            .build());
    }

    /**
     * Reset password using token.
     * POST /auth/reset-password
     */
    @PostMapping("/reset-password")
    public ResponseEntity<MessageResponseDTO> resetPassword(@Valid @RequestBody ResetPasswordRequestDTO req) {
        authService.resetPassword(req.getToken(), req.getNewPassword());
        return ResponseEntity.ok(MessageResponseDTO.builder()
            .message("Contraseña restablecida exitosamente. Ya puedes iniciar sesión con tu nueva contraseña.")
            .build());
    }

    @PostMapping("/dev-create-admin")
    public ResponseEntity<?> createAdmin() {
        try {
            // Primero verificar si ya existe
            try {
                User existing = authService.getUserByUsername("admin");
                return ResponseEntity.ok("Admin ya existe con ID: " + existing.getId());
            } catch (Exception e) {
                // No existe, crear uno nuevo directamente sin enviar email
            }
            
            // Crear admin directamente usando UserService (sin email)
            User admin = authService.registerUserWithRole(
                    "admin",
                    "admin@bellavista.com",
                    "admin123",
                    "Admin",
                    "Sistema",
                    Role.ADMIN
            );
            return ResponseEntity.ok("Admin creado correctamente con ID: " + admin.getId());
        } catch (Exception e) {
            // Retornar el error completo para debugging
            return ResponseEntity.status(500).body("Error al crear admin: " + e.getClass().getName() + " - " + e.getMessage());
        }
    }
    
    @PostMapping("/admin/create-user")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponseDTO> createUserWithRole(@Valid @RequestBody RegisterWithRoleRequestDTO req) {
        User created = authService.registerUserWithRole(
                req.getUsername(),
                req.getEmail(),
                req.getPassword(),
                req.getFirstName(),
                req.getLastName(),
                req.getRole()
        );

        UserResponseDTO resp = UserResponseDTO.builder()
                .id(created.getId())
                .username(created.getUsername())
                .email(created.getEmail())
                .firstName(created.getFirstName())
                .lastName(created.getLastName())
                .role(created.getRole().name())
                .build();

        return ResponseEntity.ok(resp);
    }
}
