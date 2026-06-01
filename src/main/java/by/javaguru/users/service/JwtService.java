package by.javaguru.users.service;

import reactor.core.publisher.Mono;

import java.util.List;

public interface JwtService {

    String generateJwt(String subject, List<String> roles);

    Mono<Boolean> validateJwt(String token);

    String extractTokenSubject(String token);

    List<String> extractRoles(String token);
}