function getToken() {
    return localStorage.getItem("jwtToken");
}

function setToken(newtoken) {
    localStorage.setItem("jwtToken", newtoken);
}

function deleteToken() {
    localStorage.removeItem("jwtToken");
}

function getUserId() {
    return localStorage.getItem("userId");
}

function setUserId(userId) {
    localStorage.setItem("userId", userId);
}

function deleteUserId() {
    localStorage.removeItem("userId");
}

function deleteRootDir() {
    localStorage.removeItem("rootDirId");
    localStorage.removeItem("rootDirName");
}

async function redirect() {
    if (getToken() == null) {
        window.location.href = "/login";
    }
    else {
        window.location.href = "/browser"
    }
}

async function login(loginStr, passwordStr) {
    const requestBody = {
        username: loginStr,
        password: passwordStr
    };

    let url = 'http://10.8.0.1:36001/auth/login';

    let response = await fetch(url, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(requestBody)
    }).then(response => {
        if (!response.ok) {
            showError("Проверьте данных для авторизации!");
            return null;
        }
        return response.json();
    })
    .then(data => {
        setToken(data['accessToken']);
        setUserId(data['id']);

        return data;
    })
    .catch(error => {
        console.error(error);
    });

    return response;
}

async function register(loginStr, emailStr, passwordStr) {
    const requestBody = {
        username: loginStr,
        email: emailStr,
        password: passwordStr
    };

    let url = 'http://10.8.0.1:36001/auth/register';

    let response = await fetch(url, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(requestBody)
    }).then(response => {
        if (!response.ok) {
            showError("Проблема с сервером!");
            return null;
        }
        return response.json();
    })
    .then(data => {
        return data;
    })
    .catch(error => {
        console.error(error);
    });
    return response;
}

function logout() {
    deleteToken();
    deleteUserId();
    deleteRootDir();
    
    window.location.href = '/';    
}

async function getCurrentUser() {
    let url = `http://10.8.0.1:36001/users/${getUserId()}`;

    let response = await fetch(url, {
        method: 'GET',
        headers: {
            'Authorization': `Bearer ${getToken()}`,
            'Content-Type': 'application/json'
        }
    }).then(response => {
        if (!response.ok) {
            throw new Error('Network response was not ok ' + response.statusText);
        }
        return response.json();
    })
    .then(data => {
        return data['content'];
    })
    .catch(error => {
        console.error(error);
        return null;
    });

    return response;
}

function showError(message) {
    $("body").append(`<div class="blackout">
        <div class="popup">
            <h1>Ошибка!</h2>
            <textarea readonly>${message}</textarea>
            <div class="buttons">
                <button class="popupokbutton button">Ок</button>
            </div>
        </div>
    </div>`);

    $(".popupokbutton").on('click', function() {
        $('.blackout').remove();
    });
}

function isAlphaNumeric(str) {
    return /^[a-zA-Z0-9]+$/.test(str);
}

function isValidEmail(email) {
    return /^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/.test(email);
};

export default { redirect, login, register, logout, getToken, getUserId, getCurrentUser, showError, isAlphaNumeric, isValidEmail };