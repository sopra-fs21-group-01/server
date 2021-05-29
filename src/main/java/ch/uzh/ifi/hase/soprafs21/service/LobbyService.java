package ch.uzh.ifi.hase.soprafs21.service;


import ch.uzh.ifi.hase.soprafs21.entity.Lobby;
import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.exceptions.DuplicatedUserException;
import ch.uzh.ifi.hase.soprafs21.repository.LobbyRepository;
import ch.uzh.ifi.hase.soprafs21.repository.UserRepository;
import org.apache.catalina.Host;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.Optional;


@Service
@Transactional
@Qualifier("lobbyService")
public class LobbyService {

    private final Logger log = LoggerFactory.getLogger(LobbyService.class);

    private final LobbyRepository lobbyRepository;

    @Autowired
    public LobbyService(@Qualifier("lobbyRepository") LobbyRepository lobbyRepository) {
        this.lobbyRepository = lobbyRepository;
    }


    public List<Lobby> getLobbies() {
        return this.lobbyRepository.findAll();
    }

    // the host is added to the list of players, the gamemode is set to standard
    public Lobby createLobby(Lobby newLobby){

        // add host to the list of players
        List<String> playerList = new ArrayList<String>();

        if (newLobby.getHost() == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Error creating Lobby with this Host!");
        }

        // verify that this lobby doesnt exist
        Optional<Lobby> optionalLobby = lobbyRepository.findById(newLobby.getId());
        if (optionalLobby.isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Lobby with given ID already exists");
        }

        playerList.add(newLobby.getHost());

        newLobby.setPlayerList(playerList);
        newLobby.setGamemode("standard");
        newLobby.setInGame(false);

        newLobby = lobbyRepository.save(newLobby);
        lobbyRepository.flush();

        log.debug("Created a new lobby for Host: {}", newLobby.getHost());

        return newLobby;
    }

    public void playerJoinsLobby(Lobby lobby, String userName){

        // check if player already in lobby
        if (!lobby.getPlayerList().contains(userName)){

        if (lobby.getPlayerList().size() >= 8){
            throw new ResponseStatusException(HttpStatus.METHOD_NOT_ALLOWED,
                    "This Lobby is already full!");
        }

        if (lobby.isInGame()){
            throw new ResponseStatusException(HttpStatus.METHOD_NOT_ALLOWED,
                    "This Lobby is already in a game!");
        }

        List<String> playerList = lobby.getPlayerList();
        playerList.add(userName);
        lobby.setPlayerList(playerList);

        }
    }

    public Lobby getLobbyById(Long lobbyID){
        Lobby lobbyFoundById = null;

        // check if there is a lobby with this ID and return it. If no lobby found, throw exception
        Optional<Lobby> optionalLobby = this.lobbyRepository.findById(lobbyID);

        if (optionalLobby.isPresent()){
            lobbyFoundById = optionalLobby.get();

            log.debug("Found and returned Lobby with ID: {}", lobbyID);
            return lobbyFoundById;
        }
        if (lobbyFoundById == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Lobby with ID " + lobbyID + " was not found");}
        return lobbyFoundById;
    }

    // changes the boolean of IsIngame
    public void changeIsInGameStat(long lobbyId){

        Lobby lobby = getLobbyById(lobbyId);
        lobby.setInGame(!lobby.isInGame());

        lobbyRepository.save(lobby);
        lobbyRepository.flush();
    }

    public Lobby updateLobby(Lobby updatedLobby){

        if (updatedLobby.getHost() == null || updatedLobby.getId() == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Error creating Lobby with this data!");}

        this.lobbyRepository.save(updatedLobby);

        log.debug("Updated Lobby with ID {}", updatedLobby.getId());
        return updatedLobby;
    }

    public void resetLobby(Long updatedLobby){

        Lobby lobbyToReset = getLobbyById(updatedLobby);

        if (lobbyToReset.getHost() == null || lobbyToReset.getId() == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Error resetting lobby!");}

        lobbyToReset.setInGame(false);

        this.lobbyRepository.save(lobbyToReset);
        lobbyRepository.flush();

        log.debug("reset Lobby with ID {}", lobbyToReset.getId());

    }

    public void deleteLobby(Long lobbyId){

        Lobby dummyLobby = getLobbyById(lobbyId);
        lobbyRepository.deleteById(lobbyId);
        lobbyRepository.flush();

        log.debug("Deleted the lobby with ID: {}", lobbyId);
    }


}
