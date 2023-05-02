function showGameList(gameList) {
    visibleBoard(false);
    gameList.forEach(game => showGame(game));
}


function appendGameInTable(gameId, login1, login2, board) {
    // Берем элемент -> таблицу со списком игр
    // Вставляем строку в конец таблицы.
    // Вставляем 4 ячейки и заполняем значениями
    // (последнее значение - маленькое поле, дублирующее поле игры)

    let gameExists = false;

    let list = document.querySelector(".gameFromList");
    list.querySelectorAll("tr").forEach(tr => {
        if (tr["id"] === gameId) {
            gameExists = true;
            tr.cells[2].replaceChildren(login2);
            tr.cells[3].replaceChildren(createSmallBoard(gameId, board));
            return;
        }
    });

    if (!gameExists) {
        list = document.getElementById("gameFromList");

        let gameRow = list.insertRow(-1);
        gameRow.setAttribute("id", gameId);

        let gameIdValue = document.createElement("p");
        gameIdValue.textContent = gameId;

        gameRow.insertCell(0).appendChild(gameIdValue);
        gameRow.insertCell(1).append(login1);
        gameRow.insertCell(2).append(login2);
        gameRow.insertCell(3).append(createSmallBoard(gameId, board));
        gameRow.insertCell(4).append(createConnectButton(gameId));
    }
}


function createConnectButton(gameId) {

    let inputElements = document.createElement("div");
    inputElements.className = "inputElements";

    let button = document.createElement("button");
    button.className = "button";
    button.onclick = () => _connectToGameById(parseElement("login"), gameId);
    button.textContent = "connect";

    inputElements.append(button);

    return inputElements;
}

function createSmallBoard(gameId, board) {
    // Создаем div элемент с классом = smallGameBoard и id = gameId, по id потом обновляем элемент с игрой в таблице
    // Заполняем элементами

    let boardValue = document.createElement("div");
    boardValue.setAttribute("class", "smallBox");
    // boardValue.setAttribute("id", gameId)

    board.forEach(row => {
        row.forEach(val => {
            let div = document.createElement("div");
            div.setAttribute("class", "smallCell");
            div.append(enumBoardValues[val]);
            boardValue.append(div);
        })
    })
    return boardValue;
}


function visibleBoard(isVisible) {
    // Если игрок создал игру, скрываем для него список игр и показываем игровое поле.
    // Когда игра закончилась (определился победитель) - скрывается игровое поле и показывается список игр.

    gameListTableElement.style.visibility = isVisible ? HIDDEN : VISIBLE;
    gameListTableElement.style.display = isVisible ? DISPLAY_NONE : DISPLAY_FLEX;

    gameBoardElement.style.visibility = isVisible ? VISIBLE : HIDDEN;
    gameBoardElement.style.display = isVisible ? DISPLAY_FLEX : DISPLAY_NONE;
}


function visibleError(id, isVisible) {
    // Показываем ошибку "Логин обязателен" если логин не введен,
    // не зависимо от gameId и действия (Создание новой игры или подключение по gameId)

    // Если логин введен и подключение по gameId, проверяем введен ли gameId

    if (isVisible) {
        errorMessage.style.visibility = VISIBLE;
        errorMessage.style.display = DISPLAY_FLEX;
        if (id === "login") {
            errorMessage.textContent = loginErrorMessage;
        } else if (id === "gameId") {
            errorMessage.textContent = gameIdErrorMessage;
        }
    } else {
        errorMessage.style.visibility = HIDDEN;
        errorMessage.style.display = DISPLAY_NONE;
        errorMessage.textContent = "";
    }
}


function visibleGameId(gameId, isVisible) {
    gameIdMessage.style.visibility = isVisible ? VISIBLE : HIDDEN;
    gameIdMessage.style.display = isVisible ? DISPLAY_FLEX : DISPLAY_NONE;
    gameIdMessage.textContent = isVisible ? gameId : "";

}