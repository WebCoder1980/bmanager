import general from '/scripts/general.js';
const { redirect, getToken, logout, showError } = general;

const id_column_ind  = 2
const id_column_type = 3
const id_column_name = 4

const name_type_dir = 'Каталог'
const name_type_notebooks = 'Блокнот'

$(document).ready(async function() {
    let userId = null

    let parentDirs = []
    let currentWay = ""

    $("#createdirbutton").on('click', function() {
        createDirModel();
    });

    $("#createnotebookbutton").on('click', function() {
        createNotebookModel();
    });

    $('#updatebutton').on('click', function() {
        fetchData();     
    });

    $('#backbutton').on('click', function() {
        goToParentDirId();     
        fetchData();
    });

    $('#logoutbutton').on('click', function() {
        logout();
        redirect();
    });
    
    
    async function init() {
        if (getToken() == null) {
            window.location.href = '/';
        }

        userId = 2
        
        let rootDir = await getRootDir();
        rootDir['type'] = name_type_dir;

        goToDirId(rootDir);
        
        fetchData();
    }

async function getRootDir() {
    if (localStorage.getItem("rootDirId") == null) {
        let result = await getRootDirRequest();

        localStorage.setItem("rootDirId", result['content']['id']);
        localStorage.setItem("rootDirName", result['content']['name']);
    }

    return { id: localStorage.getItem("rootDirId"), name: localStorage.getItem("rootDirName") };
}

async function getRootDirRequest() {
    let url = 'http://10.8.0.1:36001/dirs/getRoot';

    let response = await fetch(url, {
        method: 'GET',
        headers: {
            'Authorization': `Bearer ${getToken()}`,
            'Content-Type': 'application/json'
        }
    }).then(response => {
        if (!response.ok) {
            showError("Проверьте данных для авторизации!");
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

function createDirModel() {
    $("body").append(`<div class="blackout">
        <div class="popup">
            <h1>Новый каталог</h1>
            <h2>Название:</h2>
            <input type="text" class="popupcontentname">
            <div class="buttons">
                <button class="popupsavebutton button">Сохранить</button>
                <button class="popupcancelbutton button">Отмена</button>
            </div>
        </div>
    </div>`);

    $(".popupsavebutton").on('click', async function() {
        await createDir($('.popupcontentname').val());
        $('.blackout').remove();
        fetchData();
    });

    $(".popupcancelbutton").on('click', function() {
        $('.blackout').remove();
    });
}

function createNotebookModel() {
    $("body").append(`<div class="blackout">
        <div class="popup">
            <h1>Новый блокнот</h2>
            <h2>Название:</h2>
            <input type="text" class="popupcontentname">
            <div class="buttons">
                <button class="popupsavebutton button">Сохранить</button>
                <button class="popupcancelbutton button">Отмена</button>
            </div>
        </div>
    </div>`);

    $(".popupsavebutton").on('click', async function() {
        await createNotebook($('.popupcontentname').val());
        $('.blackout').remove();
        fetchData();
    });

    $(".popupcancelbutton").on('click', function() {
        $('.blackout').remove();
    });
}

async function fetchData() {
    $('#browseritems tbody').empty();

    let dirs = await getDirs();
    let nextRowInd = 1;

    for (let i = 0; i < dirs.length; i++) {
        let item = dirs[i];
        $('#browseritems tbody').append(`
            <tr class="browseritem">
                <td><button class="browseropenbutton button" data-id="${nextRowInd}">Открыть</button></td>
                <td>${item.id}</td>
                <td>${name_type_dir}</td>
                <td>${item.name}</td>
                <td>${item.dateTimeCreated}</td>
                <td>-</td>
                <td><button class="browserdeletebutton button" data-id="${nextRowInd}">Удалить</button></td>
            </tr>
        `);
        nextRowInd++;
    }

    let notebooks = await getNotebooks();
    for (let i = 0; i < notebooks.length; i++) {
        let item = notebooks[i];
        $('#browseritems tbody').append(`
            <tr class="browseritem">
                <td><button class="browseropenbutton button" data-id="${nextRowInd}">Открыть</button></td>
                <td>${item.id}</td>
                <td>${name_type_notebooks}</td>
                <td>${item.name}</td>
                <td>${item.dateTimeCreated}</td>
                <td>${item.dateTimeUpdated}</td>
                <td><button class="browserdeletebutton button" data-id="${nextRowInd}">Удалить</button></td>
            </tr>
        `);
        nextRowInd++;
    }

    $('#browseritems tbody .browseritem .browseropenbutton').on('click', function() {
        let id = $(this).data('id');
        let currentRow = $(`#browseritems tbody .browseritem:nth-child(${id})`);
        browserItemClicked({id: $(currentRow).find(`td:nth-child(${id_column_ind})`).text(), type: currentRow.find(`td:nth-child(${id_column_type})`).text(), name: currentRow.find(`td:nth-child(${id_column_name})`).text()});
    });

    $('#browseritems tbody .browseritem .browserdeletebutton').on('click', async function() {
        let id = $(this).data('id');
        let currentRow = $(`#browseritems tbody .browseritem:nth-child(${id})`);

        if ($(currentRow).find(`td:nth-child(${id_column_type})`).text() == name_type_dir) {
            let dirId = $(currentRow).find(`td:nth-child(${id_column_ind}`).text(); 
            await deleteDir(dirId);
        }
        else {
            let notebookId = $(currentRow).find(`td:nth-child(${id_column_ind}`).text();
            await deleteNotebook(notebookId);
        }
        
        fetchData();
    });
}

async function getDirs() {
        let response = await fetch(`http://10.8.0.1:36001/dirs?parentDirId=${parentDirs.at(-1)['id']}`, {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${getToken()}`,
                'Content-Type': 'application/json'
            }
        });

        if (!response.ok) {
            console.log(`Network response was not ok ${response.status} - ${response.statusText}`);
            return null;
        }

        let requestResult = await response.json();
        requestResult = requestResult['content'];

        let result = []

        for (let i = 0; i < requestResult.length; i++) {
            let item = requestResult[i];
            
            let dateTimeCreated = new Date(item.datetime_created);
            let formattedDateTimeCreated = dateTimeCreated.getFullYear() + '-' +
                ('0' + (dateTimeCreated.getMonth() + 1)).slice(-2) + '-' +
                ('0' + dateTimeCreated.getDate()).slice(-2) + ' ' +
                ('0' + dateTimeCreated.getHours()).slice(-2) + ':' +
                ('0' + dateTimeCreated.getMinutes()).slice(-2) + ':' +
                ('0' + dateTimeCreated.getSeconds()).slice(-2);

            result.push({'id': item.id, 'name': item.name, 'dateTimeCreated': formattedDateTimeCreated});
        }

        return result;
    }

    async function getNotebooks() {
        let response = await fetch(`http://10.8.0.1:36001/notebooks?parentDirId=${parentDirs.at(-1)['id']}`, {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${getToken()}`,
                'Content-Type': 'application/json'
            }
        });

        if (!response.ok) {
            console.log('Network response was not ok');
            return null;
        }

        let requestResult = await response.json();
        requestResult = requestResult['content'];

        let result = []

        for (let i = 0; i < requestResult.length; i++) {
            let item = requestResult[i];
            
            let dateTimeCreated = new Date(item.datetime_created);
            let formattedDateTimeCreated = getFormattedDatetime(dateTimeCreated)

            let dateTimeUpdated = new Date(item.datetime_updated);
            let formattedDateTimeUpdated = getFormattedDatetime(dateTimeUpdated)

            result.push({'id': item.id, 'name': item.name, 'dateTimeCreated': formattedDateTimeCreated, 'dateTimeUpdated': formattedDateTimeUpdated });
        }

        return result;
    }

    function getFormattedDatetime(datetime) {
        return datetime.getFullYear() + '-' +
                ('0' + (datetime.getMonth() + 1)).slice(-2) + '-' +
                ('0' + datetime.getDate()).slice(-2) + ' ' +
                ('0' + datetime.getHours()).slice(-2) + ':' +
                ('0' + datetime.getMinutes()).slice(-2) + ':' +
                ('0' + datetime.getSeconds()).slice(-2);
    }

    function browserItemClicked(item) {
        if (item['type'] == name_type_dir) {
            goToDirId(item);
            fetchData();
        }
        else if (item['type'] == name_type_notebooks) {
            window.open(`/read/markdown?id=${item.id}`, "_blank");
        }
    }

    function goToDirId(newParentDir) {
        parentDirs.push(newParentDir);

        changeCurrentWay();
    }

    function goToParentDirId() {
        if (parentDirs.length == 1) {
            return;
        }
        parentDirs.pop();

        changeCurrentWay();
    }

    function changeCurrentWay() {
        currentWay = '';
        for (let i = 0; i < parentDirs.length - 1; i++) {
            currentWay = currentWay + parentDirs[i]['name'] + ' > ';
        }
        currentWay = currentWay + parentDirs.at(-1)['name'];
        $('#currentway').text(currentWay);
    }

    async function createDir(name) {
        const url = `http://10.8.0.1:36001/dirs`;

        const body = {
            "parentDirId": parentDirs.at(-1)['id'],
            "name": name,
            "userId": userId
        }

        await fetch(url, {
            method: "POST",
            headers: {
                "Authorization": `Bearer ${getToken()}`,
                "Content-Type": "application/json"
            },
            body: JSON.stringify(body)
        })
        .then(async response => {
            if (!response.ok) {
                console.log(`Error: ${response.status} - ${response.statusText}`);
                return null;
            }
            return response.json()
        })
        .then(async data => {
            
        })
        .catch(async error => {
            console.error("An error occurred:", error);
        });
    }

    async function createNotebook(name) {
        const url = `http://10.8.0.1:36001/notebooks`;

        const body = {
            "parentDirId": parentDirs.at(-1)['id'],
            "name": name,
            "userId": userId
        }

        await fetch(url, {
            method: "POST",
            headers: {
                "Authorization": `Bearer ${getToken()}`,
                "Content-Type": "application/json"
            },
            body: JSON.stringify(body)
        })
        .then(async response => {
            if (!response.ok) {
                console.log(`Error: ${response.status} - ${response.statusText}`);
                return null;
            }
            return response.json()
        })
        .then(async data => {
            
        })
        .catch(async error => {
            console.error("An error occurred:", error);
        });
    }

    async function deleteNotebook(id) {
        const url = `http://10.8.0.1:36001/notebooks/${id}`;

        await fetch(url, {
            method: "DELETE",
            headers: {
                "Authorization": `Bearer ${getToken()}`,
                "Content-Type": "application/json"
            }
        })
        .then(async response => {
            if (!response.ok) {
                console.log(`Error: ${response.status} - ${response.statusText}`);
                return null;
            }
            return response.json()
        })
        .then(async data => {
            
        })
        .catch(async error => {
            console.error("An error occurred:", error);
        });
    }

    async function deleteDir(id) {
        const url = `http://10.8.0.1:36001/dirs/${id}`;

        await fetch(url, {
            method: "DELETE",
            headers: {
                "Authorization": `Bearer ${getToken()}`,
                "Content-Type": "application/json"
            }
        })
        .then(async response => {
            if (!response.ok) {
                console.log(`Error: ${response.status} - ${response.statusText}`);
                return null;
            }
            return response.json()
        })
        .then(async data => {
            
        })
        .catch(async error => {
            console.error("An error occurred:", error);
        });
    }


    await init();
});
