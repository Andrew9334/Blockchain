package com.blockchaine.blockchane.controller;

import com.blockchaine.blockchane.dto.TransactionDetail;
import com.blockchaine.blockchane.service.SolanaService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/solana")
public class SolanaController {

    private final SolanaService solanaService;

    public SolanaController(SolanaService solanaService) {
        this.solanaService = solanaService;
    }

    @GetMapping("/balance/token")
    public long getSolanaBalance(@Valid @RequestParam String walletAddress) {
        return solanaService.getBalance(walletAddress);
    }

    public List<TransactionDetail> getTransactions(@Valid @RequestParam String walletAddress, @Valid @RequestParam(defaultValue = "10") int limit) {
        return solanaService.getTransactions(walletAddress, limit);
    }
}
