/**
 * get the auth header Authorization: Bearer  + access_token
 */
export default function authHeader() {
    const user = JSON.parse(localStorage.getItem('user'));

    if (user && user.access_token) {
        return {
            Authorization: 'Bearer ' + user.access_token,
        };
    } else {
        return {};
    }
}