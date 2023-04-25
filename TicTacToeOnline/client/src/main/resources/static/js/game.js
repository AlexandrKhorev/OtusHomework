const boardValues = {
    "CROSS": "X",
    "ZERO": "O",
    "NULL": ".",

}


function checkWinner(data) {
    if (data.winner) {
        alert("You are win");
    }
}


function fillBoard(data) {
    data.board.forEach((row, idx_row) => {
        row.forEach((col, idx_col) => {
            $(`#${idx_row}_${idx_col}`).text(boardValues[col]);
        })
    })
}


function clickCell(cell) {
    let [yCoordinate, xCoordinate] = cell.id.split("_");

    stompClient.send(`/app/gameplay.${gameId}`, {}, JSON.stringify({
        "xCoordinate": xCoordinate, "yCoordinate": yCoordinate, "playerType": playerType
    }));
}
