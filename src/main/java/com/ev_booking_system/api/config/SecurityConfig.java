package com.ev_booking_system.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()           // disable CSRF for testing POST requests
            .authorizeHttpRequests()
            .anyRequest().permitAll();  // allow all requests without login
        return http.build();
    }
}
