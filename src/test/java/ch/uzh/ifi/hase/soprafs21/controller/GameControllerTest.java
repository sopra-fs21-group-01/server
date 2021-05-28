package ch.uzh.ifi.hase.soprafs21.controller;

import ch.uzh.ifi.hase.soprafs21.entity.Game;
import ch.uzh.ifi.hase.soprafs21.entity.Hand;
import ch.uzh.ifi.hase.soprafs21.entity.Lobby;
import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.repository.HandRepository;
import ch.uzh.ifi.hase.soprafs21.rest.dto.GamePostDTO;
import ch.uzh.ifi.hase.soprafs21.service.GameService;
import ch.uzh.ifi.hase.soprafs21.service.LobbyService;
import ch.uzh.ifi.hase.soprafs21.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import static org.hamcrest.Matchers.*;
import static org.mockito.BDDMockito.given;


@WebMvcTest(GameController.class)
public class GameControllerTest {



    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GameService gameService;

    @MockBean
    private LobbyService lobbyService;

    @MockBean
    private UserService userService;

    @MockBean
    private Lobby testLobby;

    @MockBean
    private  Game testGame;

    @MockBean
    private Hand testHand;

    @MockBean
    private User testUser;

    @MockBean
    private HandRepository handRepository;




    // POST Test valid post, returns Lobby location as string
    @Test
    public void createGame_validInput_gameCreated() throws Exception {
        List<String> testPlayerListForLobby = new ArrayList<String>(){
          {
             add("Hans");
                }};

        User testUser = new User();
        testUser.setId(2L);
        testUser.setUsername("Hans");

        Lobby testLobby = new Lobby();
        testLobby.setId(1L);
        testLobby.setHost("testHost");
        testLobby.setPlayerList(testPlayerListForLobby);

        GamePostDTO gamePostDTO = new GamePostDTO();
        gamePostDTO.setHost("Hans");
        gamePostDTO.setId(1L);

        Game testGame = new Game();

        List<String> testCardStack = new ArrayList<String >(){
            {
                add("ACE");
                add("KING");
                add("QUEEN");
            }};

        // List of player ID's
        List<Long> testPlayerList = new ArrayList<Long>(){
            {
                add(2L);
            }};

        testGame.setId(1L);
        testGame.setHost("testHost");
        testGame.setPlayerList(testPlayerList);

        given(gameService.convertUserNamesToIds(1L)).willReturn(testPlayerList);
        given(lobbyService.getLobbyById(Mockito.any())).willReturn(testLobby);
        given(gameService.createGame(Mockito.any())).willReturn(testGame);

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder postRequest = post("/game/{id}/kickOff", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(gamePostDTO));

        // then !! Just Check for the expected String output and for the status type
        mockMvc.perform(postRequest)
                .andExpect(status().isCreated())
                .andExpect(content().string("game/"+testGame.getId()+"/kickOff"));
    }


    // POST Test invalid post, no hostname given, Conflict is thrown
    @Test
    public void createGame_invalidInput_gameCreationUnsuccessful() throws Exception {
        // given
        Game testGame = new Game();

        GamePostDTO gamePostDTO = new GamePostDTO();
        gamePostDTO.setHost("Hans");
        gamePostDTO.setId(1L);

        List<String> testCardStack = new ArrayList<String >(){
            {
                add("ACE");
                add("KING");
                add("QUEEN");
            }};

        // List of player ID's
        List<Long> testPlayerList = new ArrayList<Long>(){
            {
                add(2L);
                add(4L);
                add(6L);
            }};

        testGame.setId(1L);
        testGame.setHost("testHost");
        testGame.setPlayerList(testPlayerList);


        given(gameService.createGame(Mockito.any())).willThrow(new ResponseStatusException(HttpStatus.CONFLICT));

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder postRequest = post("/game/{id}/kickOff", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(gamePostDTO));

        // then !! Just Check for the expected String output and for the status type
        mockMvc.perform(postRequest)
                .andExpect(status().isConflict())
        ;
    }


