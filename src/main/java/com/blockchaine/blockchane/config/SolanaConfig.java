package com.blockchaine.blockchane.config;

import org.p2p.solanaj.rpc.RpcClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SolanaConfig {

    private static final Logger logger = LoggerFactory.getLogger(SolanaConfig.class);

    // URL для RPC подключений берется из application.yml или application.properties
    @Value("${solana.rpc.url}")
    private String solanaRpcUrl;

    @Bean
    public RpcClient solanaRpcClient() {
        RpcClient rpcClient = new RpcClient(solanaRpcUrl);
        logger.info("Initializing Solana RPC Client with URL: {}", solanaRpcUrl);

        // Проверка подключения
        try {
            String version = String.valueOf(rpcClient.getApi().getVersion());
            logger.info("Successfully connected to Solana RPC. Version: {}", version);
        } catch (Exception e) {
            logger.error("Failed to connect to Solana RPC at {}: {}", solanaRpcUrl, e.getMessage(), e);
        }

        return rpcClient;
    }
}
