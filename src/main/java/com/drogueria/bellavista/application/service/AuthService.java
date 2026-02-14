package com.drogueria.bellavista.application.service;

import com.drogueria.bellavista.domain.model.Role;
import com.drogueria.bellavista.domain.model.User;
import com.drogueria.bellavista.domain.service.UserService;
import com.drogueria.bellavista.exception.AuthenticationException;
import com.drogueria.bellavista.infrastructure.security.JwtUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Collections;
/**
 * Servicio de aplicación encargado de la autenticación de usuarios.
 *
 * <p>Este servicio actúa como orquestador entre la capa de seguridad,
 * el dominio y la generación de tokens JWT. Proporciona funcionalidades
 * para registro de usuarios, validación de credenciales, generación de
 * tokens y consulta del usuario autenticado.</p>
 *
 * <p>También implementa {@link UserDetailsService} para integrarse con
 * el mecanismo de autenticación de Spring Security.</p>
 *
 * <p>Forma parte de la capa de aplicación dentro de la arquitectura hexagonal.</p>
 */
@Service
@Transactional
public class AuthService implements UserDetailsService {

    private final UserService userService;
    private final JwtUtils jwtUtils;
    private final PasswordEncoder passwordEncoder;
    /**
     * Constructor con inyección de dependencias.
     *
     * @param userService servicio de dominio para gestión de usuarios
     * @param jwtUtils utilidades para generación y validación de JWT
     * @param passwordEncoder codificador de contraseñas
     */
    public AuthService(UserService userService, JwtUtils jwtUtils, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.jwtUtils = jwtUtils;
        this.passwordEncoder = passwordEncoder;
    }
    /**
     * Registra un nuevo usuario con rol por defecto USER.
     *
     * @param username nombre de usuario
     * @param email correo electrónico
     * @param password contraseña en texto plano
     * @param firstName nombre
     * @param lastName apellido
     * @return usuario creado en el dominio
     */
    public User registerUser(String username, String email, String password, String firstName, String lastName) {
        return userService.createUser(username, email, password, firstName, lastName, Role.USER);
    }

    /**
     * Registra un nuevo usuario con un rol específico.
     * Usado normalmente por administradores.
     *
     * @param username nombre de usuario
     * @param email correo electrónico
     * @param password contraseña
     * @param firstName nombre
     * @param lastName apellido
     * @param role rol asignado
     * @return usuario creado
     */
    public User registerUserWithRole(String username, String email, String password, String firstName, String lastName, Role role) {
        return userService.createUser(username, email, password, firstName, lastName, role);
    }

    /**
     * Autentica un usuario validando sus credenciales y genera un token JWT.
     *
     * <p>Este método:
     * <ul>
     *     <li>Busca el usuario por username</li>
     *     <li>Verifica que esté activo</li>
     *     <li>Valida la contraseña</li>
     *     <li>Actualiza el último login</li>
     *     <li>Genera el token JWT</li>
     * </ul>
     *
     * @param username nombre de usuario
     * @param password contraseña en texto plano
     * @return token JWT generado
     * @throws AuthenticationException si las credenciales no son válidas
     */
    public String authenticateUser(String username, String password) {
        try {
            User user = userService.getUserByUsername(username);

            if (!user.isActive()) {
                throw new AuthenticationException("User account is disabled");
            }

            if (!userService.verifyPassword(password, user.getPassword())) {
                throw new AuthenticationException("Invalid username or password");
            }

            // Update last login
            userService.updateLastLogin(user.getId());

            // Generate token using custom authentication-like object
            String token = jwtUtils.generateTokenFromUsername(username);
            return token;

        } catch (Exception e) {
            if (e instanceof AuthenticationException) {
                throw e;
            }
            throw new AuthenticationException("Authentication failed: " + e.getMessage());
        }
    }

    /**
     * Valida si un token JWT es correcto y no ha expirado.
     *
     * @param token token JWT
     * @return true si es válido, false en caso contrario
     */
    public boolean validateToken(String token) {
        return jwtUtils.validateToken(token);
    }

    /**
     * Extrae el username contenido dentro del token JWT.
     *
     * @param token token JWT
     * @return nombre de usuario contenido en el token
     */
    public String getUsernameFromToken(String token) {
        return jwtUtils.getUsernameFromToken(token);
    }

    /**
     * Obtiene el usuario autenticado actualmente desde el contexto de seguridad.
     *
     * @return usuario autenticado en el sistema
     * @throws AuthenticationException si no hay usuario autenticado
     */
    public User getCurrentUser() {
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AuthenticationException("No authenticated user found");
        }

        String username = authentication.getName();
        return userService.getUserByUsername(username);
    }

    /**
     * Verifica si un username está disponible para registro.
     *
     * @param username nombre de usuario
     * @return true si está disponible
     */
    public boolean isUsernameAvailable(String username) {
        return userService.isUsernameAvailable(username);
    }
    /**
     * Verifica si un email está disponible para registro.
     *
     * @param email correo electrónico
     * @return true si está disponible
     */
    public boolean isEmailAvailable(String email) {
        return userService.isEmailAvailable(email);
    }
    /**
     * Implementación requerida por Spring Security para cargar los datos
     * del usuario durante el proceso de autenticación.
     *
     * <p>Convierte el usuario del dominio en un {@link UserDetails}
     * compatible con Spring Security.</p>
     *
     * @param username nombre de usuario
     * @return detalles del usuario para Spring Security
     * @throws UsernameNotFoundException si el usuario no existe
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            User user = userService.getUserByUsername(username);

            // Build authorities from user role
            Collection<GrantedAuthority> authorities = Collections.singletonList(
                new SimpleGrantedAuthority("ROLE_" + user.getRole().name())
            );

            return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .authorities(authorities)
                .accountExpired(false)
                .accountLocked(!user.isActive())
                .credentialsExpired(false)
                .disabled(!user.isActive())
                .build();

        } catch (Exception e) {
            throw new UsernameNotFoundException("User not found: " + username);
        }
    }
    /**
     * Obtiene un usuario por su username.
     *
     * @param username nombre de usuario
     * @return usuario del dominio
     */
    public User getUserByUsername(String username) {
        return userService.getUserByUsername(username);
    }
}
