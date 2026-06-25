/**
 * ML risk-scoring engine for ClimaLens.
 * <p>
 * Contains the flood and heat risk models built with Smile gradient boosting,
 * feature importance extraction, and the plain-language explanation generator.
 * The {@link com.climaterisk.scoring.service.ScoringService} orchestrates
 * model inference per zone and persists results to the database.
 * </p>
 */
package com.climaterisk.scoring;