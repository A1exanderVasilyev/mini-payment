package dev.auditservice.dto;

import java.util.List;

public record PaymentAuditHistoryDto(
        List<PaymentAuditDto> events
) {
}
