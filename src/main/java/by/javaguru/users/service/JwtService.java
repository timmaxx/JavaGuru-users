package by.javaguru.users.service;

import reactor.core.publisher.Mono;

import java.util.List;

public interface JwtService {

    String generateJwt(String subject);

    Mono<Boolean> validateJwt(String token);

    Object extractTokenSubject(String token);

    List<String> extractRoles(String token);
}
