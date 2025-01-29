import React, { useState, useEffect } from 'react';
import axios from 'axios';

const API_URL = "http://localhost:8080/api/selenium/wallet";

function WalletPage() {
    const [walletData, setWalletData] = useState(null);
    const [address, setAddress] = useState('');

    useEffect(() => {
        if (address) {
            console.log(`🚀 Отправляем запрос к бэкенду: ${API_URL}?address=${address}`);

            axios.get(`${API_URL}?address=${address}`)
                .then((response) => {
                    console.log("✅ Ответ от бэкенда:", response.data);

                    if (!response.data || Object.keys(response.data).length === 0) {
                        console.error("⚠️ Бэкенд вернул пустой объект!", response);
                        return;
                    }

                    setWalletData(response.data);
                })
                .catch((error) => {
                    console.error("❌ Ошибка запроса:", error);
                });
        }
    }, [address]);


    return (
        <div>
            <h1>Wallet Data</h1>
            <input
                type="text"
                placeholder="Enter Wallet Address"
                value={address}
                onChange={(e) => setAddress(e.target.value)}
            />
            {walletData ? (
                <div>
                    <p>Balance: {walletData.balance}</p>
                    <ul>
                        {walletData.transactionHistory && walletData.transactionHistory.length > 0 ? (
                            walletData.transactionHistory.map((transaction, index) => (
                                <li key={index}>
                                    <p>Transaction {index + 1}</p>
                                    <p>From: {transaction.from}</p>
                                    <p>To: {transaction.to}</p>
                                    <p>Value: {transaction.value}</p>
                                    <p>Token: {transaction.token}</p>
                                    <p>USD: {transaction.usd}</p>
                                </li>
                            ))
                        ) : (
                            <p>🔄 Транзакции отсутствуют</p>
                        )}
                    </ul>
                </div>
            ) : (
                <p>⌛ Загрузка...</p>
            )}
        </div>
    );
}

export default WalletPage;
