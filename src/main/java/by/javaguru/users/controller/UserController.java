package by.javaguru.users.controller;

import by.javaguru.users.service.UserService;
import by.javaguru.users.service.dto.CreateUserDto;
import by.javaguru.users.service.dto.UserDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
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
    @PreAuthorize("authentication.principal.equals(#userId.toString()) or hasRole('ADMIN')")
    public Mono<ResponseEntity<UserDto>> getUser(@PathVariable UUID userId) {
        return userService.getUserById(userId)
                .map(userDto -> ResponseEntity.status(HttpStatus.OK).body(userDto))
                .switchIfEmpty(Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).build()));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Flux<UserDto> getUsers(@RequestParam(value = "page", defaultValue = "0") int page,
                                  @RequestParam(value = "limit", defaultValue = "50") int limit) {
        return userService.findAll(page, limit);
    }

    @DeleteMapping("/{userId}")
    @PreAuthorize("!authentication.principal.equals(#userId.toString()) and hasRole('ROLE_ADMIN')") //  ToDo 1 (TM): Почему здесь 'ROLE_'?
    public Mono<ResponseEntity<Void>> deleteUser(@PathVariable UUID userId) {

        return userService.deleteUserById(userId)   //  ToDo 2 (TM): Как при удалении существующего, так и при удалении несуществующего, ответом будет HttpStatus.OK. Правильно-ли так?
                .map(userDto -> ResponseEntity.status(HttpStatus.OK).body(userDto));
    }
}
