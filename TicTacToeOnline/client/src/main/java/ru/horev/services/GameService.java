package ru.horev.services;

import ru.horev.controllers.dto.Cell;
import ru.horev.models.Game;
import ru.horev.models.Player;

import java.util.List;

public interface GameService {
    Game createGame(Player player1);
    Game addPlayerToGame(String gameId, Player player2);
    Game updateBoard(String gameId, Cell cell);
    List<Game> getListGames();
}
