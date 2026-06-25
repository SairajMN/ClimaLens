package com.climaterisk.api;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

/**
 * Smoke test that verifies the Spring application context loads successfully.
 * <p>
 * Uses the {@code test} profile with Testcontainers for PostGIS.
 * This test will pass once Flyway migrations and basic bean wiring are correct.
 * </p>
 */
@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(properties = {
        "spring.data.redis.host=localhost",
        "spring.data.redis.port=6379",
        "spring.mail.host=localhost",
        "spring.mail.port=3025"
})
class ClimaLensApplicationTest {

    @Test
    void contextLoads() {
        // Verifies the application context starts without errors.
        // This is a placeholder — it will be expanded with real tests later.
    }
}