package com.climaterisk.common.repository;

import com.climaterisk.common.entity.ZoneFeature;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ZoneFeatureRepository extends JpaRepository<ZoneFeature, UUID> {
    Optional<ZoneFeature> findTopByZoneIdOrderByFeatureTimestampDesc(UUID zoneId);
}