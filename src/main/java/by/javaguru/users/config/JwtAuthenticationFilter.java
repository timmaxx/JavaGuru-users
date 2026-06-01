package by.javaguru.users.config;

import by.javaguru.users.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.Collections;

@RequiredArgsConstructor
public class JwtAuthenticationFilter implements WebFilter {

    private final JwtService jwtService;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String token = extractToken(exchange);

        if (token == null) return chain.filter(exchange);

        return validateToken(token)
                .flatMap(isValid -> isValid ? authenticateAndContinue(token, exchange, chain)
                        : handleInvalidToken(exchange));
    }

    private Mono<? extends Void> authenticateAndContinue(String token, ServerWebExchange exchange, WebFilterChain chain) {
        return Mono.just(jwtService.extractTokenSubject(token))
                .flatMap(subject -> {
                    Authentication auth = new UsernamePasswordAuthenticationToken(subject, null,
                            Collections.emptyList());

                    return chain.filter(exchange)
                            .contextWrite(ReactiveSecurityContextHolder.withAuthentication(auth));
                });
    }


    private Mono<? extends Void> handleInvalidToken(ServerWebExchange exchange) {
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        return exchange.getResponse().setComplete();
    }

    private String extractToken(ServerWebExchange exchange) {
        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

        if (StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7).trim();
        }

        return null;
    }

    private Mono<Boolean> validateToken(String token) {
        return jwtService.validateJwt(token);
    }
}
