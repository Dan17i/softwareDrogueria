package com.drogueria.bellavista.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.mail.javamail.JavaMailSender;

import static org.mockito.Mockito.mock;

/**
 * Test configuration for email functionality.
 * Provides a mock JavaMailSender bean to avoid requiring a real mail server during tests.
 */
@TestConfiguration
public class TestMailConfig {
    
    @Bean
    @Primary
    public JavaMailSender javaMailSender() {
        return mock(JavaMailSender.class);
    }
}
