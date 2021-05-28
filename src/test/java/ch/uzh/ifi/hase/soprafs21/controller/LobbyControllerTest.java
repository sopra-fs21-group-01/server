package ch.uzh.ifi.hase.soprafs21.controller;

import ch.uzh.ifi.hase.soprafs21.entity.Lobby;
import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.rest.dto.LobbyPostDTO;
import ch.uzh.ifi.hase.soprafs21.rest.dto.PlayByNamePutDTO;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.BDDMockito.given;


@WebMvcTest(LobbyController.class)

public class LobbyControllerTest {

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
   public void createLobby_validInput_lobbyCreated() throws Exception {
 // given
     LobbyPostDTO lobbyPostDTO = new LobbyPostDTO();


     Lobby testLobby = new Lobby();
     testLobby.setId(1L);
     //   testLobby.setName("testName");
     //   testLobby.setPassword("testPassword");
     testLobby.setHost("testHost")
;


 given(lobbyService.createLobby(Mockito.any())).willReturn(testLobby);

 // when/then -> do the request + validate the result
 MockHttpServletRequestBuilder postRequest = post("/lobbies")
 .contentType(MediaType.APPLICATION_JSON)
 .content(asJsonString(lobbyPostDTO));

 // then !! Just Check for the expected String output and for the status type
 mockMvc.perform(postRequest)
 .andExpect(status().isCreated())
 .andExpect(content().string("lobbies/"+testLobby.getId()));
 }


 // POST Test invalid post, no hostname given, Conflict is thrown
   @Test
   public void createLobby_invalidInput_lobbycreationUnseccesfull() throws Exception {
 // given
     LobbyPostDTO lobbyPostDTO = new LobbyPostDTO();

     Lobby testLobby = new Lobby();
     testLobby.setId(1L);
     //  testLobby.setName("testName");
     //   testLobby.setPassword("testPassword");
     testLobby.setHost(null);




 given(lobbyService.createLobby(Mockito.any())).willThrow(new ResponseStatusException(HttpStatus.CONFLICT));

 // when/then -> do the request + validate the result
 MockHttpServletRequestBuilder postRequest = post("/lobbies")
 .contentType(MediaType.APPLICATION_JSON)
 .content(asJsonString(lobbyPostDTO));

 // then !! Just Check for the expected String output and for the status type
 mockMvc.perform(postRequest)
 .andExpect(status().isConflict())
 ;
 }


 //GET tests the GET for single Lobby with valid id. Test for Status and Output content
   @Test
   public void singleLobby_whenGetSingleLobby_thenReturnJsonArray() throws Exception {
 // given
 Lobby testLobby = new Lobby();
 testLobby.setId(1L);
 testLobby.setHost("testHost");

 // this mocks the UserService -> we define above what the userService should return when getUsers() is called
 given(lobbyService.getLobbyById(Mockito.any())).willReturn(testLobby);

 // when
 MockHttpServletRequestBuilder getRequest = get("/lobbies/{id}", 1L)
 .contentType(MediaType.APPLICATION_JSON);

 // then
 mockMvc.perform(getRequest).andExpect(status().isOk())
 .andExpect(jsonPath("$.id", is(testLobby.getId().intValue())))
 .andExpect(jsonPath("$.host", is(testLobby.getHost())));
 }


 // GET tests the  GET for single lobby with invalid input. This will not throw the erro but catch it and return null
   @Test
   public void getLobby_invalidID_willReturnNull() throws Exception {
     Lobby testLobby = new Lobby();
     testLobby.setId(1L);
     testLobby.setHost("testHost");

     // this mocks the UserService -> we define above what the userService should return when getUsers() is called
     given(lobbyService.getLobbyById(Mockito.any())).willThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Lobby with given ID was not found"));

     MockHttpServletRequestBuilder getRequest = get("/lobbies/{id}", 2L)
             .contentType(MediaType.APPLICATION_JSON);
     // then
     mockMvc.perform(getRequest).andExpect(status().isOk());
     Mockito.verify(lobbyService, Mockito.times(1)).getLobbyById(Mockito.any());
 }

