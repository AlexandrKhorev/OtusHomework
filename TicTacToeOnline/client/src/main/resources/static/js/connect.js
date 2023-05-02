let stompClientMain = null;
let stompClientGameplay = null;

let gameId;
let playerType;
let playerValue;


function connectSocket() {
    stompClientMain = Stomp.over(new SockJS(socketRegistryNames.main));

    stompClientMain.connect({}, frame => {
        console.log(`connected to main socket: ${frame}`);

        // Получение списка созданных игр при создании соединения с сокетом
        stompClientMain.subscribe(topicNames.getGameList, response => showGameList(JSON.parse(response.body)));

        // Получение игры для всех при ее созданиии
        stompClientMain.subscribe(topicNames.addGameInGameList, response => showGame(JSON.parse(response.body)));

        // Обновление маленького поля в списке игр
        stompClientMain.subscribe(topicNames.updateSmallGame, response => updateBoardFromList(JSON.parse(response.body)));

        // Отправляем запрос на получение списка игр
        stompClientMain.send(appChannelNames.getGameList);
    })

}


function disconnectSocketMain() {
    console.log("disconnect main socket");
    stompClientMain.disconnect();
}


function createOrJoinGame(data) {
    console.log("CREATE GAME " + JSON.stringify(data));
    gameId = data.gameId;

    // Подключаемся к сокету, подписанному на текущую игру и отключаемся от главного сокета.

    data.player2 ? setPlayerType("2") : setPlayerType("1");
    console.log(playerType)
    visibleBoard(true);
    visibleError(null, false);
    visibleGameId(gameId, true);
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


function parseElement(id){
    let element = document.getElementById(id).value;
    if (element === null || element === ''){
        return null
    }
    return element
}

function createGame() {
    // Проверяем введенный логин, если нет - отображается ошибка. Отправляем запрос на создание игры
    let login = parseElement("login");
    if (!login) {
        visibleError("login", true);
        return;
    }

    stompClientMain.subscribe(topicNames.createOrJoinGame(login), response => {
        reconnectToGameplaySocket(JSON.parse(response.body));
    });

    stompClientMain.send(appChannelNames.createGame, {}, JSON.stringify({"login": login}));
    // stompClientMain.send(appChannelNames.login, {}, JSON.stringify({"login": login}));
}


function connectToGameById(){
    _connectToGameById(
        parseElement("login"),
        parseElement("gameId")
    );
}

function _connectToGameById(inputLogin, inputGameId) {

    // Проверяем login и gameId и отправляем запрос на подключение к игре
    if (!inputLogin) {
        visibleError("login", true);
        return;
    }

    if (!inputGameId) {
        visibleError("gameId", true);
        return;
    }

    stompClientMain.subscribe(topicNames.createOrJoinGame(inputLogin), response => {
        reconnectToGameplaySocket(JSON.parse(response.body));
    });

    stompClientMain.send(appChannelNames.connectToGame(inputGameId), {}, JSON.stringify({"login": inputLogin}));
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

