const API = 'http://localhost:8080/api';

function showTab(tab) {
    document.getElementById('loginForm').classList.toggle('hidden', tab !== 'login');
    document.getElementById('registerForm').classList.toggle('hidden', tab !== 'register');
    document.querySelectorAll('.tab-btn').forEach((btn, i) => {
        btn.classList.toggle('active', (i === 0 && tab === 'login') || (i === 1 && tab === 'register'));
    });
}

async function handleLogin(e) {
    e.preventDefault();
    const errorEl = document.getElementById('loginError');
    errorEl.classList.add('hidden');

    try {
        const res = await fetch(`${API}/auth/login`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({
                email: document.getElementById('loginEmail').value,
                password: document.getElementById('loginPassword').value
            })
        });

        if (!res.ok) throw new Error(typeof t === 'function' ? t('errors.loginInvalid') : 'Email o contrasena incorrectos');

        const data = await res.json();
        localStorage.setItem('token', data.token);
        localStorage.setItem('username', data.username);
        window.location.href = 'dashboard.html';
    } catch (err) {
        errorEl.textContent = err.message;
        errorEl.classList.remove('hidden');
    }
}

async function handleRegister(e) {
    e.preventDefault();
    const errorEl = document.getElementById('registerError');
    errorEl.classList.add('hidden');

    try {
        const res = await fetch(`${API}/auth/register`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({
                username: document.getElementById('regUsername').value,
                email: document.getElementById('regEmail').value,
                password: document.getElementById('regPassword').value
            })
        });

        if (!res.ok) {
            const err = await res.json();
            throw new Error(err.message || (typeof t === 'function' ? t('errors.register') : 'Error al registrarse'));
        }

        const data = await res.json();
        localStorage.setItem('token', data.token);
        localStorage.setItem('username', data.username);
        window.location.href = 'dashboard.html';
    } catch (err) {
        errorEl.textContent = err.message;
        errorEl.classList.remove('hidden');
    }
}

async function handleForgotPassword(e) {
    e.preventDefault();
    const errorEl = document.getElementById('forgotError');
    const successEl = document.getElementById('forgotSuccess');
    errorEl.classList.add('hidden');
    successEl.classList.add('hidden');

    try {
        const res = await fetch(`${API}/auth/forgot-password`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ email: document.getElementById('forgotEmail').value })
        });

        const data = await res.json();
        if (!res.ok) throw new Error(data.message || (typeof t === 'function' ? t('errors.forgotSend') : 'No se pudo enviar el enlace'));

        successEl.textContent = data.message || (typeof t === 'function' ? t('errors.forgotSuccess') : 'Revisa tu correo para continuar.');
        successEl.classList.remove('hidden');
        if (data.devHint) {
            successEl.className = 'bg-amber-50 border border-amber-200 text-amber-800 rounded-lg px-3.5 py-2.5 text-sm break-all';
            successEl.innerHTML = data.devHint.replace(/(https?:\/\/[^\s]+)/g, '<a href="$1" class="underline font-bold">$1</a>');
        }
    } catch (err) {
        errorEl.textContent = err.message;
        errorEl.classList.remove('hidden');
    }
}

async function handleResetPassword(e) {
    e.preventDefault();
    const errorEl = document.getElementById('resetError');
    const successEl = document.getElementById('resetSuccess');
    errorEl.classList.add('hidden');
    successEl.classList.add('hidden');

    const password = document.getElementById('resetPassword').value;
    const confirm = document.getElementById('resetPasswordConfirm').value;
    if (password !== confirm) {
        errorEl.textContent = typeof t === 'function' ? t('errors.passwordMismatch') : 'Las contraseñas no coinciden';
        errorEl.classList.remove('hidden');
        return;
    }

    const params = new URLSearchParams(window.location.search);
    const token = (params.get('token') || '').trim();
    if (!token) {
        errorEl.textContent = typeof t === 'function' ? t('errors.resetInvalidLink') : 'Enlace invalido. Solicita uno nuevo desde el login.';
        errorEl.classList.remove('hidden');
        return;
    }

    try {
        const res = await fetch(`${API}/auth/reset-password`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ token, password })
        });

        const data = await res.json();
        if (!res.ok) throw new Error(data.message || (typeof t === 'function' ? t('errors.resetInvalidExpired') : 'Enlace invalido o expirado. Solicita uno nuevo.'));

        successEl.textContent = data.message || (typeof t === 'function' ? t('errors.resetSuccess') : 'Contraseña actualizada.');
        successEl.classList.remove('hidden');
        document.getElementById('resetForm').classList.add('hidden');
    } catch (err) {
        errorEl.textContent = err.message;
        errorEl.classList.remove('hidden');
    }
}

if (localStorage.getItem('token') && !window.location.pathname.endsWith('reset-password.html')) {
    window.location.href = 'dashboard.html';
}
