package ch.uzh.ifi.hase.soprafs21.controller;

import ch.uzh.ifi.hase.soprafs21.entity.*;
import ch.uzh.ifi.hase.soprafs21.rest.dto.*;
import ch.uzh.ifi.hase.soprafs21.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs21.service.GameService;
import ch.uzh.ifi.hase.soprafs21.service.LobbyService;
import ch.uzh.ifi.hase.soprafs21.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;

import java.util.ArrayList;
import java.util.List;
import org.json.*;


@RestController
@Qualifier("gameController")
public class GameController {

    private final GameService gameService;
    private final LobbyService lobbyService;
    private final UserService userService;

    GameController(GameService gameService, LobbyService lobbyService, UserService userService) {
        this.gameService = gameService;
        this.lobbyService = lobbyService;
        this.userService = userService;
    }

    // post a gme
    @PostMapping("/game/{id}/kickOff")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public String createGame(@PathVariable(value = "id") Long id, @RequestBody GamePostDTO gamePostDTO) {
        // convert API Game to internal representation
        Game newGame = DTOMapper.INSTANCE.convertGamePostDTOtoEntity(gamePostDTO);

        // creates and sets the list of userIDs
        newGame.setPlayerList(gameService.convertUserNamesToIds(id));

        Game createdGame = gameService.createGame(newGame);

        // set the lobby to "isInGame" and create a Game
        lobbyService.changeIsInGameStat(id);

        // return URL of where to find the User
        String url = "game/" + createdGame.getId() + "/kickOff";

        return url;
    }

    // delete the game by its ID
    @DeleteMapping("/game/{id}/deletion")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void deleteGame(@PathVariable(value = "id") Long id) {
        //delete the game and say the lobby is not in a game anymore
        gameService.deleteGame(id);
        lobbyService.getLobbyById(id).setInGame(false);
    }

    // Put mapping when a player plays a card and this card is put on top of the cardstack
    @PutMapping("/game/{id}/playerTurn")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    public void playCard(@PathVariable(value = "id") Long id, @RequestBody PlayerMoveDTO playerMoveDTO) {

        PlayerMove playerMove = DTOMapper.INSTANCE.convertPlayerMoveDTOToEntity(playerMoveDTO);
        Game gameOfId = gameService.getGameById(id);
        User playerOfMove = userService.getUseryById(playerMove.getPlayerId());

        Card cardToPlay = new Card(playerMove.getColor(), playerMove.getValue());
        String cardName = cardToPlay.getCardName();

        Game updatedGame = gameService.playCard(gameOfId, playerOfMove, cardName);

        if(playerMoveDTO.getWishedColor() != null){
             gameService.wishColor(playerMoveDTO.getWishedColor(), gameOfId);
        }
        System.out.println("Set the the current color to" + playerMoveDTO.getWishedColor() + "In the controller");
    }

    // Put mapping when a player has finished his hand and is removed from the player pool
    @PutMapping("/game/{id}/wins")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    public void playerFinishesHand(@PathVariable(value = "id") Long id, @RequestBody PlayerMoveDTO playerMoveDTO) {
        PlayerMove playerMove = DTOMapper.INSTANCE.convertPlayerMoveDTOToEntity(playerMoveDTO);
        Game gameOfId = gameService.getGameById(id);
        gameService.removePlayerFromPlayerList(gameOfId, playerMove.getPlayerId());
        }

    @PutMapping("/game/{id}/drawCard")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    public void drawCard(@PathVariable(value = "id") Long id) {
        Game gameOfId = gameService.getGameById(id);

        gameService.cantPlayDrawCard(gameOfId);

    }

    @PutMapping("/game/{id}/sayUno")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    public void sayUno(@PathVariable(value = "id") Long id, @RequestBody PlayerMoveDTO playerMoveDTO) {

        PlayerMove playerMove = DTOMapper.INSTANCE.convertPlayerMoveDTOToEntity(playerMoveDTO);
        Game gameOfId = gameService.getGameById(id);
        Long playerId = playerMove.getPlayerId();

        gameService.sayUno(gameOfId, playerId);
        }

    // GetMapping for receiving the players
    @GetMapping("/game/{id}/kickOff")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public GameGetDTO getGamePlayers(@PathVariable(value = "id") Long id) {

        Game gameOfId = gameService.getGameById(id);

        List<Long> opponentList = gameOfId.getPlayerList();
        List<Integer> opponentHandSize = new ArrayList<>();

        System.out.println("opponentList (before Loop) is: " + opponentList);


        for (int i = 0; i < opponentList.size(); i++) {
            int handSize = gameService.getHandById(opponentList.get(i)).getHandSize();
            opponentHandSize.add(handSize);
        }

        System.out.println("opponentList is: " + opponentList);
        System.out.println("opponentHandSize is: " + opponentHandSize);

        List<String> userNameHandSize = new ArrayList<>();
        for (int i = 0; i < opponentList.size(); i++) {
            String usernameHand = opponentList.get(i).toString() + ","
                    + userService.getUseryById(opponentList.get(i)).getUsername()
                    + "," + opponentHandSize.get(i).toString()
                    + "," +gameService.getHandById(opponentList.get(i)).getUnoStatus();
            userNameHandSize.add(usernameHand);
        }

        System.out.println("userNameHandSize is: " + userNameHandSize);

        GameGetDTO gameGetDTO = DTOMapper.INSTANCE.convertEntityToGameGetDTO(gameOfId);
        gameGetDTO.setOpponentListHands(userNameHandSize);

        return gameGetDTO;
    }

    // GetMapping for getting the current player of a running game, returns single Id
    @GetMapping("/game/{id}/kickOff/currentPlayerIds")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public long getCurrentPlayer(@PathVariable(value = "id") Long id) {

        return gameService.getGameById(id).getCurrentPlayerId();
    }





}
