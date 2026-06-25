package com.climaterisk.common.repository;

import com.climaterisk.common.entity.ScoreExplanation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ScoreExplanationRepository extends JpaRepository<ScoreExplanation, UUID> {
    List<ScoreExplanation> findByRiskScoreId(UUID riskScoreId);
}