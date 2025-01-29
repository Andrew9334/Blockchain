package com.blockchaine.blockchane.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable) // 🔥 Отключаем CSRF (не рекомендуется в продакшене)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/selenium/wallet").permitAll() // 🔓 Разрешаем доступ к API
                        .anyRequest().authenticated() // 🔒 Остальные запросы требуют авторизации
                )
                .logout(logout -> logout.logoutSuccessUrl("/").permitAll()); // 🔓 Добавляем логаут

        return http.build();
    }
}