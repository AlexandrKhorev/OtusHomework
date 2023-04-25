const url = 'http://localhost:8080';

let stompClient = null;
let gameId;
let playerType;


function connectToSocket(gameId) {
    console.log("connecting to the game");
    stompClient = Stomp.over(new SockJS("/gameplay"));
    stompClient.connect({}, frame => {
        console.log(`connected to game: ${gameId},  frame: ${frame}`);
        stompClient.subscribe(`/topic/game-progress.${gameId}`, response => {
            let data = JSON.parse(response.body);
            console.log(data);
            fillBoard(data);
            checkWinner(data);
        })
    })
}

function createGame() {
    // Проверяем введенный логин, если нет - ошибка
    let login = document.getElementById("login").value;
    if (login == null || login === '') {
        alert("Please enter login");
    } else {
        console.log(JSON.stringify({"login": login}));
        // Отправляем запрос на /start, инициализируем подключение к сокету.
        $.ajax({
            url: url + "/start",
            type: 'POST',
            contentType: "application/json",
            data: JSON.stringify({"login": login}),

            // В случае успешного ответа, инициализируем gameId, заполняем поле стандартными значениями из ответа. (ставим флаг gameOn в True)
            success: data => {
                console.log(JSON.stringify(data));
                gameId = data.gameId;
                playerType = 'X';
                fillBoard(data);
                connectToSocket(gameId);
                alert("Your created a game. Game id is: " + data.gameId);
                gameOn = true;
                viewDiv();
            },

            error: error => {
                console.log(error);
            }
        })
    }
}

function connectToGameById() {
    let login = document.getElementById("login").value;
    if (login == null || login === '') {
        alert("Please enter login");
    } else {
        let inputGameId = document.getElementById("gameId").value;
        if (inputGameId == null || inputGameId === '') {
            alert("Please enter gameId");
        } else {
            $.ajax({
                url: url + "/connect?gameId=" + inputGameId,
                type: 'POST',
                contentType: "application/json",
                data: JSON.stringify({"login": login}),
                success: data => {
                    console.log(JSON.stringify(data));
                    gameId = data.gameId;
                    playerType = 'O';
                    fillBoard(data);
                    connectToSocket(gameId);
                    alert("Congrats you're playing with: " + data.player1.login);
                },
                error: error => {
                    console.log(error);
                }
            })
        }
    }
}

function viewDiv(){
    document.getElementById("gameBoard").style.display = "block";
}