package com.drogueria.bellavista.exception;

/**
 * Exception thrown when authentication fails (invalid credentials).
 */
public class AuthenticationException extends RuntimeException {
    public AuthenticationException(String message) {
        super(message);
    }

    public AuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }
}
