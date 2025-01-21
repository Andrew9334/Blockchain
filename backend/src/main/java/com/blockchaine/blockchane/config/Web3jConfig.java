package com.blockchaine.blockchane.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

@Configuration
public class Web3jConfig {

    private static final Logger logger = LoggerFactory.getLogger(Web3jConfig.class);

    // URL для подключения к сети Ethereum через Infura
    @Value("${ethereum.infura.url}")
    private String infuraUrl;

    @Bean
    public Web3j web3j() {
        Web3j web3j = Web3j.build(new HttpService(infuraUrl));
        logger.info("Initializing Web3j client with URL: {}", infuraUrl);

        try {
            String clientVersion = web3j.web3ClientVersion().send().getWeb3ClientVersion();
            logger.info("Successfully connected to Ethereum RPC. Client Version: {}", clientVersion);
        } catch (Exception e) {
            logger.error("Failed to connect to Ethereum RPC at {}: {}", infuraUrl, e.getMessage(), e);
        }
        return web3j;
    }
}
