package ru.horev.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.horev.controllers.dto.Cell;
import ru.horev.models.Game;
import ru.horev.models.Player;
import ru.horev.storage.GameStorage;

@Service
public class GameServiceImpl implements GameService {

    @Autowired
    private GameStorage gameStorage;

    @Override
    public Game createGame(Player player1) {
        Game game = new Game(player1);
        gameStorage.addGames(game);
        return game;
    }

    @Override
    public Game addPlayerToGame(String gameId, Player player2) {
        Game game = gameStorage.getGameById(gameId);
        game.setPlayer2(player2);
        return game;
    }

    @Override
    public Game updateBoard(String gameId, Cell cell) {
        Game game = gameStorage.getGameById(gameId);
        game.updateBoard(cell);
        game.checkWinner();
        return game;
    }
}
