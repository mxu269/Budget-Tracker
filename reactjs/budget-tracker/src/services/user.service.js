import axios from 'axios';
import authHeader from './auth-header';

const API_URL = process.env.REACT_APP_API_URL + '/api/user/';

/**
 * centralized user service, api end points
 */
class UserService {
    postNewTransaction(data) {
        return axios.post(API_URL + 'new-transaction', data, { headers: authHeader() });
    }

    getTransaction(id) {
        return axios.get(API_URL + 'transaction', {
            params: {id: id}, 
            headers: authHeader(), 
        });
    }

    getSearchTransaction(keyword) {
        return axios.get(API_URL + 'search-transaction', {
            params: {key: keyword}, 
            headers: authHeader(), 
        });
    }

    postDeleteTransaction(id) {
        return axios.post(API_URL + 'delete-transaction', {id: id}, { headers: authHeader() });

    }

    getWeeklySummary() {
        return axios.get(API_URL + 'week-summary', { headers: authHeader() });
    }

    getBalance() {
        return axios.get(API_URL + 'balance', { headers: authHeader() });
    }

    getRecentTransaction() {
        return axios.get(API_URL + 'recent-transaction', { headers: authHeader() });
    }
  
}

export default new UserService();