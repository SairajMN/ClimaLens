package com.climaterisk.api.integration;

import com.climaterisk.common.entity.MicroZone;
import com.climaterisk.common.entity.RiskScore;
import com.climaterisk.common.repository.MicroZoneRepository;
import com.climaterisk.common.repository.RiskScoreRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.OffsetDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Full-stack integration test using Testcontainers.
 * <p>
 * Verifies that the application starts with a real PostgreSQL/PostGIS
 * container and basic CRUD operations work end-to-end.
 * </p>
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
public class ClimaLensIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgis/postgis:15-3.4")
            .withDatabaseName("climalens_test")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.datasource.driver-class-name", () -> "org.postgresql.Driver");
        registry.add("spring.jpa.properties.hibernate.dialect",
                () -> "org.hibernate.dialect.PostgreSQLDialect");
        registry.add("spring.flyway.enabled", () -> "true");
    }

    @Autowired
    private MicroZoneRepository microZoneRepository;

    @Autowired
    private RiskScoreRepository riskScoreRepository;

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        microZoneRepository.deleteAll();
        riskScoreRepository.deleteAll();
    }

    @Test
    void applicationStartsWithPostgis() {
        assertTrue(postgres.isRunning(), "PostGIS container should be running");
    }

    @Test
    void shouldCreateAndRetrieveZone() {
        MicroZone zone = MicroZone.builder()
                .h3Index("8928308280fffff")
                .city("Mumbai")
                .country("India")
                .build();

        MicroZone saved = microZoneRepository.save(zone);
        assertNotNull(saved.getId(), "Zone should have an ID after save");

        MicroZone found = microZoneRepository.findByH3Index("8928308280fffff").orElse(null);
        assertNotNull(found, "Zone should be retrievable by H3 index");
        assertEquals("Mumbai", found.getCity());
    }

    @Test
    void shouldPersistAndQueryRiskScores() {
        RiskScore score = new RiskScore();
        score.setScoreType("current");
        score.setFloodRiskScore(85);
        score.setHeatRiskScore(72);
        score.setScoreTimestamp(OffsetDateTime.now());
        score.setModelVersion("heuristic-v1");

        RiskScore saved = riskScoreRepository.save(score);
        assertNotNull(saved.getId(), "RiskScore should have an ID after save");

        // Verify basic retrieval
        assertTrue(riskScoreRepository.findById(saved.getId()).isPresent());
    }
}