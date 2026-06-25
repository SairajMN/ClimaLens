package com.climaterisk.api.controller;

import com.climaterisk.common.entity.AlertLog;
import com.climaterisk.common.repository.AlertLogRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.util.List;

/**
 * REST controller for alert operations.
 * <p>
 * Provides endpoints to trigger and retrieve alert logs.
 * </p>
 */
@RestController
@RequestMapping("/api/v1/alerts")
public class AlertController {

    private final AlertLogRepository alertLogRepository;

    public AlertController(AlertLogRepository alertLogRepository) {
        this.alertLogRepository = alertLogRepository;
    }

    /**
     * Get all alert logs.
     */
    @GetMapping
    public ResponseEntity<List<AlertLog>> getAllAlerts() {
        List<AlertLog> alerts = alertLogRepository.findAll();
        return ResponseEntity.ok(alerts);
    }

    /**
     * Get recent alerts for a zone.
     */
    @GetMapping("/zone/{h3Index}")
    public ResponseEntity<List<AlertLog>> getAlertsForZone(@PathVariable String h3Index,
            @RequestParam(defaultValue = "24") int hours) {
        OffsetDateTime since = OffsetDateTime.now().minusHours(hours);
        // TODO: Implement when AlertLog has zone relationship
        return ResponseEntity.ok(List.of());
    }

    /**
     * Trigger a test alert.
     */
    @PostMapping("/test")
    public ResponseEntity<AlertLog> triggerTestAlert(@RequestParam String alertType,
            @RequestParam String message) {
        AlertLog alert = new AlertLog();
        alert.setAlertType(alertType);
        alert.setMessage(message);
        alert.setSeverity("MEDIUM");
        alert.setChannel("EMAIL");
        alert.setDispatchedAt(OffsetDateTime.now());
        alert.setAcknowledged(false);
        // TODO: Add email dispatch logic
        AlertLog saved = alertLogRepository.save(alert);
        return ResponseEntity.ok(saved);
    }
}