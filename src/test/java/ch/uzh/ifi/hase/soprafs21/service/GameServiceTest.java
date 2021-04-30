package ch.uzh.ifi.hase.soprafs21.service;

import ch.uzh.ifi.hase.soprafs21.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs21.entity.*;

import ch.uzh.ifi.hase.soprafs21.repository.GameRepository;
import ch.uzh.ifi.hase.soprafs21.repository.HandRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.web.server.ResponseStatusException;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

public class GameServiceTest {

    @Autowired
    private TestEntityManager entityManager;

    @Mock
    private GameRepository gameRepository;

    @Mock
    private HandRepository handRepository;

    @Mock
    private User testUser;

    @Mock
    private Hand userHand;

    @InjectMocks
    private GameService gameService;

    @Mock
    private Game testGame;

    @Mock
    private Deck testDeck;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        testGame = new Game();

        List<Long> testPlayerList = Collections.singletonList(2L);

        testGame.setId(1L);
        testGame.setHost("testHost");
        testGame.setPlayerList(testPlayerList);

        List<String> myList = new ArrayList<>();
        userHand.setCards(myList);
        userHand.setUnoStatus(false);


        // when -> any object is being save in the lobbyRepository -> return the dummy Hand
        Mockito.when(gameRepository.save(Mockito.any())).thenReturn(userHand);
    }


    /**
    @Test
    public void sayUnoTest(){

        testUser.setId(1L);
        testUser.setUsername("firstname");
        testUser.setEmail("firstname@uzh.ch");
        testUser.setPassword("123");
        testUser.setToken("1");

        userHand.setId(testUser.getId());



        Mockito.when(gameRepository.save(Mockito.any())).thenReturn(userHand);

        gameService.sayUno(testGame, 1L);
        assertTrue(userHand.getUnoStatus());

    }

    @Test
    public void initializeHandTest(){
        testUser.setId(1L);
        testUser.setUsername("firstname");
        testUser.setEmail("firstname@uzh.ch");
        testUser.setPassword("123");
        testUser.setToken("1");

        List<Long> myPlayers = new ArrayList<>();
        myPlayers.add(2L);
        testGame.setHost("hostName");
        testGame.setId(3L);
        testGame.setPlayerList(myPlayers);

        testDeck.setId(2L);

        Mockito.when(gameRepository.save(Mockito.any())).thenReturn(testGame);

        gameService.initializeHands(testGame);
        assertNotNull(testUser.getHandId());


    } */

}
