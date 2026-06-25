package com.climaterisk.api.config;

import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;
import io.github.resilience4j.timelimiter.TimeLimiter;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

/**
 * Resilience4j configuration for external API clients.
 * <p>
 * Each external data client has:
 * - Timeout: 5 seconds max
 * - Retry: 2 retries with 1s backoff
 * - Fallback: returns degraded last-known data
 * </p>
 */
@Configuration
public class ResilienceConfig {

    @Bean
    public TimeLimiter externalApiTimeLimiter() {
        return TimeLimiter.of(TimeLimiterConfig.custom()
                .timeoutDuration(Duration.ofSeconds(5))
                .build());
    }

    @Bean
    public Retry externalApiRetry() {
        return Retry.of("external-api-retry", RetryConfig.custom()
                .maxAttempts(2)
                .waitDuration(Duration.ofSeconds(1))
                .build());
    }

    @Bean
    public TimeLimiter weatherApiTimeLimiter() {
        return TimeLimiter.of(TimeLimiterConfig.custom()
                .timeoutDuration(Duration.ofSeconds(8))
                .build());
    }

    @Bean
    public Retry weatherApiRetry() {
        return Retry.of("weather-api-retry", RetryConfig.custom()
                .maxAttempts(3)
                .waitDuration(Duration.ofSeconds(2))
                .build());
    }
}