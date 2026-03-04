package com.drogueria.bellavista.controller;

import com.drogueria.bellavista.application.dto.*;
import com.drogueria.bellavista.application.service.AuthService;
import com.drogueria.bellavista.domain.model.Role;
import com.drogueria.bellavista.domain.model.User;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/dev-create-admin")
    public String createAdmin() {
        authService.registerUserWithRole(
                "admin",
                "admin@bellavista.com",
                "admin123",
                "Admin",
                "Sistema",
                Role.ADMIN
        );
        return "Admin creado correctamente";
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
