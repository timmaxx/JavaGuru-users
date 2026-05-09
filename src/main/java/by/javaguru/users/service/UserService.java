package by.javaguru.users.service;

import by.javaguru.users.service.dto.CreateUserDto;
import by.javaguru.users.service.dto.UserDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;


public interface UserService {

    Mono<UserDto> createUser(Mono<CreateUserDto> createUserDtoMono);

    Mono<UserDto> getUserById(UUID id);

    Flux<UserDto> findAll(int page, int limit);

}
