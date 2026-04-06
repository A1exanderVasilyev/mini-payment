package dev.vasilyev.minipayment.repositories;

import dev.vasilyev.minipayment.domain.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<PaymentEntity, Long> {
}
