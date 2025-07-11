document.getElementById('signupForm').addEventListener('submit', async function(e) {
    e.preventDefault();
    const name = document.getElementById('name').value;
    const email = document.getElementById('email').value;
    const password = document.getElementById('password').value;

    const response = await fetch('auth/signup', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ name, email, password})
    });

    if (response.ok) {
        const result = await response.json();
        document.cookie = `token=${result.token}; path=/`;
        window.location.href = 'dashboard';
    } else {
        const text = await response.text();
        document.getElementById('error').innerText = text;
    }
});
