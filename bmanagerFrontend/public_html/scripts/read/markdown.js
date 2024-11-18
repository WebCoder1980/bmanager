import general from '/scripts/general.js';
const { redirect, logout, getToken, showError } = general;

const id_column_ind  = 1
const id_column_type = 2
const id_column_name = 3

const name_type_dir = 'Каталог'
const name_type_notebooks = 'Блокнот'

$(document).ready(function() {
    let userId = null

    $('#logoutbutton').on('click', function() {
        logout();
        redirect();
    });

    $('#markdowntext').on('input', function() {
        updatePreview();
    });

    $('#savebutton').on('click', function() {
        saveNotebook();     
    });
    
    
    function init() {
        if (getToken() == null) {
            window.location.href = '/';
        }

        getContent();
    }

    function updatePreview() {
        $('#previewtext').html(marked.parse($('#markdowntext').val()));
    }

    function getContent() {
        let currentURL = new URL(window.location.href);

        const url = `http://10.8.0.1:36001/notebooks/${currentURL.searchParams.get("id")}`;

        fetch(url, {
            method: "GET",
            headers: {
                "Authorization": `Bearer ${getToken()}`,
                "Content-Type": "application/json"
            }
        })
        .then(response => {
            if (!response.ok) {
                console.log(`Error: ${response.status} - ${response.statusText}`);
                return null;
            }
            return response.json()
        })
        .then(data => {
            $("#markdowntext").val(data['content']['content']);
            updatePreview();
        })
        .catch(error => {
            console.error("An error occurred:", error);
        });
    }

    function saveNotebook() {
        let currentURL = new URL(window.location.href);
        const url = `http://10.8.0.1:36001/notebooks/${currentURL.searchParams.get("id")}`;

        const body = {
            content: $("#markdowntext").val()
        }

        fetch(url, {
            method: "PUT",
            headers: {
                "Authorization": `Bearer ${getToken()}`,
                "Content-Type": "application/json"
            },
            body: JSON.stringify(body)
        })
        .then(response => {
            if (!response.ok) {
                console.log(`Error: ${response.status} - ${response.statusText}`);
                return null;
            }
            return response.json()
        })
        .then(data => {
            
        })
        .catch(error => {
            console.error("An error occurred:", error);
        });
    }

    init();
});
