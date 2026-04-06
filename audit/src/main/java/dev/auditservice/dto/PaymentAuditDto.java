package dev.auditservice.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record PaymentAuditDto(
        UUID eventId,
        String eventType,
        Long paymentId,
        Long userId,
        BigDecimal amount,
        String status,
        LocalDateTime occurredAt,
        LocalDateTime createdAt
) {
}
