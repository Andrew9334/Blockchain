package com.blockchaine.blockchane.service;

import com.blockchaine.blockchane.config.ArkhamConfig;
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
        WebDriverManager.chromedriver().setup(); // Автоматическая настройка драйвера
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
            System.out.println("Баланс: " + balance);

            WebElement transactionHistoryElement = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector("div.Transactions_transactionsGrid__kegW5")
            ));
            List<WebElement> transactionElements = transactionHistoryElement.findElements(By.cssSelector("div"));
            List<String> transactions = new ArrayList<>();
            for (WebElement transaction : transactionElements) {
                transactions.add(transaction.getText());
            }

            walletData = new WalletData(balance, transactions);
        } catch (Exception e) {
            logger.error("Ошибка при парсинге данных кошелька", e);
        } finally {
            driver.quit();
        }
        return walletData;
    }
}
