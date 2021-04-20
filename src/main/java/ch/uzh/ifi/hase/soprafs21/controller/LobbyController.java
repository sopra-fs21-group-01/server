package ch.uzh.ifi.hase.soprafs21.controller;

import ch.uzh.ifi.hase.soprafs21.entity.Lobby;
import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.rest.dto.LobbyGetDTO;
import ch.uzh.ifi.hase.soprafs21.rest.dto.LobbyPostDTO;
import ch.uzh.ifi.hase.soprafs21.rest.dto.UserGetDTO;
import ch.uzh.ifi.hase.soprafs21.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs21.service.LobbyService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.persistence.Lob;
import java.util.ArrayList;
import java.util.List;

@RestController
public class LobbyController {

    private  final LobbyService lobbyService;

    LobbyController(LobbyService lobbyService){this.lobbyService = lobbyService; }

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

    // Put mapping to for joining a Lobby
    @PutMapping("/lobbies/{id}/joinedLobbies")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    public void updateLobby(@PathVariable(value = "id") Long id, @RequestBody String userName) {

        Lobby lobbyOfId = lobbyService.getLobbyById(id);

        // lobbyOfId.setName(updatedLobbyDetails.getName());
        List<String> playerList = lobbyOfId.getPlayerList();
        playerList.add(userName);
        lobbyOfId.setPlayerList(playerList);


        final Lobby updatedLobby = lobbyService.updateLobby(lobbyOfId);
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

    // get a single lobbie
    @GetMapping("/lobbies/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public LobbyGetDTO getSingleLobby(@PathVariable(value = "id") Long id){

        // get Lobby from repository
        Lobby lobbyOfId = lobbyService.getLobbyById(id);

        // convert internal representation of lobby back to API
        return DTOMapper.INSTANCE.convertEntityToLobbyGetDTO(lobbyOfId);}

}
