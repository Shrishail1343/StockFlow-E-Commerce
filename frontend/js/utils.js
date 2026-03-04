/**
 * StockFlow — Shared Utilities
 * Auth, dark mode, toasts, user init
 */

// ===== AUTH =====
function authGuard() {
    const token = localStorage.getItem('token');
    if (!token) window.location.href = 'login.html';
}

function logout() {
    localStorage.removeItem('token');
    localStorage.removeItem('user');
    window.location.href = 'login.html';
}

function initUser() {
    try {
        const user = JSON.parse(localStorage.getItem('user') || '{}');
        const initials = (user.fullName || user.username || 'AJ')
            .split(' ').map(n => n[0]).join('').substring(0,2).toUpperCase();

        const avatarEls = ['sidebarAvatar', 'topAvatar'];
        avatarEls.forEach(id => { const el = document.getElementById(id); if(el) el.textContent = initials; });
        const nameEl = document.getElementById('sidebarName');
        if (nameEl) nameEl.textContent = user.fullName || user.username || 'Admin';
        const welcomeEl = document.getElementById('welcomeName');
        if (welcomeEl) welcomeEl.textContent = (user.fullName || user.username || 'Admin').split(' ')[0];
    } catch(e) {}
}

// ===== DARK MODE =====
function initDark() {
    const isDark = localStorage.getItem('darkMode') === 'true';
    if (isDark) document.body.classList.add('dark');
    const btn = document.getElementById('darkBtn');
    if (btn) btn.textContent = isDark ? '☀️' : '🌙';
}

function toggleDark() {
    const isDark = document.body.classList.toggle('dark');
    localStorage.setItem('darkMode', isDark);
    const btn = document.getElementById('darkBtn');
    if (btn) btn.textContent = isDark ? '☀️' : '🌙';
}

// ===== TOASTS =====
function showToast(message, type = 'info', duration = 3500) {
    const container = document.getElementById('toastContainer');
    if (!container) return;

    const icons = { success: '✅', error: '❌', warning: '⚠️', info: 'ℹ️' };
    const toast = document.createElement('div');
    toast.className = `toast ${type}`;
    toast.innerHTML = `<span class="toast-icon">${icons[type] || 'ℹ️'}</span><span class="toast-msg">${message}</span>`;
    container.appendChild(toast);

    setTimeout(() => {
        toast.style.animation = 'toastOut 0.3s ease forwards';
        setTimeout(() => toast.remove(), 300);
    }, duration);
}

// Inject toast-out animation
if (!document.getElementById('toastOutStyle')) {
    const style = document.createElement('style');
    style.id = 'toastOutStyle';
    style.textContent = `@keyframes toastOut { from { opacity:1; transform:translateX(0); } to { opacity:0; transform:translateX(100%); } }`;
    document.head.appendChild(style);
}

// ===== HELPERS =====
function capitalize(s) { return s.charAt(0).toUpperCase() + s.slice(1).toLowerCase(); }
function formatCurrency(v) { return '$' + Number(v).toLocaleString('en-US', { minimumFractionDigits: 2, maximumFractionDigits: 2 }); }
function formatDate(d) { return new Date(d).toLocaleDateString('en-US', { month:'short', day:'numeric', year:'numeric' }); }
