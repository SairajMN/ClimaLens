/**
 * Shared domain layer for ClimaLens.
 * <p>
 * Contains JPA entities, shared DTOs, geospatial utility classes (H3, JTS helpers),
 * and constants used across all modules. This module has no Spring Boot auto-configuration;
 * it is a plain JAR intended to be imported by {@code ingestion}, {@code scoring}, and {@code api}.
 * </p>
 */
package com.climaterisk.common;