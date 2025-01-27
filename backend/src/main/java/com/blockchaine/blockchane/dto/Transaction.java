package com.blockchaine.blockchane.dto;

import lombok.Getter;

@Getter
public class Transaction {
    private String date;
    private String from;
    private String to;
    private String value;
    private String token;
    private String usd;

    // Конструктор
    public Transaction(String date, String from, String to, String value, String token, String usd) {
        this.date = date;
        this.from = from;
        this.to = to;
        this.value = value;
        this.token = token;
        this.usd = usd;
    }
}
