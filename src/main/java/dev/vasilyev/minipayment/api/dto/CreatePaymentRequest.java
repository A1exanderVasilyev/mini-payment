package dev.vasilyev.minipayment.api.dto;

import java.math.BigDecimal;

public record CreatePaymentRequest(
        Long userId,
        BigDecimal amount
) {
}
