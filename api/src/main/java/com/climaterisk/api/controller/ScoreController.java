package com.climaterisk.api.controller;

import com.climaterisk.common.entity.RiskScore;
import com.climaterisk.common.repository.RiskScoreRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * REST controller for risk score operations.
 * <p>
 * Provides endpoints to retrieve current, forecast, and historical
 * risk scores for zones.
 * </p>
 */
@RestController
@RequestMapping("/api/v1/scores")
public class ScoreController {

    private final RiskScoreRepository riskScoreRepository;

    public ScoreController(RiskScoreRepository riskScoreRepository) {
        this.riskScoreRepository = riskScoreRepository;
    }

    /**
     * Get all current risk scores with simplified zone data for frontend.
     */
    @GetMapping("/current")
    public ResponseEntity<List<Object>> getAllCurrentScores() {
        List<RiskScore> scores = riskScoreRepository.findAll();

        List<Object> simplified = scores.stream().map(score -> {
            var zone = score.getZone();
            return new Object() {
                public String id = score.getId().toString();
                public String zoneId = zone.getId().toString();
                public String zoneName = zone.getZoneName();
                public String city = zone.getCity();
                public double lat = zone.getCentroidLat();
                public double lon = zone.getCentroidLon();
                public int floodRiskScore = score.getFloodRiskScore();
                public int heatRiskScore = score.getHeatRiskScore();
                public String floodRiskClass = score.getFloodRiskClass();
                public String heatRiskClass = score.getHeatRiskClass();
                public String scoreTimestamp = score.getScoreTimestamp().toString();
            };
        }).collect(Collectors.toList());

        return ResponseEntity.ok(simplified);
    }

    /**
     * Get high-risk flood zones (hotspots).
     */
    @GetMapping("/hotspots/flood")
    public ResponseEntity<List<RiskScore>> getFloodHotspots(@RequestParam(defaultValue = "10") int limit) {
        List<RiskScore> hotspots = riskScoreRepository.findFloodHotspots(limit);
        return ResponseEntity.ok(hotspots);
    }

    /**
     * Get high-risk heat zones (hotspots).
     */
    @GetMapping("/hotspots/heat")
    public ResponseEntity<List<RiskScore>> getHeatHotspots(@RequestParam(defaultValue = "10") int limit) {
        List<RiskScore> hotspots = riskScoreRepository.findHeatHotspots(limit);
        return ResponseEntity.ok(hotspots);
    }
}