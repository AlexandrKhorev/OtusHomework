package ru.horev.services;

import org.springframework.stereotype.Service;
import ru.horev.TicTacToeValue;
import ru.horev.datastore.models.Game;
import ru.horev.datastore.models.Player;
import ru.horev.exceptions.GameException;
import ru.horev.models.Cell;
import ru.horev.storage.GameStorage;

import java.util.List;
import java.util.UUID;

@Service
public class GameServiceImpl implements GameService {

    private final GameStorage gameStorage;

    public GameServiceImpl(GameStorage gameStorage) {
        this.gameStorage = gameStorage;
    }

    @Override
    public Game createGame(Player player1) {
        Game game = new Game(player1);
        gameStorage.addGames(game);
        return game;
    }

    @Override
    public Game addPlayerToGame(UUID gameId, Player player2) {
        Game game = gameStorage.getGameById(gameId);
        game.setPlayer2(player2);
        return game;
    }

    @Override
    public Game updateBoard(UUID gameId, Cell cell) {
        // Получаем игру
        Game game = gameStorage.getGameById(gameId);
        updateBoard(game, cell);
        return game;
    }

    @Override
    public List<Game> getListGames() {
        return gameStorage.getListGames();
    }

    private void checkPlayer2(Game game) {
        if (game.getPlayer2() == null) {
            throw new GameException("Waiting second player");
        }
    }


    private void updateBoard(Game game, Cell cell) {
        // Проверяем наличие второго игрока
        checkPlayer2(game);

        // Обновляем игровую доску
        game.getGameCore().updateBoard(cell.xCoordinate(), cell.yCoordinate(), cell.playerType());

        // Проверяем победителя
        checkWinner(game);
    }

    private void checkWinner(Game game) {
        TicTacToeValue winner = game.getGameCore().checkWinner();
        if (winner != null) {
            // Если есть победитель, сохраняем его в объекте игры и перемещаем в архив
            game.setWinner(winner);
            gameStorage.moveGameInArchive(game);
        }
    }
}
