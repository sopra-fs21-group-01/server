package ch.uzh.ifi.hase.soprafs21.service;

import ch.uzh.ifi.hase.soprafs21.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs21.entity.Lobby;
import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.exceptions.DuplicatedUserException;
import ch.uzh.ifi.hase.soprafs21.repository.GameRepository;
import ch.uzh.ifi.hase.soprafs21.repository.LobbyRepository;
import ch.uzh.ifi.hase.soprafs21.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.server.ResponseStatusException;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@WebAppConfiguration
@SpringBootTest
public class LobbyServiceIntegrationTest {

    @Qualifier("lobbyRepository")
    @Autowired
    private LobbyRepository lobbyRepository;

    @Autowired
    private LobbyService lobbyService;

    @BeforeEach
    public void setup() {
        lobbyRepository.deleteAll();
    }

    // test for valid Lobby creation
    @Test
    public void createLobby_validInputs_success() {
        // given
        assertThrows(NoSuchElementException.class, () -> {lobbyRepository.findById(1L).get();});

        Lobby testLobby = new Lobby();
        testLobby.setHost("testHost");
        testLobby.setId(1L);

        Lobby createdLobby = lobbyService.createLobby(testLobby);


        // then
        assertEquals(testLobby.getId(), testLobby.getId());
        assertEquals(testLobby.getHost(), testLobby.getHost());
        assertEquals(testLobby.getPlayerList(), testLobby.getPlayerList());
        assertEquals(testLobby.getGamemode(), testLobby.getGamemode());
        assertEquals(testLobby.isInGame(), testLobby.isInGame());

    }
    // since a lobby is always created with the corresponding Host ID, it should never get duplicated ID.
    // However if in the same session a Host gets deleted from the databank but the Lobby remains, it might happen
    // that two lobbies with same ID are tried to initiate. This might give Problems later when initializing the game.
    @Test
    public void createLobby_duplicateID_throwsException() {
        assertThrows(NoSuchElementException.class, () -> {lobbyRepository.findById(1L).get();});


        Lobby testLobby = new Lobby();
        testLobby.setHost("testHost");
        testLobby.setId(1L);
        lobbyService.createLobby(testLobby);

        // attempt to create second user with same username
        Lobby testLobby2 = new Lobby();
        testLobby.setHost("testHost2");
        testLobby.setId(1L);

        // check that an error is thrown
        assertThrows(ResponseStatusException.class, () ->   lobbyService.createLobby(testLobby2));
    }
}
