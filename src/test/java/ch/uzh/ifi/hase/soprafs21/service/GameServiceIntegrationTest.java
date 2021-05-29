package ch.uzh.ifi.hase.soprafs21.service;

import ch.qos.logback.core.util.COWArrayList;
import ch.uzh.ifi.hase.soprafs21.entity.*;
import ch.uzh.ifi.hase.soprafs21.repository.*;
import javassist.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@WebAppConfiguration
@SpringBootTest
public class GameServiceIntegrationTest {

    @Qualifier("gameRepository")
    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private GameService gameService;


    @BeforeEach
    public void setup() {
        gameRepository.deleteAll();
    }

    // test for valid Game saving. Creation is already tests in Service and includes creation of hands and decks
    @Test
    public void createGame_validInputs_success() {
        // given
        assertThrows(NoSuchElementException.class, () -> {gameRepository.findById(1L).get();});

        Game testGame = new Game();
        testGame.setHost("testHost");
        testGame.setId(1L);
        List<Long> playerList = new ArrayList<>();
        playerList.add(1L);
        testGame.setPlayerList(playerList);

        Game createdGame = gameRepository.save(testGame);

        // then
        assertEquals(testGame.getId(), createdGame.getId());
        assertEquals(testGame.getHost(), createdGame.getHost());
        assertEquals(testGame.getPlayerList(), createdGame.getPlayerList());
        assertEquals(testGame.getCurrentColor(), createdGame.getCurrentColor());
        assertEquals(testGame.getGameDirection(), createdGame.getGameDirection());
    }

    // since a Game is always created with the corresponding Lobby ID, it should never get duplicated ID.

    @Test
    public void createGame_duplicateID_throwsException() {

        assertThrows(NoSuchElementException.class, () -> {gameRepository.findById(1L).get();});

        Game testGame = new Game();
        testGame.setHost("testHost");
        testGame.setId(1L);
        List<Long> playerList = new ArrayList<>();
        playerList.add(1L);
        testGame.setPlayerList(playerList);

        Game createdGame = gameRepository.save(testGame);

        // attempt to create second lobby with same username
        Game testGame2 = new Game();
        testGame2.setHost("testHost");
        testGame2.setId(1L);
        List<Long> playerList2 = new ArrayList<>();
        playerList.add(1L);
        testGame2.setPlayerList(playerList2);

        // check that an error is thrown
        assertThrows(ResponseStatusException.class, () ->   gameService.createGame(testGame2));
    }
}
