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
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.when;

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

        List<String> testPlayerList = new ArrayList<String>();
        testPlayerList.add("testHost");
        testPlayerList.add("Hans");


        testLobby = new Lobby();
        testLobby.setId(1L);
        testLobby.setHost("testHost");
        testLobby.setInGame(false);
        testLobby.setPlayerList(testPlayerList);


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

    // find lobby by Id, succesfull
    @Test
    public void getLobbyById_succesfully_returnsLobby(){

       when(lobbyRepository.findById(Mockito.any())).thenReturn(Optional.of(testLobby));

       Lobby lobbyById = lobbyService.getLobbyById(testLobby.getId());
       Mockito.verify(lobbyRepository, Mockito.times(1)).findById(Mockito.any());
       assertSame(lobbyById, testLobby);

    }

    // find lobby by Id, Unsuccesfull
    @Test
    public void getLobbyById_Unsuccesfully_throwError(){

        when(lobbyRepository.save(Mockito.any())).thenReturn(Optional.of(testLobby));
        assertThrows(ResponseStatusException.class, () -> lobbyService.getLobbyById(testLobby.getId()+1));

    }

    // test  changing the inGame Stat of a lobby
    @Test
    public void changeInGameStat_success(){

        when(lobbyRepository.findById(Mockito.any())).thenReturn(Optional.of(testLobby));

        lobbyService.changeIsInGameStat(1L);

        assertTrue(testLobby.isInGame());
        Mockito.verify(lobbyRepository, Mockito.times(1)).findById(Mockito.any());
        Mockito.verify(lobbyRepository, Mockito.times(1)).save(Mockito.any());
        Mockito.verify(lobbyRepository, Mockito.times(1)).flush();

            }

    // test reset lobby. changes inGame stat to false and save it
    @Test
    public void changeResetLobby_success(){

        testLobby.setInGame(true);
        when(lobbyRepository.findById(Mockito.any())).thenReturn(Optional.of(testLobby));

        lobbyService.resetLobby(1L);

        assertFalse(testLobby.isInGame());

        Mockito.verify(lobbyRepository, Mockito.times(1)).findById(Mockito.any());
        Mockito.verify(lobbyRepository, Mockito.times(1)).save(Mockito.any());
        Mockito.verify(lobbyRepository, Mockito.times(1)).flush();

    }


    // test reset lobby. noHost found, will throw error
    @Test
    public void changeResetLobby_NoSuccess_noHost(){

        testLobby.setHost(null);
        when(lobbyRepository.findById(Mockito.any())).thenReturn(Optional.of(testLobby));

        assertThrows(ResponseStatusException.class, () -> lobbyService.resetLobby(1L));
    }
    // test reset lobby. no ID, lobby not found, will throw error
    @Test
    public void changeResetLobby_NoSuccess_noID(){

        testLobby.setId(null);
        when(lobbyRepository.findById(Mockito.any())).thenReturn(Optional.of(testLobby));

        assertThrows(ResponseStatusException.class, () -> lobbyService.resetLobby(1L));
    }

    // test delete a lobby
    @Test
    public void deleteLobby_success(){

        when(lobbyRepository.findById(Mockito.any())).thenReturn(Optional.of(testLobby));

        lobbyService.deleteLobby(1L);

        Mockito.verify(lobbyRepository, Mockito.times(1)).findById(Mockito.any());
        Mockito.verify(lobbyRepository, Mockito.times(1)).deleteById(Mockito.any());
        Mockito.verify(lobbyRepository, Mockito.times(1)).flush();

    }


    // test if a player can join a lobby
    @Test
    public void playerJoinsLobbyTest_succesfully(){
        Lobby createdLobby = lobbyService.createLobby(testLobby);

        lobbyService.playerJoinsLobby(createdLobby, "nextPlayer");

        // test size and name of player added
        assertEquals(createdLobby.getPlayerList().size(), 2);
        assertEquals(createdLobby.getPlayerList().get(1), "nextPlayer");
    }

    // test that access to lobby is denied if the lobby is in a game already
    @Test
    public void playerJoinsLobbyTest_LobbyIsAlreadyInGame(){
        Lobby createdLobby = lobbyService.createLobby(testLobby);
        createdLobby.setInGame(true);

        assertThrows(ResponseStatusException.class, () ->  lobbyService.playerJoinsLobby(createdLobby, "nextPlayer"));
    }



    // update a lobby, change the lobby object
    @Test
    public void updateLobbyTest_valid(){
       Lobby createdLobby = new Lobby();

       Lobby updatedLobby = new Lobby();
       updatedLobby.setId(1L);
       updatedLobby.setHost("newHost");

       createdLobby = lobbyService.updateLobby(updatedLobby);

        assertSame(createdLobby.getId(), updatedLobby.getId());
        assertSame("newHost", createdLobby.getHost());
    }

    // update a lobby, lobby not found exception
    @Test
    public void updateLobbyTest_Invalid_throwsException(){
        Lobby createdLobby = lobbyService.createLobby(testLobby);

        Lobby updatedLobby = new Lobby();
        updatedLobby.setId(1L);
        updatedLobby.setHost(null);

        assertThrows(ResponseStatusException.class, () -> lobbyService.updateLobby(updatedLobby));
    }

    }


