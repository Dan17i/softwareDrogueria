package com.drogueria.bellavista.domain.service;

import com.drogueria.bellavista.domain.model.Role;
import com.drogueria.bellavista.domain.model.User;
import com.drogueria.bellavista.domain.repository.UserRepository;
import com.drogueria.bellavista.exception.BusinessException;
import com.drogueria.bellavista.exception.ResourceNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * Service for user management operations.
 * Handles user creation, retrieval, and password management with security best practices.
 */
@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Create a new user with validation.
     * Password is automatically encoded using PasswordEncoder (BCrypt).
     */
    public User createUser(String username, String email, String rawPassword, String firstName, String lastName, Role role) {
        // Validation
        if (username == null || username.trim().isEmpty()) {
            throw new BusinessException("Username is required");
        }
        if (username.length() < 3 || username.length() > 50) {
            throw new BusinessException("Username must be between 3 and 50 characters");
        }

        if (email == null || email.trim().isEmpty()) {
            throw new BusinessException("Email is required");
        }
        if (!isValidEmail(email)) {
            throw new BusinessException("Invalid email format");
        }

        if (rawPassword == null || rawPassword.trim().isEmpty()) {
            throw new BusinessException("Password is required");
        }
        if (rawPassword.length() < 8) {
            throw new BusinessException("Password must be at least 8 characters");
        }

        if (userRepository.existsByUsername(username)) {
            throw new BusinessException("Username already exists");
        }

        if (userRepository.existsByEmail(email)) {
            throw new BusinessException("Email already exists");
        }

        // Create user with encoded password
        User user = User.builder()
            .username(username)
            .email(email)
            .password(passwordEncoder.encode(rawPassword))
            .firstName(firstName)
            .lastName(lastName)
            .role(role != null ? role : Role.USER)
            .active(true)
            .createdAt(LocalDateTime.now())
            .build();

        return userRepository.save(user);
    }

    /**
     * Find user by username.
     */
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
            .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));
    }

    /**
     * Find user by ID.
     */
    public User getUserById(Long userId) {
        return userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));
    }

    /**
     * Find user by email.
     */
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
            .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
    }

    /**
     * Check if username is available.
     */
    public boolean isUsernameAvailable(String username) {
        return !userRepository.existsByUsername(username);
    }

    /**
     * Check if email is available.
     */
    public boolean isEmailAvailable(String email) {
        return !userRepository.existsByEmail(email);
    }

    /**
     * Verify password for authentication.
     */
    public boolean verifyPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    /**
     * Update last login timestamp.
     */
    public void updateLastLogin(Long userId) {
        User user = getUserById(userId);
        user.setLastLogin(LocalDateTime.now());
        userRepository.save(user);
    }

    /**
     * Validate email format (simple regex).
     */
    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        return email.matches(emailRegex);
    }
}