    // GET test for the whole game object
    @Test
    public void getGame_whenGetPlayerlist_thenReturnGame() throws Exception {
        testHand = new Hand();
        testHand.setId(1L);
        List<String> testCards =new ArrayList<>();
        testCards.add("red/8");
        testCards.add("blue/1");
        testHand.setCards(testCards);

        testUser = new User();
        testUser.setUsername("Hans");
        testUser.setId(2L);
        List<String> testPlayerListForLobby = new ArrayList<String>(){
            {
                add("Hans");
            }};
        testUser.setHandId(testHand.getId());

        testLobby = new Lobby();
        testLobby.setId(1L);
        testLobby.setHost("testHost");
        testLobby.setPlayerList(testPlayerListForLobby);

        // List of player ID's
        List<Long> testPlayerList = new ArrayList<Long>(){
            {
                add(2L);
            }};

        testGame = new Game();
        testGame.setPlayerList(testPlayerList);
        testGame.setId(1L);
        testGame.setHost("Hans");

        Mockito.when(handRepository.save(Mockito.any())).thenReturn(testHand);
        given(gameService.getHandById(Mockito.any())).willReturn(testHand);
        given(gameService.getGameById(Mockito.any())).willReturn(testGame);
        given(userService.getUseryById(Mockito.any())).willReturn(testUser);
        given(lobbyService.getLobbyById(Mockito.any())).willReturn(testLobby);


        // when
        MockHttpServletRequestBuilder getRequest = get("/game/{id}/kickOff", 1L)
                .contentType(MediaType.APPLICATION_JSON);

        // then
        mockMvc.perform(getRequest).andExpect(status().isOk())
                .andExpect(jsonPath("currentPlayer", is(2)))
                .andExpect(jsonPath("id", is(1)))
                .andExpect(jsonPath("host", is("Hans")))
                .andExpect(jsonPath("$.opponentListHands[0]", is("2,Hans,2,false")))
                ;

    }

    @Test
    public void getGame_whenGetPlayerlist_thenReturnArrayWithNames_wrongGameID() throws Exception {


     User testUser = new User();
     testUser.setUsername("Hans");
     testUser.setId(2L);
     List<String> testPlayerListForLobby = new ArrayList<String>(){
         {
             add("Hans");
             add("Jörg");
             add("Peter");
         }};


     Lobby testLobby = new Lobby();
     testLobby.setId(1L);
     testLobby.setHost("testHost");
     testLobby.setPlayerList(testPlayerListForLobby);

     // List of player ID's
     List<Long> testPlayerList = new ArrayList<Long>(){
         {
             add(2L);
         }};

     testGame.setPlayerList(testPlayerList);
     testGame.setId(1L);

     given(gameService.getGameById(1L)).willThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Game with given ID was not found"));


     // when
     MockHttpServletRequestBuilder getRequest = get("/game/{id}/kickOff", 1L)
             .contentType(MediaType.APPLICATION_JSON);

     // then
     mockMvc.perform(getRequest).andExpect(status().isNotFound())
     ;}


    // GET tests the  GET for single game playerList with invalid input. Test if if status is right
    @Test
    public void getPlayerListOffGame_invalidID_throwsException() throws Exception {
        List<String> testPlayerListForLobby = new ArrayList<String>(){
            {
                add("Hans");
                add("Jörg");
                add("Peter");
            }};

        Lobby testLobby = new Lobby();
        testLobby.setId(1L);
        testLobby.setHost("testHost");
        testLobby.setPlayerList(testPlayerListForLobby);

        // List of player ID's
        List<Long> testPlayerList = new ArrayList<Long>(){
            {
                add(2L);
                add(4L);
                add(6L);
            }};

        given(gameService.getGameById(1L)).willThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Game with given ID was not found"));

        // when
        MockHttpServletRequestBuilder getRequest = get("/game/{id}/kickOff", 1L)
                .contentType(MediaType.APPLICATION_JSON);
        // then
        mockMvc.perform(getRequest).andExpect(status().isNotFound())
        ; }


