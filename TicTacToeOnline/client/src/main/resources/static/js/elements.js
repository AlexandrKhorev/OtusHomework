const gameListTableElement = document.querySelector(".gameList");
const gameBoardElement = document.querySelector(".gameBoard");
const gameIdMessage = document.querySelector(".textGameId");
const errorMessage = document.querySelector(".errorMessage");

const loginErrorMessage = "Login is required";
const gameIdErrorMessage = "GameId is required";

const VISIBLE = "visible";
const HIDDEN = "hidden";

const DISPLAY_FLEX = "flex";
const DISPLAY_NONE = "none";


function visibleBoard(isVisible) {
    // Если игрок создал игру, скрываем для него список игр и показываем игровое поле.
    // Когда игра закончилась (определился победитель) - скрывается игровое поле и показывается список игр.

    gameListTableElement.style.visibility = isVisible ? HIDDEN : VISIBLE;
    gameListTableElement.style.display = isVisible ? DISPLAY_NONE : DISPLAY_FLEX;

    gameBoardElement.style.visibility = isVisible ? VISIBLE : HIDDEN;
    gameBoardElement.style.display = isVisible ? DISPLAY_FLEX : DISPLAY_NONE;
}


function drawSmallGame(gameId, login1, login2, board) {
    // Берем элемент -> таблицу со списком игр
    // Вставляем строку в конец таблицы.
    // Вставляем 4 ячейки и заполняем значениями
    // (последнее значение - маленькое поле, дублирующее поле игры)

    // const list2 = document.querySelectorAll(".gameFromList")[0];
    // list2.forEach(v => {
    //     console.log(v);
    //     v.get
    //     if (v.textContent === gameId) {
    //
    //     }
    // });
    // console.log(list2);
    // list2.forEach(v => console.log(v));

    const list = document.getElementById("gameFromList");
    let gameRow = list.insertRow(-1);

    let gameIdValue = document.createElement("p");
    gameIdValue.textContent = gameId;

    gameRow.insertCell(0).appendChild(gameIdValue);
    gameRow.insertCell(1).append(login1);
    gameRow.insertCell(2).append(login2);
    gameRow.insertCell(3).append(createSmallBoard(gameId, board));
}

function createSmallBoard(gameId, board) {
    // Создаем div элемент с классом = smallGameBoard и id = gameId, по id потом обновляем элемент с игрой в таблице
    // Заполняем элементами

    let boardValue = document.createElement("div");
    boardValue.setAttribute("class", "smallBox");
    boardValue.setAttribute("id", gameId)

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