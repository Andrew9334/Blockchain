package com.blockchaine.blockchane.service;

import org.p2p.solanaj.core.PublicKey;
import org.p2p.solanaj.rpc.RpcClient;
import org.p2p.solanaj.rpc.RpcException;
import org.p2p.solanaj.rpc.types.ProgramAccount;
import org.p2p.solanaj.rpc.types.TokenResultObjects.TokenAmountInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class SolanaService {

    private static final Logger logger = LoggerFactory.getLogger(SolanaService.class);

    private final RpcClient rpcClient;

    // Идентификатор программы SPL Token
    private static final String SPL_TOKEN_PROGRAM_ID = "TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA";

    public SolanaService(RpcClient rpcClient) {
        this.rpcClient = rpcClient;
    }

    /**
     * Получение баланса SOL для кошелька.
     *
     * @param walletAddress адрес кошелька
     * @return баланс в лямпортах
     */
    public long getBalance(String walletAddress) {
        try {
            PublicKey publicKey = new PublicKey(walletAddress);
            return rpcClient.getApi().getBalance(publicKey);
        } catch (Exception e) {
            logger.error("Ошибка получения баланса SOL для кошелька {}: {}", walletAddress, e.getMessage(), e);
            throw new RuntimeException("Ошибка получения баланса SOL", e);
        }
    }

    /**
     * Получение балансов всех токенов SPL для кошелька.
     *
     * @param walletAddress адрес кошелька
     * @return список токенов с их балансами
     */
    public TokenBalance getTokenBalance(String walletAddress, String tokenMint) {
        try {
            PublicKey ownerPublicKey = new PublicKey(walletAddress);
            PublicKey tokenMintKey = new PublicKey(tokenMint);

            // Получение токен-аккаунта
            PublicKey tokenAccount = rpcClient.getApi().getTokenAccountsByOwner(ownerPublicKey, tokenMintKey);

            if (tokenAccount == null) {
                logger.warn("Токен-аккаунт для mint {} не найден у владельца {}", tokenMint, walletAddress);
                return new TokenBalance(null, 0);
            }

            // Получение баланса токен-аккаунта
            double balance = rpcClient.getApi().getTokenAccountBalance(tokenAccount).getUiAmount();
            return new TokenBalance(tokenAccount.toBase58(), balance);

        } catch (RpcException e) {
            logger.error("Ошибка получения токен-аккаунта для владельца {} и mint {}: {}", walletAddress, tokenMint, e.getMessage(), e);
            throw new RuntimeException("Ошибка получения токен-аккаунта", e);
        }
    }

    /**
     * Вспомогательный класс для представления токенов и их балансов.
     */
    public static class TokenBalance {
        private final String accountAddress;
        private final double balance;

        public TokenBalance(String accountAddress, double balance) {
            this.accountAddress = accountAddress;
            this.balance = balance;
        }

        public String getAccountAddress() {
            return accountAddress;
        }

        public double getBalance() {
            return balance;
        }

        @Override
        public String toString() {
            return "TokenBalance{" +
                    "accountAddress='" + accountAddress + '\'' +
                    ", balance=" + balance +
                    '}';
        }
    }
}
