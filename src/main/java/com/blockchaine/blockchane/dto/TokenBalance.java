package com.blockchaine.blockchane.dto;

import lombok.Getter;

@Getter
public class TokenBalance {
    private final String tokenAccountAddress;
    private final String balance;

    public TokenBalance(String tokenAccountAddress, String balance) {
        this.tokenAccountAddress = tokenAccountAddress;
        this.balance = balance;
    }
}
