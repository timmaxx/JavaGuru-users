package by.javaguru.users.service;

import by.javaguru.users.data.UserRepository;
import by.javaguru.users.service.dto.AlbumDto;
import by.javaguru.users.service.dto.CreateUserDto;
import by.javaguru.users.service.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatusCode;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;
import reactor.core.scheduler.Schedulers;

import java.util.ArrayList;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final Sinks.Many<UserDto> usersSink;
    private final WebClient webClient;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public Mono<UserDto> createUser(Mono<CreateUserDto> createUserDtoMono) {
        return createUserDtoMono
                .map(userMapper::createUserDtoToUserEntity)
                .publishOn(Schedulers.boundedElastic())
                .doOnNext(e -> e.setPassword(passwordEncoder.encode(e.getPassword())))
                .flatMap(userRepository::save)
                .map(userMapper::userEntityToUserDto)
                .doOnSuccess(usersSink::tryEmitNext);
    }


    @Override
    public Mono<UserDto> getUserById(UUID id, Boolean isAlbum, String jwt) {
        return userRepository.findById(id)
                .mapNotNull(userMapper::userEntityToUserDto)
                .flatMap(userDto -> {
                    if (Boolean.TRUE.equals(isAlbum)) {
                        return includeAlbums(userDto, jwt);
                    }
                    return Mono.just(userDto);
                });
    }

    private Mono<UserDto> includeAlbums(UserDto userDto, String jwt) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .port(8084)
                        .path("/albums")
                        .queryParam("userId", userDto.getId())
                        .build())
                .header("Authorization", jwt)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        response -> Mono.error(new RuntimeException("Albums not found for user")))
                .onStatus(HttpStatusCode::is5xxServerError,
                        response -> Mono.error(new RuntimeException("Server error while fetching albums")))
                .bodyToFlux(AlbumDto.class)
                .collectList()
                .map(albums -> {
                    userDto.setAlbums(albums);
                    return userDto;
                })
                .onErrorResume(e -> {
                    logger.error("Error fetching albums: ", e);
                    return Mono.just(userDto);
                });
    }

    @Override
    public Flux<UserDto> findAll(int page, int limit) {
        Pageable pageable = PageRequest.of(page, limit);

        return userRepository.findAllBy(pageable)
                .map(userMapper::userEntityToUserDto);
    }

    @Override
    public Flux<UserDto> streamUser() {
        return usersSink.asFlux()
                .publish()
                .autoConnect(1);
    }


    @Override
    public Mono<UserDetails> findByUsername(String username) {
        return userRepository.findByEmail(username)
                .map(userEntity -> User
                        .withUsername(userEntity.getEmail())
                        .password(userEntity.getPassword())
                        .authorities(new ArrayList<>())
                        .build());
    }
}


