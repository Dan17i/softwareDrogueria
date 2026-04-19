package com.drogueria.bellavista.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

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
     * Send welcome email with HTML format (no verification required).
     */
    @Async
    public void sendWelcomeEmail(String to, String username) {
        try {
            String subject = "¡Bienvenido a Invetoryx! Tu cuenta está lista";
            String htmlBody = buildWelcomeEmailTemplate(username);
            
            sendHtmlEmail(to, subject, htmlBody);
            log.info("Welcome email sent to: {}", to);
            
        } catch (Exception e) {
            log.error("Error sending welcome email to {}: {}", to, e.getMessage(), e);
        }
    }
    
    /**
     * Build HTML template for welcome email.
     */
    private String buildWelcomeEmailTemplate(String username) {
        return "<!DOCTYPE html><html lang=\"es\"><head><meta charset=\"UTF-8\"><meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\"><title>Bienvenido a Invetoryx</title><style>* { margin: 0; padding: 0; box-sizing: border-box; }body { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; background: #f5f5f5; }.email-container { max-width: 600px; margin: 0 auto; background: #ffffff; }.header { background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); color: white; padding: 40px 20px; text-align: center; }.header h1 { font-size: 28px; margin-bottom: 10px; }.header p { font-size: 14px; opacity: 0.9; }.content { padding: 40px 30px; }.greeting { font-size: 18px; color: #333; margin-bottom: 20px; }.greeting strong { color: #667eea; }.description { color: #555; line-height: 1.6; margin-bottom: 30px; font-size: 14px; }.features { background: #f9f9f9; border-left: 4px solid #667eea; padding: 20px; margin: 30px 0; border-radius: 4px; }.features h3 { color: #667eea; margin-bottom: 15px; font-size: 16px; }.feature-item { color: #555; margin-bottom: 10px; padding-left: 20px; position: relative; font-size: 13px; }.feature-item:before { content: '✓'; position: absolute; left: 0; color: #667eea; font-weight: bold; }.credentials { background: #f0f4ff; padding: 15px; border-radius: 4px; margin: 25px 0; }.credentials p { color: #333; font-size: 13px; margin-bottom: 8px; }.username-box { background: white; padding: 10px; border-left: 3px solid #667eea; font-family: monospace; color: #667eea; font-weight: bold; }.cta-button { display: inline-block; background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); color: white; padding: 14px 40px; text-decoration: none; border-radius: 4px; margin: 25px 0; font-weight: bold; font-size: 14px; }.cta-button:hover { opacity: 0.9; }.video-section { background: #f9f9f9; padding: 20px; border-radius: 4px; margin: 25px 0; text-align: center; }.video-section a { display: inline-block; background: #667eea; color: white; padding: 10px 25px; text-decoration: none; border-radius: 4px; margin-top: 10px; font-size: 13px; }.video-section a:hover { background: #764ba2; }.footer { background: #f5f5f5; padding: 20px 30px; border-top: 1px solid #e0e0e0; text-align: center; color: #777; font-size: 12px; }.footer a { color: #667eea; text-decoration: none; }.divider { height: 1px; background: #e0e0e0; margin: 20px 0; }@media (max-width: 600px) { .content { padding: 25px 15px; }.header { padding: 30px 15px; }.header h1 { font-size: 22px; }}</style></head><body><div class=\"email-container\"><div class=\"header\"><h1>🎉 ¡Bienvenido a Invetoryx!</h1><p>Tu cuenta está lista para usar</p></div><div class=\"content\"><div class=\"greeting\">Hola <strong>" + username + "</strong>,</div><div class=\"description\">¡Gracias por registrarte en <strong>Droguería Bellavista</strong>! 🙌</div><p style=\"color: #555; line-height: 1.6; font-size: 14px; margin-bottom: 20px;\">Nos complace confirmar que tu cuenta ha sido creada <strong>exitosamente</strong>. Ya puedes comenzar a utilizar nuestro sistema de gestión empresarial.</p><div class=\"credentials\"><p>Tu nombre de usuario es:</p><div class=\"username-box\">" + username + "</div></div><div class=\"features\"><h3>📊 ¿Qué puedes hacer con tu cuenta?</h3><div class=\"feature-item\">Gestionar inventario de productos en tiempo real</div><div class=\"feature-item\">Administrar clientes y proveedores</div><div class=\"feature-item\">Crear y gestionar órdenes de compra</div><div class=\"feature-item\">Controlar recepción de mercancía</div><div class=\"feature-item\">Generar reportes de gestión</div></div><center><a href=\"" + frontendUrl + "\" class=\"cta-button\">→ Acceder al Sistema</a></center><div class=\"video-section\"><p style=\"color: #555; font-size: 14px; font-weight: 500; margin-bottom: 5px;\">🎥 ¿Primera vez usando el sistema?</p><p style=\"color: #777; font-size: 13px; margin-bottom: 10px;\">Te invitamos a ver nuestro tutorial completo</p><a href=\"https://youtu.be/GQ_C6K_xuFA\" target=\"_blank\">Ver Video Tutorial</a></div><div class=\"divider\"></div><p style=\"color: #555; font-size: 13px; line-height: 1.6;\">Si tienes alguna pregunta o necesitas ayuda, estamos aquí para apoyarte. No dudes en contactarnos respondiendo a este email.</p><p style=\"color: #555; font-size: 14px; margin-top: 25px;\"><strong>¡Bienvenido a bordo! 🚀</strong></p></div><div class=\"footer\"><p style=\"margin-bottom: 10px; color: #555;\">Droguería Bellavista</p><p>Sistema de Gestión Empresarial © 2026</p><div><a href=\"" + frontendUrl + "\">Sistema</a> | <a href=\"mailto:soporte@bellavista.com\">Soporte</a></div><p style=\"margin-top: 15px; color: #999; font-size: 11px;\">Este es un correo automático. Por favor no respondas a esta dirección.</p></div></div></body></html>";
    }
    
    /**
     * Send password reset email with reset link (HTML format).
     */
    @Async
    public void sendPasswordResetEmail(String to, String username, String resetToken) {
        try {
            String subject = "Recuperación de Contraseña - Droguería Bellavista";
            String resetLink = frontendUrl + "/reset-password?token=" + resetToken;
            String htmlBody = buildPasswordResetEmailTemplate(username, resetLink);
            
            sendHtmlEmail(to, subject, htmlBody);
            log.info("Password reset email sent to: {}", to);
            
        } catch (Exception e) {
            log.error("Error sending password reset email to {}: {} - CAUSE: {}",
                to, e.getMessage(), e.getCause() != null ? e.getCause().getMessage() : "none", e);
            throw new RuntimeException(e);
        }
    }
    
    /**
     * Build HTML template for password reset email.
     */
    private String buildPasswordResetEmailTemplate(String username, String resetLink) {
        return "<!DOCTYPE html><html lang=\"es\"><head><meta charset=\"UTF-8\"><meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\"><title>Recuperar Contraseña</title><style>* { margin: 0; padding: 0; box-sizing: border-box; }body { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; background: #f5f5f5; }.email-container { max-width: 600px; margin: 0 auto; background: #ffffff; }.header { background: linear-gradient(135deg, #ff6b6b 0%, #ee5a52 100%); color: white; padding: 40px 20px; text-align: center; }.header h1 { font-size: 24px; margin-bottom: 10px; }.content { padding: 40px 30px; }.cta-button { display: inline-block; background: linear-gradient(135deg, #ff6b6b 0%, #ee5a52 100%); color: white; padding: 14px 40px; text-decoration: none; border-radius: 4px; margin: 25px 0; font-weight: bold; font-size: 14px; }.reset-link { background: #f9f9f9; padding: 15px; border-radius: 4px; word-break: break-all; font-family: monospace; font-size: 12px; color: #666; margin: 20px 0; }.warning { background: #fff3cd; border-left: 4px solid #ff6b6b; padding: 15px; border-radius: 4px; margin: 20px 0; }.security-notice { background: #e8f4f8; padding: 15px; border-left: 4px solid #0066cc; border-radius: 4px; margin: 20px 0; }.footer { background: #f5f5f5; padding: 20px 30px; border-top: 1px solid #e0e0e0; text-align: center; color: #777; font-size: 12px; }@media (max-width: 600px) { .content { padding: 25px 15px; }.header { padding: 30px 15px; }}</style></head><body><div class=\"email-container\"><div class=\"header\"><h1>🔐 Recuperar Contraseña</h1></div><div class=\"content\"><p>Hola " + username + ",</p><p style=\"margin-top: 15px;\">Recibimos una solicitud para <strong>restablecer la contraseña</strong> de tu cuenta.</p><p style=\"margin-top: 15px; color: #555; line-height: 1.6; font-size: 14px;\">Para crear una nueva contraseña, haz clic en el botón de abajo:</p><center><a href=\"" + resetLink + "\" class=\"cta-button\">→ Restablecer Contraseña</a></center><p style=\"color: #999; font-size: 12px; text-align: center; margin: 15px 0;\">O copia y pega este enlace en tu navegador:</p><div class=\"reset-link\">" + resetLink + "</div><div class=\"warning\"><p><strong>⏰ Este enlace es válido por 1 hora</strong></p><p>Después de ese tiempo, deberás solicitar un nuevo enlace de recuperación.</p></div><div class=\"security-notice\"><p><strong>🛡️ Importante:</strong> Si no solicitaste restablecer tu contraseña, puedes ignorar este mensaje.</p><p style=\"margin-top: 8px;\">Por tu seguridad, <strong>nunca compartas</strong> este enlace con nadie.</p></div></div><div class=\"footer\"><p style=\"margin-bottom: 10px; color: #555;\">Droguería Bellavista</p><p>Sistema de Gestión Empresarial © 2026</p></div></div></body></html>";
    }
    
    /**
     * Send email verification success notification (HTML format).
     */
    @Async
    public void sendEmailVerifiedNotification(String to, String username) {
        try {
            String subject = "Email Verificado - Droguería Bellavista";
            String htmlBody = buildEmailVerifiedTemplate(username);
            
            sendHtmlEmail(to, subject, htmlBody);
            log.info("Email verified notification sent to: {}", to);
            
        } catch (Exception e) {
            log.error("Error sending email verified notification to {}: {}", to, e.getMessage(), e);
        }
    }
    
    /**
     * Build HTML template for email verified notification.
     */
    private String buildEmailVerifiedTemplate(String username) {
        return "<!DOCTYPE html><html lang=\"es\"><head><meta charset=\"UTF-8\"><meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\"><title>Email Verificado</title><style>* { margin: 0; padding: 0; box-sizing: border-box; }body { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; background: #f5f5f5; }.email-container { max-width: 600px; margin: 0 auto; background: #ffffff; }.header { background: linear-gradient(135deg, #11998e 0%, #38ef7d 100%); color: white; padding: 40px 20px; text-align: center; }.header h1 { font-size: 28px; margin-bottom: 10px; }.content { padding: 40px 30px; text-align: center; }.checkmark { font-size: 60px; margin-bottom: 20px; }.success-box { background: #f0fff4; border-left: 4px solid #11998e; padding: 20px; border-radius: 4px; margin: 20px 0; }.cta-button { display: inline-block; background: linear-gradient(135deg, #11998e 0%, #38ef7d 100%); color: white; padding: 14px 40px; text-decoration: none; border-radius: 4px; margin: 25px 0; font-weight: bold; font-size: 14px; }.footer { background: #f5f5f5; padding: 20px 30px; border-top: 1px solid #e0e0e0; text-align: center; color: #777; font-size: 12px; }@media (max-width: 600px) { .content { padding: 25px 15px; }.header { padding: 30px 15px; }}</style></head><body><div class=\"email-container\"><div class=\"header\"><h1>✓ Email Verificado</h1></div><div class=\"content\"><div class=\"checkmark\">✅</div><p style=\"font-size: 18px; color: #333; margin-bottom: 20px;\">¡Excelente, " + username + "!</p><div class=\"success-box\"><p><strong>Tu email ha sido verificado exitosamente</strong></p></div><p style=\"color: #555; line-height: 1.6; margin-bottom: 25px; font-size: 14px;\">Ya puedes acceder a <strong>todas las funcionalidades</strong> de tu cuenta.</p><center><a href=\"" + frontendUrl + "\" class=\"cta-button\">→ Acceder al Sistema</a></center></div><div class=\"footer\"><p style=\"margin-bottom: 10px; color: #555;\">Droguería Bellavista</p><p>Sistema de Gestión Empresarial © 2026</p></div></div></body></html>";
    }
    
    /**
     * Resend welcome email.
     */
    @Async
    public void resendWelcomeEmail(String to, String username) {
        sendWelcomeEmail(to, username);
    }
    
    /**
     * Send password changed confirmation email (HTML format).
     */
    @Async
    public void sendPasswordChangedEmail(String to, String username) {
        try {
            String subject = "Contraseña Actualizada - Droguería Bellavista";
            String htmlBody = buildPasswordChangedTemplate(username);
            
            sendHtmlEmail(to, subject, htmlBody);
            log.info("Password changed email sent to: {}", to);
            
        } catch (Exception e) {
            log.error("Error sending password changed email to {}: {}", to, e.getMessage(), e);
        }
    }
    
    /**
     * Build HTML template for password changed notification.
     */
    private String buildPasswordChangedTemplate(String username) {
        return "<!DOCTYPE html><html lang=\"es\"><head><meta charset=\"UTF-8\"><meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\"><title>Contraseña Actualizada</title><style>* { margin: 0; padding: 0; box-sizing: border-box; }body { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; background: #f5f5f5; }.email-container { max-width: 600px; margin: 0 auto; background: #ffffff; }.header { background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); color: white; padding: 40px 20px; text-align: center; }.header h1 { font-size: 24px; margin-bottom: 10px; }.content { padding: 40px 30px; }.alert-box { background: #f8f9fa; border-left: 4px solid #667eea; padding: 15px; border-radius: 4px; margin: 20px 0; }.footer { background: #f5f5f5; padding: 20px 30px; border-top: 1px solid #e0e0e0; text-align: center; color: #777; font-size: 12px; }@media (max-width: 600px) { .content { padding: 25px 15px; }.header { padding: 30px 15px; }}</style></head><body><div class=\"email-container\"><div class=\"header\"><h1>🔒 Contraseña Actualizada</h1></div><div class=\"content\"><p>Hola " + username + ",</p><div class=\"alert-box\"><p><strong>✓ Tu contraseña ha sido actualizada exitosamente</strong></p></div><p style=\"margin-top: 15px; color: #555; font-size: 14px; line-height: 1.6;\">Tu nueva contraseña está activa y lista para usar.</p><p style=\"margin-top: 15px; color: #555; font-size: 14px; line-height: 1.6;\"><strong>Nota importante:</strong> Si no realizaste este cambio de contraseña, por favor contacta inmediatamente con soporte.</p><p style=\"color: #999; font-size: 13px; text-align: center; margin-top: 30px;\">Para cualquier duda, contáctanos respondiendo a este email.</p></div><div class=\"footer\"><p style=\"margin-bottom: 10px; color: #555;\">Droguería Bellavista</p><p>Sistema de Gestión Empresarial © 2026</p></div></div></body></html>";
    }
    
    /**
     * Internal method to send HTML email using JavaMailSender.
     */
    private void sendHtmlEmail(String to, String subject, String htmlBody) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        
        helper.setFrom(fromEmail);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlBody, true); // true = HTML format
        
        mailSender.send(message);
    }
}

