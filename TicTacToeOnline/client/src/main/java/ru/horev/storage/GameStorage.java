package ru.horev.storage;

import ru.horev.models.Game;

import java.util.List;

public interface GameStorage {
    void addGames(Game game);

    Game getGameById(String id);

    List<Game> getListGames();

    void saveGameInArchive(Game game);
}
