package ru.horev.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.horev.controllers.dto.Cell;
import ru.horev.models.Game;
import ru.horev.models.Player;
import ru.horev.services.GameService;

import java.util.List;

@Controller
public class GameController {
    private static final Logger logger = LoggerFactory.getLogger(GameController.class);

    private static final String GAME_PROGRESS_TEMPLATE = "/topic/game-progress.";

    private static final String TOPIC_GAME_LIST = "/topic/gameList";
    private static final String TOPIC_UPDATE_GAME_LIST = "/topic/updateSmallGame";
    private static final String TOPIC_JOIN_GAME = "/topic/joinGame.";

    private final GameService gameService;
    private final SimpMessagingTemplate template;

    public GameController(GameService gameService, SimpMessagingTemplate simpMessagingTemplate) {
        this.gameService = gameService;
        this.template = simpMessagingTemplate;
    }

//    @GetMapping("/index")
//    public String getStartedGames(Model model) {
//        List<Game> games = gameService.getListGames();
//        model.addAttribute("games", games);
//        return "index";
//    }

    @MessageMapping("/createGame")
    public void createGame(Player player1) {
        // Создаем игру и отправляем:
        //      в топик TOPIC_GAME_LIST для отображения в списке игр
        //      в топик TOPIC_JOIN_GAME для игрока
        Game game = gameService.createGame(player1);
        logger.info("started game for player1 - {}, gameId - {}", player1.getLogin(), game.getGameId());

        template.convertAndSend(TOPIC_GAME_LIST, game);
        template.convertAndSend(String.format("%s%s", TOPIC_JOIN_GAME, player1.getLogin()), game);
//        template.convertAndSend(TOPIC_JOIN_GAME, game);
    }

    @MessageMapping("/connectToGame.{gameId}")
    public void connectToGameById(@DestinationVariable String gameId, Player player2) {
        // Добавляем второго игрока к игре, отправляем в список и для игрока
        Game game = gameService.addPlayerToGame(gameId, player2);
        logger.info("connected to game: {}, player2: {}", game.getGameId(), player2.getLogin());

        template.convertAndSend(TOPIC_GAME_LIST, game);
        template.convertAndSend(String.format("%s%s", TOPIC_JOIN_GAME, player2.getLogin()), game);
//        template.convertAndSend(TOPIC_JOIN_GAME, game);
    }

    @MessageMapping("/gameplay.{gameId}")
    public void gameplay(@DestinationVariable String gameId, Cell cell) {
        logger.info("gameId: {}, gameplay: {}", gameId, cell.toString());
        Game game = gameService.updateBoard(gameId, cell);

        template.convertAndSend(String.format("%s%s", GAME_PROGRESS_TEMPLATE, gameId), game);
        template.convertAndSend(TOPIC_UPDATE_GAME_LIST, game);
    }
}
