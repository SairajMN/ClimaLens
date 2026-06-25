package com.climaterisk.common.entity;

import jakarta.persistence.*;
import org.locationtech.jts.geom.Polygon;

import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * A street-level hexagon zone at H3 resolution 9 (~150m).
 * <p>
 * This is the core spatial entity. All risk scores, features, and alerts
 * reference a single {@code MicroZone}. The {@code geometry} field uses
 * PostGIS's GEOMETRY(POLYGON, 4326) type for all spatial queries.
 * </p>
 */
@Entity
@Table(name = "micro_zones", indexes = {
        @Index(name = "idx_micro_zones_h3_index", columnList = "h3Index", unique = true)
})
public class MicroZone {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "h3_index", nullable = false, unique = true, length = 15)
    private String h3Index;

    @Column(name = "geometry", columnDefinition = "GEOMETRY(POLYGON, 4326)", nullable = false)
    private Polygon geometry;

    @Column(name = "zone_name", length = 100)
    private String zoneName;

    @Column(name = "city", nullable = false, length = 100)
    private String city;

    @Column(name = "country", nullable = false, length = 100)
    private String country;

    @Column(name = "centroid_lat", nullable = false)
    private Double centroidLat;

    @Column(name = "centroid_lon", nullable = false)
    private Double centroidLon;

    @Column(name = "area_sq_m")
    private Double areaSqM;

    @Column(name = "elevation_m")
    private Double elevationM;

    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    public MicroZone() {
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

    // --- Builder pattern ---

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final MicroZone zone = new MicroZone();

        public Builder h3Index(String h3Index) {
            zone.h3Index = h3Index;
            return this;
        }

        public Builder geometry(Polygon geometry) {
            zone.geometry = geometry;
            return this;
        }

        public Builder zoneName(String zoneName) {
            zone.zoneName = zoneName;
            return this;
        }

        public Builder city(String city) {
            zone.city = city;
            return this;
        }

        public Builder country(String country) {
            zone.country = country;
            return this;
        }

        public Builder centroidLat(Double centroidLat) {
            zone.centroidLat = centroidLat;
            return this;
        }

        public Builder centroidLon(Double centroidLon) {
            zone.centroidLon = centroidLon;
            return this;
        }

        public Builder areaSqM(Double areaSqM) {
            zone.areaSqM = areaSqM;
            return this;
        }

        public Builder elevationM(Double elevationM) {
            zone.elevationM = elevationM;
            return this;
        }

        public MicroZone build() {
            return zone;
        }
    }

    // --- Getters and setters ---

    public UUID getId() {
        return id;
    }

    public String getH3Index() {
        return h3Index;
    }

    public Polygon getGeometry() {
        return geometry;
    }

    public String getZoneName() {
        return zoneName;
    }

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }

    public Double getCentroidLat() {
        return centroidLat;
    }

    public Double getCentroidLon() {
        return centroidLon;
    }

    public Double getAreaSqM() {
        return areaSqM;
    }

    public Double getElevationM() {
        return elevationM;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setH3Index(String h3Index) {
        this.h3Index = h3Index;
    }

    public void setGeometry(Polygon geometry) {
        this.geometry = geometry;
    }

    public void setZoneName(String zoneName) {
        this.zoneName = zoneName;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setCentroidLat(Double centroidLat) {
        this.centroidLat = centroidLat;
    }

    public void setCentroidLon(Double centroidLon) {
        this.centroidLon = centroidLon;
    }

    public void setAreaSqM(Double areaSqM) {
        this.areaSqM = areaSqM;
    }

    public void setElevationM(Double elevationM) {
        this.elevationM = elevationM;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof MicroZone microZone))
            return false;
        return Objects.equals(id, microZone.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}