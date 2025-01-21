package com.blockchaine.blockchane.service;

import com.blockchaine.blockchane.dto.TokenBalanceSolana;
import com.blockchaine.blockchane.dto.TransactionDetail;
import org.p2p.solanaj.core.PublicKey;
import org.p2p.solanaj.rpc.RpcClient;
import org.p2p.solanaj.rpc.RpcException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SolanaService {

    private static final Logger logger = LoggerFactory.getLogger(SolanaService.class);

    private final RpcClient rpcClient;

    private static final String SPL_TOKEN_PROGRAM_ID = "TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA";

    public SolanaService(RpcClient rpcClient) {
        this.rpcClient = rpcClient;
    }

    public long getBalance(String walletAddress) {
        if (walletAddress == null || walletAddress.length() < 32) {
            throw new IllegalArgumentException("Invalid wallet address");
        }
        try {
            PublicKey publicKey = new PublicKey(walletAddress);
            return rpcClient.getApi().getBalance(publicKey);
        } catch (Exception e) {
            logger.error("Ошибка получения баланса SOL для кошелька {}: {}", walletAddress, e.getMessage(), e);
            throw new RuntimeException("Ошибка получения баланса SOL", e);
        }
    }

    public TokenBalanceSolana getTokenBalance(String walletAddress, String tokenMint) {
        try {
            PublicKey ownerPublicKey = new PublicKey(walletAddress);
            PublicKey tokenMintKey = new PublicKey(tokenMint);
            PublicKey tokenAccount = rpcClient.getApi().getTokenAccountsByOwner(ownerPublicKey, tokenMintKey);

            if (tokenAccount == null) {
                logger.warn("Токен-аккаунт для mint {} не найден у владельца {}", tokenMint, walletAddress);
                return new TokenBalanceSolana(null, 0);
            }
            double balance = rpcClient.getApi().getTokenAccountBalance(tokenAccount).getUiAmount();
            return new TokenBalanceSolana(tokenAccount.toBase58(), balance);
        } catch (RpcException e) {
            logger.error("Ошибка получения токен-аккаунта для владельца {} и mint {}: {}", walletAddress, tokenMint, e.getMessage(), e);
            throw new RuntimeException("Ошибка получения токен-аккаунта", e);
        }
    }


    public List<TransactionDetail> getTransactions(String walletAddress, int limit) {
        try {
            PublicKey publicKey = new PublicKey(walletAddress);
            List<org.p2p.solanaj.rpc.types.SignatureInformation> signatureInfos =
                    rpcClient.getApi().getConfirmedSignaturesForAddress2(publicKey, limit);
            List<TransactionDetail> transactions = new ArrayList<>();

            for (org.p2p.solanaj.rpc.types.SignatureInformation signatureInfo : signatureInfos) {
                try {
                    String signature = signatureInfo.getSignature();
                    var transaction = rpcClient.getApi().getTransaction(signature);
                    String memo = extractMemo(transaction);
                    transactions.add(new TransactionDetail(signature, transaction.getSlot(), memo));
                } catch (RpcException e) {
                    logger.error("Ошибка получения транзакции для подписи {}: {}", signatureInfo.getSignature(), e.getMessage());
                }
            }
            return transactions;
        } catch (RpcException e) {
            logger.error("Ошибка получения транзакций для кошелька {}: {}", walletAddress, e.getMessage());
            throw new RuntimeException("Ошибка получения транзакций", e);
        }
    }


    private String extractMemo(org.p2p.solanaj.rpc.types.ConfirmedTransaction transaction) {
        try {
            if (transaction.getTransaction().getMessage() != null) {
                var message = transaction.getTransaction().getMessage();
                var instructions = message.getInstructions();

                for (var instruction : instructions) {
                    long programIdIndex = instruction.getProgramIdIndex();
                    PublicKey programId = new PublicKey(message.getAccountKeys().get((int) programIdIndex));
                    if ("MemoSq4gqABAXKb96qnH8TysNcWxMyWCqXgDLGmfcHr".equals(programId.toBase58())) {
                        return new String(instruction.getData());
                    }
                }
            }
        } catch (Exception e) {
            logger.error("Ошибка извлечения memo: {}", e.getMessage(), e);
        }
        return null;
    }


}
