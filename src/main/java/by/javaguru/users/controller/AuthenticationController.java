package by.javaguru.users.controller;

import by.javaguru.users.service.AuthenticationService;
import by.javaguru.users.service.dto.AuthenticationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public Mono<ResponseEntity<Object>> login(@RequestBody Mono<AuthenticationRequest> authenticationRequestMono) {
        return authenticationRequestMono
                .flatMap(authenticationRequest ->
                        authenticationService.authenticate(authenticationRequest.getEmail(),
                                authenticationRequest.getPassword()))
                .map(map -> ResponseEntity.ok()
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + map.get("token"))
                        .header("UserId", map.get("userId"))
                        .build());

    }

}
