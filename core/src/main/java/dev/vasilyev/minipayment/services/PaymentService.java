package dev.vasilyev.minipayment.services;

import dev.vasilyev.minipayment.api.dto.CreatePaymentRequest;
import dev.vasilyev.minipayment.api.dto.PaymentDto;
import dev.vasilyev.minipayment.domain.PaymentEntity;
import dev.vasilyev.minipayment.domain.PaymentEntityMapper;
import dev.vasilyev.minipayment.domain.PaymentStatus;
import dev.vasilyev.minipayment.domain.UserEntity;
import dev.vasilyev.minipayment.kafka.PaymentEventPublisher;
import dev.vasilyev.minipayment.repositories.PaymentRepository;
import dev.vasilyev.minipayment.repositories.UserRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final UserRepository userRepository;
    private final PaymentEntityMapper mapper;
    private final PaymentEventPublisher eventPublisher;

    public PaymentService(PaymentRepository paymentRepository, UserRepository userRepository, PaymentEntityMapper mapper, PaymentEventPublisher eventPublisher) {
        this.paymentRepository = paymentRepository;
        this.userRepository = userRepository;
        this.mapper = mapper;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public PaymentDto createPayment(CreatePaymentRequest request) {
        UserEntity user = userRepository.findById(request.userId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found: " + request.userId()));
        PaymentEntity paymentEntity = PaymentEntity.builder()
                .user(user)
                .amount(request.amount())
                .status(PaymentStatus.NEW)
                .build();

        PaymentEntity savedPayment = paymentRepository.save(paymentEntity);

        eventPublisher.publishCreatedPayment(savedPayment);

        return mapper.convertEntityToDto(savedPayment);
    }

    @Cacheable(cacheNames = "payments", key = "#id")
    public PaymentDto getPayment(Long id) {
        PaymentEntity payment = findPaymentOrThrow(id);
        return mapper.convertEntityToDto(payment);
    }

    private PaymentEntity findPaymentOrThrow(Long id) {
        return paymentRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Payment not found with id " + id));
    }

    @Transactional
    @CacheEvict(cacheNames = "payments", key = "#id")
    public PaymentDto confirmPayment(Long id) {
        PaymentEntity payment = findPaymentOrThrow(id);
        if (!payment.getStatus().equals(PaymentStatus.NEW)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Wrong payment status");
        }
        payment.setStatus(PaymentStatus.SUCCEEDED);
        PaymentEntity savedPayment = paymentRepository.save(payment);
        eventPublisher.publishPaymentSuccess(savedPayment);
        return mapper.convertEntityToDto(savedPayment);
    }
}
