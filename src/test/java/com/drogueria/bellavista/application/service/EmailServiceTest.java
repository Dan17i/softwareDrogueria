package com.drogueria.bellavista.application.service;

import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.*;

class EmailServiceTest {

    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private EmailService emailService;

    private MimeMessage mimeMessage;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(emailService, "fromEmail", "noreply@bellavista.com");
        ReflectionTestUtils.setField(emailService, "frontendUrl", "http://localhost:5173");
        mimeMessage = new MimeMessage((jakarta.mail.Session) null);
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
    }

    @Test
    @DisplayName("✅ sendWelcomeEmail - invoca mailSender.send()")
    void shouldSendWelcomeEmail() {
        doNothing().when(mailSender).send(any(MimeMessage.class));

        emailService.sendWelcomeEmail("user@test.com", "daniel");

        verify(mailSender).createMimeMessage();
        verify(mailSender).send(any(MimeMessage.class));
    }

    @Test
    @DisplayName("✅ sendPasswordResetEmail - invoca mailSender.send() con token")
    void shouldSendPasswordResetEmail() {
        doNothing().when(mailSender).send(any(MimeMessage.class));

        emailService.sendPasswordResetEmail("user@test.com", "daniel", "reset-token-abc");

        verify(mailSender).send(any(MimeMessage.class));
    }

    @Test
    @DisplayName("✅ sendEmailVerifiedNotification - invoca mailSender.send()")
    void shouldSendEmailVerifiedNotification() {
        doNothing().when(mailSender).send(any(MimeMessage.class));

        emailService.sendEmailVerifiedNotification("user@test.com", "daniel");

        verify(mailSender).send(any(MimeMessage.class));
    }

    @Test
    @DisplayName("✅ sendPasswordChangedEmail - invoca mailSender.send()")
    void shouldSendPasswordChangedEmail() {
        doNothing().when(mailSender).send(any(MimeMessage.class));

        emailService.sendPasswordChangedEmail("user@test.com", "daniel");

        verify(mailSender).send(any(MimeMessage.class));
    }

    @Test
    @DisplayName("✅ resendWelcomeEmail - delega a sendWelcomeEmail e invoca send()")
    void shouldResendWelcomeEmail() {
        doNothing().when(mailSender).send(any(MimeMessage.class));

        emailService.resendWelcomeEmail("user@test.com", "daniel");

        verify(mailSender).send(any(MimeMessage.class));
    }

    @Test
    @DisplayName("❌ sendWelcomeEmail - excepción SMTP no se propaga al caller")
    void shouldHandleSmtpExceptionInWelcomeEmail() {
        doThrow(new RuntimeException("SMTP connection refused"))
            .when(mailSender).send(any(MimeMessage.class));

        assertDoesNotThrow(() -> emailService.sendWelcomeEmail("user@test.com", "daniel"));
    }

    @Test
    @DisplayName("❌ sendPasswordResetEmail - excepción SMTP no se propaga")
    void shouldHandleSmtpExceptionInPasswordReset() {
        doThrow(new RuntimeException("Auth failed"))
            .when(mailSender).send(any(MimeMessage.class));

        assertDoesNotThrow(() -> emailService.sendPasswordResetEmail("user@test.com", "daniel", "token"));
    }

    @Test
    @DisplayName("❌ sendPasswordChangedEmail - excepción SMTP no se propaga")
    void shouldHandleSmtpExceptionInPasswordChanged() {
        doThrow(new RuntimeException("SMTP error"))
            .when(mailSender).send(any(MimeMessage.class));

        assertDoesNotThrow(() -> emailService.sendPasswordChangedEmail("user@test.com", "daniel"));
    }

    @Test
    @DisplayName("❌ sendEmailVerifiedNotification - excepción SMTP no se propaga")
    void shouldHandleSmtpExceptionInEmailVerified() {
        doThrow(new RuntimeException("Timeout"))
            .when(mailSender).send(any(MimeMessage.class));

        assertDoesNotThrow(() -> emailService.sendEmailVerifiedNotification("user@test.com", "daniel"));
    }
}
