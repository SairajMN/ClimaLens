package com.climaterisk.api.controller;

import com.climaterisk.common.entity.MicroZone;
import com.climaterisk.common.repository.MicroZoneRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for zone operations.
 * <p>
 * Provides CRUD endpoints for micro-zones and spatial queries.
 * </p>
 */
@RestController
@RequestMapping("/api/v1/zones")
public class ZoneController {

    private final MicroZoneRepository microZoneRepository;

    public ZoneController(MicroZoneRepository microZoneRepository) {
        this.microZoneRepository = microZoneRepository;
    }

    /**
     * Get all zones.
     */
    @GetMapping
    public ResponseEntity<List<MicroZone>> getAllZones() {
        List<MicroZone> zones = microZoneRepository.findAll();
        return ResponseEntity.ok(zones);
    }

    /**
     * Get zone by H3 index.
     */
    @GetMapping("/{h3Index}")
    public ResponseEntity<MicroZone> getZone(@PathVariable String h3Index) {
        return microZoneRepository.findByH3Index(h3Index)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Get zones by city.
     */
    @GetMapping("/city/{city}")
    public ResponseEntity<List<MicroZone>> getZonesByCity(@PathVariable String city) {
        List<MicroZone> zones = microZoneRepository.findByCity(city);
        return ResponseEntity.ok(zones);
    }

    /**
     * Create a new zone.
     */
    @PostMapping
    public ResponseEntity<MicroZone> createZone(@RequestBody MicroZone zone) {
        MicroZone saved = microZoneRepository.save(zone);
        return ResponseEntity.ok(saved);
    }

    /**
     * Delete a zone.
     */
    @DeleteMapping("/{h3Index}")
    public ResponseEntity<Void> deleteZone(@PathVariable String h3Index) {
        MicroZone zone = microZoneRepository.findByH3Index(h3Index).orElse(null);
        if (zone != null) {
            microZoneRepository.delete(zone);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}