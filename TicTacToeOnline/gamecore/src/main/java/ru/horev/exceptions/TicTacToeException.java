package ru.horev.exceptions;

public class TicTacToeException extends RuntimeException {

    public TicTacToeException(String message) {
        super(message);
    }

    public TicTacToeException(Exception exception) {
        super(exception);
    }
}
