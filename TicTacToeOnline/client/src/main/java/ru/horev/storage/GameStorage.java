package ru.horev.storage;

import ru.horev.datastore.models.Game;

import java.util.List;
import java.util.UUID;

public interface GameStorage {
    void addGames(Game game);

    Game getGameById(UUID gameId);

    List<Game> getListGames();

    void moveGameInArchive(Game game);
}
