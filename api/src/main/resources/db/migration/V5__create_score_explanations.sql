-- ===================================================================
-- ClimaLens :: V5__create_score_explanations.sql
-- ===================================================================
-- Stores explainability data for each risk score: top contributing
-- factors, their importance weights, and a plain-language summary.
-- ===================================================================

CREATE TABLE score_explanations (
    id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    risk_score_id       UUID NOT NULL REFERENCES risk_scores(id) ON DELETE CASCADE,
    risk_type           VARCHAR(10) NOT NULL CHECK (risk_type IN ('FLOOD', 'HEAT')),
    top_factors         JSONB NOT NULL DEFAULT '[]'::JSONB,
    plain_summary       TEXT NOT NULL,
    created_at          TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW()
);

-- Index for fast lookups by risk score
CREATE INDEX idx_score_explanations_risk_score_id
    ON score_explanations
    USING BTREE (risk_score_id);