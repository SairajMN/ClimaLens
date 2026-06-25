package com.climaterisk.common.repository;

import com.climaterisk.common.entity.RiskScore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RiskScoreRepository extends JpaRepository<RiskScore, UUID> {

    Optional<RiskScore> findTopByZoneIdAndScoreTypeOrderByScoreTimestampDesc(UUID zoneId, String scoreType);

    @Query(value = "SELECT * FROM risk_scores WHERE score_type = 'CURRENT' AND flood_risk_class IN ('HIGH', 'EXTREME') ORDER BY flood_risk_score DESC LIMIT :limit", nativeQuery = true)
    List<RiskScore> findFloodHotspots(@Param("limit") int limit);

    @Query(value = "SELECT * FROM risk_scores WHERE score_type = 'CURRENT' AND heat_risk_class IN ('HIGH', 'EXTREME') ORDER BY heat_risk_score DESC LIMIT :limit", nativeQuery = true)
    List<RiskScore> findHeatHotspots(@Param("limit") int limit);

    List<RiskScore> findByZoneIdAndScoreTypeOrderByScoreTimestampDesc(UUID zoneId, String scoreType);
}
