import React, { useState, useEffect } from "react";
import axios from "axios";
import { motion } from "framer-motion"; // Анимации
import { API_URL } from "../config"; // Импорт API_URL
import "../styles.css"; // Подключаем стили
import "../index.css"; // Подключаем Tailwind

function WalletPage() {
    const [walletData, setWalletData] = useState(null);
    const [address, setAddress] = useState(localStorage.getItem("wallet") || "");
    const [darkMode, setDarkMode] = useState(() => localStorage.getItem("theme") === "dark");

    // Устанавливаем тему при загрузке
    useEffect(() => {
        if (darkMode) {
            document.documentElement.classList.add("dark"); // Меняем html, а не body
            localStorage.setItem("theme", "dark");
        } else {
            document.documentElement.classList.remove("dark");
            localStorage.setItem("theme", "light");
        }
    }, [darkMode]);

    // Запрос на бэкенд
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
                    localStorage.setItem("wallet", address);
                })
                .catch((error) => {
                    console.error("❌ Ошибка запроса:", error);
                });
        }
    }, [address]);

    return (
        <div className={`min-h-screen flex flex-col items-center justify-center px-4 py-8 transition-all duration-300 ${darkMode ? "bg-gray-900 text-white" : "bg-gray-100 text-black"}`}>
            {/* Переключатель темы */}
            <button
                className="absolute top-4 right-4 p-2 bg-gray-800 text-white dark:bg-gray-200 dark:text-black rounded-full transition duration-300"
                onClick={() => setDarkMode(!darkMode)}
            >
                {darkMode ? "🌞 Светлая" : "🌙 Тёмная"}
            </button>

            {/* Поле ввода кошелька */}
            <motion.div
                initial={{ opacity: 0, y: -10 }}
                animate={{ opacity: 1, y: 0 }}
                transition={{ duration: 0.5 }}
                className="w-full max-w-xl bg-white dark:bg-gray-800 rounded-lg shadow-lg p-6"
            >
                <h2 className="text-xl font-bold text-center mb-4">🔍 Введите адрес кошелька</h2>
                <input
                    type="text"
                    value={address}
                    onChange={(e) => setAddress(e.target.value)}
                    className="w-full p-3 text-lg border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 dark:bg-gray-700 dark:text-white"
                    placeholder="0x123... полный адрес"
                />
            </motion.div>

            {/* Данные кошелька */}
            {walletData ? (
                <motion.div
                    initial={{ opacity: 0, scale: 0.9 }}
                    animate={{ opacity: 1, scale: 1 }}
                    transition={{ duration: 0.5 }}
                    className="mt-6 w-full max-w-xl bg-white dark:bg-gray-800 rounded-lg shadow-lg p-6"
                >
                    <h3 className="text-xl font-bold text-center">💰 Баланс: {walletData.balance}</h3>
                    <ul className="mt-4 divide-y divide-gray-300 dark:divide-gray-600">
                        {walletData.transactionHistory && walletData.transactionHistory.length > 0 ? (
                            walletData.transactionHistory.map((tx, index) => (
                                <li key={index} className="py-4">
                                    <p className="font-semibold">🔗 Транзакция {index + 1}</p>
                                    <p>📤 Отправитель: {tx.from}</p>
                                    <p>📥 Получатель: {tx.to}</p>
                                    <p>💰 Сумма: {tx.value} {tx.token}</p>
                                    <p>💵 USD: {tx.usd}</p>
                                </li>
                            ))
                        ) : (
                            <p className="text-center">🔄 Транзакции отсутствуют</p>
                        )}
                    </ul>
                </motion.div>
            ) : (
                <p className="mt-6">⌛ Загрузка...</p>
            )}
        </div>
    );
}

export default WalletPage;
