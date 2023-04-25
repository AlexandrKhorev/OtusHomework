package ru.horev.storage;

import ru.horev.models.Game;

public interface GameStorage {
    void addGames(Game game);

    Game getGameById(String id);
}
