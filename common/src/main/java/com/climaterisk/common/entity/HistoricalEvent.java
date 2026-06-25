package com.climaterisk.common.entity;

import jakarta.persistence.*;
import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "historical_events", indexes = {
        @Index(name = "idx_historical_events_zone_id", columnList = "zoneId"),
        @Index(name = "idx_historical_events_start", columnList = "eventStart DESC")
})
public class HistoricalEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "zone_id", nullable = false)
    private MicroZone zone;

    @Column(name = "event_type", nullable = false, length = 20)
    private String eventType;

    @Column(name = "event_severity", nullable = false, length = 10)
    private String eventSeverity;

    @Column(name = "event_start", nullable = false)
    private OffsetDateTime eventStart;

    @Column(name = "event_end")
    private OffsetDateTime eventEnd;

    @Column(name = "observed_flood_risk")
    private Integer observedFloodRisk;

    @Column(name = "observed_heat_risk")
    private Integer observedHeatRisk;

    @Column(name = "source", nullable = false, length = 50)
    private String source;

    @Column(name = "source_id", length = 100)
    private String sourceId;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    public HistoricalEvent() {
    }

    @PrePersist
    protected void onCreate() {
        createdAt = OffsetDateTime.now();
    }

    // Getters & Setters
    public UUID getId() {
        return id;
    }

    public MicroZone getZone() {
        return zone;
    }

    public void setZone(MicroZone zone) {
        this.zone = zone;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getEventSeverity() {
        return eventSeverity;
    }

    public void setEventSeverity(String eventSeverity) {
        this.eventSeverity = eventSeverity;
    }

    public OffsetDateTime getEventStart() {
        return eventStart;
    }

    public void setEventStart(OffsetDateTime eventStart) {
        this.eventStart = eventStart;
    }

    public OffsetDateTime getEventEnd() {
        return eventEnd;
    }

    public void setEventEnd(OffsetDateTime eventEnd) {
        this.eventEnd = eventEnd;
    }

    public Integer getObservedFloodRisk() {
        return observedFloodRisk;
    }

    public void setObservedFloodRisk(Integer observedFloodRisk) {
        this.observedFloodRisk = observedFloodRisk;
    }

    public Integer getObservedHeatRisk() {
        return observedHeatRisk;
    }

    public void setObservedHeatRisk(Integer observedHeatRisk) {
        this.observedHeatRisk = observedHeatRisk;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof HistoricalEvent that))
            return false;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}