-- ===================================================================
-- ClimaLens :: V7__create_alert_log.sql
-- ===================================================================
-- Tracks all alerts dispatched by the alert engine. Each alert
-- references a micro-zone and the risk score that triggered it.
-- ===================================================================

CREATE TABLE alert_log (
    id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    zone_id             UUID NOT NULL REFERENCES micro_zones(id) ON DELETE CASCADE,
    risk_score_id       UUID REFERENCES risk_scores(id) ON DELETE SET NULL,
    alert_type          VARCHAR(20) NOT NULL CHECK (alert_type IN ('FLOOD', 'HEAT', 'STORM', 'COMPOSITE')),
    severity            VARCHAR(10) NOT NULL CHECK (severity IN ('INFO', 'WARNING', 'CRITICAL')),
    message             TEXT NOT NULL,
    channel             VARCHAR(20) NOT NULL DEFAULT 'EMAIL' CHECK (channel IN ('EMAIL', 'SMS', 'PUSH', 'WEBHOOK')),
    recipient           VARCHAR(200),
    dispatched_at       TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    acknowledged        BOOLEAN NOT NULL DEFAULT FALSE,
    acknowledged_at     TIMESTAMP WITH TIME ZONE,
    created_at          TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW()
);

-- Index for zone-based alert history
CREATE INDEX idx_alert_log_zone_id
    ON alert_log
    USING BTREE (zone_id);

-- Index for dispatch-time queries
CREATE INDEX idx_alert_log_dispatched
    ON alert_log
    USING BTREE (dispatched_at DESC);