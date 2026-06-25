package com.climaterisk.common.entity;

import jakarta.persistence.*;
import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "risk_scores", indexes = {
        @Index(name = "idx_risk_scores_zone_id", columnList = "zoneId"),
        @Index(name = "idx_risk_scores_zone_ts", columnList = "zoneId, scoreTimestamp DESC"),
        @Index(name = "idx_risk_scores_flood_high", columnList = "floodRiskScore DESC"),
        @Index(name = "idx_risk_scores_heat_high", columnList = "heatRiskScore DESC")
})
public class RiskScore {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "zone_id", nullable = false)
    private MicroZone zone;

    @Column(name = "score_type", nullable = false, length = 20)
    private String scoreType;

    @Column(name = "flood_risk_score", nullable = false)
    private int floodRiskScore;

    @Column(name = "heat_risk_score", nullable = false)
    private int heatRiskScore;

    @Column(name = "flood_risk_class", nullable = false, length = 10)
    private String floodRiskClass;

    @Column(name = "heat_risk_class", nullable = false, length = 10)
    private String heatRiskClass;

    @Column(name = "forecast_horizon")
    private OffsetDateTime forecastHorizon;

    @Column(name = "score_timestamp", nullable = false)
    private OffsetDateTime scoreTimestamp;

    @Column(name = "model_version", nullable = false, length = 20)
    private String modelVersion;

    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    public RiskScore() {
    }

    @PrePersist
    protected void onCreate() {
        createdAt = OffsetDateTime.now();
    }

    public UUID getId() {
        return id;
    }

    public MicroZone getZone() {
        return zone;
    }

    public void setZone(MicroZone zone) {
        this.zone = zone;
    }

    public String getScoreType() {
        return scoreType;
    }

    public void setScoreType(String scoreType) {
        this.scoreType = scoreType;
    }

    public int getFloodRiskScore() {
        return floodRiskScore;
    }

    public void setFloodRiskScore(int floodRiskScore) {
        this.floodRiskScore = floodRiskScore;
    }

    public int getHeatRiskScore() {
        return heatRiskScore;
    }

    public void setHeatRiskScore(int heatRiskScore) {
        this.heatRiskScore = heatRiskScore;
    }

    public String getFloodRiskClass() {
        return floodRiskClass;
    }

    public void setFloodRiskClass(String floodRiskClass) {
        this.floodRiskClass = floodRiskClass;
    }

    public String getHeatRiskClass() {
        return heatRiskClass;
    }

    public void setHeatRiskClass(String heatRiskClass) {
        this.heatRiskClass = heatRiskClass;
    }

    public OffsetDateTime getForecastHorizon() {
        return forecastHorizon;
    }

    public void setForecastHorizon(OffsetDateTime forecastHorizon) {
        this.forecastHorizon = forecastHorizon;
    }

    public OffsetDateTime getScoreTimestamp() {
        return scoreTimestamp;
    }

    public void setScoreTimestamp(OffsetDateTime scoreTimestamp) {
        this.scoreTimestamp = scoreTimestamp;
    }

    public String getModelVersion() {
        return modelVersion;
    }

    public void setModelVersion(String modelVersion) {
        this.modelVersion = modelVersion;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof RiskScore that))
            return false;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}