import React from 'react';

const TransactionHistory = ({ transactions }) => {
    return (
        <div>
            <h2>Transaction History</h2>
            <table>
                <thead>
                <tr>
                    <th>Time</th>
                    <th>From</th>
                    <th>To</th>
                    <th>Value</th>
                    <th>Token</th>
                    <th>USD</th>
                </tr>
                </thead>
                <tbody>
                {transactions.map((transaction, index) => (
                    <tr key={index}>
                        <td>{transaction.time}</td>
                        <td>{transaction.from}</td>
                        <td>{transaction.to}</td>
                        <td>{transaction.value}</td>
                        <td>{transaction.token}</td>
                        <td>{transaction.usd}</td>
                    </tr>
                ))}
                </tbody>
            </table>
        </div>
    );
};

export default TransactionHistory;
