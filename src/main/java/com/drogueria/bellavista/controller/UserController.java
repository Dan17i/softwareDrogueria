package com.drogueria.bellavista.controller;

import com.drogueria.bellavista.application.dto.UpdateRoleRequestDTO;
import com.drogueria.bellavista.application.dto.UpdateStatusRequestDTO;
import com.drogueria.bellavista.application.dto.UserListDTO;
import com.drogueria.bellavista.application.dto.UserResponseDTO;
import com.drogueria.bellavista.domain.model.User;
import com.drogueria.bellavista.domain.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Controlador REST - Gestión de Usuarios
 * 
 * MÉTRICAS DE CALIDAD:
 * - Métrica 4.3: Control de acceso - Solo ADMIN puede gestionar usuarios
 * - Métrica 4.4: Mensajes claros en respuestas
 * - Métrica 3.3: Consultas optimizadas
 */
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class UserController {
    
    private final UserService userService;
    
    /**
     * Listar todos los usuarios
     * GET /users
     * Solo ADMIN
     * 
     * Métrica 3.3: Tiempo de consulta optimizado
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserListDTO>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        
        List<UserListDTO> response = users.stream()
            .map(user -> UserListDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .role(user.getRole() != null ? user.getRole().name() : null)
                .active(user.getActive())
                .createdAt(user.getCreatedAt())
                .lastLogin(user.getLastLogin())
                .build())
            .collect(Collectors.toList());
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * Obtener usuario por ID
     * GET /users/{id}
     * Solo ADMIN
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        
        UserResponseDTO response = UserResponseDTO.builder()
            .id(user.getId())
            .username(user.getUsername())
            .email(user.getEmail())
            .firstName(user.getFirstName())
            .lastName(user.getLastName())
            .role(user.getRole() != null ? user.getRole().name() : null)
            .build();
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * Actualizar rol de usuario
     * PATCH /users/{id}/role
     * Solo ADMIN
     * 
     * Métrica 4.3: Control de acceso basado en roles
     * Métrica 2.2: Validación con mensajes claros
     */
    @PatchMapping("/{id}/role")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponseDTO> updateUserRole(
            @PathVariable Long id,
            @Valid @RequestBody UpdateRoleRequestDTO request) {
        
        User updatedUser = userService.updateUserRole(id, request.getRole());
        
        UserResponseDTO response = UserResponseDTO.builder()
            .id(updatedUser.getId())
            .username(updatedUser.getUsername())
            .email(updatedUser.getEmail())
            .firstName(updatedUser.getFirstName())
            .lastName(updatedUser.getLastName())
            .role(updatedUser.getRole().name())
            .build();
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * Activar/Desactivar usuario
     * PATCH /users/{id}/status
     * Solo ADMIN
     * 
     * Métrica 4.3: Control de acceso basado en roles
     */
    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponseDTO> updateUserStatus(
            @PathVariable Long id,
            @Valid @RequestBody UpdateStatusRequestDTO request) {
        
        User updatedUser = userService.updateUserStatus(id, request.getActive());
        
        UserResponseDTO response = UserResponseDTO.builder()
            .id(updatedUser.getId())
            .username(updatedUser.getUsername())
            .email(updatedUser.getEmail())
            .firstName(updatedUser.getFirstName())
            .lastName(updatedUser.getLastName())
            .role(updatedUser.getRole().name())
            .build();
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * Eliminar usuario
     * DELETE /users/{id}
     * Solo ADMIN
     * 
     * Métrica 4.1: Integridad transaccional
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
