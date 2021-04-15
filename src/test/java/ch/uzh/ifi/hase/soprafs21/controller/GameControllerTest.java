package ch.uzh.ifi.hase.soprafs21.controller;

import ch.uzh.ifi.hase.soprafs21.entity.Game;
import ch.uzh.ifi.hase.soprafs21.rest.dto.GamePostDTO;
import ch.uzh.ifi.hase.soprafs21.service.GameService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
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

    /**

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GameService gameService;

    // Test valid post, returns Lobby location as string
    @Test
    public void createGame_validInput_gameCreated() throws Exception {
        // given
        Game testGame = new Game();

        ArrayList<String> testCardStack = new ArrayList<String>();
        testCardStack.add("ACE");
        testCardStack.add("KING");
        testCardStack.add("QUEEN");

        String[] testPlayerList = {"Just", "Some", "Names"};

        testGame.setId(1L);
        testGame.setHost("testHost");
        testGame.setPlayerList(testPlayerList);
        testGame.setCardStack(testCardStack);



        given(gameService.createGame(Mockito.any())).willReturn(testGame);

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder postRequest = post("/game/{id}/kickOff", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(testGame));

        // then !! Just Check for the expected String output and for the status type
        mockMvc.perform(postRequest)
                .andExpect(status().isCreated())
                .andExpect(content().string("game/"+testGame.getId()+"/kickOff"));
    }


    // Test invalid post, no hostname given, Conflict is thrown
    @Test
    public void createGame_invalidInput_gameCreationUnsuccessful() throws Exception {
        // given
        Game testGame = new Game();

        ArrayList<String> testCardStack = new ArrayList<String>();
        testCardStack.add("ACE");
        testCardStack.add("KING");
        testCardStack.add("QUEEN");

        String[] testPlayerList = {"Just", "Some", "Names"};

        testGame.setId(1L);
        testGame.setHost("testHost");
        testGame.setPlayerList(testPlayerList);
        testGame.setCardStack(testCardStack);


        given(gameService.createGame(Mockito.any())).willThrow(new ResponseStatusException(HttpStatus.CONFLICT));

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder postRequest = post("/game/{id}/kickOff", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(testGame));

        // then !! Just Check for the expected String output and for the status type
        mockMvc.perform(postRequest)
                .andExpect(status().isConflict())
        ;
    }


    // tests the GET for single Lobby with valid id. Test for Status and Output content
    @Test
    public void getGame_whenGetGame_thenReturnJsonArray() throws Exception {
        // given
        Game testGame = new Game();

        ArrayList<String> testCardStack = new ArrayList<String>();
        testCardStack.add("ACE");
        testCardStack.add("KING");
        testCardStack.add("QUEEN");

        String[] testPlayerList = {"Just", "Some", "Names"};

        testGame.setId(1L);
        testGame.setHost("testHost");
        testGame.setPlayerList(testPlayerList);
        testGame.setCardStack(testCardStack);

        // this mocks the UserService -> we define above what the userService should return when getUsers() is called
        given(gameService.getGameById(Mockito.any())).willReturn(testGame);

        // when
        MockHttpServletRequestBuilder getRequest = get("/game/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON);

        // then
        mockMvc.perform(getRequest).andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(testGame.getId().intValue())))
                .andExpect(jsonPath("$.host", is(testGame.getHost())));

                /** HOW TO YOU TEST FOR ARRAYLISTS????
                 *
                .andExpect(jsonPath("$.playerList", is(testGame.getPlayerList())))
                .andExpect(jsonPath("$.cardStack", is(testGame.getCardStack())));


    }


    // tests the  GET for single lobby with invalid input. Test if if status is right
    @Test
    public void getGame_invalidID_throwsException() throws Exception {
        // given
        Game testGame = new Game();

        ArrayList<String> testCardStack = new ArrayList<String>();
        testCardStack.add("ACE");
        testCardStack.add("KING");
        testCardStack.add("QUEEN");

        String[] testPlayerList = {"Just", "Some", "Names"};

        testGame.setId(1L);
        testGame.setHost("testHost");
        testGame.setPlayerList(testPlayerList);
        testGame.setCardStack(testCardStack);


        // this mocks the GameService -> we define above what the gameService should return when getUsers() is called
        given(gameService.getGameById(Mockito.any())).willThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Game with given ID was not found"));

        // when
        MockHttpServletRequestBuilder getRequest = get("/game/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON);
        // then
        mockMvc.perform(getRequest).andExpect(status().isNotFound())
        ; }


    // tests valid PUT method for Game update. test Status
    @Test
    public void updateGame_validInput_returnsNothing() throws Exception {
        // given
        Game testGame = new Game();

        ArrayList<String> testCardStack = new ArrayList<String>();
        testCardStack.add("ACE");
        testCardStack.add("KING");
        testCardStack.add("QUEEN");

        String[] testPlayerList = {"Just", "Some", "Names"};

        testGame.setId(1L);
        testGame.setHost("testHost");
        testGame.setPlayerList(testPlayerList);
        testGame.setCardStack(testCardStack);


        given(gameService.updateGame(Mockito.any())).willReturn(testGame);
        given(gameService.getGameById(Mockito.any())).willReturn(testGame);

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder putRequest = put("/game/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(testGame));

        // then !!! Just check for NULL output and status code
        mockMvc.perform(putRequest)
                .andExpect(status().isNoContent());}


    // tests invalid PUT method for Game update with not existing lobby
    @Test
    public void updateGame_invalidID() throws Exception {
        // given
        Game testGame = new Game();

        ArrayList<String> testCardStack = new ArrayList<String>();
        testCardStack.add("ACE");
        testCardStack.add("KING");
        testCardStack.add("QUEEN");

        String[] testPlayerList = {"Just", "Some", "Names"};

        testGame.setId(1L);
        testGame.setHost("testHost");
        testGame.setPlayerList(testPlayerList);
        testGame.setCardStack(testCardStack);


        given(gameService.updateGame(Mockito.any())).willThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Lobby with given ID was not found"));
        given(gameService.getGameById(Mockito.any())).willReturn(testGame);

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder putRequest = put("/game/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(testGame));

        // then !!! Just check for NULL output and status code
        mockMvc.perform(putRequest)
                .andExpect(status().isNotFound());}


    // test for successfully deleting a Game
    @Test
    public void deleteGame_succesfully() throws Exception {
        // given
        Game testGame = new Game();

        ArrayList<String> testCardStack = new ArrayList<String>();
        testCardStack.add("ACE");
        testCardStack.add("KING");
        testCardStack.add("QUEEN");

        String[] testPlayerList = {"Just", "Some", "Names"};

        testGame.setId(1L);
        testGame.setHost("testHost");
        testGame.setPlayerList(testPlayerList);
        testGame.setCardStack(testCardStack);

        given(gameService.getGameById(Mockito.any())).willReturn(testGame);

        MockHttpServletRequestBuilder deleteRequest = delete("/game/{id}/deletion", 1L);

        mockMvc.perform(deleteRequest)
                .andExpect(status().isOk());}

    /**
     // test for unsuccesfully deleting a Lobby beacuse of invalid id
     @Test
     public void deleteLobby_unsuccesfully_invalidID() throws Exception {
     Lobby testLobby = new Lobby();
     testLobby.setId(1L);
     testLobby.setName("testName");
     testLobby.setPassword("testPassword");
     testLobby.setHost("testHost");

     given(lobbyService.getLobbyById(Mockito.any())).willThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Lobby with given ID was not found"));

     mockMvc.perform(MockMvcRequestBuilders.delete("/lobby/{id}", 1))
     .andExpect(status().isNotFound());
     }
     */


    /**
     * Helper Method to convert userPostDTO into a JSON string such that the input can be processed
     * Input will look like this: {"name": "Test User", "username": "testUsername"}
     * @param object
     * @return string

    private String asJsonString(final Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        }
        catch (JsonProcessingException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format("The request body could not be created.%s", e.toString()));
        }
    }
    */
}

