-- ===================================================================
-- ClimaLens :: V1__baseline.sql — Initial Schema Baseline
-- ===================================================================
-- This migration establishes the PostGIS extension and creates the
-- foundational schema_version tracking for Flyway.
-- All future schema changes MUST be added as new versioned migrations.
-- ===================================================================

-- Enable PostGIS (required for all spatial operations)
CREATE EXTENSION IF NOT EXISTS postgis;
CREATE EXTENSION IF NOT EXISTS postgis_topology;

-- Enable UUID generation
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Create a function to update updated_at timestamps automatically
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = NOW();
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;