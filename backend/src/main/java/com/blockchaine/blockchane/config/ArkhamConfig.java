package com.blockchaine.blockchane.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class ArkhamConfig {

    @Value("${arkham.baseUrl}")
    private String baseUrl;
}
