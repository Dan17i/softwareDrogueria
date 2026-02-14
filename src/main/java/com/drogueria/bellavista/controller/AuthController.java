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
/**
 * Controlador REST para la autenticación de usuarios.
 * <p>
 * Proporciona endpoints para el registro y login de usuarios.
 * Este controlador actúa como adaptador de entrada en la arquitectura hexagonal,
 * delegando la lógica de negocio al {@link AuthService}.
 * </p>
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    /**
     * Constructor para inyectar el servicio de autenticación.
     *
     * @param authService Servicio de autenticación que maneja la lógica de negocio de usuarios
     */
    public AuthController(AuthService authService) {
        this.authService = authService;
    }
    /**
     * Endpoint para registrar un nuevo usuario en el sistema.
     * <p>
     * Valida los datos del request y crea un usuario persistente.
     * Devuelve la información del usuario creado en un {@link UserResponseDTO}.
     * </p>
     *
     * @param req DTO con los datos necesarios para el registro del usuario
     * @return {@link ResponseEntity} con {@link UserResponseDTO} que contiene la información del usuario registrado
     */
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
    /**
     * Endpoint para iniciar sesión de un usuario existente.
     * <p>
     * Valida las credenciales del usuario y genera un token JWT de autenticación.
     * Devuelve el token junto con información básica del usuario en un {@link AuthResponseDTO}.
     * </p>
     *
     * @param req DTO con las credenciales de login (usuario y contraseña)
     * @return {@link ResponseEntity} con {@link AuthResponseDTO} que contiene el token JWT y datos del usuario
     */
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
