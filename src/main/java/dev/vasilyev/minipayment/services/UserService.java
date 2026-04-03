package dev.vasilyev.minipayment.services;

import dev.vasilyev.minipayment.api.dto.CreateUserRequest;
import dev.vasilyev.minipayment.api.dto.UserDto;
import dev.vasilyev.minipayment.domain.PaymentEntityMapper;
import dev.vasilyev.minipayment.domain.UserEntity;
import dev.vasilyev.minipayment.repositories.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PaymentEntityMapper mapper;

    public UserService(UserRepository userRepository, PaymentEntityMapper mapper) {
        this.userRepository = userRepository;
        this.mapper = mapper;
    }

    @Transactional
    public UserDto createUser(CreateUserRequest request) {
        if (userRepository.existsByEmailIgnoreCase(request.email())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already exists");
        }
        UserEntity userEntity = new UserEntity(request.email());
        UserEntity savedUser = userRepository.save(userEntity);

        return convertToDto(savedUser);
    }


    private UserDto convertToDto(UserEntity userEntity) {
        return new UserDto(userEntity.getId(),
                userEntity.getEmail(),
                mapper.convertPaymentsToDtoList(userEntity.getPayments()));
    }

}
