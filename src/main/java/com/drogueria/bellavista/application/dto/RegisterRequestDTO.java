package com.drogueria.bellavista.application.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * Data Transfer Object used to encapsulate the information required
 * to register a new user in the system.
 *
 * <p>This DTO is received from the client layer (usually via REST API)
 * and validated using Jakarta Bean Validation annotations before being
 * processed by the application service layer.</p>
 *
 * <p>It contains the minimum data necessary to create a user account,
 * including authentication credentials and personal identification data.</p>
 *
 * <p>Validation rules ensure:</p>
 * <ul>
 *     <li>Username is mandatory and length-restricted</li>
 *     <li>Email format is valid</li>
 *     <li>Password meets minimum security length</li>
 *     <li>First and last names are present and valid</li>
 * </ul>
 *
 * <p>This object should never contain encoded passwords or persistence data.</p>
 *
 * @author Daniel Jurado & equipo se desarrollo
 * @version 1.0
 * @since 2026
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterRequestDTO {
    /**
     * Unique username chosen by the user.
     * <p>
     * Must not be blank and must contain between 3 and 50 characters.
     * This value is used for login identification and must be unique
     * in the system.
     */
    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    private String username;
    /**
     * Email address of the user.
     * <p>
     * Must not be blank and must follow a valid email format.
     * This value is also used for identification, password recovery,
     * and notifications.
     */
    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;
    /**
     * Raw password provided by the user during registration.
     * <p>
     * Must not be blank and must contain at least 8 characters.
     * This password will be encoded before being stored in the database.
     */
    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters")
    private String password;
    /**
     * First name of the user.
     * <p>
     * Must not be blank and must contain between 2 and 100 characters.
     * Used for identification and personalization purposes.
     */
    @NotBlank(message = "First name is required")
    @Size(min = 2, max = 100, message = "First name must be between 2 and 100 characters")
    private String firstName;
    /**
     * Last name of the user.
     * <p>
     * Must not be blank and must contain between 2 and 100 characters.
     * Used for identification and personalization purposes.
     */
    @NotBlank(message = "Last name is required")
    @Size(min = 2, max = 100, message = "Last name must be between 2 and 100 characters")
    private String lastName;
}
