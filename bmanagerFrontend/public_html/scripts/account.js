import general from '/scripts/general.js';
const { redirect, getToken, getUserId, logout, getCurrentUser } = general;

$(document).ready(async function() {
    if (getToken() == null) {
        window.location.href = '/';
    }

    $('#logoutbutton').on('click', function() {
        logout();
        redirect();
    });

    await init();
});

let currentUser = null;

async function init() {
    currentUser = await getCurrentUser();
    $('#outputCurrentLogin').val(currentUser['username']);
    $('#outputCurrentEmail').val(currentUser['email']);

}