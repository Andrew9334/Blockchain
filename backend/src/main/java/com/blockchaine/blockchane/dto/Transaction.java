package com.blockchaine.blockchane.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {
    private String time;
    private String from;
    private String to;
    private String value;
    private String token;
    private String usd;
    private String tokenName;
}
