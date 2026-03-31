package dev.vasilyev.minipayment.domain;

import dev.vasilyev.minipayment.api.dto.PaymentDto;
import org.springframework.stereotype.Component;

@Component
public class PaymentEntityMapper {

    public PaymentDto convertEntityToDto(PaymentEntity paymentEntity) {
        return new PaymentDto(
                paymentEntity.getId(),
                paymentEntity.getUserId(),
                paymentEntity.getAmount(),
                paymentEntity.getStatus(),
                paymentEntity.getCreatedAt(),
                paymentEntity.getUpdatedAt()
        );
    }
}
