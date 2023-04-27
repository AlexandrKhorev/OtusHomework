const gameListTableElement = document.querySelector(".gameList");
const gameBoardElement = document.querySelector(".gameBoard");
const gameIdMessage = document.querySelector(".textGameId");
const errorMessage = document.querySelector(".errorMessage");

const loginErrorMessage = "Login is required";
const gameIdErrorMessage = "GameId is required";

function visibleBoard(isVisible) {
    // Если игрок создал игру, скрываем для него список игр и показываем игровое поле.
    // Когда игра закончилась (определился победитель) - скрывается игровое поле и показывается список игр.

    if (isVisible) {
        gameListTableElement.style.visibility = "hidden";
        gameBoardElement.style.visibility = "visible";
    } else {
        gameListTableElement.style.visibility = "visible";
        gameBoardElement.style.visibility = "hidden";
    }
}


function drawSmallGame(gameId, login1, login2, board) {
    // Берем элемент -> таблицу со списком игр
    // Вставляем строку в конец таблицы.
    // Вставляем 4 ячейки и заполняем значениями
    // (последнее значение - маленькое поле, дублирующее поле игры)

    const list2 = document.querySelectorAll(".gameFromList")[0];
    list2.forEach(v=> {
        console.log(v);
        v.get
        if (v.textContent === gameId){

        }
    });
    console.log(list2);
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
    // Создаем div элемент с классом = smallGameBoard и id = gameId
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

function visibleError(id, isVisible){


    if (isVisible){
        errorMessage.style.visibility = "visible";
        if (id === "login"){
            errorMessage.textContent = loginErrorMessage;
        } else if (id === "gameId") {
            errorMessage.textContent = gameIdErrorMessage;
        }
    } else {
        errorMessage.style.visibility = "hidden";
        errorMessage.textContent = "";
    }
}

function visibleGameId(gameId, isVisible){
    if (isVisible){
        gameIdMessage.style.visibility = "visible";
        gameIdMessage.textContent = gameId;
    } else {
        gameIdMessage.style.visibility = "hidden";
        gameIdMessage.textContent = "";
    }
}