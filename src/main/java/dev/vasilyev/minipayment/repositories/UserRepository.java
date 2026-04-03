package dev.vasilyev.minipayment.repositories;

import dev.vasilyev.minipayment.domain.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity,Long> {
    boolean existsByEmailIgnoreCase(String email);
}
