package com.drogueria.bellavista.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * Service for sending emails.
 * Uses Spring Mail with async support for non-blocking email delivery.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {
    
    private final JavaMailSender mailSender;
    
    @Value("${app.mail.from:noreply@bellavista.com}")
    private String fromEmail;
    
    @Value("${app.frontend.url:http://localhost:5173}")
    private String frontendUrl;
    
    /**
     * Send welcome email with verification link.
     */
    @Async
    public void sendWelcomeEmail(String to, String username, String verificationToken) {
        try {
            String subject = "¡Bienvenido a Droguería Bellavista!";
            String verificationLink = frontendUrl + "/verify-email?token=" + verificationToken;
            
            String body = String.format(
                "Hola %s,\n\n" +
                "¡Bienvenido a Droguería Bellavista!\n\n" +
                "Tu cuenta ha sido creada exitosamente. Para activar tu cuenta y verificar tu email, " +
                "por favor haz clic en el siguiente enlace:\n\n" +
                "%s\n\n" +
                "Este enlace es válido por 24 horas.\n\n" +
                "Si no creaste esta cuenta, puedes ignorar este mensaje.\n\n" +
                "Saludos,\n" +
                "Equipo Droguería Bellavista",
                username,
                verificationLink
            );
            
            sendEmail(to, subject, body);
            log.info("Welcome email sent to: {}", to);
            
        } catch (Exception e) {
            log.error("Error sending welcome email to {}: {}", to, e.getMessage());
        }
    }
    
    /**
     * Send password reset email with reset link.
     */
    @Async
    public void sendPasswordResetEmail(String to, String username, String resetToken) {
        try {
            String subject = "Recuperación de Contraseña - Droguería Bellavista";
            String resetLink = frontendUrl + "/reset-password?token=" + resetToken;
            
            String body = String.format(
                "Hola %s,\n\n" +
                "Recibimos una solicitud para restablecer la contraseña de tu cuenta.\n\n" +
                "Para crear una nueva contraseña, haz clic en el siguiente enlace:\n\n" +
                "%s\n\n" +
                "Este enlace es válido por 1 hora.\n\n" +
                "Si no solicitaste restablecer tu contraseña, puedes ignorar este mensaje. " +
                "Tu contraseña actual seguirá siendo válida.\n\n" +
                "Por seguridad, nunca compartas este enlace con nadie.\n\n" +
                "Saludos,\n" +
                "Equipo Droguería Bellavista",
                username,
                resetLink
            );
            
            sendEmail(to, subject, body);
            log.info("Password reset email sent to: {}", to);
            
        } catch (Exception e) {
            log.error("Error sending password reset email to {}: {}", to, e.getMessage());
        }
    }
    
    /**
     * Send email verification success notification.
     */
    @Async
    public void sendEmailVerifiedNotification(String to, String username) {
        try {
            String subject = "Email Verificado - Droguería Bellavista";
            
            String body = String.format(
                "Hola %s,\n\n" +
                "¡Tu email ha sido verificado exitosamente!\n\n" +
                "Ya puedes acceder a todas las funcionalidades de tu cuenta.\n\n" +
                "Saludos,\n" +
                "Equipo Droguería Bellavista",
                username
            );
            
            sendEmail(to, subject, body);
            log.info("Email verified notification sent to: {}", to);
            
        } catch (Exception e) {
            log.error("Error sending email verified notification to {}: {}", to, e.getMessage());
        }
    }
    
    /**
     * Send password changed confirmation email.
     */
    @Async
    public void sendPasswordChangedEmail(String to, String username) {
        try {
            String subject = "Contraseña Actualizada - Droguería Bellavista";
            
            String body = String.format(
                "Hola %s,\n\n" +
                "Tu contraseña ha sido actualizada exitosamente.\n\n" +
                "Si no realizaste este cambio, por favor contacta inmediatamente con soporte.\n\n" +
                "Saludos,\n" +
                "Equipo Droguería Bellavista",
                username
            );
            
            sendEmail(to, subject, body);
            log.info("Password changed email sent to: {}", to);
            
        } catch (Exception e) {
            log.error("Error sending password changed email to {}: {}", to, e.getMessage());
        }
    }
    
    /**
     * Internal method to send email using JavaMailSender.
     */
    private void sendEmail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        
        mailSender.send(message);
    }
}
