package com.blockchaine.blockchane.dto;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class TokenBalanceEth {
    private String walletAddress;
    private String tokenContractAddress;
    private BigDecimal balance;

    // Конструктор с тремя параметрами
    public TokenBalanceEth(String walletAddress, String tokenContractAddress, BigDecimal balance) {
        this.walletAddress = walletAddress;
        this.tokenContractAddress = tokenContractAddress;
        this.balance = balance;
    }

    @Override
    public String toString() {
        return "TokenBalance{" +
                "walletAddress='" + walletAddress + '\'' +
                ", tokenContractAddress='" + tokenContractAddress + '\'' +
                ", balance=" + balance +
                '}';
    }
}
