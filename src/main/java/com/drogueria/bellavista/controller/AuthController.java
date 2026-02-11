package com.drogueria.bellavista.controller;

import com.drogueria.bellavista.application.dto.AuthResponseDTO;
import com.drogueria.bellavista.application.dto.LoginRequestDTO;
import com.drogueria.bellavista.application.dto.RegisterRequestDTO;
import com.drogueria.bellavista.application.dto.UserResponseDTO;
import com.drogueria.bellavista.application.service.AuthService;
import com.drogueria.bellavista.domain.model.User;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
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
}
