package by.javaguru.users.service;

import reactor.core.publisher.Mono;

import java.util.Map;

public interface AuthentificationService {

    Mono<Map<String, String>> authenticate(String username, String password);
}
