package by.javaguru.users.controller;

import by.javaguru.users.service.AuthentificationService;
import by.javaguru.users.service.dto.AuthenticationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthentificationService authentificationService;

    @PostMapping("/login")
    public Mono<ResponseEntity<Object>> login(@RequestBody Mono<AuthenticationRequest> authenticationRequestMono) {
        return authenticationRequestMono
                .flatMap(authenticationRequest ->
                        authentificationService.authenticate(authenticationRequest.getEmail(),
                                authenticationRequest.getPassword()))
                .map(map -> ResponseEntity.ok()
                        .header(HttpHeaders.AUTHORIZATION, "Bearer" + map.get("token"))
                        .header("UserId", map.get("userId"))
                        .build())
                .onErrorReturn(BadCredentialsException.class, ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Invalid credentials"))
                .onErrorReturn(Exception.class, ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
    }

}
