package ru.horev.models;

import ru.horev.TicTacToeValue;
import ru.horev.datastore.models.Player;

import java.util.List;
import java.util.UUID;

public record GameDto(UUID gameId,
                      Player player1,
                      Player player2,
                      List<List<TicTacToeValue>> board,
                      TicTacToeValue winner) {
}