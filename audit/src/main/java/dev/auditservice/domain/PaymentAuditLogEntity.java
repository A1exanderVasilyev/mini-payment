package dev.auditservice.domain;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Table(name = "payment_audit_log")
@Entity
@NoArgsConstructor
public class PaymentAuditLogEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "event_id", nullable = false)
    private UUID eventId;

    @Column(name = "event_type", nullable = false, length = 64)
    private String eventType;

    @Column(name = "payment_id", nullable = false)
    private Long paymentId;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "amount", precision = 19, scale = 2)
    private BigDecimal amount;

    @Column(name = "status", length = 32)
    private String status;

    @Column(name = "occurred_at", nullable = false)
    private LocalDateTime occurredAt;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public PaymentAuditLogEntity(UUID eventId, String eventType, Long paymentId, Long userId, BigDecimal amount, String status, LocalDateTime occurredAt) {
        this.eventId = eventId;
        this.eventType = eventType;
        this.paymentId = paymentId;
        this.userId = userId;
        this.amount = amount;
        this.status = status;
        this.occurredAt = occurredAt;
    }
}
