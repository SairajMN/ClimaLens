-- ===================================================================
-- ClimaLens :: V4__create_risk_scores.sql
-- ===================================================================
-- Stores computed flood and heat risk scores for each micro-zone.
-- Supports current (latest), forecast (future horizon), and historical
-- score lookups.
-- ===================================================================

CREATE TABLE risk_scores (
    id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    zone_id             UUID NOT NULL REFERENCES micro_zones(id) ON DELETE CASCADE,
    score_type          VARCHAR(20) NOT NULL CHECK (score_type IN ('CURRENT', 'FORECAST', 'HISTORICAL')),
    flood_risk_score    SMALLINT NOT NULL CHECK (flood_risk_score BETWEEN 0 AND 100),
    heat_risk_score     SMALLINT NOT NULL CHECK (heat_risk_score BETWEEN 0 AND 100),
    flood_risk_class    VARCHAR(10) NOT NULL CHECK (flood_risk_class IN ('LOW', 'MODERATE', 'HIGH', 'EXTREME')),
    heat_risk_class     VARCHAR(10) NOT NULL CHECK (heat_risk_class IN ('LOW', 'MODERATE', 'HIGH', 'EXTREME')),
    forecast_horizon    TIMESTAMP WITH TIME ZONE,  -- NULL for CURRENT/HISTORICAL
    score_timestamp     TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    model_version       VARCHAR(20) NOT NULL DEFAULT '1.0.0',
    created_at          TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW()
);

-- Index for current-score lookups per zone
CREATE INDEX idx_risk_scores_zone_id
    ON risk_scores
    USING BTREE (zone_id);

-- Composite index for time-series queries
CREATE INDEX idx_risk_scores_zone_ts
    ON risk_scores
    USING BTREE (zone_id, score_timestamp DESC);

-- Index for hotspot queries (high-risk zones)
CREATE INDEX idx_risk_scores_flood_high
    ON risk_scores (flood_risk_score DESC)
    WHERE flood_risk_class IN ('HIGH', 'EXTREME');

CREATE INDEX idx_risk_scores_heat_high
    ON risk_scores (heat_risk_score DESC)
    WHERE heat_risk_class IN ('HIGH', 'EXTREME');