    // GET tests the GET for the ID of the current player
    @Test
    public void getGame_whengetCurrentID_thenLongOfPlayerWhosTurnItIs() throws Exception {

        Game testGame = new Game();

        // List of player ID's
        List<Long> testPlayerList = new ArrayList<Long>(){
            {add(2L);}};

        testGame.setId(1L);
        testGame.setHost("testHost");
        testGame.setPlayerList(testPlayerList);
        testGame.setCurrentPlayer(0);

        given(gameService.updateGame(Mockito.any())).willReturn(testGame);
        given(gameService.getGameById(Mockito.any())).willReturn(testGame);

        // when
        MockHttpServletRequestBuilder getRequest = get("/game/{id}/kickOff/currentPlayerIds", 1L)
                .contentType(MediaType.APPLICATION_JSON);
        // then
        mockMvc.perform(getRequest).andExpect(status().isOk()).andExpect(content().string("2"))
        ;

    }

    // GET tests the GET for the ID of the current player
    @Test
    public void getGame_whenGetHandOfPlayer_InvalidIdWillReturnError() throws Exception {
        List<String> testPlayerListForLobby = new ArrayList<String>(){
            {
                add("Hans");
            }};

        User testUser = new User();
        testUser.setId(2L);
        testUser.setUsername("Hans");

        Lobby testLobby = new Lobby();
        testLobby.setId(1L);
        testLobby.setHost("testHost");
        testLobby.setPlayerList(testPlayerListForLobby);

        Game testGame = new Game();

        List<String> testCardStack = new ArrayList<String >(){
            {
                add("ACE");
                add("KING");
                add("QUEEN");
            }};

        // List of player ID's
        List<Long> testPlayerList = new ArrayList<Long>(){
            {
                add(2L);
                //      add(4L);
                //      add(6L);
            }};

        testGame.setId(1L);
        testGame.setHost("testHost");
        testGame.setPlayerList(testPlayerList);
        testGame.setCurrentPlayer(0);

        given(gameService.getGameById(Mockito.any())).willThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Game with given ID was not found"));
        // when
        MockHttpServletRequestBuilder getRequest = get("/game/{id}/kickOff/currentPlayerIds", 1L)
                .contentType(MediaType.APPLICATION_JSON);

        // then
        mockMvc.perform(getRequest).andExpect(status().isNotFound())

        ;

    }

    // test valid draw card method, returns nothing
    @Test
    public void drawCard_validInput_returnsNothing() throws Exception {

        GamePostDTO gamePostDTO = new GamePostDTO();
        gamePostDTO.setHost("Hans");
        gamePostDTO.setId(1L);

        List<String> testPlayerListForLobby = new ArrayList<String>(){
            {
                add("Hans");
                //       add("Jörg");
                //      add("Peter");
            }};

        User testUser = new User();
        testUser.setId(2L);
        testUser.setUsername("Hans");

        Lobby testLobby = new Lobby();
        testLobby.setId(1L);
        //   testLobby.setName("testName");
        //  testLobby.setPassword("testPassword");
        testLobby.setHost("testHost");
        testLobby.setPlayerList(testPlayerListForLobby);

        Game testGame = new Game();

        List<String> testCardStack = new ArrayList<String >(){
            {
                add("ACE");
                add("KING");
                add("QUEEN");
            }};

        // List of player ID's
        List<Long> testPlayerList = new ArrayList<Long>(){
            {
                add(2L);
                //      add(4L);
                //      add(6L);
            }};

        testGame.setId(1L);
        testGame.setHost("testHost");
        testGame.setPlayerList(testPlayerList);

        given(gameService.convertUserNamesToIds(1L)).willReturn(testPlayerList);
        given(lobbyService.getLobbyById(Mockito.any())).willReturn(testLobby);
        given(gameService.createGame(Mockito.any())).willReturn(testGame);
        given(gameService.updateGame(Mockito.any())).willReturn(testGame);
        given(gameService.getGameById(Mockito.any())).willReturn(testGame);

        MockHttpServletRequestBuilder putRequest = put("/game/{id}/drawCard", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(gamePostDTO));

        // then !!! Just check for NULL output and status code
        mockMvc.perform(putRequest)
                .andExpect(status().isNoContent());}

