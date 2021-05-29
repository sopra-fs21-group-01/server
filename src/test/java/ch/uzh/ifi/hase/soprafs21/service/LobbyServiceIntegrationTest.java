package ch.uzh.ifi.hase.soprafs21.service;

import ch.uzh.ifi.hase.soprafs21.entity.Lobby;
import ch.uzh.ifi.hase.soprafs21.repository.LobbyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.server.ResponseStatusException;
import java.util.NoSuchElementException;
import java.util.Optional;

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
}
