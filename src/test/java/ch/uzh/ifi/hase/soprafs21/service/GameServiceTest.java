package ch.uzh.ifi.hase.soprafs21.service;

import ch.uzh.ifi.hase.soprafs21.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs21.entity.Card;
import ch.uzh.ifi.hase.soprafs21.entity.Game;

import ch.uzh.ifi.hase.soprafs21.entity.Hand;
import ch.uzh.ifi.hase.soprafs21.entity.User;
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


// MOST OF THE GAMESERVICE TESTS ARE TESTS OUTSIDE IN HAND/DECK/CARD TESTS TO AVOID UNNECASSERY MOCKING AND COMPLICATIONS
public class GameServiceTest {



    @Autowired
    @Mock
    private HandRepository handRepository;

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

       List<Long> testPlayerList = Collections.singletonList(2L);

        testGame.setId(1L);
        testGame.setHost("testHost");
        testGame.setPlayerList(testPlayerList);
    }



    @Test
    public void checkWinTest(){

    }

}
