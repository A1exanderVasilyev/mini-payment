-- liquibase formatted sql

-- changeset al_vasilyev:4
ALTER TABLE payments
    ADD CONSTRAINT fk_payments_on_user_id
    FOREIGN KEY (user_id) REFERENCES users (id)