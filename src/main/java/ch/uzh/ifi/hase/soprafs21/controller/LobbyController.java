package ch.uzh.ifi.hase.soprafs21.controller;

import ch.uzh.ifi.hase.soprafs21.entity.Game;
import ch.uzh.ifi.hase.soprafs21.entity.Lobby;
import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.rest.dto.LobbyGetDTO;
import ch.uzh.ifi.hase.soprafs21.rest.dto.LobbyPostDTO;
import ch.uzh.ifi.hase.soprafs21.rest.dto.PlayByNamePutDTO;
import ch.uzh.ifi.hase.soprafs21.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs21.service.GameService;
import ch.uzh.ifi.hase.soprafs21.service.LobbyService;
import ch.uzh.ifi.hase.soprafs21.service.UserService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@RestController
@Qualifier("lobbyController")
public class LobbyController {

    private  final LobbyService lobbyService;
    private  final UserService userService;
    private  final GameService gameService;

    LobbyController(LobbyService lobbyService, UserService userService, GameService gameService){this.lobbyService = lobbyService; this.userService = userService; this.gameService = gameService;}

    // post a lobby
    @PostMapping("/lobbies")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public String createLobby(@RequestBody LobbyPostDTO lobbyPostDTO){
        // convert API Lobby to internal representation
        Lobby userInput = DTOMapper.INSTANCE.convertLobbyPostDTOtoEntity(lobbyPostDTO);

        // create Lobby
        Lobby createdLobby = lobbyService.createLobby(userInput);

        // return URL of where to find the User
        String url = "lobbies/"+createdLobby.getId();

        return url;
    }

    // delete the lobby by its ID
    @DeleteMapping("/lobbies/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void deleteLobby(@PathVariable(value = "id") Long id){

        lobbyService.deleteLobby(id);
    }

    // Put mapping to update all lobby attributes: name, players in playerlist, gamemode
    @PutMapping("/lobbies/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    public void updateLobby(@PathVariable(value = "id") Long id, @RequestBody LobbyPostDTO updatedLobbyDetails) {

        Lobby lobbyOfId = lobbyService.getLobbyById(id);

       // lobbyOfId.setName(updatedLobbyDetails.getName());
        lobbyOfId.setPlayerList(updatedLobbyDetails.getPlayerList());
        lobbyOfId.setGamemode(updatedLobbyDetails.getGamemode());

        final Lobby updatedLobby = lobbyService.updateLobby(lobbyOfId);
    }

    @PutMapping("/lobbies/{id}/resets")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    public void setIsInGameBoolToFalse(@PathVariable(value = "id") Long id) {
        lobbyService.resetLobby(id);
        Game game = gameService.getGameById(id);
        if (game.getPlayerList().size()==1){
            gameService.addWinner(game, game.getPlayerList().get(0) );
       }

    }

    // Put mapping to for joining a Lobby
    @PutMapping("/lobbies/{id}/joinedLobbies")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    public void updateLobby(@PathVariable(value = "id") Long id, @RequestBody PlayByNamePutDTO playerByNamePutDTO) {

        Lobby lobbyOfId = lobbyService.getLobbyById(id);
        User userToJoin = userService.getUser(playerByNamePutDTO.getPlayerName());

        lobbyService.playerJoinsLobby(lobbyOfId, userToJoin.getUsername());

    }

    // get all the lobbies
    @GetMapping("/lobbies")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<LobbyGetDTO> getAllLobbies() {
        // fetch all users in the internal representation
        List<Lobby> lobbies = lobbyService.getLobbies();
        List<LobbyGetDTO> lobbyGetDTOs = new ArrayList<>();

        // convert each user to the API representation
        for (Lobby lobby : lobbies) {
            lobbyGetDTOs.add(DTOMapper.INSTANCE.convertEntityToLobbyGetDTO(lobby));
        }
        return lobbyGetDTOs;
    }

    // get a single lobby
    @GetMapping("/lobbies/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public LobbyGetDTO getSingleLobby(@PathVariable(value = "id") Long id){

try{
    Lobby lobbyOfId = lobbyService.getLobbyById(id);

    LobbyGetDTO lobbyGetDTO = DTOMapper.INSTANCE.convertEntityToLobbyGetDTO(lobbyOfId);
    lobbyGetDTO.setWinnerList(gameService.getWinner(id));
    return lobbyGetDTO;
}catch (Exception e){
    return null;
}
    }

}
