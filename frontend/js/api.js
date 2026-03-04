/**
 * StockFlow — API Client
 * Handles all API communication with the Spring Boot backend
 */

const API_BASE = 'http://localhost:8080/api';

const api = {
    _getHeaders() {
        const token = localStorage.getItem('token');
        return {
            'Content-Type': 'application/json',
            ...(token ? { 'Authorization': `Bearer ${token}` } : {})
        };
    },

    async _request(method, path, body = null) {
        const response = await fetch(`${API_BASE}${path}`, {
            method,
            headers: this._getHeaders(),
            ...(body ? { body: JSON.stringify(body) } : {})
        });

        if (response.status === 401) {
            localStorage.removeItem('token');
            localStorage.removeItem('user');
            window.location.href = 'login.html';
            throw new Error('Unauthorized');
        }

        if (!response.ok) {
            const err = await response.json().catch(() => ({}));
            throw new Error(err.message || `HTTP ${response.status}`);
        }

        if (response.status === 204) return null;
        return response.json();
    },

    get: (path) => api._request('GET', path),
    post: (path, body) => api._request('POST', path, body),
    put: (path, body) => api._request('PUT', path, body),
    patch: (path, body) => api._request('PATCH', path, body),
    delete: (path) => api._request('DELETE', path),
};
