package ru.horev.models;

import ru.horev.TicTacToe;
import ru.horev.TicTacToeValue;
import ru.horev.controllers.dto.Cell;
import ru.horev.exceptions.GameException;

import java.util.UUID;

public class Game {
    private final String gameId;
    private final Player player1;
    private Player player2;
    private Player winner;
    private final TicTacToe gameCore;
//    private List<Player> spectators = new ArrayList<>();

    public Game(Player player1) {
        this.gameId = UUID.randomUUID().toString();
        this.player1 = player1;
        this.gameCore = new TicTacToe(3);
    }

    public void updateBoard(Cell cell) {
        checkPlayer2();
        gameCore.updateBoard(cell.xCoordinate(), cell.yCoordinate(), cell.playerType());
    }

    public void checkWinner(){
        TicTacToeValue winner = gameCore.checkWinner();
        if (winner != null){
            if (winner == TicTacToeValue.CROSS){
                this.winner = player1;
            } else if (winner == TicTacToeValue.ZERO){
                this.winner = player2;
            }
        }
    }

    private void checkPlayer2() {
        if (player2 == null) {
            throw new GameException("Waiting second player");
        }
    }

    public String getGameId() {
        return gameId;
    }

    public Player getPlayer1() {
        return player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    public Player getWinner() {
        return winner;
    }

    public Object getBoard() {
        return gameCore.getBoard();
    }

    public void setPlayer2(Player player2) {
        this.player2 = player2;
    }

    public void setWinner(Player winner) {
        this.winner = winner;
    }
}