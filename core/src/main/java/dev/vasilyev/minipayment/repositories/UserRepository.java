package dev.vasilyev.minipayment.repositories;

import dev.vasilyev.minipayment.domain.UserEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<UserEntity,Long> {
    @EntityGraph("User.withPayments")
    List<UserEntity> findAll();
    boolean existsByEmailIgnoreCase(String email);
}
