package com.drogueria.bellavista.application.service;

import com.drogueria.bellavista.domain.model.Role;
import com.drogueria.bellavista.domain.model.User;
import com.drogueria.bellavista.domain.service.UserService;
import com.drogueria.bellavista.exception.AuthenticationException;
import com.drogueria.bellavista.exception.ResourceNotFoundException;
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
 * Authentication service handling user login, registration and token management.
 * Integrates with Spring Security and JWT token generation.
 */
@Service
@Transactional
public class AuthService implements UserDetailsService {

    private final UserService userService;
    private final JwtUtils jwtUtils;
    private final PasswordEncoder passwordEncoder;
    private final com.drogueria.bellavista.application.service.EmailService emailService;
    private final com.drogueria.bellavista.domain.service.PasswordResetService passwordResetService;

    public AuthService(UserService userService, JwtUtils jwtUtils, PasswordEncoder passwordEncoder,
                      com.drogueria.bellavista.application.service.EmailService emailService,
                      com.drogueria.bellavista.domain.service.PasswordResetService passwordResetService) {
        this.userService = userService;
        this.jwtUtils = jwtUtils;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
        this.passwordResetService = passwordResetService;
    }

    /**
     * Register new user in the system and send welcome email.
     */
    public User registerUser(String username, String email, String password, String firstName, String lastName) {
        User user = userService.createUser(username, email, password, firstName, lastName, Role.USER);
        
        // Send simple welcome email (no verification required)
        emailService.sendWelcomeEmail(user.getEmail(), user.getUsername());
        
        return user;
    }

    /**
     * Register new user with specific role (admin only).
     */
    public User registerUserWithRole(String username, String email, String password, String firstName, String lastName, Role role) {
        return userService.createUser(username, email, password, firstName, lastName, role);
    }

    /**
     * Authenticate user and generate JWT token.
     * Validates credentials and updates last login timestamp.
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
     * Validate JWT token.
     */
    public boolean validateToken(String token) {
        return jwtUtils.validateToken(token);
    }

    /**
     * Extract username from JWT token.
     */
    public String getUsernameFromToken(String token) {
        return jwtUtils.getUsernameFromToken(token);
    }

    /**
     * Get current authenticated user from security context.
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
     * Check if username is available for registration.
     */
    public boolean isUsernameAvailable(String username) {
        return userService.isUsernameAvailable(username);
    }

    /**
     * Check if email is available for registration.
     */
    public boolean isEmailAvailable(String email) {
        return userService.isEmailAvailable(email);
    }

    /**
     * Spring Security UserDetailsService implementation.
     * Loads user details for authentication.
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

    public User getUserByUsername(String username) {
        return userService.getUserByUsername(username);
    }

    /**
     * Request password reset - sends email with reset link.
     */
    public void requestPasswordReset(String email) {
        try {
            User user = userService.getUserByEmail(email);
            com.drogueria.bellavista.domain.model.PasswordResetToken token = 
                passwordResetService.createPasswordResetToken(email);
            
            emailService.sendPasswordResetEmail(user.getEmail(), user.getUsername(), token.getToken());
        } catch (ResourceNotFoundException e) {
            // Don't reveal if email exists - security best practice
            // Just log and return success to prevent email enumeration
        }
    }

    /**
     * Reset password using token.
     */
    public void resetPassword(String token, String newPassword) {
        passwordResetService.resetPassword(token, newPassword);
    }
}
