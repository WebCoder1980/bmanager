import general from '/scripts/general.js';
const { redirect, login, getToken } = general;

$(document).ready(function() {
    if (getToken() != null) {
        window.location.href = '/';
    }

    $('#loginbutton').on('click', async function() {
        let loginStr = $('#inputlogin').val();
        let passwordStr = $('#inputpassword').val();

        if (await login(loginStr, passwordStr) == null) {
            return;
        }
        
        await redirect();
    });
});