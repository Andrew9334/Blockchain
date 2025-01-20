package com.blockchaine.blockchane.controller;

import com.blockchaine.blockchane.service.EthereumService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.web3j.utils.Convert;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/ethereum")
public class EthereumController {

    private static final Logger logger = LoggerFactory.getLogger(EthereumController.class);
    private final EthereumService ethereumService;

    public EthereumController(EthereumService ethereumService) {
        this.ethereumService = ethereumService;
    }

    @GetMapping("/balance/eth")
    public BigDecimal getEthBalance(@Valid @RequestParam String walletAddress) {
        logger.info("Получение баланса ETH для кошелька: {}", walletAddress);
        BigDecimal balance = ethereumService.getBalance(walletAddress);
        logger.info("Баланс ETH для кошелька {}: {} ETH", walletAddress, balance);
        return balance;
    }

    @GetMapping("/transactions")
    public List<String> getTransactions(@RequestParam String walletAddress) {
        logger.info("Получение списка транзакций для кошелька: {}", walletAddress);

        List<String> transactions = ethereumService.getTransactions(walletAddress).stream()
                .map(transaction -> {
                    try {
                        BigDecimal valueInEth = transaction.getValue() != null
                                ? Convert.fromWei(transaction.getValue().toString(), Convert.Unit.ETHER)
                                : BigDecimal.ZERO;

                        return String.format(
                                "Hash: %s, From: %s, To: %s, Value: %s ETH",
                                transaction.getHash(),
                                transaction.getFrom(),
                                transaction.getTo(),
                                valueInEth
                        );
                    } catch (Exception e) {
                        logger.error("Ошибка обработки транзакции с hash {}: {}", transaction.getHash(), e.getMessage());
                        return String.format("Ошибка обработки транзакции с hash: %s", transaction.getHash());
                    }
                })
                .toList();

        logger.info("Найдено {} транзакций для кошелька {}", transactions.size(), walletAddress);
        return transactions;
    }
}
