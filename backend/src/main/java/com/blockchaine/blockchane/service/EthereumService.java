package com.blockchaine.blockchane.service;

import com.blockchaine.blockchane.dto.TokenBalanceEth;
import com.blockchaine.blockchane.dto.TokenBalanceSolana;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.datatypes.Uint;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.core.methods.response.EthLog;
import org.web3j.protocol.core.methods.response.Transaction;
import org.web3j.utils.Convert;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Address;


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
            BigDecimal ethBalance = Convert.fromWei(wei.toString(), Convert.Unit.ETHER);
            logger.info("Баланс для {}: {} ETH", walletAddress, ethBalance);
            return ethBalance;
        } catch (Exception e) {
            logger.error("Не удалось получить баланс для кошелька {}: {}", walletAddress, e.getMessage(), e);
            return BigDecimal.ZERO;
        }
    }

    public List<Transaction> getTransactions(String walletAddress) {
        try {
            EthFilter filter = new EthFilter(
                    DefaultBlockParameterName.EARLIEST,
                    DefaultBlockParameterName.LATEST,
                    walletAddress
            );

            EthLog ethLog = web3j.ethGetLogs(filter).send();

            return ethLog.getLogs().stream()
                    .map(logResult -> {
                        if (logResult instanceof EthLog.LogObject logObject) {
                            try {
                                return web3j.ethGetTransactionByHash(logObject.getTransactionHash()).send().getTransaction().orElse(null);
                            } catch (Exception e) {
                                logger.error("Ошибка получения транзакции с hash {}: {}", logObject.getTransactionHash(), e.getMessage(), e);
                                return null;
                            }
                        }
                        return null;
                    })
                    .filter(transaction -> transaction != null)
                    .collect(Collectors.toList());

        } catch (Exception e) {
            logger.error("Ошибка получения транзакций для кошелька {}: {}", walletAddress, e.getMessage(), e);
            return List.of();
        }
    }

    public TokenBalanceEth getTokenBalance(String walletAddress, String tokenContractAddress) {
        try {
            Function function = new Function(
                    "balanceOf",
                    List.of(new Address(walletAddress)),
                    List.of(new org.web3j.abi.TypeReference<Uint>() {})
            );

            String encodedFunction = FunctionEncoder.encode(function);

            org.web3j.protocol.core.methods.response.EthCall response = web3j.ethCall(
                    org.web3j.protocol.core.methods.request.Transaction.createEthCallTransaction(
                            walletAddress, tokenContractAddress, encodedFunction
                    ),
                    DefaultBlockParameterName.LATEST
            ).send();

            List<Type> decoded = FunctionReturnDecoder.decode(response.getValue(), function.getOutputParameters());
            BigInteger balance = (decoded != null && !decoded.isEmpty()) ? (BigInteger) decoded.get(0).getValue() : BigInteger.ZERO;

            logger.info("Баланс токена {} для {}: {}", tokenContractAddress, walletAddress, balance);

            return new TokenBalanceEth(walletAddress, tokenContractAddress, Convert.fromWei(balance.toString(), Convert.Unit.ETHER));
        } catch (Exception e) {
            logger.error("Ошибка получения баланса токена {} для {}: {}", tokenContractAddress, walletAddress, e.getMessage(), e);
            return new TokenBalanceEth(walletAddress, tokenContractAddress, BigDecimal.ZERO);
        }
    }
}
