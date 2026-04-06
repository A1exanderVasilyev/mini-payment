package dev.auditservice.services;

import dev.auditservice.domain.PaymentAuditLogEntity;
import dev.auditservice.dto.PaymentAuditDto;
import dev.auditservice.repositories.PaymentAuditLogRepository;
import dev.auditservice.repositories.ProcessedEventRepository;
import dev.vasilyev.minipayment.domain.PaymentEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AuditService {
    private static final Logger log = LoggerFactory.getLogger(AuditService.class);
    private final PaymentAuditLogRepository auditRepository;
    private final ProcessedEventRepository processedEventRepository;

    public AuditService(PaymentAuditLogRepository auditRepository, ProcessedEventRepository processedEventRepository) {
        this.auditRepository = auditRepository;
        this.processedEventRepository = processedEventRepository;
    }

    @Transactional
    public void processEvent(PaymentEvent paymentEvent) {
        boolean isEventAlreadyProcessed = checkIsEventProcessed(paymentEvent);

        if (isEventAlreadyProcessed) {
            log.info("Event already processed, skipping");
            return;
        }

        PaymentAuditLogEntity logEntity = convertToLogEntity(paymentEvent);
        auditRepository.save(logEntity);
        log.info("Audit log saved for payment: {}", paymentEvent.eventId());
    }

    @Transactional(readOnly = true)
    public List<PaymentAuditDto> getByPaymentId(Long paymentId) {
        return auditRepository.findByPaymentId(paymentId).stream()
                .map(this::convertToPaymentAuditDto)
                .toList();
    }

    private PaymentAuditDto convertToPaymentAuditDto(PaymentAuditLogEntity entity) {
        return new PaymentAuditDto(
                entity.getEventId(),
                entity.getEventType(),
                entity.getPaymentId(),
                entity.getUserId(),
                entity.getAmount(),
                entity.getStatus(),
                entity.getOccurredAt(),
                entity.getCreatedAt()
        );
    }

    private PaymentAuditLogEntity convertToLogEntity(PaymentEvent paymentEvent) {
        return new PaymentAuditLogEntity(
                paymentEvent.eventId(),
                paymentEvent.type().name(),
                paymentEvent.paymentId(),
                paymentEvent.userId(),
                paymentEvent.amount(),
                paymentEvent.status(),
                paymentEvent.occurredAt()
        );
    }

    private boolean checkIsEventProcessed(PaymentEvent paymentEvent) {
        int countInserted = processedEventRepository
                .insertIfNotExists(paymentEvent.eventId());
        return countInserted == 0;
    }
}
