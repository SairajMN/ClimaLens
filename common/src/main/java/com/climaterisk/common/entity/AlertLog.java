package com.climaterisk.common.entity;

import jakarta.persistence.*;
import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "alert_log", indexes = {
        @Index(name = "idx_alert_log_zone_id", columnList = "zoneId"),
        @Index(name = "idx_alert_log_dispatched", columnList = "dispatchedAt DESC")
})
public class AlertLog {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "zone_id", nullable = false)
    private MicroZone zone;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "risk_score_id")
    private RiskScore riskScore;

    @Column(name = "alert_type", nullable = false, length = 20)
    private String alertType;

    @Column(name = "severity", nullable = false, length = 10)
    private String severity;

    @Column(name = "message", nullable = false, columnDefinition = "TEXT")
    private String message;

    @Column(name = "channel", nullable = false, length = 20)
    private String channel;

    @Column(name = "recipient", length = 200)
    private String recipient;

    @Column(name = "dispatched_at", nullable = false)
    private OffsetDateTime dispatchedAt;

    @Column(name = "acknowledged", nullable = false)
    private boolean acknowledged;

    @Column(name = "acknowledged_at")
    private OffsetDateTime acknowledgedAt;

    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    public AlertLog() {
    }

    @PrePersist
    protected void onCreate() {
        createdAt = OffsetDateTime.now();
        if (dispatchedAt == null)
            dispatchedAt = OffsetDateTime.now();
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

    public RiskScore getRiskScore() {
        return riskScore;
    }

    public void setRiskScore(RiskScore riskScore) {
        this.riskScore = riskScore;
    }

    public String getAlertType() {
        return alertType;
    }

    public void setAlertType(String alertType) {
        this.alertType = alertType;
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public OffsetDateTime getDispatchedAt() {
        return dispatchedAt;
    }

    public void setDispatchedAt(OffsetDateTime dispatchedAt) {
        this.dispatchedAt = dispatchedAt;
    }

    public boolean isAcknowledged() {
        return acknowledged;
    }

    public void setAcknowledged(boolean acknowledged) {
        this.acknowledged = acknowledged;
    }

    public OffsetDateTime getAcknowledgedAt() {
        return acknowledgedAt;
    }

    public void setAcknowledgedAt(OffsetDateTime acknowledgedAt) {
        this.acknowledgedAt = acknowledgedAt;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof AlertLog that))
            return false;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}