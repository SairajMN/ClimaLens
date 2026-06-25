package com.climaterisk.scoring.service;

import com.climaterisk.common.entity.RiskScore;
import com.climaterisk.common.entity.ScoreExplanation;
import com.climaterisk.common.repository.RiskScoreRepository;
import com.climaterisk.common.repository.ScoreExplanationRepository;
import com.climaterisk.scoring.model.FloodRiskModel;
import com.climaterisk.scoring.model.HeatRiskModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Orchestrates risk scoring for all zones.
 * <p>
 * Runs both flood and heat risk models, persists scores,
 * and generates explanations.
 * </p>
 */
@Service
public class ScoringService {

    private final FloodRiskModel floodRiskModel;
    private final HeatRiskModel heatRiskModel;
    private final ExplanationService explanationService;
    private final RiskScoreRepository riskScoreRepository;
    private final ScoreExplanationRepository scoreExplanationRepository;

    public ScoringService(FloodRiskModel floodRiskModel,
            HeatRiskModel heatRiskModel,
            ExplanationService explanationService,
            RiskScoreRepository riskScoreRepository,
            ScoreExplanationRepository scoreExplanationRepository) {
        this.floodRiskModel = floodRiskModel;
        this.heatRiskModel = heatRiskModel;
        this.explanationService = explanationService;
        this.riskScoreRepository = riskScoreRepository;
        this.scoreExplanationRepository = scoreExplanationRepository;
    }

    /**
     * Scores a single zone and persists results.
     *
     * @param zoneId H3 index of the zone
     * @return RiskScore entity
     */
    @Transactional
    public RiskScore scoreZone(String zoneId, Map<String, Object> staticFeatures, Map<String, Object> dynamicFeatures) {
        // Predict scores
        double floodScore = floodRiskModel.predict(staticFeatures, dynamicFeatures);
        double heatScore = heatRiskModel.predict(staticFeatures, dynamicFeatures);

        // Persist risk score
        RiskScore riskScore = new RiskScore();
        riskScore.setScoreType("current");
        riskScore.setFloodRiskScore((int) floodScore);
        riskScore.setHeatRiskScore((int) heatScore);
        riskScore.setScoreTimestamp(java.time.OffsetDateTime.now());
        riskScore.setModelVersion("heuristic-v1");

        RiskScore savedScore = riskScoreRepository.save(riskScore);

        // Generate and persist explanations
        saveExplanations(savedScore, staticFeatures, dynamicFeatures, floodScore, heatScore);

        return savedScore;
    }

    /**
     * Scores all zones in a city.
     *
     * @param city city name
     * @return list of RiskScore entities
     */
    @Transactional
    public List<RiskScore> scoreCity(String city) {
        // TODO: Implement batch scoring when MicroZoneRepository is available
        // For now, placeholder
        return List.of();
    }

    private void saveExplanations(RiskScore riskScore,
            Map<String, Object> staticFeatures,
            Map<String, Object> dynamicFeatures,
            double floodScore,
            double heatScore) {
        // Flood explanations
        List<ExplanationService.ExplanationFactor> floodFactors = explanationService
                .explainFloodRisk(staticFeatures, dynamicFeatures, floodScore);

        String floodSummary = floodFactors.isEmpty() ? "No data"
                : floodFactors.get(floodFactors.size() - 1).description();
        String floodJson = floodFactors.stream()
                .filter(f -> !"Summary".equals(f.name()))
                .map(f -> String.format("{\"factor\":\"%s\",\"value\":\"%s\",\"contribution\":%.1f}",
                        f.name(), f.value(), f.contribution()))
                .collect(Collectors.joining(",", "[", "]"));

        ScoreExplanation floodExplanation = new ScoreExplanation();
        floodExplanation.setRiskScore(riskScore);
        floodExplanation.setRiskType("flood");
        floodExplanation.setTopFactors(floodJson);
        floodExplanation.setPlainSummary(floodSummary);
        scoreExplanationRepository.save(floodExplanation);

        // Heat explanations
        List<ExplanationService.ExplanationFactor> heatFactors = explanationService
                .explainHeatRisk(staticFeatures, dynamicFeatures, heatScore);

        String heatSummary = heatFactors.isEmpty() ? "No data" : heatFactors.get(heatFactors.size() - 1).description();
        String heatJson = heatFactors.stream()
                .filter(f -> !"Summary".equals(f.name()))
                .map(f -> String.format("{\"factor\":\"%s\",\"value\":\"%s\",\"contribution\":%.1f}",
                        f.name(), f.value(), f.contribution()))
                .collect(Collectors.joining(",", "[", "]"));

        ScoreExplanation heatExplanation = new ScoreExplanation();
        heatExplanation.setRiskScore(riskScore);
        heatExplanation.setRiskType("heat");
        heatExplanation.setTopFactors(heatJson);
        heatExplanation.setPlainSummary(heatSummary);
        scoreExplanationRepository.save(heatExplanation);
    }
}