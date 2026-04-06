-- liquibase formatted sql

-- changeset al_vasilyev:6
CREATE TABLE IF NOT EXISTS processed_events
(
    id           UUID PRIMARY KEY,
    processed_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);