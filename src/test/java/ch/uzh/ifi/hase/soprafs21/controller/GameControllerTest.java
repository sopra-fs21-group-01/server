package ch.uzh.ifi.hase.soprafs21.controller;

import ch.uzh.ifi.hase.soprafs21.entity.Game;
import ch.uzh.ifi.hase.soprafs21.entity.Lobby;
import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.rest.dto.GamePostDTO;
import ch.uzh.ifi.hase.soprafs21.rest.dto.LobbyPostDTO;
import ch.uzh.ifi.hase.soprafs21.service.GameService;
import ch.uzh.ifi.hase.soprafs21.service.LobbyService;
import ch.uzh.ifi.hase.soprafs21.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.*;
import static org.hamcrest.Matchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

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
    private Lobby lobby;




    // POST Test valid post, returns Lobby location as string
    @Test
    public void createGame_validInput_gameCreated() throws Exception {
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
          //      add(4L);
          //      add(6L);
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


    // GET tests the GET for retreiving the playerList of a game
    // -> How to control one return that is not a json File?
    @Test
    public void getGame_whenGetPlayerlist_thenReturnArrayWithNames() throws Exception {
        List<String> testPlayerListForLobby = new ArrayList<String>(){
            {
                add("Hans");
                add("Jörg");
                add("Peter");
            }};

        Lobby testLobby = new Lobby();
        testLobby.setId(1L);
        //   testLobby.setName("testName");
        //  testLobby.setPassword("testPassword");
        testLobby.setHost("testHost");
        testLobby.setPlayerList(testPlayerListForLobby);

        // List of player ID's
        List<Long> testPlayerList = new ArrayList<Long>(){
            {
                add(2L);
                add(4L);
                add(6L);
            }};

        given(lobbyService.getLobbyById(Mockito.any())).willReturn(testLobby);
        given(lobby.getPlayerList()).willReturn(testPlayerListForLobby);

        // when
        MockHttpServletRequestBuilder getRequest = get("/game/{id}/kickOff", 1L)
                .contentType(MediaType.APPLICATION_JSON);

        // then
        mockMvc.perform(getRequest).andExpect(status().isOk())
                ;

    }


    // GET tests the  GET for single game playerList with invalid input. Test if if status is right
    @Test
    public void getPlayerListoffGame_invalidID_throwsException() throws Exception {
        List<String> testPlayerListForLobby = new ArrayList<String>(){
            {
                add("Hans");
                add("Jörg");
                add("Peter");
            }};

        Lobby testLobby = new Lobby();
        testLobby.setId(1L);
        //   testLobby.setName("testName");
        //  testLobby.setPassword("testPassword");
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
    // -> How to control one return that is not a json File?
    @Test
    public void getGame_whenGetHandOfPlayer_thenReturnArrayWithCardNames() throws Exception {
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
        testGame.setCurrentPlayer(0);

        given(gameService.convertUserNamesToIds(1L)).willReturn(testPlayerList);
        given(lobbyService.getLobbyById(Mockito.any())).willReturn(testLobby);
        given(gameService.createGame(Mockito.any())).willReturn(testGame);
        given(lobbyService.getLobbyById(Mockito.any())).willReturn(testLobby);
        given(lobby.getPlayerList()).willReturn(testPlayerListForLobby);
        given(gameService.createGame(Mockito.any())).willReturn(testGame);

        given(gameService.updateGame(Mockito.any())).willReturn(testGame);
        given(gameService.getGameById(Mockito.any())).willReturn(testGame);

        // when
        MockHttpServletRequestBuilder getRequest = get("/game/{id}/kickOff/currentPlayerIds", 1L)
                .contentType(MediaType.APPLICATION_JSON);

        // then
        mockMvc.perform(getRequest).andExpect(status().isOk())

        ;

    }

    // GET tests the GET for the ID of the current player
    // -> How to control one return that is not a json File?
    @Test
    public void getGame_whenGetHandOfPlayer_InvalidIdWillReturnError() throws Exception {
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
        testGame.setCurrentPlayer(0);

        given(gameService.getGameById(Mockito.any())).willThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Game with given ID was not found"));
        // when
        MockHttpServletRequestBuilder getRequest = get("/game/{id}/kickOff/currentPlayerIds", 1L)
                .contentType(MediaType.APPLICATION_JSON);

        // then
        mockMvc.perform(getRequest).andExpect(status().isNotFound())

        ;

    }

    // PUT tests valid PUT method for Playerturn, returns a game
    @Test
    public void updateGame_validInput_returnsNothing() throws Exception {

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

