import general from '/scripts/general.js';
const { redirect, register, getToken, showError, isAlphaNumeric, isValidEmail } = general;

$(document).ready(function() {
    if (getToken() != null) {
        window.location.href = '/';
    }


    $('#registerbutton').on('click', async function() {
        let loginStr = $('#inputlogin').val();
        let emailStr = $('#inputemail').val();
        let passwordStr = $('#inputpassword').val();
        let passwordStr2 = $('#inputpassword2').val();

        if (loginStr.length < 5) {
            showError('Ошибка! Логин должен состоять минимум из 5 символов!');
            return;
        }

        if (isAlphaNumeric(loginStr) == false) {
            showError('Ошибка! Логин должен состоять только из английских букв и цифр!');
            return;
        }

        if (isValidEmail(emailStr) == false) {
            showError('Ошибка! Почта некорректна!');
            return;
        }

        if (passwordStr != passwordStr2) {
            showError('Ошибка! Пароли не совпадают!');
            return;
        }

        if (passwordStr.length < 5) {
            showError('Ошибка! Пароль должен состоять минимум из 5 символов!');
            return;
        }

        await register(loginStr, emailStr, passwordStr);
        
        await redirect();
    });
});