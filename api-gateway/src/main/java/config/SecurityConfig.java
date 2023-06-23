package config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity   // Enable Spring Security
public class SecurityConfig {


    @Bean
    public  SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity serverHttpSecurity) {
          serverHttpSecurity.csrf()
                .disable()
                .authorizeExchange(exchange -> exchange.pathMatchers("/eureka/**")
                        .permitAll()
                        .anyExchange()
                        .authenticated())
                .oauth2ResourceServer(ServerHttpSecurity.OAuth2ResourceServerSpec::jwt);

        SecurityWebFilterChain build = serverHttpSecurity.build();

        return  build;


    }
}

