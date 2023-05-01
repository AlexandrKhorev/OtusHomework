package ru.horev.services;

import ru.horev.datastore.models.Game;
import ru.horev.datastore.models.Player;
import ru.horev.models.Cell;

import java.util.List;
import java.util.UUID;

public interface GameService {
    Game createGame(Player player1);

    Game addPlayerToGame(UUID gameId, Player player2);

    Game updateBoard(UUID gameId, Cell cell);

    List<Game> getListGames();
}
