-- ===================================================================
-- ClimaLens :: V8__create_users.sql
-- ===================================================================
-- Application users with role-based access control.
-- Roles: PUBLIC (read-only), OFFICIAL (read + alerts), ADMIN (full)
-- ===================================================================

CREATE TABLE users (
    id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    email               VARCHAR(255) NOT NULL UNIQUE,
    password_hash       VARCHAR(255) NOT NULL,
    display_name        VARCHAR(100) NOT NULL,
    role                VARCHAR(20) NOT NULL DEFAULT 'PUBLIC' CHECK (role IN ('PUBLIC', 'OFFICIAL', 'ADMIN')),
    enabled             BOOLEAN NOT NULL DEFAULT TRUE,
    email_verified      BOOLEAN NOT NULL DEFAULT FALSE,
    created_at          TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at          TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW()
);

-- Index for login lookups
CREATE INDEX idx_users_email
    ON users
    USING BTREE (email);

-- Trigger to auto-update updated_at
CREATE TRIGGER trg_users_updated_at
    BEFORE UPDATE ON users
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_at_column();