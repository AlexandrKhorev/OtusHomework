package ru.horev.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.horev.datastore.models.Game;
import ru.horev.datastore.models.Player;
import ru.horev.exceptions.GameException;
import ru.horev.models.Cell;
import ru.horev.models.GameDto;
import ru.horev.services.GameService;

import java.util.UUID;

@Controller
public class GameController {
    private static final Logger logger = LoggerFactory.getLogger(GameController.class);

    private static final String GAME_PROGRESS_TEMPLATE = "/topic/game-progress.";
    private static final String TOPIC_JOIN_GAME = "/topic/joinGame.";
    private static final String TOPIC_GAME_LIST = "/topic/gameList";
    private static final String TOPIC_UPDATE_GAME_LIST = "/topic/updateSmallGame";

    private final GameService gameService;
    private final SimpMessagingTemplate template;

    public GameController(GameService gameService, SimpMessagingTemplate simpMessagingTemplate) {
        this.gameService = gameService;
        this.template = simpMessagingTemplate;
    }

    @MessageMapping("/createGame")
    public void createGame(Player player1) {
        // Создаем игру и отправляем:
        //      в топик TOPIC_GAME_LIST для отображения в списке игр
        //      в топик TOPIC_JOIN_GAME для игрока
        GameDto gameDto = convertGameToDto(gameService.createGame(player1));
        logger.info("started game for player1 - {}, gameId - {}", player1.getLogin(), gameDto.gameId());

        template.convertAndSend(TOPIC_GAME_LIST, gameDto);
        template.convertAndSend(String.format("%s%s", TOPIC_JOIN_GAME, player1.getLogin()), gameDto);
    }

    @MessageMapping("/connectToGame.{gameId}")
    public void connectToGameById(@DestinationVariable UUID gameId, Player player2) {
        // Добавляем второго игрока к игре, отправляем в список и для игрока
        GameDto gameDto = convertGameToDto(gameService.addPlayerToGame(gameId, player2));
        logger.info("connected to game: {}, player2: {}", gameDto.gameId(), player2.getLogin());

        template.convertAndSend(TOPIC_GAME_LIST, gameDto);
        template.convertAndSend(String.format("%s%s", TOPIC_JOIN_GAME, player2.getLogin()), gameDto);
    }

    @MessageMapping("/gameplay.{gameId}")
    public void gameplay(@DestinationVariable UUID gameId, Cell cell) {
        logger.info("gameId: {}, gameplay: {}", gameId, cell.toString());
        // Обновляем поле и возвращаем игру игрокам и в топик со списком игр.
        GameDto gameDto = convertGameToDto(gameService.updateBoard(gameId, cell));

        template.convertAndSend(String.format("%s%s", GAME_PROGRESS_TEMPLATE, gameId), gameDto);
        template.convertAndSend(TOPIC_UPDATE_GAME_LIST, gameDto);
    }

    private GameDto convertGameToDto(Game game) {
        return new GameDto(
                game.getGameId(),
                game.getPlayer1(),
                game.getPlayer2(),
                game.getGameCore().getBoard(),
                game.getWinner()
        );
    }

    @ExceptionHandler(GameException.class)
    public void handleException(Throwable t) {
        logger.info(t.toString());
    }
}
