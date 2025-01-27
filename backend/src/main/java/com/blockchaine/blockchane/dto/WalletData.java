package com.blockchaine.blockchane.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class WalletData {

    private String balance;
    private List<String> transactionHistory;

    public WalletData(String balance, List<String> transactionHistory) {
        this.balance = balance;
        this.transactionHistory = transactionHistory;
    }
}
