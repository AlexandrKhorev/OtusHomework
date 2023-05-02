package ru.horev.exceptions;

public class InvalidValueException extends RuntimeException {

    public InvalidValueException(String message) {
        super(message);
    }
    public InvalidValueException(Exception exception) {
        super(exception);
    }
}
