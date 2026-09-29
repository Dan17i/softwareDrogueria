package com.drogueria.bellavista.controller;

import com.drogueria.bellavista.application.dto.MessageResponseDTO;
import com.drogueria.bellavista.application.service.AuthService;
import com.drogueria.bellavista.domain.model.Role;
import com.drogueria.bellavista.domain.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.SecureRandom;
import java.util.Base64;

/**
 * Herramientas de bootstrap solo para desarrollo.
 * Anotado con @Profile("dev"): el bean no se registra bajo ningún otro perfil,
 * así que el endpoint no existe en producción sin importar la config de SecurityConfig.
 */
@RestController
@RequestMapping("/auth")
@Profile("dev")
public class DevToolsController {

    private static final Logger log = LoggerFactory.getLogger(DevToolsController.class);
    private static final SecureRandom RANDOM = new SecureRandom();

    private final AuthService authService;

    public DevToolsController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/dev-create-admin")
    public ResponseEntity<MessageResponseDTO> createAdmin() {
        if (!authService.isUsernameAvailable("admin")) {
            return ResponseEntity.ok(MessageResponseDTO.builder()
                .message("El admin de desarrollo ya existe.")
                .build());
        }

        String password = generatePassword();
        User admin = authService.registerUserWithRole(
            "admin", "admin@bellavista.local", password, "Admin", "Sistema", Role.ADMIN);

        log.warn("Admin de desarrollo creado (id={}). Password temporal: {}", admin.getId(), password);

        return ResponseEntity.ok(MessageResponseDTO.builder()
            .message("Admin de desarrollo creado. La password temporal quedó impresa en los logs del servidor.")
            .build());
    }

    private String generatePassword() {
        byte[] bytes = new byte[18];
        RANDOM.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }
}
