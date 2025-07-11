

document.getElementById('loginForm').addEventListener('submit', async function(e) {
    e.preventDefault();
    const email = document.getElementById('email').value;
    const password = document.getElementById('password').value;

    const response = await fetch('auth/login', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ username: email, password })
    });

    if (response.ok) {
        const result = await response.json();

        document.cookie = `token=${result.token}; path=/`;
        window.location.href = '/dashboard';
    } else {
        document.getElementById('error').innerText = "Invalid email or password.";
    }
});
