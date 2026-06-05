package by.javaguru.users.controller;

import by.javaguru.users.service.UserService;
import by.javaguru.users.service.dto.CreateUserDto;
import by.javaguru.users.service.dto.UserDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.time.Duration;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @PostMapping
    public Mono<ResponseEntity<UserDto>> createUser(@RequestBody @Valid Mono<CreateUserDto> createUserDto) {

        return userService.createUser(createUserDto)
                .map(userDto -> ResponseEntity
                        .status(HttpStatus.CREATED)
                        .location(URI.create("/users/" + userDto.getId()))
                        .body(userDto)
                );

    }

    @GetMapping("/{userId}")
    @PreAuthorize("authentication.principal.equals(#userId.toString()) or hasRole('ROLE_ADMIN')")
//    @PostAuthorize("returnObject.body != null and (returnObject.body.id.toString.equals(authentication.principal))")
    public Mono<ResponseEntity<UserDto>> getUser(@PathVariable UUID userId,
                                                 @RequestParam(name = "isAlbum", required = false) Boolean isAlbum,
                                                 @RequestHeader(name = "Authorization") String jwt) {

        return userService.getUserById(userId, isAlbum, jwt)
                .map(userDto -> ResponseEntity.status(HttpStatus.OK).body(userDto))
                .switchIfEmpty(Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).build()));
    }

    @GetMapping
    public Flux<UserDto> getUsers(@RequestParam(value = "page", defaultValue = "0") int page,
                                  @RequestParam(value = "limit", defaultValue = "50") int limit) {

        return userService.findAll(page, limit);
    }

    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<UserDto> streamUsers() {
        return userService.streamUser();
    }

}
