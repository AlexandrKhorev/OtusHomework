package ru.horev;

import ru.horev.exceptions.InvalidValueException;

import java.util.stream.Stream;

public enum TicTacToeValue {
    CROSS("X"),
    ZERO("O"),
    NULL("");

    private final String value;

    TicTacToeValue(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static TicTacToeValue getByValue(String value) {
        return Stream.of(TicTacToeValue.values())
                .filter(v -> v.getValue().equals(value))
                .findFirst()
                .orElseThrow(() -> new InvalidValueException("TicTacToe with value " + value + " not found"));
    }
}
