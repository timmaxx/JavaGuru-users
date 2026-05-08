package by.javaguru.users.service;

import by.javaguru.users.data.UserRepository;
import by.javaguru.users.service.dto.CreateUserDto;
import by.javaguru.users.service.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

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
                .mapNotNull(userMapper::userEntityToUserDto);
    }

}
