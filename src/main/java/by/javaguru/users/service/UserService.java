package by.javaguru.users.service;

import by.javaguru.users.service.dto.CreateUserDto;
import by.javaguru.users.service.dto.UserDto;
import reactor.core.publisher.Mono;

public interface UserService {

    Mono<UserDto> createUser(Mono<CreateUserDto> createUserDtoMono);
}
