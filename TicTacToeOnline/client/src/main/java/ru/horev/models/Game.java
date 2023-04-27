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
    private TicTacToeValue winner;
    private final TicTacToe gameCore;


    public Game(Player player1) {
        this.gameId = UUID.randomUUID().toString().toUpperCase();
        this.player1 = player1;
        this.gameCore = new TicTacToe(3);
    }

    @Override
    public String toString() {
        return "Game{" +
                "gameId='" + gameId + '\'' +
                ", player1=" + player1 +
                ", player2=" + player2 +
                ", winner=" + winner +
                ", gameCore=" + gameCore +
                '}';
    }

    public void updateBoard(Cell cell) {
        checkPlayer2();
        gameCore.updateBoard(cell.xCoordinate(), cell.yCoordinate(), cell.playerType());
    }

    public boolean checkWinner(){
        TicTacToeValue winner = gameCore.checkWinner();
        if (winner != null){
            this.winner = winner;
            return true;
        }
        return false;
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

    public TicTacToeValue getWinner() {
        return winner;
    }

    public Object getBoard() {
        return gameCore.getBoard();
    }

    public void setPlayer2(Player player2) {
        this.player2 = player2;
    }

    public void setWinner(TicTacToeValue winner) {
        this.winner = winner;
    }
}