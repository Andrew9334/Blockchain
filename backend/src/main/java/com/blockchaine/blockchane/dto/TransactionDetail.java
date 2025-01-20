package com.blockchaine.blockchane.dto;

import lombok.Getter;

@Getter
public class TransactionDetail {
    private final String signature;
    private final long slot;
    private final String memo;

    public TransactionDetail(String signature, long slot, String memo) {
        this.signature = signature;
        this.slot = slot;
        this.memo = memo;
    }

    @Override
    public String toString() {
        return "TransactionDetail{" +
                "signature='" + signature + '\'' +
                ", slot=" + slot +
                ", memo='" + memo + '\'' +
                '}';
    }
}
