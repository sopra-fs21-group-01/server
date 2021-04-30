package ch.uzh.ifi.hase.soprafs21.service;

import ch.uzh.ifi.hase.soprafs21.entity.Lobby;

import ch.uzh.ifi.hase.soprafs21.repository.LobbyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

public class LobbyServiceTest {

    @Mock
    private LobbyRepository lobbyRepository;

    @InjectMocks
    private LobbyService lobbyService;

    @Mock
    private Lobby testLobby;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        // given
        testLobby = new Lobby();
        testLobby.setId(1L);
       // testLobby.setName("testName");
       // testLobby.setPassword("testPassword");
        testLobby.setHost("testHost");



        // when -> any object is being save in the lobbyRepository -> return the dummy testLobby
        Mockito.when(lobbyRepository.save(Mockito.any())).thenReturn(testLobby);

    }

    // test creation of lobby with valid inputs
    @Test
    public void createLobby_validInputs_success(){
        // save the dummy lobby to database and return it
        Lobby createdLobby = lobbyService.createLobby(testLobby);
        List<String> testPlayerList = new ArrayList<String>();
        testPlayerList.add("testHost");

        // then test if the data was correctly saved
        Mockito.verify(lobbyRepository, Mockito.times(1)).save(Mockito.any());

        assertEquals(testLobby.getId(), createdLobby.getId());

        assertEquals(testLobby.getHost(), createdLobby.getHost());

        // test the data that was automatically created and saved
        assertEquals("standard", createdLobby.getGamemode());
        assertEquals(testPlayerList, createdLobby.getPlayerList());

    }

    // test for wrong host
    @Test
    public void createLobby_InvalidHostname_exception(){
        testLobby.setHost(null);
        assertThrows(ResponseStatusException.class, () -> lobbyService.createLobby(testLobby));
    }

    @Test
    public void playerJoinsLobbyTest_succesfully(){
        Lobby createdLobby = lobbyService.createLobby(testLobby);

        lobbyService.playerJoinsLobby(createdLobby, "nextPlayer");

        // test size and name of player added
        assertEquals(createdLobby.getPlayerList().size(), 2);
        assertEquals(createdLobby.getPlayerList().get(1), "nextPlayer");
    }

    @Test
    public void playerJoinsLobbyTest_LobbyIsAlreadyInGame(){
        Lobby createdLobby = lobbyService.createLobby(testLobby);
        createdLobby.setInGame(true);

        assertThrows(ResponseStatusException.class, () ->  lobbyService.playerJoinsLobby(createdLobby, "nextPlayer"));
    }


    @Test
    public void updateLobbyTest_valid(){
       Lobby createdLobby = lobbyService.createLobby(testLobby);

       Lobby updatedLobby = new Lobby();
       updatedLobby.setId(1L);
       updatedLobby.setHost("newHost");

       createdLobby = lobbyService.updateLobby(updatedLobby);

        assertSame(createdLobby.getId(), updatedLobby.getId());
        assertSame("newHost", createdLobby.getHost());
    }

    @Test
    public void updateLobbyTest_Invalid_throwsException(){
        Lobby createdLobby = lobbyService.createLobby(testLobby);

        Lobby updatedLobby = new Lobby();
        updatedLobby.setId(1L);
        updatedLobby.setHost(null);

        assertThrows(ResponseStatusException.class, () -> lobbyService.updateLobby(updatedLobby));
    }

    }


