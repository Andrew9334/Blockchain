package com.blockchaine.blockchane.dto;

import lombok.Getter;

@Getter
public class Transaction {
    private String time;
    private String from;
    private String to;
    private String value;
    private String token;
    private String usd;
    private String tokenName;

    public Transaction(String time, String from, String to, String value, String token, String usd, String tokenName) {
        this.time = time;
        this.from = from;
        this.to = to;
        this.value = value;
        this.token = token;
        this.usd = usd;
        this.tokenName = tokenName;
    }
}
