package com.climaterisk.common.repository;

import com.climaterisk.common.entity.AlertLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AlertLogRepository extends JpaRepository<AlertLog, UUID> {
    List<AlertLog> findByZoneIdOrderByDispatchedAtDesc(UUID zoneId);

    List<AlertLog> findBySeverityAndAcknowledgedFalse(String severity);
}