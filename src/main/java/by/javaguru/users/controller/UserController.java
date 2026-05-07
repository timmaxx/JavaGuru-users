package by.javaguru.users.controller;

import by.javaguru.users.service.dto.CreateUserDto;
import by.javaguru.users.service.dto.UserDto;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserController {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<ResponseEntity<UserDto>> createUser(@RequestBody @Valid Mono<CreateUserDto> createUserDto) {

        return createUserDto.map(request -> new UserDto(UUID.randomUUID(),
                        request.getFirstName(),
                        request.getLastName(),
                        request.getEmail()))
                .map(userDto -> ResponseEntity
                        .status(HttpStatus.CREATED)
                        .location(URI.create("/users/" + userDto.getId()))
                        .body(userDto)
                );
    }

    @GetMapping("/{userId}")
    public Mono<UserDto> getUser(@PathVariable UUID userId) {
        return Mono.just(new UserDto(
                userId,
                "Андрей",
                "Борисов",
                "javaguru.by@gmail.com"
        ));
    }

    @GetMapping
    public Flux<UserDto> getUsers(@RequestParam(value = "offset", defaultValue = "0") int offset,
                                  @RequestParam(value = "limit", defaultValue = "50") int limit) {
        return Flux.just(
                new UserDto(UUID.randomUUID(), "Андрей", "Борисов", "javaguru.by@gmail.com"),
                new UserDto(UUID.randomUUID(), "Алексей", "Борисов", "javaguru.by@gmail.com"),
                new UserDto(UUID.randomUUID(), "Сергей", "Борисов", "javaguru.by@gmail.com")
        );
    }
}
