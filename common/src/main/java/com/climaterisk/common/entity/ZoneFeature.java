package com.climaterisk.common.entity;

import jakarta.persistence.*;
import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "zone_features", indexes = {
        @Index(name = "idx_zone_features_zone_id", columnList = "zoneId"),
        @Index(name = "idx_zone_features_zone_ts", columnList = "zoneId, featureTimestamp DESC")
})
public class ZoneFeature {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "zone_id", nullable = false)
    private MicroZone zone;

    @Column(name = "static_features", columnDefinition = "JSONB", nullable = false)
    private String staticFeatures;

    @Column(name = "dynamic_features", columnDefinition = "JSONB", nullable = false)
    private String dynamicFeatures;

    @Column(name = "feature_timestamp", nullable = false)
    private OffsetDateTime featureTimestamp;

    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    public ZoneFeature() {
    }

    @PrePersist
    protected void onCreate() {
        createdAt = OffsetDateTime.now();
        updatedAt = OffsetDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = OffsetDateTime.now();
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

    public String getStaticFeatures() {
        return staticFeatures;
    }

    public void setStaticFeatures(String staticFeatures) {
        this.staticFeatures = staticFeatures;
    }

    public String getDynamicFeatures() {
        return dynamicFeatures;
    }

    public void setDynamicFeatures(String dynamicFeatures) {
        this.dynamicFeatures = dynamicFeatures;
    }

    public OffsetDateTime getFeatureTimestamp() {
        return featureTimestamp;
    }

    public void setFeatureTimestamp(OffsetDateTime featureTimestamp) {
        this.featureTimestamp = featureTimestamp;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof ZoneFeature that))
            return false;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}