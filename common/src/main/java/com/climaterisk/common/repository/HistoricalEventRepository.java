package com.climaterisk.common.repository;

import com.climaterisk.common.entity.HistoricalEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface HistoricalEventRepository extends JpaRepository<HistoricalEvent, UUID> {
    List<HistoricalEvent> findByZoneIdOrderByEventStartDesc(UUID zoneId);

    List<HistoricalEvent> findByEventStartAfterAndEventType(OffsetDateTime after, String eventType);
}