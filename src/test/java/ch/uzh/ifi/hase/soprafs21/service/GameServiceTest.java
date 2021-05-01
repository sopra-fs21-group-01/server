package ch.uzh.ifi.hase.soprafs21.service;

import ch.uzh.ifi.hase.soprafs21.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs21.entity.*;

import ch.uzh.ifi.hase.soprafs21.repository.*;
import org.aspectj.apache.bcel.generic.Type;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.server.ResponseStatusException;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

public class GameServiceTest {

    @Autowired
    private TestEntityManager entityManager;

    @Mock
    private GameRepository gameRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private LobbyRepository lobbyRepository;

    @Mock
    private HandRepository handRepository;

    @Mock
    private DeckRepository deckRepository;

    @Mock
    private LobbyService lobbyService;

    @Mock
    private UserService userService;


    @Mock
    private User testUser;

    @Mock
    private Hand userHand;

    @InjectMocks
    @MockBean
    private GameService gameService;

    @Mock
    private Lobby testLobby;

    @Mock
    private Game testGame;

    @Mock
    private Deck testDeck;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        testUser = new User();
        testUser.setId(1L);
        testUser.setHandId(1L);

        // given
        testLobby = new Lobby();
        testLobby.setId(1L);
        testLobby.setHost("testHost");

        testGame = new Game();
        List<Long> testPlayerList = Collections.singletonList(2L);
        testGame.setId(1L);
        testGame.setHost("testHost");
        testGame.setPlayerList(testPlayerList);

        userHand = new Hand();
        List<String> myList = new ArrayList<>();
        myList.add("0/Blue");
        userHand.setCards(myList);
        userHand.setUnoStatus(false);
        userHand.setId(1L);


        // when -> any object is being save in the lobbyRepository -> return the dummy Hand
        Mockito.when(gameRepository.save(Mockito.any())).thenReturn(testGame);
      //  Mockito.when(gameRepository.findById(Mockito.any())).thenReturn(Optional.of(testGame));


        Mockito.when(lobbyRepository.save(Mockito.any())).thenReturn(testLobby);
        given(lobbyService.getLobbyById(Mockito.any())).willReturn(testLobby);

       Mockito.when(handRepository.save(Mockito.any())).thenReturn(userHand);
    //   Mockito.when(handRepository.findById(Mockito.any())).thenReturn(Optional.of(userHand));

        Mockito.when(userRepository.save(Mockito.any())).thenReturn(testUser);
     //   Mockito.when(userRepository.findById(Mockito.any())).thenReturn(Optional.of(testUser));

