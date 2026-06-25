package com.climaterisk.common.util;

import com.climaterisk.common.entity.MicroZone;
import com.climaterisk.common.repository.MicroZoneRepository;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Polygon;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Generates H3 resolution-9 hexagon zones (~150m) from a city bounding box
 * and persists them to the database.
 * <p>
 * Uses the Uber H3 library for hex grid indexing and JTS for polygon
 * geometry construction. Each zone is stored as a PostGIS POLYGON with
 * SRID 4326 (WGS84).
 * </p>
 */
@Component
public class H3ZoneGenerator {

    private static final int H3_RESOLUTION = 9;

    private final MicroZoneRepository microZoneRepository;
    private final GeometryFactory geometryFactory;

    public H3ZoneGenerator(MicroZoneRepository microZoneRepository) {
        this.microZoneRepository = microZoneRepository;
        this.geometryFactory = new GeometryFactory();
    }

    /**
     * Generates H3 zones within the given bounding box and persists them.
     *
     * @param minLat  southern boundary (WGS84)
     * @param minLon  western boundary (WGS84)
     * @param maxLat  northern boundary (WGS84)
     * @param maxLon  eastern boundary (WGS84)
     * @param city    city name for metadata
     * @param country country name for metadata
     * @return list of persisted MicroZone entities
     */
    public List<MicroZone> generateAndSave(double minLat, double minLon, double maxLat, double maxLon, String city,
            String country) {
        // TODO: Implement H3 polyfill when h3-java library is properly configured
        // For now, return empty list as placeholder
        return new ArrayList<>();
    }

    /**
     * Calculates the approximate area of an H3 res-9 hexagon in square meters.
     */
    private double calculateHexAreaM2() {
        // Approximate area for H3 res-9: ~0.105 km²
        return 105_000.0;
    }
}