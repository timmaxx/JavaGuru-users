package by.javaguru.users.service;

import by.javaguru.users.data.UserRepository;
import by.javaguru.users.service.dto.CreateUserDto;
import by.javaguru.users.service.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public Mono<UserDto> createUser(Mono<CreateUserDto> createUserDtoMono) {

        return createUserDtoMono
                .mapNotNull(userMapper::createUserDtoToUserEntity)
                .flatMap(userRepository::save)
                .mapNotNull(userMapper::userEntityToUserDto)
                .onErrorMap(throwable -> {
                    if (throwable instanceof DuplicateKeyException)
                        return new ResponseStatusException(HttpStatus.CONFLICT, throwable.getMessage());
                    else if (throwable instanceof DataIntegrityViolationException)
                        return new ResponseStatusException(HttpStatus.BAD_REQUEST, throwable.getMessage());
                    else
                        return new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, throwable.getMessage());
                });
    }

    @Override
    public Mono<UserDto> getUserById(UUID id) {
        return userRepository.findById(id)
                .mapNotNull(userMapper::userEntityToUserDto);
    }

    @Override
    public Flux<UserDto> findAll(int page, int limit) {
        Pageable pageable = PageRequest.of(page, limit);

        return userRepository.findAllBy(pageable)
                .map(userMapper::userEntityToUserDto);
    }


}
