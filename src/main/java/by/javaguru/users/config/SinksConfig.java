package by.javaguru.users.config;

import by.javaguru.users.service.dto.UserDto;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Sinks;

@Configuration
public class SinksConfig {

    @Bean
    public Sinks.Many<UserDto> userSink() {
        return Sinks.many().multicast().onBackpressureBuffer();
    }
}
