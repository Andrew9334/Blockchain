package com.blockchaine.blockchane.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
class CorsConfig {
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/api/**") // Разрешить доступ только к путям, начинающимся с /api/
                        .allowedOrigins("http://localhost:3000") // Разрешить запросы с этого домена
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Разрешить необходимые методы
                        .allowedHeaders("*") // Разрешить все заголовки
                        .allowCredentials(true); // Разрешить передачу cookies и авторизационных данных
            }
        };
    }
}
