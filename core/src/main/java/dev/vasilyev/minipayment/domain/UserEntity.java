package dev.vasilyev.minipayment.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@Table(name = "users")
@Entity
@NamedEntityGraph(
        name = "User.withPayments",
        attributeNodes = {
                @NamedAttributeNode(value = "payments")
        }
)
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<PaymentEntity> payments = new ArrayList<>();

    public UserEntity(String email) {
        this.email = email;
    }
}
