const socketRegistryNames = {
    main: "/main",
    gameplay: "/gameplay"
}

const topicNames = {
    getGameList: `/topic/gameList`,
    addGameInGameList: `/topic/addGameInGameList`,
    updateSmallGame: '/topic/updateSmallGame',
    createOrJoinGame: login => `/topic/joinGame.${login}`,
    gameProgress: gameId => `/topic/game-progress.${gameId}`
}

const appChannelNames = {
    getGameList: '/app/getGameList',
    createGame: '/app/createGame',
    connectToGame: gameId => `/app/connectToGame.${gameId}`,
    gameplay: gameId => `/app/gameplay.${gameId}`
}


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