package ru.horev;

import ru.horev.exceptions.TicTacToeException;

import java.util.ArrayList;
import java.util.List;

public class TicTacToe {
    private final int size;  // Размер игрового поля
    private String lastPlayerType;  //  Тип игрока, ходившего последним
    private final List<List<TicTacToeValue>> board = new ArrayList<>();  // Двумерный список значений игрового поля

    private static final int[][][] winnerIds = {
            // По горизонтали
            {{0, 0}, {1, 0}, {2, 0}},
            {{0, 1}, {1, 1}, {2, 1}},
            {{0, 2}, {1, 2}, {2, 2}},
            // По вертикали
            {{0, 0}, {0, 1}, {0, 2}},
            {{1, 0}, {1, 1}, {1, 2}},
            {{2, 0}, {2, 1}, {2, 2}},
            // По диагонали
            {{0, 0}, {1, 1}, {2, 2}},
            {{0, 2}, {1, 1}, {2, 0}},
    };

    public TicTacToe(int size) {
        this.size = size;
        this.initFields();
    }

    public void updateBoard(int xCoordinate, int yCoordinate, String playerType) {
        // Первым ходит Крестик. Если lastPlayerType == null, значит никто еще не ходил.
        if (lastPlayerType == null && playerType.equals("O")) {
            throw new TicTacToeException("The player with the CROSS goes first");
        }
        // Игрок не может сходить два раза подряд.
        if (playerType.equals(lastPlayerType)) {
            throw new TicTacToeException("Player " + playerType + " moves a second time");
        }
        // Проверяем пустая ли ячейка
        if (board.get(yCoordinate).get(xCoordinate) != TicTacToeValue.NULL) {
            throw new TicTacToeException("The cell already has a value");
        }
        // Обновляем значение, кто ходил последним и обновляем значение игрового поля
        lastPlayerType = playerType;
        board.get(yCoordinate).set(xCoordinate, TicTacToeValue.getByValue(playerType));
    }

    public TicTacToeValue checkWinner() {
        for (int[][] ids : winnerIds) {
            TicTacToeValue value = board.get(ids[0][0]).get(ids[0][1]);
            if (value != board.get(ids[1][0]).get(ids[1][1])) {
                continue;
            }
            if (value != board.get(ids[2][0]).get(ids[2][1])) {
                continue;
            }
            if (value != TicTacToeValue.NULL) {
                return value;
            }
        }
        return null;
    }

    public List<List<TicTacToeValue>> getBoard() {
        return board;
    }

    private void initFields() {
        // Инициализируем поле нулевыми значениями
        for (int i = 0; i < size; i++) {
            board.add(new ArrayList<>());
            for (int j = 0; j < size; j++) {
                board.get(i).add(TicTacToeValue.NULL);
            }
        }
    }
}

