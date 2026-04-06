package dev.vasilyev.minipayment.api.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record CreatePaymentRequest(
        @NotNull @Positive Long userId,
        @NotNull @Positive BigDecimal amount
) {
}
