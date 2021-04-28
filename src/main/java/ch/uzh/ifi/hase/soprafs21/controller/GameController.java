package ch.uzh.ifi.hase.soprafs21.controller;

import ch.uzh.ifi.hase.soprafs21.entity.*;
import ch.uzh.ifi.hase.soprafs21.rest.dto.GameGetDTO;
import ch.uzh.ifi.hase.soprafs21.rest.dto.GamePostDTO;
import ch.uzh.ifi.hase.soprafs21.rest.dto.PlayerMoveDTO;
import ch.uzh.ifi.hase.soprafs21.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs21.service.GameService;
import ch.uzh.ifi.hase.soprafs21.service.LobbyService;
import ch.uzh.ifi.hase.soprafs21.service.UserService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.persistence.Lob;
import java.util.ArrayList;
import java.util.List;

@RestController
@Qualifier("gameController")
public class GameController {

    private final GameService gameService;
    private final LobbyService lobbyService;
    private final UserService userService;

   GameController(GameService gameService, LobbyService lobbyService, UserService userService)
   {this.gameService = gameService; this.lobbyService = lobbyService; this.userService = userService;}

    // post a gme
    @PostMapping("/game/{id}/kickOff")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public String createGame(@PathVariable(value = "id") Long id, @RequestBody GamePostDTO gamePostDTO){
        // convert API Game to internal representation
        Game newGame = DTOMapper.INSTANCE.convertGamePostDTOtoEntity(gamePostDTO);

        // creates and sets the list of userIDs
        newGame.setPlayerList(gameService.convertUserNamesToIds(id));

        // set the lobby to "isInGame" and create a Game
        lobbyService.changeIsInGameStat(id);

        Game createdGame = gameService.createGame(newGame);

        // return URL of where to find the User
        String url = "game/"+createdGame.getId()+"/kickOff";

        return url;
    }

    // delete the game by its ID
    @DeleteMapping("/game/{id}/deletion")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void deleteGame(@PathVariable(value = "id") Long id){
        //delete the game and say the lobby is not in a game anymore
        gameService.deleteGame(id);
        lobbyService.getLobbyById(id).setInGame(false);
    }

    /**
    // Put mapping
    @PutMapping("/game/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    public void updateGame(@PathVariable(value = "id") Long id, @RequestBody GamePostDTO gamePostDTO) {

        Game gameOfId = gameService.getGameById(id);


        //gameOfId.setPlayerList(gamePostDTO.getPlayerList());


        final Game updatedGame = gameService.updateGame(gameOfId);
    }
    */

    // Put mapping when a player plays a card and this card is put on top of the cardstack
    @PutMapping("/game/{id}/playerTurn")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    public void playCard(@PathVariable(value = "id") Long id, @RequestBody PlayerMoveDTO playerMoveDTO) {
        PlayerMove playerMove = DTOMapper.INSTANCE.convertPlayerMoveDTOToEntity(playerMoveDTO);
        Game gameOfId = gameService.getGameById(id);
        User playerOfMove = userService.getUserbyId(playerMove.getPlayerId());

        Card cardToPlay = new Card(playerMove.getColor(), playerMove.getValue());
        String cardName = cardToPlay.getCardName();

        Game updatedGame = gameService.playCard(gameOfId, playerOfMove, cardName);

    }



    // do we really need to different put methods? (on for kicking a player and one for updateing something else?

    // GetMapping for receiving the players
    @GetMapping("/game/{id}/kickOff")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public GameGetDTO getGamePlayers(@PathVariable(value = "id") Long id){


       Game gameOfId = gameService.getGameById(id);
       GameGetDTO gameGetDTO = DTOMapper.INSTANCE.convertEntityToGameGetDTO(gameOfId);

       return gameGetDTO;
   }

    // GetMapping for getting the current player of a running game, returns single Id
    @GetMapping("/game/{id}/kickOff/currentPlayerIds")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public long getCurrentPlayer(@PathVariable(value = "id") Long id){

        return gameService.getGameById(id).getCurrentPlayerId();
    }

}
