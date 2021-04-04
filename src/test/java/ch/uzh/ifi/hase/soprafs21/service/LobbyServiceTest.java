package ch.uzh.ifi.hase.soprafs21.service;

import ch.uzh.ifi.hase.soprafs21.entity.Lobby;
import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.repository.LobbyRepository;
import ch.uzh.ifi.hase.soprafs21.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;

public class LobbyServiceTest {

    @Mock
    private LobbyRepository lobbyRepository;

    @InjectMocks
    private LobbyService lobbyService;

    private Lobby testLobby;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        // given
        testLobby = new Lobby();
        testLobby.setId(1L);
        testLobby.setName("testName");
        testLobby.setPassword("testPassword");
        testLobby.setHost("testHost");



        // when -> any object is being save in the lobbyRepository -> return the dummy testLobby
        Mockito.when(lobbyRepository.save(Mockito.any())).thenReturn(testLobby);
    }

    // test creation of lobby with valid inputs
    @Test
    public void createLobby_validInputs_success(){
        // save the dummy lobby to database and return it
        Lobby createdLobby = lobbyService.createLobby(testLobby);
        String[] testPlayerList = {"testHost"};

        // then test if the data was correctly saved
        Mockito.verify(lobbyRepository, Mockito.times(1)).save(Mockito.any());

        assertEquals(testLobby.getId(), createdLobby.getId());
        assertEquals(testLobby.getName(), createdLobby.getName());
        assertEquals(testLobby.getHost(), createdLobby.getHost());
        assertEquals(testLobby.getPassword(), createdLobby.getPassword());

        // test the data that was automatically created and saved
        assertEquals("standard", createdLobby.getGamemode());
        assertArrayEquals(testPlayerList, createdLobby.getPlayerList());
    }

}