package ch.uzh.ifi.hase.soprafs21.service;

import ch.uzh.ifi.hase.soprafs21.entity.Game;

import ch.uzh.ifi.hase.soprafs21.repository.GameRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class GameServiceTest {
/**

    @Mock
    private GameRepository gameRepository;

    @InjectMocks
    private GameService gameService;

    private Game testGame;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
// given
        testGame = new Game();

        ArrayList<String> testCardStack = new ArrayList<String>();
        testCardStack.add("ACE");
        testCardStack.add("KING");
        testCardStack.add("QUEEN");

        String[] testPlayerList = {"Just", "Some", "Names"};

        testGame.setId(1L);
        testGame.setHost("testHost");
        testGame.setPlayerList(testPlayerList);
        testGame.setCardStack(testCardStack);



        // when -> any object is being save in the lobbyRepository -> return the dummy testLobby
        Mockito.when(gameRepository.save(Mockito.any())).thenReturn(testGame);
    }

    // test creation of lobby with valid inputs
    @Test
    public void createGame_validInputs_success(){
        // save the dummy lobby to database and return it
        Game createdGame = gameService.createGame(testGame);
                // then test if the data was correctly saved
        Mockito.verify(gameRepository, Mockito.times(1)).save(Mockito.any());

        assertEquals(testGame.getId(), createdGame.getId());
        assertEquals(testGame.getHost(), createdGame.getHost());
        assertEquals(testGame.getPlayerList(), createdGame.getPlayerList());
        assertEquals(testGame.getCardStack(), createdGame.getCardStack());

    }
*/
}
