package com.drogueria.bellavista.domain.service;

import com.drogueria.bellavista.application.service.AuthService;
import com.drogueria.bellavista.domain.model.Role;
import com.drogueria.bellavista.domain.model.User;
import com.drogueria.bellavista.domain.service.UserService;
import com.drogueria.bellavista.exception.AuthenticationException;
import com.drogueria.bellavista.infrastructure.security.JwtUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthServiceTest {

    @Mock
    private UserService userService;

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthService authService;

    private User activeUser;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        activeUser = User.builder()
                .id(1L)
                .username("daniel")
                .password("encoded")
                .role(Role.USER)
                .active(true)
                .build();
    }

    // ✅ LOGIN OK
    @Test
    void shouldAuthenticateAndReturnToken() {
        when(userService.getUserByUsername("daniel")).thenReturn(activeUser);
        when(userService.verifyPassword("1234", "encoded")).thenReturn(true);
        when(jwtUtils.generateTokenFromUsername("daniel")).thenReturn("token123");

        String token = authService.authenticateUser("daniel", "1234");

        assertEquals("token123", token);
        verify(userService).updateLastLogin(1L);
    }

    // ❌ USER INACTIVE
    @Test
    void shouldFailIfUserInactive() {
        activeUser.setActive(false);
        when(userService.getUserByUsername("daniel")).thenReturn(activeUser);

        assertThrows(AuthenticationException.class,
                () -> authService.authenticateUser("daniel", "1234"));
    }

    // ❌ PASSWORD WRONG
    @Test
    void shouldFailIfPasswordInvalid() {
        when(userService.getUserByUsername("daniel")).thenReturn(activeUser);
        when(userService.verifyPassword("1234", "encoded")).thenReturn(false);

        assertThrows(AuthenticationException.class,
                () -> authService.authenticateUser("daniel", "1234"));
    }

    // ✅ LOAD USERDETAILS
    @Test
    void shouldLoadUserDetailsCorrectly() {
        when(userService.getUserByUsername("daniel")).thenReturn(activeUser);

        UserDetails details = authService.loadUserByUsername("daniel");

        assertEquals("daniel", details.getUsername());
        assertTrue(details.isEnabled());
        assertEquals(1, details.getAuthorities().size());
    }

    // ❌ USER NOT FOUND
    @Test
    void shouldThrowIfUserNotFound() {
        when(userService.getUserByUsername("daniel"))
                .thenThrow(new RuntimeException("not found"));

        assertThrows(Exception.class,
                () -> authService.loadUserByUsername("daniel"));
    }
}