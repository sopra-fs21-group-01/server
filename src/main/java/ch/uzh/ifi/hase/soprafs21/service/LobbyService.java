package ch.uzh.ifi.hase.soprafs21.service;


import ch.uzh.ifi.hase.soprafs21.entity.Lobby;
import ch.uzh.ifi.hase.soprafs21.entity.User;
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
        playerList.add(newLobby.getHost());

        newLobby.setPlayerList(playerList);
        newLobby.setGamemode("standard");

        newLobby = lobbyRepository.save(newLobby);
        lobbyRepository.flush();

        log.debug("Created a new lobby for Host: {}", newLobby.getHost());

        return newLobby;
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
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Lobby with given ID was not found");}
        return lobbyFoundById;
    }

    public Lobby updateLobby(Lobby updatedLobby){
        this.lobbyRepository.save(updatedLobby);

        log.debug("Updated Lobby with ID {}", updatedLobby.getId());
        return updatedLobby;
    }

    public void deleteLobby(Long lobbyId){

        Lobby dummyLobby = getLobbyById(lobbyId);
        lobbyRepository.deleteById(lobbyId);

        log.debug("Deleted the lobby with ID: {}", lobbyId);
    }
}
