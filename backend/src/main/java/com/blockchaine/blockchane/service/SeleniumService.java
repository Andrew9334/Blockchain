package com.blockchaine.blockchane.service;

import com.blockchaine.blockchane.config.ArkhamConfig;
import com.blockchaine.blockchane.dto.RawWalletData;
import com.blockchaine.blockchane.dto.Transaction;
import com.blockchaine.blockchane.dto.WalletData;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
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

        logger.info("🔍 Начало парсинга кошелька: {}", walletAddress);

        try {
            String url = arkhamConfig.getBaseUrl() + walletAddress;
            driver.get(url);
            logger.info("🌍 Открыта страница: {}", url);

            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("window.scrollBy(0, 100)");
            Thread.sleep(2000);
            logger.info("📜 Прокрутка страницы выполнена");

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));

            WebElement balanceElement = new WebDriverWait(driver, Duration.ofSeconds((10)))
                    .until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("span.Header_portfolioValue__AemOW")));
            String balance = balanceElement.getText();
            logger.info("💰 Баланс найден: {}", balance);

            WebElement transactionHistoryElement = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector("div.Transactions_transactionsGrid__kegW5")
            ));
            wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(By.cssSelector("div.Transactions_transactionsGrid__kegW5 div"), 0));
            List<WebElement> transactionElements = transactionHistoryElement.findElements(By.cssSelector("div"));
            logger.info("📑 Найдено {} элементов в истории транзакций", transactionElements.size());

            List<Transaction> transactions = new ArrayList<>();

            for (WebElement transaction : transactionElements) {
                String text = transaction.getText().trim();
                if (!text.isEmpty() && !text.matches("^(TIME|FROM|TO|VALUE|TOKEN|USD)$")) {
                    Transaction parsedTransaction = parseTransaction(text);
                    if (parsedTransaction != null) {
                        transactions.add(parsedTransaction);
                        logger.info("📌 Транзакция добавлена: {}", parsedTransaction);
                    }
                }
            }

            walletData = new WalletData(balance, transactions);
        } catch (Exception e) {
            logger.error("❌ Ошибка при парсинге кошелька {}: {}", walletAddress, e.getMessage(), e);
        } finally {
            driver.quit();
            logger.info("✅ Завершение работы Selenium, браузер закрыт.");
        }
        return walletData;
    }

    public WalletData processRawWalletData(RawWalletData rawWalletData) {
        String balance = rawWalletData.getRawBalance();
        List<Transaction> transactions = new ArrayList<>();

        logger.info("🔄 Обработка сырых данных кошелька...");
        for (String rawTransaction : rawWalletData.getRawTransactions()) {
            Transaction transaction = parseTransaction(rawTransaction.trim());
            if (transaction != null) {
                transactions.add(transaction);
                logger.info("📌 Добавлена транзакция после обработки: {}", transaction);
            }
        }
        return new WalletData(balance, transactions);
    }


    private Transaction parseTransaction(String rawTransaction) {
        String[] parts = rawTransaction.split("\n");
        if (parts.length == 6) {
            String tokenName = extractTokenName(parts);
            Transaction transaction = new Transaction(parts[0], parts[1], parts[2], parts[3], parts[4], parts[5], tokenName);
            logger.info("🔍 Разобрана транзакция: {}", transaction);
            return transaction;
        }
        logger.warn("⚠️ Пропущена некорректная транзакция: {}", rawTransaction);
        return null;
    }

    private String extractTokenName(String[] parts) {
        return parts.length > 4 ? parts[4] : "Unknown";
    }
}
