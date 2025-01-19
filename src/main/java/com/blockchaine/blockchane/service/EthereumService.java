package com.blockchaine.blockchane.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthFilter;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.core.methods.response.EthLog;
import org.web3j.protocol.core.methods.response.Transaction;
import org.web3j.utils.Convert;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EthereumService {

    private static final Logger logger = LoggerFactory.getLogger(EthereumService.class);
    private final Web3j web3j;

    public EthereumService(Web3j web3j) {
        this.web3j = web3j;
    }

    public BigDecimal getBalance(String walletAddress) {
        try {
            EthGetBalance ethGetBalance = web3j.ethGetBalance(walletAddress, DefaultBlockParameterName.LATEST).send();
            BigInteger wei = ethGetBalance.getBalance();
            return Convert.fromWei(wei.toString(), Convert.Unit.ETHER);
        } catch (Exception e) {
            logger.error("Failed to fetch balance for wallet {}: {}", walletAddress, e.getMessage(), e);
            return BigDecimal.ZERO;
        }
    }

    public List<Transaction> getTransactions(String walletAddress) {
        try {
            org.web3j.protocol.core.methods.request.EthFilter filter =
                    new org.web3j.protocol.core.methods.request.EthFilter(
                            DefaultBlockParameterName.EARLIEST,
                            DefaultBlockParameterName.LATEST,
                            walletAddress
                    );

            EthLog ethLog = web3j.ethGetLogs(filter).send();

            return ethLog.getLogs().stream()
                    .map(logResult -> {
                        if (logResult instanceof EthLog.LogObject) {
                            EthLog.LogObject logObject = (EthLog.LogObject) logResult;
                            try {
                                return web3j.ethGetTransactionByHash(logObject.getTransactionHash()).send().getResult();
                            } catch (Exception e) {
                                e.printStackTrace();
                                return null;
                            }
                        }
                        return null;
                    })
                    .filter(transaction -> transaction != null)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }
}
