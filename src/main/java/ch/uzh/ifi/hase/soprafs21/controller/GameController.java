package ch.uzh.ifi.hase.soprafs21.controller;

import ch.uzh.ifi.hase.soprafs21.entity.Game;
import ch.uzh.ifi.hase.soprafs21.entity.Lobby;
import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.entity.Card;
import ch.uzh.ifi.hase.soprafs21.rest.dto.GameGetDTO;
import ch.uzh.ifi.hase.soprafs21.rest.dto.GamePostDTO;
import ch.uzh.ifi.hase.soprafs21.rest.dto.PlayerMoveDTO;
import ch.uzh.ifi.hase.soprafs21.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs21.service.GameService;
import ch.uzh.ifi.hase.soprafs21.service.LobbyService;
import ch.uzh.ifi.hase.soprafs21.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.persistence.Lob;
import java.util.ArrayList;
import java.util.List;

@RestController
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
        Game input = DTOMapper.INSTANCE.convertGamePostDTOtoEntity(gamePostDTO);

        //get PlayerList from Lobby with same Id like game
        List<String> playerList = lobbyService.getLobbyById(gamePostDTO.getId()).getPlayerList();

        //Set all players from Lobby into Map playerList of Game class.
        for (String player : playerList){
            User user = userService.getUser(player);
            input.setPlayerList(user);
        }

        // set the lobby to "isInGame" and create a Game
        lobbyService.getLobbyById(input.getId()).setInGame(true);
        Game createdGame = gameService.createGame(input);

        // return URL of where to find the User
        String url = "game/"+createdGame.getId()+"/kickOff";

        return url;
    }

    // why is there a second Post here?
    /**
    //how to covert frontend user, card etc. to backend??
    @PostMapping("/game/{id}/play")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public void playCard(@PathVariable(value = "id") Long id, @RequestBody GamePostDTO gamePostDTO){
        // convert API Game to internal representation
        Game input = DTOMapper.INSTANCE.convertGamePostDTOtoEntity(gamePostDTO);

        // create Lobby
        Game createdGame = gameService.createGame(input);

        // return URL of where to find the User
        String url = "game/"+createdGame.getId()+"/kickOff";
    }
    */


    // delete the game by its ID
    @DeleteMapping("/game/{id}/deletion")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void deleteGame(@PathVariable(value = "id") Long id){
        //delete the game and say the lobby is not in a game anymore
        gameService.deleteGame(id);
        lobbyService.getLobbyById(id).setInGame(false);
    }

    // Put mapping
    @PutMapping("/game/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    public void updateGame(@PathVariable(value = "id") Long id, @RequestBody GamePostDTO gamePostDTO) {

        Game gameOfId = gameService.getGameById(id);


        //gameOfId.setPlayerList(gamePostDTO.getPlayerList());


        final Game updatedGame = gameService.updateGame(gameOfId);
    }

    // Put mapping when a player plays a card and this card is put on top of the cardstack
    @PutMapping("/game/{id}/playerTurn")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    public void playCard(@PathVariable(value = "id") Long id, @RequestBody PlayerMoveDTO playerMoveDTO) {
        // TODO api to internal representation fehlt noch auch im DTO Mapper
        Game gameOfId = gameService.getGameById(id);
        User playerOfMove = userService.getUseryById(playerMoveDTO.getPlayerId());

        Card cardToPlay = new Card(playerMoveDTO.getColor(), playerMoveDTO.getValue());

        Game updatedGame = gameService.playCard(gameOfId, playerOfMove, cardToPlay);

    }



    // do we really need to different put methods? (on for kicking a player and one for updateing something else?


    @GetMapping("/game/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public GameGetDTO getGame(@PathVariable(value = "id") Long id){

        // get Lobby from repository
        Game gameOfId = gameService.getGameById(id);

        // convert internal representation of lobby back to API
        return DTOMapper.INSTANCE.convertEntityToGameGetDTO(gameOfId);}

}
