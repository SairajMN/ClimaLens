-- ===================================================================
-- ClimaLens :: V2__create_micro_zones.sql
-- ===================================================================
-- Street-level hexagon zones at H3 resolution 9 (~150m).
-- This is the core spatial entity; all risk scores and features
-- reference a micro_zone.
-- ===================================================================

CREATE TABLE micro_zones (
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    h3_index        VARCHAR(15) NOT NULL UNIQUE,
    geometry        GEOMETRY(POLYGON, 4326) NOT NULL,
    zone_name       VARCHAR(100),
    city            VARCHAR(100) NOT NULL DEFAULT 'Unknown',
    country         VARCHAR(100) NOT NULL DEFAULT 'Unknown',
    centroid_lat    DOUBLE PRECISION NOT NULL,
    centroid_lon    DOUBLE PRECISION NOT NULL,
    area_sq_m       DOUBLE PRECISION,
    elevation_m     DOUBLE PRECISION,
    created_at      TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at      TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW()
);

-- Spatial index for bounding-box and nearest-neighbour queries
CREATE INDEX idx_micro_zones_geometry
    ON micro_zones
    USING GIST (geometry);

-- Index for fast H3 lookups
CREATE INDEX idx_micro_zones_h3_index
    ON micro_zones
    USING BTREE (h3_index);

-- Trigger to auto-update updated_at
CREATE TRIGGER trg_micro_zones_updated_at
    BEFORE UPDATE ON micro_zones
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_at_column();