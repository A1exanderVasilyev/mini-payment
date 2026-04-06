package dev.vasilyev.minipayment.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record PaymentEvent(
        UUID eventId,
        PaymentEventType type,
        Long userId,
        Long paymentId,
        LocalDateTime occurredAt,
        BigDecimal amount,
        String status
) {
}
