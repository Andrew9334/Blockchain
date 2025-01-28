package com.blockchaine.blockchane.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class WalletData {

    private String balance;
    private List<Transaction> transactionHistory;

    public WalletData(String balance, List<Transaction> transactionHistory) {
        this.balance = balance;
        this.transactionHistory = transactionHistory;
    }
}
