package com.blockchaine.blockchane.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WalletData {

    private String balance;
    private List<Transaction> transactionHistory;
}
