package ru.horev;

import ru.horev.exceptions.InvalidValueException;

public enum TicTacToeValue {
    CROSS("X"),
    ZERO("O"),
    NULL("");

    private final String value;

    TicTacToeValue(String value) {
        this.value = value;
    }

    public static TicTacToeValue getByValue(String value){
        for (TicTacToeValue element: TicTacToeValue.values()){
            if (element.value.equals(value)){
                return element;
            }
        }
        throw new InvalidValueException("TicTacToe with value " + value + " not found");
    }
}