        Mockito.when(deckRepository.save(Mockito.any())).thenReturn(testDeck);
        //   Mockito.when(deckRepository.findById(Mockito.any())).thenReturn(Optional.of(testDeck));

    }

    // test that it breaks the convertion when recognizing list is null
    @Test
    public void convertUserNamesToIdTest_PlayListWasNotSet() {

        testGame.setPlayerList(null);
        assertThrows(ResponseStatusException.class, () -> gameService.convertUserNamesToIds(testGame.getId()));
    }

    // Test that proofs convertion breaks when there are no players
    @Test
    public void convertUserNamesToIdTest_PlayListHasNoPlayers() {
        List<Long> myEmptyList = new ArrayList<>();
        testGame.setPlayerList(myEmptyList);
        assertThrows(ResponseStatusException.class, () -> gameService.convertUserNamesToIds(testGame.getId()));
    }

    // test for invalid game IDs of Game
    @Test
    public void getGameByIdTest_Invalid_ID_(){
        given(gameRepository.save(Mockito.any())).willReturn(testGame);
        assertThrows(ResponseStatusException.class, () -> gameService.getGameById(23331L));

    }
    // test for invalid game IDs of Deck
    @Test
    public void getDeckByIdTest_Invalid_ID_(){
        given(gameRepository.save(Mockito.any())).willReturn(testDeck);
        assertThrows(ResponseStatusException.class, () -> gameService.getDeckById(23331L));

    }
    // test for invalid game IDs of Hand
    @Test
    public void getHandByIdTest_Invalid_ID_(){
        given(gameRepository.save(Mockito.any())).willReturn(userHand);
        assertThrows(ResponseStatusException.class, () -> gameService.getHandById(23331L));

    }

    // test for allowed color and allowed value
    @Test
    public void checkIfMoveAllowedTest_valid(){
        userHand = new Hand();
        List<String> myList = new ArrayList<>();
        myList.add("0/Blue");
        myList.add("9/Red");
        userHand.setCards(myList);
        userHand.setUnoStatus(false);
        userHand.setId(1L);

        testGame = new Game();
        testGame.setCurrentColor("Blue");
        testGame.setCurrentValue("0");
        List<Long> testPlayerList = Collections.singletonList(1L);
        testGame.setPlayerList(testPlayerList);

        String allowedCard = "5/Blue";
        String allowedCard2 = "0/Red";

        Mockito.when(handRepository.findById(Mockito.any())).thenReturn(Optional.of(userHand));
        Mockito.when(deckRepository.findById(Mockito.any())).thenReturn(Optional.of(testDeck));

        assertTrue(gameService.checkIfMoveAllowed(testGame, allowedCard));
        assertTrue(gameService.checkIfMoveAllowed(testGame, allowedCard));

    }

     @Test
    public void checkIfMoveAllowedTest_invalid(){
        testGame = new Game();
        testGame.setCurrentColor("Blue");
        testGame.setCurrentValue("0");
        List<Long> testPlayerList = Collections.singletonList(1L);
        testGame.setPlayerList(testPlayerList);

        String notallowedCard = "5/Red";

        given(testDeck.getCardDeck()).willReturn(Collections.singletonList("0/Blue"));

        Mockito.when(handRepository.findById(Mockito.any())).thenReturn(Optional.of(userHand));
        Mockito.when(deckRepository.findById(Mockito.any())).thenReturn(Optional.of(testDeck));

        assertFalse(gameService.checkIfMoveAllowed(testGame, notallowedCard));}


   // Test if the Uno boolean of a Hand is set to True if player calls it and handsize is 1
   @Test
   public void sayUnoTest(){

       Hand newuserHand = new Hand();
       List<String> myList = Collections.singletonList("0/Blue");
       newuserHand.setCards(myList);
       newuserHand.setUnoStatus(false);
       newuserHand.setId(1L);

       given(userService.getUseryById(Mockito.any())).willReturn(testUser);
       Mockito.when(handRepository.findById(Mockito.any())).thenReturn(Optional.of(newuserHand));

       gameService.sayUno(testGame, 1L);

       assertTrue(gameService.getHandById(testUser.getHandId()).getUnoStatus());
   }

    // Test if the Uno boolean of a Hand is NOT set to True if player calls it and handsize is bigger than 1
    @Test
    public void sayUnoTest_tooManyCards(){

        Hand newuserHand = new Hand();
        List<String> myList = new ArrayList<>();
        myList.add("9/Red");
        myList.add("0/Blue");

        newuserHand.setCards(myList);
        newuserHand.setUnoStatus(false);
        newuserHand.setId(1L);

        given(userService.getUseryById(Mockito.any())).willReturn(testUser);
        Mockito.when(handRepository.findById(Mockito.any())).thenReturn(Optional.of(newuserHand));

        gameService.sayUno(testGame, 1L);

        assertFalse(gameService.getHandById(testUser.getHandId()).getUnoStatus());
    }
    // test if the user receives a hand after initilization and that it consists cards
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

        Mockito.when(deckRepository.findById(Mockito.any())).thenReturn(Optional.of(testDeck));
        Mockito.when(gameRepository.save(Mockito.any())).thenReturn(testGame);
        Mockito.when(handRepository.findById(Mockito.any())).thenReturn(Optional.of(userHand));
        given(userService.getUseryById(Mockito.any())).willReturn(testUser);

        gameService.initializeHands(testGame);
        assertNotNull(testUser.getHandId());
        assertNotNull(gameService.getHandById(testUser.getHandId()));
        assertNotNull(gameService.getHandById(testUser.getHandId()).getCards());
    }

    // test if it aborts when the deck could not be found
    @Test
    public void initializeHandTest_failEmpptyOrNotFoundDeck(){
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

        // dont mock the return of the deck so it cannot be found
        Mockito.when(gameRepository.save(Mockito.any())).thenReturn(testGame);
        Mockito.when(handRepository.findById(Mockito.any())).thenReturn(Optional.of(userHand));
        given(userService.getUseryById(Mockito.any())).willReturn(testUser);


        assertThrows(ResponseStatusException.class, () -> gameService.initializeHands(testGame));

    }

    @Test
    public void getColorTest(){
        assertEquals("Blue", gameService.getColorOfCard("0/Blue"));
    }

    @Test
    public void getValueTest(){
        assertEquals("0", gameService.getValueOfCard("0/Blue"));
    }

    @Test
    public void getCardValuesTest(){
        assertEquals(gameService.getCardValuies("0/Blue").length, 2);
        assertEquals("0", gameService.getCardValuies("0/Blue")[0]);
    }

}