    // PUT tests valid PUT method for Playerturn, returns a game
    @Test
    public void updateGame_validInput_returnsNothing() throws Exception {

        GamePostDTO gamePostDTO = new GamePostDTO();
        gamePostDTO.setHost("Hans");
        gamePostDTO.setId(1L);

        List<String> testPlayerListForLobby = new ArrayList<String>(){
            {
                add("Hans");
            }};

        User testUser = new User();
        testUser.setId(2L);
        testUser.setUsername("Hans");

        Lobby testLobby = new Lobby();
        testLobby.setId(1L);
        testLobby.setHost("testHost");
        testLobby.setPlayerList(testPlayerListForLobby);

        Game testGame = new Game();

        List<String> testCardStack = new ArrayList<String >(){
            {
                add("ACE");
                add("KING");
                add("QUEEN");
            }};

        // List of player ID's
        List<Long> testPlayerList = new ArrayList<Long>(){
            {
                add(2L);
            }};

        testGame.setId(1L);
        testGame.setHost("testHost");
        testGame.setPlayerList(testPlayerList);

        given(gameService.convertUserNamesToIds(1L)).willReturn(testPlayerList);
        given(lobbyService.getLobbyById(Mockito.any())).willReturn(testLobby);
        given(gameService.createGame(Mockito.any())).willReturn(testGame);
        given(gameService.updateGame(Mockito.any())).willReturn(testGame);
        given(gameService.getGameById(Mockito.any())).willReturn(testGame);

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder putRequest = put("/game/{id}/playerTurn", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(gamePostDTO));

        // then !!! Just check for NULL output and status code
        mockMvc.perform(putRequest)
                .andExpect(status().isNoContent());}


    // PUT tests invalid PUT method for Game update with not existing lobby
    @Test
    public void updateGame_invalidID() throws Exception {
        // given
        Game testGame = new Game();

        GamePostDTO gamePostDTO = new GamePostDTO();
        gamePostDTO.setHost("Hans");
        gamePostDTO.setId(1L);

        List<String> testCardStack = new ArrayList<String >(){
            {
                add("ACE");
                add("KING");
                add("QUEEN");
            }};

        // List of player ID's
        List<Long> testPlayerList = new ArrayList<Long>(){
            {
                add(2L);
                add(4L);
                add(6L);
            }};

        testGame.setId(1L);
        testGame.setHost("testHost");
        testGame.setPlayerList(testPlayerList);

        given(gameService.playCard(Mockito.any(), Mockito.any(), Mockito.any())).willThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Game with given ID was not found"));

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder putRequest = put("/game/{id}/playerTurn", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(gamePostDTO));

        // then !!! Just check for NULL output and status code
        mockMvc.perform(putRequest)
                .andExpect(status().isNotFound());}


    // PUT test for succesfully leaving the game
    @Test
    public void leaveGame_succesfully() throws Exception{
        GamePostDTO gamePostDTO = new GamePostDTO();
        gamePostDTO.setId(2L);

        testUser = new User();
        testUser.setUsername("Hans");
        testUser.setId(2L);

        Game testGame = new Game();

        // List of player ID's
        List<Long> testPlayerList = new ArrayList<Long>(){
            {
                add(2L);
                add(3L);
                add(5L);
            }};

        testGame = new Game();
        testGame.setPlayerList(testPlayerList);
        testGame.setId(1L);
        testGame.setHost("Hans");
        testGame.setCurrentPlayer(2);





        given(userService.getUseryById(Mockito.any())).willReturn(testUser);
        given(gameService.getGameById(Mockito.any())).willReturn(testGame);


        // when
        MockHttpServletRequestBuilder putRequest = put("/game/{id}/leave", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(gamePostDTO));

        // then
        mockMvc.perform(putRequest).andExpect(status().isNoContent());
        assertEquals(2, testGame.getCurrentPlayer());
        Mockito.verify(gameService, Mockito.times(1)).removePlayerFromPlayerList(Mockito.any(), Mockito.any());




    }

