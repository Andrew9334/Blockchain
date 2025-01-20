package com.blockchaine.blockchane.dto;

import lombok.Getter;

@Getter
public class TokenBalance {
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

