/**
 * StockFlow — API Client
 * Handles all API communication with the Spring Boot backend
 */

// In production (Netlify), requests go to /api and are proxied to the backend.
// In local dev, they go directly to localhost:8080.
const API_BASE = (window.location.hostname === 'localhost' || window.location.hostname === '127.0.0.1')
    ? 'http://localhost:8080/api'
    : '/api';

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
