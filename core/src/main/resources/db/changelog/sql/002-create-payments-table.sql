-- liquibase formatted sql

-- changeset al_vasilyev:2
CREATE TABLE IF NOT EXISTS payments
(
    id         BIGSERIAL PRIMARY KEY,
    user_id    BIGINT         NOT NULL,
    amount     NUMERIC(19, 2) NOT NULL,
    status     VARCHAR(32)    NOT NULL,
    created_at TIMESTAMP      NOT NULL,
    updated_at TIMESTAMP      NOT NULL
);

-- changeset al_vasilyev:3
CREATE INDEX idx_payments_user_id ON payments (user_id);