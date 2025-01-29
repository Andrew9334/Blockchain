package com.blockchaine.blockchane.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RawWalletData {

    private final String rawBalance;
    private final List<String> rawTransactions;

    public RawWalletData(String rawBalance, List<String> rawTransactions) {
        this.rawBalance = rawBalance;
        this.rawTransactions = rawTransactions;
    }

}
