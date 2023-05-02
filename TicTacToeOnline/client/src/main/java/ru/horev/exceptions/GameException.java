package ru.horev.exceptions;

public class GameException extends RuntimeException {
    public GameException(String message) {
        super(message);
    }
    public GameException(Exception exception) {
        super(exception);
    }
}
