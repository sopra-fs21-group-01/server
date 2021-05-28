package ch.uzh.ifi.hase.soprafs21.service;

import ch.qos.logback.classic.gaffer.GafferUtil;
import ch.uzh.ifi.hase.soprafs21.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs21.entity.*;

import ch.uzh.ifi.hase.soprafs21.exceptions.DuplicatedUserException;
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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.server.ResponseStatusException;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

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

    @MockBean
    private Hand userHandBean;

    @InjectMocks
    @MockBean
    private GameService gameService;

    @Mock
    private GameService gameServiceNoBean;

    @Mock
    private Lobby testLobby;

    @Mock
    private Game testGame;

    @Mock
    private Game testGameBean;

    @Mock
    private Deck testDeck;

    @MockBean
    private Deck testDeckBean;


    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        testUser = new User();
        testUser.setId(1L);
        testUser.setHandId(1L);
        testUser.setUsername("Hans");

        // given
        testLobby = new Lobby();
        testLobby.setId(1L);
        testLobby.setHost("testHost");
        List<String> testPlayerList_names = Collections.singletonList("Hans");
        testLobby.setPlayerList(testPlayerList_names);


        testDeck= new Deck();
        testDeck.setId(1L);

        testGame = new Game();
        List<Long> testPlayerList = new ArrayList<>();
        testPlayerList.add(1L);
        testPlayerList.add(2L);
        testGame.setId(1L);
        testGame.setHost("testHost");
        testGame.setPlayerList(testPlayerList);
        testGame.setCurrentColor("Blue");
        testGame.setCurrentValue("0");
        testGame.setCurrentPlayer(1);

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

    // a game with one player is given, the deck and the hand are initialized
    @Test
    public void createGameTest_valid(){
        Game testGame2 = new Game();
        List<Long> testPlayerList = new ArrayList<>();
        testPlayerList.add(1L);
        testPlayerList.add(2L);
        testGame2.setId(1L);
        testGame2.setHost("testHost");
        testGame2.setPlayerList(testPlayerList);


        // assert that two users are present
        assertEquals(testGame2.getPlayerList().size(), 2);

        Mockito.when(deckRepository.findById(Mockito.any())).thenReturn(Optional.of(testDeck));
        Mockito.when(handRepository.findById(Mockito.any())).thenReturn(Optional.of(userHand));
        Mockito.when(userRepository.findById(Mockito.any())).thenReturn(Optional.of(testUser));
        Mockito.when(userService.getUseryById(Mockito.any())).thenReturn((testUser));

        // create the game and make it be returned upon saving
        Game createdGame = gameService.createGame(testGame2);
        Mockito.when(gameRepository.save(Mockito.any())).thenReturn(createdGame);


        // test that hand a deck is initialized, one deck should be initiatiated and two Hands are saved
        Mockito.verify(deckRepository, Mockito.times(1)).save(Mockito.any());
        Mockito.verify(handRepository, Mockito.times(2)).save(Mockito.any());

        // test if data was received properly
        assertSame("testHost", createdGame.getHost());
        assertSame(2, createdGame.getPlayerList().size());
        assertTrue(createdGame.getGameDirection());
        assertSame(testGame2.getPlayerList().get(0), createdGame.getPlayerList().get(0));
    }

    // test that it breaks the convertion when recognizing list is null
    @Test
    public void convertUserNamesToIdTest_Sucess() {

        when(lobbyRepository.findById(Mockito.any())).thenReturn(Optional.of(testLobby));
        when(userRepository.findById(Mockito.any())).thenReturn(Optional.of(testUser));
        when(userRepository.findByUsername(Mockito.any())).thenReturn(testUser);
        when(userService.getUser(Mockito.any())).thenReturn(testUser);

       List<Long> expectedList = new ArrayList<>();
       expectedList.add(1L);

        List<Long> producedList = gameService.convertUserNamesToIds(testGame.getId());

       assertEquals(expectedList, producedList);
    }

    // test that it breaks the convertion when recognizing list is null
    @Test
    public void convertUserNamesToIdTest_PlayListWasNotSet() {

        testLobby.setPlayerList(null);
        assertThrows(ResponseStatusException.class, () -> gameService.convertUserNamesToIds(testGame.getId()));
    }

    // Test that proofs convertion breaks when there are no players
    @Test
    public void convertUserNamesToIdTest_PlayListHasNoPlayers() {
        List<String> myEmptyList = new ArrayList<>();
        testLobby.setPlayerList(myEmptyList);
        assertThrows(ResponseStatusException.class, () -> gameService.convertUserNamesToIds(testGame.getId()));
    }

    // test for valid game IDs of Game
    @Test
    public void getGameByIdTest_Valid_ID_(){
        when(gameRepository.findById(Mockito.any())).thenReturn(Optional.of(testGame));

        Game gameOfId = gameService.getGameById(testGame.getId());
        Mockito.verify(gameRepository, Mockito.times(1)).findById(Mockito.any());
        assertSame(gameOfId, testGame);
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

// TEST NOT WOTRKING; CANNOT INVOKE CHECKIFEXTRACARD BECAUS CARDNAME IS NULL

    @Test
    public void playCardTest(){
        testDeckBean = new Deck();

        userHandBean = new Hand();
        List<String> myList = new ArrayList<>();
        myList.add("5/Blue");
        myList.add("9/Red");
        userHandBean.setCards(myList);
        userHandBean.setUnoStatus(false);
        userHandBean.setId(1L);


        testGameBean = new Game();
        testGameBean.setCurrentColor("Blue");
        testGameBean.setCurrentValue("0");
        String[] cardValues = {"Blue", "0"};
        testGameBean.setCurrentColor("Blue");
        testGameBean.setCurrentValue("0");

        List<Long> testPlayerList = Collections.singletonList(1L);
        testGameBean.setPlayerList(testPlayerList);



        String allowedCard = "5/Blue";


        Mockito.when(handRepository.findById(Mockito.any())).thenReturn(Optional.of(userHandBean));
        Mockito.when(deckRepository.findById(Mockito.any())).thenReturn(Optional.of(testDeckBean));


        gameService.playCard(testGame, testUser, allowedCard);

        // verify game is safed
        Mockito.verify(gameRepository, Mockito.times(1)).save(Mockito.any());
       // Mockito.verify(gameService, Mockito.times(1)).determineNextPlayer(Mockito.any(),Mockito.any());

    }

   /** // test when the played card is not in the user hand, throw exception
    @Test
    public void playCardTest_cardNotInUserHand(){
        userHand = new Hand();
        List<String> myList = new ArrayList<>();
        myList.add("2/Yellow");
        userHand.setCards(myList);
        userHand.setUnoStatus(false);
        userHand.setId(1L);

        testGame = new Game();
        testGame.setCurrentColor("Blue");
        testGame.setCurrentValue("0");
        String[] cardValues = {"Blue", "0"};
        List<Long> testPlayerList = Collections.singletonList(1L);
        testGame.setPlayerList(testPlayerList);



        String NotAllowedCard = "5/Blue";

        Mockito.when(handRepository.findById(Mockito.any())).thenReturn(Optional.of(userHand));
        Mockito.when(deckRepository.findById(Mockito.any())).thenReturn(Optional.of(testDeck));

        assertThrows(ResponseStatusException.class, () ->gameService.playCard(testGame, testUser, NotAllowedCard));
    }
*/

    // throws exception because it cannot find the users Hand (user might be deleted or already out of the game)
     @Test
     public void playCardTest_userHandNotFound(){
   String allowedCard = "5/Blue";

   Mockito.when(deckRepository.findById(Mockito.any())).thenReturn(Optional.of(testDeck));

    assertThrows(ResponseStatusException.class, () ->gameService.playCard(testGame, testUser, allowedCard));
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

   // check with invalid cards, can be extended by all edge cases
    @Test
    public void checkIfMoveAllowedTest_invalid(){
        testGame = new Game();
        testGame.setCurrentColor("Blue");
        testGame.setCurrentValue("0");
        List<Long> testPlayerList = Collections.singletonList(1L);
        testGame.setPlayerList(testPlayerList);

        String notallowedCard = "5/Red";

        // given(testDeck.getCardDeck()).willReturn(Collections.singletonList("0/Blue"));

        Mockito.when(handRepository.findById(Mockito.any())).thenReturn(Optional.of(userHand));
        Mockito.when(deckRepository.findById(Mockito.any())).thenReturn(Optional.of(testDeck));

        assertFalse(gameService.checkIfMoveAllowed(testGame, notallowedCard));}


    // test that player is succesfully removed from the playerList
    @Test
    public void removePlayerFromList_succesfull(){
        Mockito.when(deckRepository.findById(Mockito.any())).thenReturn(Optional.of(testDeck));

        // assert player is in playerList
        assertTrue(testGame.getPlayerList().contains(1L));

        gameService.removePlayerFromPlayerList(testGame, 1L);
        assertFalse(testGame.getPlayerList().contains(1L));
        Mockito.verify(gameRepository, Mockito.times(1)).save(Mockito.any());
        Mockito.verify(gameRepository, Mockito.times(1)).flush();
    }

    // test that player is succesfully removed from the playerList
    @Test
    public void wishColor_succesfull(){
        Mockito.when(deckRepository.findById(Mockito.any())).thenReturn(Optional.of(testDeck));

        gameService.wishColor("Blue", testGame);
        assertSame("Blue", testGame.getCurrentColor());
        Mockito.verify(gameRepository, Mockito.times(1)).save(Mockito.any());
        Mockito.verify(gameRepository, Mockito.times(1)).flush();
    }

    // test delete a game
    @Test
    public void deleteGame_success(){

        when(gameRepository.findById(Mockito.any())).thenReturn(Optional.of(testGame));

        gameService.deleteGame(1L);

        Mockito.verify(gameRepository, Mockito.times(1)).deleteById(Mockito.any());
        Mockito.verify(gameRepository, Mockito.times(1)).flush();

    }

    // update a game, change the game object
    @Test
    public void updateGameTest_valid(){
        Game createdGame = new Game();

        Game updatedGame = new Game();
        updatedGame.setId(1L);
        updatedGame.setHost("newHost");

        createdGame = gameService.updateGame(updatedGame);

        assertSame(createdGame.getId(), updatedGame.getId());
        assertSame("newHost", createdGame.getHost());
    }


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
        myPlayers.add(3L);
        testGame.setHost("hostName");
        testGame.setId(3L);
        testGame.setPlayerList(myPlayers);

        testDeck.setId(2L);

        Mockito.when(deckRepository.findById(Mockito.any())).thenReturn(Optional.of(testDeck));
        Mockito.when(gameRepository.save(Mockito.any())).thenReturn(testGame);
        Mockito.when(handRepository.findById(Mockito.any())).thenReturn(Optional.of(userHand));
        given(userService.getUseryById(Mockito.any())).willReturn(testUser);

        gameService.initializeHands(testGame);

        // for every player a hand should be saved -> 2 players means 2 calls
        Mockito.verify(handRepository, Mockito.times(2)).save(Mockito.any());

        // verify every player gets pulled -> will always be twice the players in list because its pulled and saved
        Mockito.verify(userService, Mockito.times(4)).getUseryById(Mockito.any());

        // check if the user got a hand assigned, if he has the id and if the hand has cards
        assertNotNull(testUser.getHandId());
        assertNotNull(gameService.getHandById(testUser.getHandId()));

    }

    // test if it aborts when the deck could not be found
    @Test
    public void initializeHandTest_failEmpptyDeck_OrNotFoundDeck(){
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
