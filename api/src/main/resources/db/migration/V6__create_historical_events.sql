-- ===================================================================
-- ClimaLens :: V6__create_historical_events.sql
-- ===================================================================
-- Records historical flood and heat events used for model training
-- and validation. Sources include NASA EONET, local records, and
-- manual annotations.
-- ===================================================================

CREATE TABLE historical_events (
    id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    zone_id             UUID NOT NULL REFERENCES micro_zones(id) ON DELETE CASCADE,
    event_type          VARCHAR(20) NOT NULL CHECK (event_type IN ('FLOOD', 'HEAT_WAVE', 'STORM', 'DROUGHT', 'WILDFIRE')),
    event_severity      VARCHAR(10) NOT NULL CHECK (event_severity IN ('LOW', 'MODERATE', 'SEVERE', 'EXTREME')),
    event_start         TIMESTAMP WITH TIME ZONE NOT NULL,
    event_end           TIMESTAMP WITH TIME ZONE,
    observed_flood_risk SMALLINT CHECK (observed_flood_risk BETWEEN 0 AND 100),
    observed_heat_risk  SMALLINT CHECK (observed_heat_risk BETWEEN 0 AND 100),
    source              VARCHAR(50) NOT NULL DEFAULT 'EONET',
    source_id           VARCHAR(100),
    notes               TEXT,
    created_at          TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW()
);

-- Index for zone-based event lookups
CREATE INDEX idx_historical_events_zone_id
    ON historical_events
    USING BTREE (zone_id);

-- Index for time-range queries
CREATE INDEX idx_historical_events_start
    ON historical_events
    USING BTREE (event_start DESC);