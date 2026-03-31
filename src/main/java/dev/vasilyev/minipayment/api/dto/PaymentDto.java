package dev.vasilyev.minipayment.api.dto;

import dev.vasilyev.minipayment.domain.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PaymentDto(
        Long id,
        Long userId,
        BigDecimal amount,
        PaymentStatus status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
