const url = 'http://localhost:8080';

const socketRegistryNames = {
    main: "/main",
    gameplay: "/gameplay"
}

const topicNames = {
    gameList: `/topic/gameList`,
    updateSmallGame: '/topic/updateSmallGame',
    createOrJoinGame: login =>  `/topic/joinGame.${login}`,
    gameProgress: gameId => `/topic/game-progress.${gameId}`
}

const appChannelNames = {
    createGame: '/app/createGame',
    connectToGame :gameId => `/app/connectToGame.${gameId}`,
    gameplay : gameId => `/app/gameplay.${gameId}`
}

let stompClientMain = null;
let stompClientGameplay = null;


let gameId;
let playerType;
let playerValue;


function connectSocket() {
    stompClientMain = Stomp.over(new SockJS(socketRegistryNames.main));

    stompClientMain.connect({}, frame => {
        console.log(`connected to main socket: ${frame}`);

        // Получение игры для всех при ее созданиии
        stompClientMain.subscribe(topicNames.gameList, response => showGame(JSON.parse(response.body)));

        // Обновление маленького поля в списке игр
        stompClientMain.subscribe(topicNames.updateSmallGame, response => updateBoardFromList(JSON.parse(response.body)));
    })
}

function disconnectSocketMain() {
    console.log("DISCONNECT MAIN SOCKET")
    stompClientMain.disconnect();
}


function createOrJoinGame(data, numberPlayer) {
    console.log("CREATE GAME " + JSON.stringify(data));
    gameId = data.gameId;

    // Подключаемся к сокету, подписанному на текущую игру и отключаемся от главного сокета.

    data.player2 ? setPlayerType("2") : setPlayerType("1");
    console.log(playerType)
    // p1
    // console.log(JSON.stringify(data));
    // gameId = data.gameId;

    // setPlayerType("1");
    // connectToSocketGameplay(gameId);
    // alert("Your created a game. Game id is: " + data.gameId);
    visibleBoard(true);
    visibleError(null, false);
    visibleGameId(gameId, true);
    // p2
    //         console.log(JSON.stringify(data));
    //         gameId = data.gameId;
    //         setPlayerType("2");
    //         connectToSocket(gameId);
    //         alert("Congrats you're playing with: " + data.player1.login);
    //         visibleBoard(true);
    //         visibleError(null, false);
    //         visibleGameId(gameId, true);
}


function connectToSocketGameplay(gameId) {
    // Создаем новый сокет и подписываемся на сообщения с геймплеем.
    console.log("connecting to the socket gameplay");

    stompClientGameplay = Stomp.over(new SockJS(socketRegistryNames.gameplay));

    stompClientGameplay.connect({}, frame => {
        stompClientGameplay.subscribe(topicNames.gameProgress(gameId), response => {
            let data = JSON.parse(response.body);
            console.log(data);
            gameplay(data);
        })
    })
}

function reconnectToGameplaySocket(data) {
    // Отключаемся от основного сокета и подключаемся к сокету, который слушает канал с геймплеем
    disconnectSocketMain();
    connectToSocketGameplay(data.gameId)
    createOrJoinGame(data);
}


function createGame() {
    // Проверяем введенный логин, если нет - отображается ошибка. Отправляем запрос на создание игры
    let login = document.getElementById("login").value;
    if (login == null || login === '') {
        visibleError("login", true);
        return;
    }

    stompClientMain.subscribe(topicNames.createOrJoinGame(login), response => {
        reconnectToGameplaySocket(JSON.parse(response.body));
    });

    stompClientMain.send(appChannelNames.createGame, {}, JSON.stringify({"login": login}));
}

function connectToGameById() {
    // Проверяем login и gameId и отправляем запрос на подключение к игре
    let login = document.getElementById("login").value;
    if (login == null || login === '') {
        visibleError("login", true);
        return;
    }

    let inputGameId = document.getElementById("gameId").value;
    if (inputGameId == null || inputGameId === '') {
        visibleError("gameId", true);
        return;
    }

    stompClientMain.subscribe(topicNames.createOrJoinGame(login), response => {
        reconnectToGameplaySocket(JSON.parse(response.body));
    });

    stompClientMain.send(appChannelNames.connectToGame(inputGameId), {}, JSON.stringify({"login": login}));
}

function setPlayerType(numberPlayer) {
    if (numberPlayer === "1") {
        playerType = "CROSS";
        playerValue = "X";
    } else if (numberPlayer === "2") {
        playerType = "ZERO";
        playerValue = "O";
    }
}

function createNewGame() {

}

function restart() {

}

