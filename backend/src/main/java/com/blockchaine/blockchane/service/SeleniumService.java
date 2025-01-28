package com.blockchaine.blockchane.service;

import com.blockchaine.blockchane.config.ArkhamConfig;
import com.blockchaine.blockchane.dto.RawWalletData;
import com.blockchaine.blockchane.dto.Transaction;
import com.blockchaine.blockchane.dto.WalletData;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Service
public class SeleniumService {

    private static final Logger logger = LoggerFactory.getLogger(SeleniumService.class);
    private final ArkhamConfig arkhamConfig;

    public SeleniumService(ArkhamConfig arkhamConfig) {
        this.arkhamConfig = arkhamConfig;
    }

    public WalletData parseWalletData(String walletAddress) {
        WebDriverManager.chromedriver().setup();
        WebDriver driver = new ChromeDriver();
        WalletData walletData = null;

        try {
            String url = arkhamConfig.getBaseUrl() + walletAddress;
            driver.get(url);

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

            WebElement balanceElement = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector("span.Header_portfolioValue__AemOW")
            ));
            String balance = balanceElement.getText();

            WebElement transactionHistoryElement = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector("div.Transactions_transactionsGrid__kegW5")
            ));
            wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(By.cssSelector("div.Transactions_transactionsGrid__kegW5 div"), 0));
            List<WebElement> transactionElements = transactionHistoryElement.findElements(By.cssSelector("div"));
            List<Transaction> transactions = new ArrayList<>();

            for (WebElement transaction : transactionElements) {
                String text = transaction.getText().trim();
                if (!text.isEmpty() && !text.matches("^(TIME|FROM|TO|VALUE|TOKEN|USD)$")) {
                    Transaction parsedTransaction = parseTransaction(text);
                    if (parsedTransaction != null) {
                        transactions.add(parsedTransaction);
                    }
                }
            }

            walletData = new WalletData(balance, transactions);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            driver.quit();
        }
        return walletData;
    }

    public WalletData processRawWalletData(RawWalletData rawWalletData) {
        String balance = rawWalletData.getRawBalance();
        List<Transaction> transactions = new ArrayList<>();

        for (String rawTransaction : rawWalletData.getRawTransactions()) {
            Transaction transaction = parseTransaction(rawTransaction.trim());
            if (transaction != null) {
                transactions.add(transaction);
            }
        }

        return new WalletData(balance, transactions);
    }


    private Transaction parseTransaction(String rawTransaction) {
        String[] parts = rawTransaction.split("\n");
        if (parts.length == 6) {
            String tokenName = extractTokenName(parts);
            return new Transaction(parts[0], parts[1], parts[2], parts[3], parts[4], parts[5], tokenName);
        }
        return null;
    }

    private String extractTokenName(String[] parts) {
        return parts.length > 4 ? parts[4] : "Unknown";
    }
}