 // GET test to get all lobbies
   @Test
   public void getAllLobbies_validReguest_getListOfLobbies() throws Exception{
     Lobby testLobby = new Lobby();
     testLobby.setId(1L);
     testLobby.setHost("buddy");

     List<Lobby> allLobbies = Collections.singletonList(testLobby);

     given(lobbyService.getLobbies()).willReturn(allLobbies);

     MockHttpServletRequestBuilder getRequest = get("/lobbies").contentType(MediaType.APPLICATION_JSON);
     // then
     mockMvc.perform(getRequest).andExpect(status().isOk())
             .andExpect(jsonPath("$", hasSize(1)))
             .andExpect(jsonPath("$[0].id", is(1)))
             .andExpect(jsonPath("$[0].host", is(testLobby.getHost())))
             ;
 }

 //PUT tests valid PUT method for lobby update. test Status
   @Test
   public void updateLobby_validInput_returnsNothing() throws Exception {
     // given
     LobbyPostDTO lobbyPostDTO = new LobbyPostDTO();


     Lobby testLobby = new Lobby();
     testLobby.setId(1L);
     testLobby.setHost("testHost")
     ;

     given(lobbyService.updateLobby(Mockito.any())).willReturn(testLobby);
     given(lobbyService.getLobbyById(Mockito.any())).willReturn(testLobby);

 // when/then -> do the request + validate the result
     MockHttpServletRequestBuilder putRequest = put("/lobbies/{id}", 1L)
             .contentType(MediaType.APPLICATION_JSON)
             .content(asJsonString(lobbyPostDTO));

 // then !!! Just check for NULL output and status code
     mockMvc.perform(putRequest)
             .andExpect(status().isNoContent());}


 // PUT tests invalid PUT method for lobby update with not existing lobby
   @Test
   public void updateUser_invalidID() throws Exception {

     // given
     LobbyPostDTO lobbyPostDTO = new LobbyPostDTO();


     Lobby testLobby = new Lobby();
     testLobby.setId(1L);
     testLobby.setHost("testHost")
     ;

     given(lobbyService.updateLobby(Mockito.any())).willThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Lobby with given ID was not found"));
     given(lobbyService.getLobbyById(Mockito.any())).willReturn(testLobby);

 // when/then -> do the request + validate the result
     MockHttpServletRequestBuilder putRequest = put("/lobbies/{id}", 1L)
             .contentType(MediaType.APPLICATION_JSON)
             .content(asJsonString(lobbyPostDTO));

 // then !!! Just check for NULL output and status code
     mockMvc.perform(putRequest)
             .andExpect(status().isNotFound());}

 // PUT test for when a player succesfully joines a lobby
   @Test
   public void joinLobby_validInputAndValidLobby_returnsNothing() throws Exception {

     Lobby testLobby = new Lobby();
     testLobby.setId(1L);

     testLobby.setHost("testHost");

     PlayByNamePutDTO playByNamePutDTO = new PlayByNamePutDTO();
     playByNamePutDTO.setPlayerName("testuser");

     User testUser = new User();
     testUser.setId(12L);
     testUser.setUsername("testUser");

     given(lobbyService.getLobbyById(testLobby.getId())).willReturn(testLobby);
     given(userService.getUser(playByNamePutDTO.getPlayerName())).willReturn(testUser);
    ;

     // when/then -> do the request + validate the result
     MockHttpServletRequestBuilder putRequest = put("/lobbies/{id}/joinedLobbies", 1L)
             .contentType(MediaType.APPLICATION_JSON)
             .content(asJsonString(playByNamePutDTO));

     // then !!! Just check for NULL output and status code
     mockMvc.perform(putRequest)
             .andExpect(status().isNoContent());}

 // DELETE test for succesfully deleting a Lobby
   @Test
   public void deleteLobby_succesfully() throws Exception {
     Lobby testLobby = new Lobby();
       testLobby.setId(1L);
       testLobby.setHost("testHost");

       given(lobbyService.getLobbyById(Mockito.any())).willReturn(testLobby);
       MockHttpServletRequestBuilder deleteRequest = delete("/lobbies/{id}", 1L);

       mockMvc.perform(deleteRequest)
       .andExpect(status().isOk());}


 // DELETE test for unsuccesfully deleting a Lobby beacuse of invalid id
   @Test
   public void deleteLobby_unsuccesfully_invalidID() throws Exception {
     Lobby testLobby = new Lobby();
     testLobby.setId(1L);
     testLobby.setHost("testHost");

     given(lobbyService.getLobbyById(Mockito.any())).willThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Lobby with given ID was not found"));
     mockMvc.perform(MockMvcRequestBuilders.delete("/lobby/{id}", 1))
             .andExpect(status().isNotFound());
 }



    private String asJsonString(final Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        }
        catch (JsonProcessingException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format("The request body could not be created.%s", e.toString()));
        }
    }

}
