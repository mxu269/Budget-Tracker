import axios from 'axios';
import jwt_decode from "jwt-decode";

const API_URL = 'http://localhost:8000/api/auth/';

/**
 * centralized authorization service
 */
class AuthService {
    login(username, password) {
        return axios.post(API_URL + 'signin', {
            username: username,
            password: password,
        })
        .then(response => {
            if (response.data.access_token) {
                localStorage.setItem("user", JSON.stringify(response.data));
                return response.data;
            }
            console.log(response);
        });
    }

    signup(name, username, password) {
        return axios.post(API_URL + 'signup', {
            name: name,
            username: username,
            password: password,
        })
        .then(response => {
            if (response.data.access_token) {
                localStorage.setItem("user", JSON.stringify(response.data));
                return response.data;
            }
            console.log(response);
        });
    }

    signout() {
        localStorage.removeItem("user");
    }

    isLoggedIn() {
        const user = JSON.parse(localStorage.getItem('user'));
        if (user && user.access_token) {
            const token = user.access_token;
            const decoded = jwt_decode(token);
            //console.log(decoded);
            const current_time = Date.now() / 1000;
            if ( decoded.exp > current_time) {
                return true;
            }
        }
        return false;
    }

    getName() {
        const user = JSON.parse(localStorage.getItem('user'));
        if (user && user.name) {
            return user.name;
        }
        return 'null';
    }
}

export default new AuthService();