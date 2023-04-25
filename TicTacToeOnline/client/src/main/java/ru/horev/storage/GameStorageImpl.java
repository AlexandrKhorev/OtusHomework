package ru.horev.storage;

import org.springframework.stereotype.Component;
import ru.horev.exceptions.GameException;
import ru.horev.models.Game;

import java.util.HashMap;
import java.util.Map;

@Component
public class GameStorageImpl implements GameStorage {
    private final Map<String, Game> games = new HashMap<>();

    public GameStorageImpl() {
    }

    @Override
    public void addGames(Game game) {
        games.put(game.getGameId(), game);
    }

    @Override
    public Game getGameById(String id) {
        Game game = games.get(id);
        if (game == null) {
            throw new GameException("Game with gameId: " + id + " not found");
        }
        return games.get(id);
    }
}
