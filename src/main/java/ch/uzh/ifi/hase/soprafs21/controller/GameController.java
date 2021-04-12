package ch.uzh.ifi.hase.soprafs21.controller;

import ch.uzh.ifi.hase.soprafs21.entity.Game;
import ch.uzh.ifi.hase.soprafs21.rest.dto.GameGetDTO;
import ch.uzh.ifi.hase.soprafs21.rest.dto.GamePostDTO;
import ch.uzh.ifi.hase.soprafs21.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs21.service.GameService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.persistence.Lob;
import java.util.ArrayList;
import java.util.List;

@RestController
public class GameController {

    private  final GameService gameService;

   GameController(GameService gameService){this.gameService = gameService; }

    // post a gme
    @PostMapping("/game/{id}/kickOff")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public String createGame(@PathVariable(value = "id") Long id, @RequestBody GamePostDTO gamePostDTO){
        // convert API Game to internal representation
        Game input = DTOMapper.INSTANCE.convertGamePostDTOtoEntity(gamePostDTO);

        // create Game
        Game createdGame = gameService.createGame(input);

        // return URL of where to find the User
        String url = "game/"+createdGame.getId()+"/kickOff";

        return url;
    }

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


    // delete the lobby by its ID
    @DeleteMapping("/game/{id}/deletion")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void deleteGame(@PathVariable(value = "id") Long id){

        gameService.deleteGame(id);
    }

    // Put mapping to update all lobby attributes: name, players in playerlist, gamemode
    @PutMapping("/game/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    public void updateGame(@PathVariable(value = "id") Long id, @RequestBody GamePostDTO gamePostDTO) {

        Game gameOfId = gameService.getGameById(id);


        //gameOfId.setPlayerList(gamePostDTO.getPlayerList());


        final Game updatedGame = gameService.updateGame(gameOfId);
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
