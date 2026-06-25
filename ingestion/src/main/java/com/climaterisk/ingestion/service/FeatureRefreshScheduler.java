package com.climaterisk.ingestion.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Scheduled job to refresh features for all zones every 15 minutes.
 * <p>
 * Uses Spring's @Scheduled annotation with fixed rate.
 * In production, this should be coordinated with ShedLock to prevent
 * duplicate runs across multiple instances.
 * </p>
 */
@Component
public class FeatureRefreshScheduler {

    private final ZoneFeatureBuilder zoneFeatureBuilder;

    public FeatureRefreshScheduler(ZoneFeatureBuilder zoneFeatureBuilder) {
        this.zoneFeatureBuilder = zoneFeatureBuilder;
    }

    /**
     * Refreshes features for all zones every 15 minutes.
     * Runs at minute 0, 15, 30, 45 of each hour.
     */
    @Scheduled(fixedRate = 900000) // 15 minutes in milliseconds
    public void refreshAllZones() {
        // TODO: Implement batch refresh when MicroZoneRepository is available
        // For now, this is a placeholder that would iterate all zones
        // and call zoneFeatureBuilder.buildForZone() for each
    }
}