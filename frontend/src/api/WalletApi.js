import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080/api/selenium';

export const getWalletData = (address) => {
    return axios.get(`${API_BASE_URL}/wallet?address=${address}`)
        .then(response => response.data)
        .catch(error => {
            console.error('Error fetching wallet data', error);
            throw error;
        });
};
