package com.drogueria.bellavista.domain.service;


import com.drogueria.bellavista.domain.model.Role;
import com.drogueria.bellavista.domain.model.User;
import com.drogueria.bellavista.domain.repository.UserRepository;
import com.drogueria.bellavista.exception.BusinessException;
import com.drogueria.bellavista.exception.ResourceNotFoundException;
import org.junit.jupiter.api.*;
import org.mockito.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for UserService.
 *
 * These tests validate:
 * - input validation rules
 * - password encoding
 * - uniqueness checks
 * - repository interaction
 * - authentication helpers
 */
class UserServiceTest {

    private static final Logger log =
            LoggerFactory.getLogger(UserServiceTest.class);

    @Mock private UserRepository userRepository;
    @Mock private PasswordEncoder passwordEncoder;

    @InjectMocks private UserService userService;

    private User user;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        user = User.builder()
                .id(1L)
                .username("daniel")
                .email("daniel@test.com")
                .password("ENCODED")
                .firstName("Daniel")
                .lastName("Test")
                .role(Role.USER)
                .active(true)
                .build();

        log.info("✔ Setup UserServiceTest listo");
    }

    // =============================
    // CREATE USER
    // =============================

    @Test
    @DisplayName("Debe crear usuario correctamente")
    void shouldCreateUserSuccessfully() {
        log.info("🧪 Test createUser SUCCESS");

        when(userRepository.existsByUsername("daniel")).thenReturn(false);
        when(userRepository.existsByEmail("daniel@test.com")).thenReturn(false);
        when(passwordEncoder.encode("password123")).thenReturn("ENCODED");
        when(userRepository.save(any())).thenReturn(user);

        User result = userService.createUser(
                "daniel",
                "daniel@test.com",
                "password123",
                "Daniel",
                "Test",
                Role.ADMIN
        );

        assertNotNull(result);
        verify(passwordEncoder).encode("password123");
        verify(userRepository).save(any());

        log.info("✅ Usuario creado correctamente");
    }

    @Test
    @DisplayName("Debe usar rol USER por defecto")
    void shouldUseDefaultRole() {
        log.info("🧪 Test default role");

        when(userRepository.existsByUsername(any())).thenReturn(false);
        when(userRepository.existsByEmail(any())).thenReturn(false);
        when(passwordEncoder.encode(any())).thenReturn("ENCODED");
        when(userRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        User result = userService.createUser(
                "daniel",
                "daniel@test.com",
                "password123",
                "Daniel",
                "Test",
                null
        );

        assertEquals(Role.USER, result.getRole());

        log.info("✅ Rol por defecto correcto");
    }

    @Test
    @DisplayName("Debe fallar si username existe")
    void shouldFailIfUsernameExists() {
        when(userRepository.existsByUsername("daniel")).thenReturn(true);

        assertThrows(BusinessException.class, () ->
                userService.createUser("daniel","a@a.com","password123","D","T",Role.USER));
    }

    @Test
    @DisplayName("Debe fallar si email existe")
    void shouldFailIfEmailExists() {
        when(userRepository.existsByUsername(any())).thenReturn(false);
        when(userRepository.existsByEmail("daniel@test.com")).thenReturn(true);

        assertThrows(BusinessException.class, () ->
                userService.createUser("daniel","daniel@test.com","password123","D","T",Role.USER));
    }

    @Test
    @DisplayName("Debe fallar si password corta")
    void shouldFailIfPasswordShort() {
        assertThrows(BusinessException.class, () ->
                userService.createUser("daniel","a@a.com","123","D","T",Role.USER));
    }

    @Test
    @DisplayName("Debe fallar si email inválido")
    void shouldFailIfInvalidEmail() {
        assertThrows(BusinessException.class, () ->
                userService.createUser("daniel","correo-malo","password123","D","T",Role.USER));
    }

    // =============================
    // GETTERS
    // =============================

    @Test
    @DisplayName("Debe obtener usuario por username")
    void shouldGetUserByUsername() {
        when(userRepository.findByUsername("daniel"))
                .thenReturn(Optional.of(user));

        User result = userService.getUserByUsername("daniel");

        assertEquals("daniel", result.getUsername());
    }

    @Test
    @DisplayName("Debe lanzar excepción si username no existe")
    void shouldFailGetUserByUsername() {
        when(userRepository.findByUsername("daniel"))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> userService.getUserByUsername("daniel"));
    }

    @Test
    @DisplayName("Debe obtener usuario por id")
    void shouldGetUserById() {
        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        assertEquals(1L, userService.getUserById(1L).getId());
    }

    // =============================
    // UTILS
    // =============================

    @Test
    @DisplayName("Debe verificar password correctamente")
    void shouldVerifyPassword() {
        when(passwordEncoder.matches("123","ENCODED"))
                .thenReturn(true);

        assertTrue(userService.verifyPassword("123","ENCODED"));
    }

    @Test
    @DisplayName("Debe validar username disponible")
    void shouldCheckUsernameAvailable() {
        when(userRepository.existsByUsername("daniel"))
                .thenReturn(false);

        assertTrue(userService.isUsernameAvailable("daniel"));
    }

    @Test
    @DisplayName("Debe actualizar last login")
    void shouldUpdateLastLogin() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        userService.updateLastLogin(1L);

        assertNotNull(user.getLastLogin());
        verify(userRepository).save(user);
    }
}
