package com.drogueria.bellavista.domain.service;

import com.drogueria.bellavista.domain.model.Role;
import com.drogueria.bellavista.domain.model.User;
import com.drogueria.bellavista.domain.repository.UserRepository;
import com.drogueria.bellavista.exception.BusinessException;
import com.drogueria.bellavista.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Tests unitarios para gestión de usuarios (nuevas funcionalidades)
 * 
 * MÉTRICAS DE CALIDAD:
 * - Cobertura de código para nuevas funcionalidades
 * - Validación de reglas de negocio
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("UserService - Gestión de Usuarios Tests")
class UserServiceManagementTest {
    
    private static final Logger log = LoggerFactory.getLogger(UserServiceManagementTest.class);
    
    @Mock
    private UserRepository userRepository;
    
    @Mock
    private PasswordEncoder passwordEncoder;
    
    @InjectMocks
    private UserService userService;
    
    private User adminUser;
    private User regularUser;
    private User managerUser;
    
    @BeforeEach
    void setUp() {
        log.info("🔧 Setup UserServiceManagementTest listo");
        
        adminUser = User.builder()
            .id(1L)
            .username("admin")
            .email("admin@test.com")
            .password("encoded_password")
            .firstName("Admin")
            .lastName("User")
            .role(Role.ADMIN)
            .active(true)
            .createdAt(LocalDateTime.now())
            .build();
        
        regularUser = User.builder()
            .id(2L)
            .username("user1")
            .email("user1@test.com")
            .password("encoded_password")
            .firstName("Regular")
            .lastName("User")
            .role(Role.USER)
            .active(true)
            .createdAt(LocalDateTime.now())
            .build();
        
        managerUser = User.builder()
            .id(3L)
            .username("manager1")
            .email("manager1@test.com")
            .password("encoded_password")
            .firstName("Manager")
            .lastName("User")
            .role(Role.MANAGER)
            .active(true)
            .createdAt(LocalDateTime.now())
            .build();
    }
    
    @Test
    @DisplayName("getAllUsers - Debe retornar lista de usuarios")
    void getAllUsers_shouldReturnUserList() {
        log.info("🧪 Test getAllUsers");
        
        // Arrange
        List<User> users = Arrays.asList(adminUser, regularUser, managerUser);
        when(userRepository.findAll()).thenReturn(users);
        
        // Act
        List<User> result = userService.getAllUsers();
        
        // Assert
        assertNotNull(result);
        assertEquals(3, result.size());
        verify(userRepository, times(1)).findAll();
        
        log.info("✅ Lista de usuarios retornada correctamente");
    }
    
    @Test
    @DisplayName("updateUserRole - Debe actualizar rol exitosamente")
    void updateUserRole_success() {
        log.info("🧪 Test updateUserRole SUCCESS");
        
        // Arrange
        when(userRepository.findById(2L)).thenReturn(Optional.of(regularUser));
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArgument(0));
        
        // Act
        User result = userService.updateUserRole(2L, Role.SALES);
        
        // Assert
        assertNotNull(result);
        assertEquals(Role.SALES, result.getRole());
        assertNotNull(result.getUpdatedAt());
        verify(userRepository, times(1)).save(any(User.class));
        
