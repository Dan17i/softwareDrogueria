package com.drogueria.bellavista.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * Configuration for async processing.
 * Enables @Async annotation for non-blocking email sending.
 */
@Configuration
@EnableAsync
public class AsyncConfig {
}
