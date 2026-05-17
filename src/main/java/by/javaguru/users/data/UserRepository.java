package by.javaguru.users.data;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

import java.util.UUID;

public interface UserRepository extends ReactiveCrudRepository<UserEntity, UUID> {

    Flux<UserEntity> findAllBy(Pageable pageable);

    Flux<UserEntity> findAllByEmailContainingIgnoreCase(String email, Pageable pageable);
}
