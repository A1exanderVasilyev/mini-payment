package dev.vasilyev.minipayment.services;

import dev.vasilyev.minipayment.api.dto.CreatePaymentRequest;
import dev.vasilyev.minipayment.api.dto.PaymentDto;
import dev.vasilyev.minipayment.domain.PaymentEntity;
import dev.vasilyev.minipayment.domain.PaymentEntityMapper;
import dev.vasilyev.minipayment.domain.PaymentStatus;
import dev.vasilyev.minipayment.repositories.PaymentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentEntityMapper mapper;

    public PaymentService(PaymentRepository paymentRepository, PaymentEntityMapper mapper) {
        this.paymentRepository = paymentRepository;
        this.mapper = mapper;
    }

    @Transactional
    public PaymentDto createPayment(CreatePaymentRequest request) {
        PaymentEntity paymentEntity = PaymentEntity.builder()
                .userId(request.userId())
                .amount(request.amount())
                .status(PaymentStatus.NEW)
                .build();

        PaymentEntity savedPayment = paymentRepository.save(paymentEntity);
        return mapper.convertEntityToDto(savedPayment);
    }
}
