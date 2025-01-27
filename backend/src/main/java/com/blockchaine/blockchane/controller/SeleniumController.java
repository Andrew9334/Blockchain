package com.blockchaine.blockchane.controller;

import com.blockchaine.blockchane.dto.WalletData;
import com.blockchaine.blockchane.service.SeleniumService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/selenium")
public class SeleniumController {

    private final SeleniumService seleniumService;

    public SeleniumController(SeleniumService seleniumService) {
        this.seleniumService = seleniumService;
    }

    @GetMapping("/wallet")
    public WalletData getWalletData(@RequestParam String address) {
        return seleniumService.parseWalletData(address);
    }
}
