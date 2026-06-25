package com.climaterisk.common.repository;

import com.climaterisk.common.entity.MicroZone;
import org.locationtech.jts.geom.Envelope;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MicroZoneRepository extends JpaRepository<MicroZone, UUID> {

    Optional<MicroZone> findByH3Index(String h3Index);

    @Query(value = "SELECT * FROM micro_zones WHERE ST_Intersects(geometry, ST_MakeEnvelope(:minLon, :minLat, :maxLon, :maxLat, 4326))", nativeQuery = true)
    List<MicroZone> findByBoundingBox(@Param("minLon") double minLon, @Param("minLat") double minLat,
            @Param("maxLon") double maxLon, @Param("maxLat") double maxLat);

    @Query(value = "SELECT * FROM micro_zones ORDER BY geometry <-> ST_SetSRID(ST_MakePoint(:lon, :lat), 4326) LIMIT :limit", nativeQuery = true)
    List<MicroZone> findNearest(@Param("lat") double lat, @Param("lon") double lon, @Param("limit") int limit);

    List<MicroZone> findByCity(String city);
}