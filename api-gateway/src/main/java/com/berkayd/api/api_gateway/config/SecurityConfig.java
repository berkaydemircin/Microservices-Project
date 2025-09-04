package com.berkayd.api.api_gateway.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
public class SecurityConfig {

    // TODO: AGGREGATE API DOCS. THIS DOESN'T WORK FOR SOME REASON
    private final String[] nonAuthUrls = {"/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**", "/swagger-resources/**", "/api-docs/**", "/aggregate/**"};

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity htptSecurity) throws Exception{
        return htptSecurity.authorizeHttpRequests(authorize -> authorize
            .requestMatchers(nonAuthUrls)
            .permitAll()
            .anyRequest()
            .authenticated())
            .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()))
            .build();
    }
}
