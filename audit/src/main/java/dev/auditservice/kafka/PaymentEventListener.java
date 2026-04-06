package dev.auditservice.kafka;

import dev.auditservice.services.AuditService;
import dev.vasilyev.minipayment.domain.PaymentEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class PaymentEventListener {
    private static final Logger log =  LoggerFactory.getLogger(PaymentEventListener.class);
    private final AuditService auditService;

    public PaymentEventListener(AuditService auditService) {
        this.auditService = auditService;
    }

    @KafkaListener(
            topics = "payments.events",
            groupId = "audit-service"
    )
    public void onEvent(PaymentEvent event) {
        log.info("Received event: paymentId={}, eventType={}",
                event.paymentId(), event.type());
        auditService.processEvent(event);
    }
}
