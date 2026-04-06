package dev.vasilyev.minipayment.kafka;

import dev.vasilyev.minipayment.domain.PaymentEntity;
import dev.vasilyev.minipayment.domain.PaymentEvent;
import dev.vasilyev.minipayment.domain.PaymentEventType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class PaymentEventPublisher {
    private static final Logger log = LoggerFactory.getLogger(PaymentEventPublisher.class);

    private final KafkaTemplate<String, PaymentEvent> kafkaTemplate;
    private final String topic;

    public PaymentEventPublisher(
            KafkaTemplate<String, PaymentEvent> kafkaTemplate,
            @Value("payments.events") String topic) {
        this.kafkaTemplate = kafkaTemplate;
        this.topic = topic;
    }

    public void publishCreatedPayment(PaymentEntity payment) {
        publish(payment, PaymentEventType.PAYMENT_CREATED);
    }

    public void publishPaymentSuccess(PaymentEntity payment) {
        publish(payment, PaymentEventType.PAYMENT_SUCCEEDED);
    }

    private void publish(PaymentEntity payment,
                         PaymentEventType paymentEventType) {
        PaymentEvent event = new PaymentEvent(
                UUID.randomUUID(),
                paymentEventType,
                payment.getUser().getId(),
                payment.getId(),
                LocalDateTime.now(),
                payment.getAmount(),
                payment.getStatus().name()
        );

        kafkaTemplate.send(topic, payment.getId().toString(), event);
        kafkaTemplate.send(topic, payment.getId().toString(), event);
        log.info("Published event {} for payment {}", paymentEventType, payment.getId());
    }
}
