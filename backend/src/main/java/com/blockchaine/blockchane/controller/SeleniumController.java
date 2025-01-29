package com.blockchaine.blockchane.controller;

import com.blockchaine.blockchane.dto.RawWalletData;
import com.blockchaine.blockchane.dto.WalletData;
import com.blockchaine.blockchane.service.SeleniumService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/selenium")
@CrossOrigin(origins = "http://localhost:3000")
public class SeleniumController {

    private final SeleniumService seleniumService;

    public SeleniumController(SeleniumService seleniumService) {
        this.seleniumService = seleniumService;
    }

    @GetMapping("/wallet")
    public WalletData getWalletData(@RequestParam String address) {
        // Метод parseWalletData уже возвращает WalletData
        return seleniumService.parseWalletData(address);
    }
}
