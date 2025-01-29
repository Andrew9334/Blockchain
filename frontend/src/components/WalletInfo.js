import React, { useState, useEffect } from 'react';
import { getWalletData } from '../api/walletApi';
import TransactionHistory from './TransactionHistory';

const WalletInfo = ({ address }) => {
    const [walletData, setWalletData] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        const fetchWalletData = async () => {
            try {
                const data = await getWalletData(address);
                setWalletData(data);
            } catch (err) {
                setError('Failed to fetch wallet data');
            } finally {
                setLoading(false);
            }
        };

        fetchWalletData();
    }, [address]);

    if (loading) return <div>Loading...</div>;
    if (error) return <div>{error}</div>;

    return (
        <div>
            <h1>Wallet Info</h1>
            <p>Balance: {walletData.balance}</p>
            <TransactionHistory transactions={walletData.transactionHistory} />
        </div>
    );
};

export default WalletInfo;
