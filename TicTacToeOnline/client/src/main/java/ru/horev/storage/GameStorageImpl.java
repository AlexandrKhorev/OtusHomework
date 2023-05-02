package ru.horev.storage;

import org.springframework.stereotype.Component;
import ru.horev.datastore.models.Game;
import ru.horev.datastore.repositories.GameRepository;
import ru.horev.exceptions.GameException;

import java.util.*;

@Component
public class GameStorageImpl implements GameStorage {
    private final Map<UUID, Game> games = new HashMap<>();

    private final GameRepository gameRepository;

    public GameStorageImpl(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    @Override
    public void addGames(Game game) {
        games.put(game.getGameId(), game);
    }

    @Override
    public Game getGameById(UUID gameId) {
        Game game = games.get(gameId);
        if (game == null) {
            throw new GameException("Game with gameId: " + gameId + " not found");
        }
        return games.get(gameId);
    }

    @Override
    public List<Game> getListGames() {
        return new ArrayList<>(games.values());
    }

    @Override
    public void moveGameInArchive(Game game) {
        games.remove(game.getGameId());
        gameRepository.save(game);
    }
}