        log.info("✅ Rol actualizado correctamente de USER a SALES");
    }
    
    @Test
    @DisplayName("updateUserRole - No debe permitir cambiar rol del único admin")
    void updateUserRole_shouldNotAllowChangingLastAdmin() {
        log.info("🧪 Test updateUserRole - Protección último admin");
        
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.of(adminUser));
        when(userRepository.findAll()).thenReturn(Arrays.asList(adminUser, regularUser));
        
        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            userService.updateUserRole(1L, Role.USER);
        });
        
        assertTrue(exception.getMessage().contains("único administrador"));
        verify(userRepository, never()).save(any(User.class));
        
        log.info("✅ Excepción correcta - No se puede cambiar rol del único admin");
    }
    
    @Test
    @DisplayName("updateUserRole - Debe permitir cambiar admin si hay múltiples")
    void updateUserRole_shouldAllowChangingAdminWhenMultipleExist() {
        log.info("🧪 Test updateUserRole - Múltiples admins");
        
        // Arrange
        User secondAdmin = User.builder()
            .id(4L)
            .username("admin2")
            .role(Role.ADMIN)
            .active(true)
            .build();
        
        when(userRepository.findById(1L)).thenReturn(Optional.of(adminUser));
        when(userRepository.findAll()).thenReturn(Arrays.asList(adminUser, secondAdmin, regularUser));
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArgument(0));
        
        // Act
        User result = userService.updateUserRole(1L, Role.MANAGER);
        
        // Assert
        assertEquals(Role.MANAGER, result.getRole());
        verify(userRepository, times(1)).save(any(User.class));
        
        log.info("✅ Rol de admin cambiado correctamente (hay otro admin)");
    }
    
    @Test
    @DisplayName("updateUserRole - Debe fallar con rol nulo")
    void updateUserRole_shouldFailWithNullRole() {
        log.info("🧪 Test updateUserRole - Rol nulo");
        
        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            userService.updateUserRole(2L, null);
        });
        
        assertTrue(exception.getMessage().contains("rol no puede ser nulo"));
        
        log.info("✅ Excepción correcta por rol nulo");
    }
    
    @Test
    @DisplayName("updateUserStatus - Debe activar/desactivar usuario")
    void updateUserStatus_success() {
        log.info("🧪 Test updateUserStatus SUCCESS");
        
        // Arrange
        when(userRepository.findById(2L)).thenReturn(Optional.of(regularUser));
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArgument(0));
        
        // Act
        User result = userService.updateUserStatus(2L, false);
        
        // Assert
        assertNotNull(result);
        assertFalse(result.getActive());
        assertNotNull(result.getUpdatedAt());
        verify(userRepository, times(1)).save(any(User.class));
        
        log.info("✅ Usuario desactivado correctamente");
    }
    
    @Test
    @DisplayName("updateUserStatus - No debe desactivar único admin activo")
    void updateUserStatus_shouldNotDeactivateLastActiveAdmin() {
        log.info("🧪 Test updateUserStatus - Protección último admin activo");
        
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.of(adminUser));
        when(userRepository.findAll()).thenReturn(Arrays.asList(adminUser, regularUser));
        
        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            userService.updateUserStatus(1L, false);
        });
        
        assertTrue(exception.getMessage().contains("único administrador activo"));
        verify(userRepository, never()).save(any(User.class));
        
        log.info("✅ Excepción correcta - No se puede desactivar único admin");
    }
    
    @Test
    @DisplayName("updateUserStatus - Debe fallar con estado nulo")
    void updateUserStatus_shouldFailWithNullStatus() {
        log.info("🧪 Test updateUserStatus - Estado nulo");
        
        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            userService.updateUserStatus(2L, null);
        });
        
        assertTrue(exception.getMessage().contains("estado activo no puede ser nulo"));
        
        log.info("✅ Excepción correcta por estado nulo");
    }
    
    @Test
    @DisplayName("deleteUser - Debe eliminar usuario exitosamente")
    void deleteUser_success() {
        log.info("🧪 Test deleteUser SUCCESS");
        
        // Arrange
        when(userRepository.findById(2L)).thenReturn(Optional.of(regularUser));
        doNothing().when(userRepository).delete(any(User.class));
        
        // Act
        userService.deleteUser(2L);
        
        // Assert
        verify(userRepository, times(1)).delete(any(User.class));
        
        log.info("✅ Usuario eliminado correctamente");
    }
    
    @Test
    @DisplayName("deleteUser - No debe eliminar único admin")
    void deleteUser_shouldNotDeleteLastAdmin() {
        log.info("🧪 Test deleteUser - Protección último admin");
        
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.of(adminUser));
        when(userRepository.findAll()).thenReturn(Arrays.asList(adminUser, regularUser));
        
        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            userService.deleteUser(1L);
        });
        
        assertTrue(exception.getMessage().contains("único administrador"));
        verify(userRepository, never()).delete(any(User.class));
        
        log.info("✅ Excepción correcta - No se puede eliminar único admin");
    }
    
    @Test
    @DisplayName("deleteUser - Debe fallar si usuario no existe")
    void deleteUser_shouldFailIfUserNotFound() {
        log.info("🧪 Test deleteUser - Usuario no existe");
        
        // Arrange
        when(userRepository.findById(999L)).thenReturn(Optional.empty());
        
        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            userService.deleteUser(999L);
        });
        
        verify(userRepository, never()).delete(any(User.class));
        
        log.info("✅ Excepción correcta por usuario inexistente");
    }
}
