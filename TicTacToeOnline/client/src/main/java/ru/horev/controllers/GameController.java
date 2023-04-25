package ru.horev.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.horev.controllers.dto.Cell;
import ru.horev.models.Game;
import ru.horev.models.Player;
import ru.horev.services.GameService;

@Controller
public class GameController {
    private static final Logger logger = LoggerFactory.getLogger(GameController.class);

    private static final String GAME_PROGRESS_TEMPLATE = "/topic/game-progress.";

    private final GameService gameService;
    private final SimpMessagingTemplate template;

    public GameController(GameService gameService, SimpMessagingTemplate simpMessagingTemplate){
        this.gameService = gameService;
        this.template = simpMessagingTemplate;
    }

    @PostMapping("/start")
    public ResponseEntity<Game> start(@RequestBody Player player) {
        logger.info("creating game for player1 - {}", player.getLogin());
        Game game = gameService.createGame(player);
        logger.info("started game for player1 - {}, gameId - {}", player.getLogin(), game.getGameId());
        return ResponseEntity.ok(game);
    }

    @PostMapping("/connect")
    public ResponseEntity<Game> connectById(@DestinationVariable String gameId, @RequestBody Player player2) {
        logger.info("connecting to game: {}, player2: {}", gameId, player2.getLogin());
        Game game = gameService.addPlayerToGame(gameId, player2);
        logger.info(game.getGameId());
        return ResponseEntity.ok(game);
    }

    @MessageMapping("/gameplay.{gameId}")
    public void gameplay(@DestinationVariable String gameId, Cell cell) {
        logger.info("gameId: {}, gameplay: {}", gameId, cell.toString());
        Game game = gameService.updateBoard(gameId, cell);
        logger.info(game.getBoard().toString());
        template.convertAndSend(String.format("%s%s", GAME_PROGRESS_TEMPLATE, gameId), game);
    }
}
