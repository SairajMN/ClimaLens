package com.climaterisk.api.controller;

import com.climaterisk.common.entity.ScoreExplanation;
import com.climaterisk.common.repository.ScoreExplanationRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for risk score explanations.
 * <p>
 * Provides endpoints to retrieve plain-language explanations
 * for flood and heat risk scores.
 * </p>
 */
@RestController
@RequestMapping("/api/v1/explanations")
public class ExplanationController {

    private final ScoreExplanationRepository scoreExplanationRepository;

    public ExplanationController(ScoreExplanationRepository scoreExplanationRepository) {
        this.scoreExplanationRepository = scoreExplanationRepository;
    }

    /**
     * Get all explanations for a risk score.
     */
    @GetMapping("/risk-score/{riskScoreId}")
    public ResponseEntity<List<ScoreExplanation>> getExplanations(@PathVariable String riskScoreId) {
        // TODO: Implement when RiskScore entity has UUID mapping
        // For now, return empty list
        return ResponseEntity.ok(List.of());
    }

    /**
     * Get flood risk explanation for a zone.
     */
    @GetMapping("/flood/{h3Index}")
    public ResponseEntity<ScoreExplanation> getFloodExplanation(@PathVariable String h3Index) {
        // TODO: Implement when zone-scoring integration is complete
        // For now, return empty response
        return ResponseEntity.noContent().build();
    }

    /**
     * Get heat risk explanation for a zone.
     */
    @GetMapping("/heat/{h3Index}")
    public ResponseEntity<ScoreExplanation> getHeatExplanation(@PathVariable String h3Index) {
        // TODO: Implement when zone-scoring integration is complete
        // For now, return empty response
        return ResponseEntity.noContent().build();
    }
}