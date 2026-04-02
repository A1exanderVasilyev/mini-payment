package dev.vasilyev.minipayment.api.errors;

import java.time.LocalDateTime;

public record ApiError(
        LocalDateTime time,
        int status,
        String error,
        String message,
        String path
) {
}
