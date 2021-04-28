package ch.uzh.ifi.hase.soprafs21.controller;

import ch.uzh.ifi.hase.soprafs21.entity.Lobby;
import ch.uzh.ifi.hase.soprafs21.rest.dto.LobbyPostDTO;
import ch.uzh.ifi.hase.soprafs21.service.LobbyService;
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
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

@WebMvcTest(LobbyController.class)

public class LobbyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LobbyService lobbyService;

 // Test valid post, returns Lobby location as string
 @Test
 public void createLobby_validInput_lobbyCreated() throws Exception {
 // given
 Lobby testLobby = new Lobby();
 testLobby.setId(1L);
 //   testLobby.setName("testName");
 //   testLobby.setPassword("testPassword");
 testLobby.setHost("testHost");


 given(lobbyService.createLobby(Mockito.any())).willReturn(testLobby);

 // when/then -> do the request + validate the result
 MockHttpServletRequestBuilder postRequest = post("/lobbies")
 .contentType(MediaType.APPLICATION_JSON)
 .content(asJsonString(testLobby));

 // then !! Just Check for the expected String output and for the status type
 mockMvc.perform(postRequest)
 .andExpect(status().isCreated())
 .andExpect(content().string("lobbies/"+testLobby.getId()));
 }


 // Test invalid post, no hostname given, Conflict is thrown
 @Test
 public void createLobby_invalidInput_lobbycreationUnseccesfull() throws Exception {
 // given
 Lobby testLobby = new Lobby();
 testLobby.setId(1L);
 //  testLobby.setName("testName");
 //   testLobby.setPassword("testPassword");
 testLobby.setHost(null);


 given(lobbyService.createLobby(Mockito.any())).willThrow(new ResponseStatusException(HttpStatus.CONFLICT));

 // when/then -> do the request + validate the result
 MockHttpServletRequestBuilder postRequest = post("/lobbies")
 .contentType(MediaType.APPLICATION_JSON)
 .content(asJsonString(testLobby));

 // then !! Just Check for the expected String output and for the status type
 mockMvc.perform(postRequest)
 .andExpect(status().isConflict())
 ;
 }


 // tests the GET for single Lobby with valid id. Test for Status and Output content
 @Test
 public void singleUser_whenGetSingleLobby_thenReturnJsonArray() throws Exception {
 // given
 Lobby testLobby = new Lobby();
 testLobby.setId(1L);
 //  testLobby.setName("testName");
 //  testLobby.setPassword("testPassword");
 testLobby.setHost("testHost");

 // this mocks the UserService -> we define above what the userService should return when getUsers() is called
 given(lobbyService.getLobbyById(Mockito.any())).willReturn(testLobby);

 // when
 MockHttpServletRequestBuilder getRequest = get("/lobbies/{id}", 1L)
 .contentType(MediaType.APPLICATION_JSON);

 // then
 mockMvc.perform(getRequest).andExpect(status().isOk())
 .andExpect(jsonPath("$.id", is(testLobby.getId().intValue())))
 //    .andExpect(jsonPath("$.name", is(testLobby.getName())))
 //    .andExpect(jsonPath("$.password", is(testLobby.getPassword())))
 .andExpect(jsonPath("$.host", is(testLobby.getHost())));
 }


 // tests the  GET for single lobby with invalid input. Test if if status is right
 @Test
 public void getLobby_invalidID_throwsException() throws Exception {
 // given
 Lobby testLobby = new Lobby();
 testLobby.setId(1L);
 //   testLobby.setName("testName");
 //  testLobby.setPassword("testPassword");
 testLobby.setHost("testHost");


 // this mocks the UserService -> we define above what the userService should return when getUsers() is called
 given(lobbyService.getLobbyById(Mockito.any())).willThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Lobby with given ID was not found"));

 // when
 MockHttpServletRequestBuilder getRequest = get("/lobbies/{id}", 1L)
 .contentType(MediaType.APPLICATION_JSON);
 // then
 mockMvc.perform(getRequest).andExpect(status().isNotFound())
 ; }

 // test to get all lobbies
 @Test
 public void getAllLobbies_validReguest_getListOfLobbies() throws Exception{

 }

 // tests valid PUT method for lobby update. test Status
 @Test
 public void updateLobby_validInput_returnsNothing() throws Exception {

 Lobby testLobby = new Lobby();
 testLobby.setId(1L);
 // testLobby.setName("testName");
 // testLobby.setPassword("testPassword");
 testLobby.setHost("testHost");


 given(lobbyService.updateLobby(Mockito.any())).willReturn(testLobby);
 given(lobbyService.getLobbyById(Mockito.any())).willReturn(testLobby);

 // when/then -> do the request + validate the result
 MockHttpServletRequestBuilder putRequest = put("/lobbies/{id}", 1L)
 .contentType(MediaType.APPLICATION_JSON)
 .content(asJsonString(testLobby));

 // then !!! Just check for NULL output and status code
 mockMvc.perform(putRequest)
 .andExpect(status().isNoContent());}


 // tests invalid PUT method for lobby update with not existing lobby
 @Test
 public void updateUser_invalidID() throws Exception {

 Lobby testLobby = new Lobby();
 testLobby.setId(1L);
 // testLobby.setName("testName");
 //  testLobby.setPassword("testPassword");
 testLobby.setHost("testHost");


 given(lobbyService.updateLobby(Mockito.any())).willThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Lobby with given ID was not found"));
 given(lobbyService.getLobbyById(Mockito.any())).willReturn(testLobby);

 // when/then -> do the request + validate the result
 MockHttpServletRequestBuilder putRequest = put("/lobbies/{id}", 1L)
 .contentType(MediaType.APPLICATION_JSON)
 .content(asJsonString(testLobby));

 // then !!! Just check for NULL output and status code
 mockMvc.perform(putRequest)
 .andExpect(status().isNotFound());}


 // test for succesfully deleting a Lobby
 @Test
 public void deleteLobby_succesfully() throws Exception {
 Lobby testLobby = new Lobby();
 testLobby.setId(1L);
 //  testLobby.setName("testName");
 //  testLobby.setPassword("testPassword");
 testLobby.setHost("testHost");

 given(lobbyService.getLobbyById(Mockito.any())).willReturn(testLobby);

 MockHttpServletRequestBuilder deleteRequest = delete("/lobbies/{id}", 1L);

 mockMvc.perform(deleteRequest)
 .andExpect(status().isOk());}


 // test for unsuccesfully deleting a Lobby beacuse of invalid id
 @Test
 public void deleteLobby_unsuccesfully_invalidID() throws Exception {
 Lobby testLobby = new Lobby();
 testLobby.setId(1L);
 // testLobby.setName("testName");
 // testLobby.setPassword("testPassword");
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