    // PUT test saying UNO
    @Test
    public void sayUNO_succesfully() throws Exception{
        GamePostDTO gamePostDTO = new GamePostDTO();
        gamePostDTO.setId(2L);

        testUser = new User();
        testUser.setUsername("Hans");
        testUser.setId(2L);

        Game testGame = new Game();
        // List of player ID's
        List<Long> testPlayerList = new ArrayList<Long>(){
            {
                add(2L);
                add(3L);
                add(5L);
            }};

        testGame = new Game();
        testGame.setPlayerList(testPlayerList);
        testGame.setId(1L);
        testGame.setHost("Hans");
        testGame.setCurrentPlayer(2);

        given(userService.getUseryById(Mockito.any())).willReturn(testUser);
        given(gameService.getGameById(Mockito.any())).willReturn(testGame);

        // when
        MockHttpServletRequestBuilder putRequest = put("/game/{id}/sayUno", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(gamePostDTO));

        // then
        mockMvc.perform(putRequest).andExpect(status().isNoContent());
        Mockito.verify(gameService, Mockito.times(1)).sayUno(Mockito.any(), Mockito.any());
        }

    // PUT test for when a player wins
    @Test
    public void playerWins_succesfully() throws Exception{
        GamePostDTO gamePostDTO = new GamePostDTO();
        gamePostDTO.setId(2L);

        testUser = new User();
        testUser.setUsername("Hans");
        testUser.setId(2L);

        Game testGame = new Game();
        // List of player ID's
        List<Long> testPlayerList = new ArrayList<Long>(){
            {
                add(2L);
                add(3L);
                add(5L);
            }};

        testGame = new Game();
        testGame.setPlayerList(testPlayerList);
        testGame.setId(1L);
        testGame.setHost("Hans");
        testGame.setCurrentPlayer(2);

        given(userService.getUseryById(Mockito.any())).willReturn(testUser);
        given(gameService.getGameById(Mockito.any())).willReturn(testGame);

        // when
        MockHttpServletRequestBuilder putRequest = put("/game/{id}/wins", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(gamePostDTO));

        // then
        mockMvc.perform(putRequest).andExpect(status().isNoContent());
        Mockito.verify(gameService, Mockito.times(1)).removePlayerFromPlayerList(Mockito.any(), Mockito.any());
    }
    // DELETE test for successfully deleting a Game
    @Test
    public void deleteGame_succesfully() throws Exception {
        // given

        List<String> testPlayerListForLobby = new ArrayList<String>(){
            {
                add("Hans");
                //       add("Jörg");
                //      add("Peter");
            }};

        User testUser = new User();
        testUser.setId(2L);
        testUser.setUsername("Hans");

        Lobby testLobby = new Lobby();
        testLobby.setId(1L);
        //   testLobby.setName("testName");
        //  testLobby.setPassword("testPassword");
        testLobby.setHost("testHost");
        testLobby.setPlayerList(testPlayerListForLobby);

        Game testGame = new Game();

        List<String> testCardStack = new ArrayList<String >(){
            {
                add("ACE");
                add("KING");
                add("QUEEN");
            }};

        // List of player ID's
        List<Long> testPlayerList = new ArrayList<Long>(){
            {
                add(2L);
                add(4L);
                add(6L);
            }};

        testGame.setId(1L);
        testGame.setHost("testHost");
        testGame.setPlayerList(testPlayerList);

        given(gameService.convertUserNamesToIds(1L)).willReturn(testPlayerList);
        given(lobbyService.getLobbyById(Mockito.any())).willReturn(testLobby);
        given(gameService.createGame(Mockito.any())).willReturn(testGame);
        given(gameService.getGameById(Mockito.any())).willReturn(testGame);

        MockHttpServletRequestBuilder deleteRequest = delete("/game/{id}/deletion", 1L);

        mockMvc.perform(deleteRequest)
                .andExpect(status().isOk());}





    /**
     * Helper Method to convert userPostDTO into a JSON string such that the input can be processed
     * Input will look like this: {"name": "Test User", "username": "testUsername"}
     * @param object
     * @return string
     */
    private String asJsonString(final Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        }
        catch (JsonProcessingException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format("The request body could not be created.%s", e.toString()));
        }
    }

}

