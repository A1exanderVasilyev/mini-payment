-- liquibase formatted sql

-- changeset al_vasilyev:5
CREATE TABLE IF NOT EXISTS payment_audit_log
(
    id          BIGSERIAL PRIMARY KEY,
    event_id    UUID        NOT NULL,
    event_type  VARCHAR(64) NOT NULL,
    payment_id  BIGINT      NOT NULL,
    user_id     BIGINT,
    amount      DECIMAL(19, 2),
    status      VARCHAR(32),
    occurred_at TIMESTAMP   NOT NULL,
    created_at  TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP
);
