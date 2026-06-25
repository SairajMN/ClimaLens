package com.climaterisk.common.entity;

import jakarta.persistence.*;
import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "score_explanations", indexes = {
        @Index(name = "idx_score_explanations_risk_score_id", columnList = "riskScoreId")
})
public class ScoreExplanation {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "risk_score_id", nullable = false)
    private RiskScore riskScore;

    @Column(name = "risk_type", nullable = false, length = 10)
    private String riskType;

    @Column(name = "top_factors", columnDefinition = "JSONB", nullable = false)
    private String topFactors;

    @Column(name = "plain_summary", nullable = false, columnDefinition = "TEXT")
    private String plainSummary;

    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    public ScoreExplanation() {
    }

    @PrePersist
    protected void onCreate() {
        createdAt = OffsetDateTime.now();
    }

    public UUID getId() {
        return id;
    }

    public RiskScore getRiskScore() {
        return riskScore;
    }

    public void setRiskScore(RiskScore riskScore) {
        this.riskScore = riskScore;
    }

    public String getRiskType() {
        return riskType;
    }

    public void setRiskType(String riskType) {
        this.riskType = riskType;
    }

    public String getTopFactors() {
        return topFactors;
    }

    public void setTopFactors(String topFactors) {
        this.topFactors = topFactors;
    }

    public String getPlainSummary() {
        return plainSummary;
    }

    public void setPlainSummary(String plainSummary) {
        this.plainSummary = plainSummary;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof ScoreExplanation that))
            return false;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}