package com.climaterisk.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Main entry point for the ClimaLens application.
 * <p>
 * Scans all four modules ({@code com.climaterisk.common},
 * {@code com.climaterisk.ingestion},
 * {@code com.climaterisk.scoring}, {@code com.climaterisk.api}) for Spring
 * components, entities,
 * and JPA repositories. {@link EnableScheduling} activates the
 * {@code @Scheduled} ingestion jobs.
 * </p>
 */
@SpringBootApplication
@ComponentScan(basePackages = "com.climaterisk")
@EntityScan(basePackages = "com.climaterisk.common.entity")
@EnableJpaRepositories(basePackages = "com.climaterisk.common.repository")
@EnableScheduling
public class ClimaLensApplication {

    /**
     * Launches the Spring Boot application.
     *
     * @param args command-line arguments (unused in MVP)
     */
    public static void main(String[] args) {
        SpringApplication.run(ClimaLensApplication.class, args);
    }
}