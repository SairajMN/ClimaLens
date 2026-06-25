-- ===================================================================
-- ClimaLens :: V3__create_zone_features.sql
-- ===================================================================
-- Stores static (invariant) and dynamic (time-varying) feature vectors
-- per micro-zone. Static features are computed once; dynamic features
-- are refreshed by scheduled ingestion jobs.
-- ===================================================================

CREATE TABLE zone_features (
    id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    zone_id             UUID NOT NULL REFERENCES micro_zones(id) ON DELETE CASCADE,
    static_features     JSONB NOT NULL DEFAULT '{}'::JSONB,
    dynamic_features    JSONB NOT NULL DEFAULT '{}'::JSONB,
    feature_timestamp   TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    created_at          TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at          TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW()
);

-- Index for fast zone lookups
CREATE INDEX idx_zone_features_zone_id
    ON zone_features
    USING BTREE (zone_id);

-- Composite index for time-series queries per zone
CREATE INDEX idx_zone_features_zone_ts
    ON zone_features
    USING BTREE (zone_id, feature_timestamp DESC);

-- Trigger to auto-update updated_at
CREATE TRIGGER trg_zone_features_updated_at
    BEFORE UPDATE ON zone_features
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_at_column();