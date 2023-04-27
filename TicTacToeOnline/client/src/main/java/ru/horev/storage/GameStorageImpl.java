package ru.horev.storage;

import org.springframework.stereotype.Component;
import ru.horev.exceptions.GameException;
import ru.horev.models.Game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
        Game game = games.get(id.toUpperCase());
        if (game == null) {
            throw new GameException("Game with gameId: " + id + " not found");
        }
        return games.get(id);
    }

    @Override
    public List<Game> getListGames() {
        return new ArrayList<>(games.values());
    }

    @Override
    public void saveGameInArchive(Game game) {
        games.remove(game.getGameId());
//        gamesArchive.put(game.getGameId(), game);
    }
}
