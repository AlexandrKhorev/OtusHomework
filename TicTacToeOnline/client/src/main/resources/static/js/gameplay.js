const enumBoardValues = {"CROSS": "X", "ZERO": "O", "NULL": ""}
const winText = "You are win"
const loseText ="You are lose"

function gameplay(data) {
    fillBoard(data.board);
    checkWinner(data.winner);
}

function checkWinner(winner) {
    if (winner) {
        let endGameElement = document.querySelector(".endGameMessage");
        let endGame = document.createElement("p");

        winner === playerType ? endGame.textContent = winText : endGame.textContent = loseText;
        endGameElement.appendChild(endGame);

        visibleBoard(false);
    }
}

function fillBoard(board) {
    board.forEach((row, idx_row) => {
        row.forEach((col, idx_col) => {
            $(`#${idx_row}_${idx_col}`).text(enumBoardValues[col]);
        })
    })
    document.getElementById("gameBoard")
}


function resetBoard() {
    $(".cell").text(enumBoardValues["NULL"]);
}


function clickCell(cell) {
    let [yCoordinate, xCoordinate] = cell.id.split("_");
    stompClientGameplay.send(`/app/gameplay.${gameId}`, {}, JSON.stringify({
        "xCoordinate": xCoordinate, "yCoordinate": yCoordinate, "playerType": playerValue
    }));
}


function showGame(game) {
    let {gameId, player1, player2, board} = game;
    let loginPlayer1 = player1 && ("login" in player1) ? player1["login"] : "-------"
    let loginPlayer2 = player2 && ("login" in player2) ? player2["login"] : "-------"
    drawSmallGame(gameId, loginPlayer1, loginPlayer2, board)
}

function updateBoardFromList(data) {
    let {board, gameId} = data;
    document.getElementById(gameId).replaceWith(createSmallBoard(gameId, board));
}
