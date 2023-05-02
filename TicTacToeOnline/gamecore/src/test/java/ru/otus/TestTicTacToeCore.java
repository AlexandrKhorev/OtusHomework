package ru.otus;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.horev.TicTacToe;
import ru.horev.TicTacToeValue;
import ru.horev.exceptions.TicTacToeException;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("TicTacToeCore:")
public class TestTicTacToeCore {

    private TicTacToe core;
    private static final int SIZE = 3;

    @BeforeEach
    void setUp() {
        core = new TicTacToe(SIZE);
    }

    @DisplayName("should be a 3x3 field filled with null values")
    @Test
    void shouldFieldFromNulls() {
        List<List<TicTacToeValue>> testBoard = Collections.nCopies(SIZE, Collections.nCopies(SIZE, TicTacToeValue.NULL));
        assertThat(core.getBoard()).isEqualTo(testBoard);
    }

    @DisplayName("should throw exceptions TicTacToeException")
    @Test
    void shouldThrowExceptions() {
        // Первым ходит O
        assertThrows(TicTacToeException.class, () -> core.updateBoard(0, 0, "O"), "The player with the CROSS goes first");

        // Один и тот же игрок ходит второй раз
        core.updateBoard(0, 0, "X");
        assertThrows(TicTacToeException.class, () -> core.updateBoard(1, 0, "X"), "Player X moves a second time");

        // Игрок ходит в занятую клетку
        core.updateBoard(0, 1, "O");
        assertThrows(TicTacToeException.class, () -> core.updateBoard(0, 1, "X"), "The cell already has a value");
    }

    @DisplayName("should be updated board")
    @Test
    void shouldThrowErrorSecondMove() {
        // |x o x|
        // |- x x|
        // |o o -|
        List<List<TicTacToeValue>> testBoard = List.of(
                List.of(TicTacToeValue.CROSS, TicTacToeValue.ZERO, TicTacToeValue.CROSS),
                List.of(TicTacToeValue.NULL, TicTacToeValue.CROSS, TicTacToeValue.CROSS),
                List.of(TicTacToeValue.ZERO, TicTacToeValue.ZERO, TicTacToeValue.NULL)
        );
        core.updateBoard(0, 0, "X");
        core.updateBoard(1, 0, "O");
        core.updateBoard(2, 0, "X");
        core.updateBoard(0, 2, "O");
        core.updateBoard(1, 1, "X");
        core.updateBoard(1, 2, "O");
        core.updateBoard(2, 1, "X");

        assertThat(core.getBoard()).isEqualTo(testBoard);
    }

    @DisplayName("should be correctly determine the winner")
    @Test
    void shouldCorrectCheckWinner() {
        // По горизонтали
        TicTacToe core1 = addValuesToBoard(new int[][]{{0, 0}, {0, 2}, {1, 0}, {0, 1}, {2, 0}});  // | x x x | o - - | o - -|
        TicTacToe core2 = addValuesToBoard(new int[][]{{0, 0}, {0, 1}, {1, 0}, {1, 1}, {0, 2}, {2, 1}});  // | x x - | o o o | x - -|
        TicTacToe core3 = addValuesToBoard(new int[][]{{0, 2}, {1, 0}, {1, 2}, {1, 1}, {2, 2}});  // | - o - | - o - | x x x |
        assertThat(core1.checkWinner()).isEqualTo(TicTacToeValue.CROSS);
        assertThat(core2.checkWinner()).isEqualTo(TicTacToeValue.ZERO);
        assertThat(core3.checkWinner()).isEqualTo(TicTacToeValue.CROSS);

        // По вертикали
        TicTacToe core4 = addValuesToBoard(new int[][]{{1, 0}, {0, 0}, {2, 0}, {0, 1}, {2, 1}, {0, 2}});  // | o x x | o - x | o - -|
        TicTacToe core5 = addValuesToBoard(new int[][]{{1, 0}, {0, 1}, {1, 1}, {0, 2}, {1, 2}});  // | - x - | o x - | o x -|
        TicTacToe core6 = addValuesToBoard(new int[][]{{1, 0}, {2, 0}, {0, 1}, {2, 1}, {1, 1}, {2, 2}});  // | - x o | x x o | - - o |
        assertThat(core4.checkWinner()).isEqualTo(TicTacToeValue.ZERO);
        assertThat(core5.checkWinner()).isEqualTo(TicTacToeValue.CROSS);
        assertThat(core6.checkWinner()).isEqualTo(TicTacToeValue.ZERO);

        // По диагонали
        TicTacToe core7 = addValuesToBoard(new int[][]{{0, 0}, {2, 0}, {1, 1}, {2, 1}, {2, 2}});  // | x - o | - x o | - - x|
        TicTacToe core8 = addValuesToBoard(new int[][]{{0, 0}, {2, 0}, {1, 0}, {1, 1}, {0, 1}, {0, 2}});  // | x x o | x o - | o - - |
        assertThat(core7.checkWinner()).isEqualTo(TicTacToeValue.CROSS);
        assertThat(core8.checkWinner()).isEqualTo(TicTacToeValue.ZERO);

    }

    private TicTacToe addValuesToBoard(int[][] cells) {
        TicTacToe core = new TicTacToe(SIZE);

        String lastPlayer = "X";
        for (int i = 0; i < cells.length; i++){
            core.updateBoard(cells[i][0], cells[i][1], lastPlayer);
            lastPlayer = lastPlayer.equals("X") ? "O" : "X";
            if (i < cells.length - 1){
                assertThat(core.checkWinner()).isEqualTo(null);
            }
        }
        return core;
    }
}
