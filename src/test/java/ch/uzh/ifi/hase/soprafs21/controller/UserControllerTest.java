package ch.uzh.ifi.hase.soprafs21.controller;

import ch.uzh.ifi.hase.soprafs21.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs21.entity.Hand;
import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.rest.dto.UserPostDTO;
import ch.uzh.ifi.hase.soprafs21.rest.dto.UserTokenDTO;
import ch.uzh.ifi.hase.soprafs21.service.GameService;
import ch.uzh.ifi.hase.soprafs21.service.LobbyService;
import ch.uzh.ifi.hase.soprafs21.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * UserControllerTest
 * This is a WebMvcTest which allows to test the UserController i.e. GET/POST request without actually sending them over the network.
 * This tests if the UserController works.
 */
@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GameService gameService;

    @MockBean
    private LobbyService lobbyService;

    @MockBean
    private UserService userService;



    // GET for all Users
    @Test
    public void givenUsers_whenGetUsers_thenReturnJsonArray() throws Exception {
        // given
        User user = new User();
        user.setUsername("testUsername");
        user.setEmail("test@uzh.ch");
        user.setPassword("Test1234");
        user.setStatus(UserStatus.OFFLINE);

        List<User> allUsers = Collections.singletonList(user);

        // this mocks the UserService -> we define above what the userService should return when getUsers() is called
        given(userService.getUsers()).willReturn(allUsers);

        // when
        MockHttpServletRequestBuilder getRequest = get("/users").contentType(MediaType.APPLICATION_JSON);

        // then
        mockMvc.perform(getRequest).andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].username", is(user.getUsername())))
                .andExpect(jsonPath("$[0].email", is(user.getEmail())))
                .andExpect(jsonPath("$[0].password", is(user.getPassword())))
                .andExpect(jsonPath("$[0].status", is(user.getStatus().toString())));
    }

    // POST for registration
    @Test
    public void createUser_validInput_userCreated() throws Exception {
        // given
        User user = new User();
        user.setId(1L);
        user.setUsername("testUsername");
        user.setEmail("test@uzh.ch");
        user.setPassword("Test1234");
        user.setToken("1");
        user.setStatus(UserStatus.ONLINE);

        UserPostDTO userPostDTO = new UserPostDTO();
        userPostDTO.setUsername("testUsername");
        userPostDTO.setEmail("test@uzh.ch");
        userPostDTO.setPassword("Test1234");
        

        given(userService.createUser(Mockito.any())).willReturn(user);

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder postRequest = post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userPostDTO));

        // then
        mockMvc.perform(postRequest)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(user.getId().intValue())))
                .andExpect(jsonPath("$.username", is(user.getUsername())))
                .andExpect(jsonPath("$.email", is(user.getEmail())))
                .andExpect(jsonPath("$.password", is(user.getPassword())))
                .andExpect(jsonPath("$.status", is(user.getStatus().toString())));
    }
    // POST for registration with invalid data
    @Test
    public void createUser_invalidInput_throwsException() throws Exception {
        // given
        User user = new User();
        user.setId(1L);
        user.setUsername("testUsername");
        user.setEmail("test@uzh.ch");
        user.setPassword("Test1234");
        user.setToken("1");
        user.setStatus(UserStatus.ONLINE);

        UserPostDTO userPostDTO = new UserPostDTO();
        userPostDTO.setUsername("testUsername");
        userPostDTO.setEmail("test@uzh.ch");
        userPostDTO.setPassword("Test1234");


        given(userService.createUser(Mockito.any())).willThrow( new ResponseStatusException(HttpStatus.CONFLICT));

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder postRequest = post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userPostDTO));

        // then
        mockMvc.perform(postRequest)
                .andExpect(status().isConflict());

    }

    // POST for Login
    @Test
    public void LoginUser_validInput_userIsReturned() throws Exception {
        // given
        User user = new User();
        user.setId(1L);
        user.setUsername("testUsername");
        user.setEmail("test@uzh.ch");
        user.setPassword("Test1234");
        user.setToken("1");
        user.setStatus(UserStatus.ONLINE);

        UserPostDTO userPostDTO = new UserPostDTO();
        userPostDTO.setUsername("testUsername");
        userPostDTO.setEmail("test@uzh.ch");
        userPostDTO.setPassword("Test1234");


        given(userService.login(Mockito.any())).willReturn(user);

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder postRequest = post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userPostDTO));

        // then
        mockMvc.perform(postRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(user.getId().intValue())))
                .andExpect(jsonPath("$.username", is(user.getUsername())))
                .andExpect(jsonPath("$.email", is(user.getEmail())))
                .andExpect(jsonPath("$.password", is(user.getPassword())))
                .andExpect(jsonPath("$.status", is(user.getStatus().toString())));
    }

    // GET single User
    @Test
    public void getSingleUser_validID() throws Exception {
        // given
        User user = new User();
        user.setId(1L);
        user.setUsername("testUsername");
        user.setEmail("test@uzh.ch");
        user.setPassword("Test1234");
        user.setToken("1");
        user.setStatus(UserStatus.ONLINE);

        given(userService.getUseryById(Mockito.any())).willReturn(user);

        // when
        MockHttpServletRequestBuilder getRequest = get("/users/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON);

        // then
        mockMvc.perform(getRequest)
                .andExpect(status().isOk())
         ;
    }

  // GET test to check if User gets his hand with right id and cards. test also method calls
   @Test
   public void getHand_validID() throws Exception {
      // given
   User user = new User();
        user.setId(1L);
        user.setUsername("testUsername");
        user.setEmail("test@uzh.ch");
        user.setPassword("Test1234");
        user.setToken("1");
        user.setStatus(UserStatus.ONLINE);

    Hand hand = new Hand();
        hand.setId(1L);
    List<String> handList = new ArrayList<>();
    handList.add("red/8");
    handList.add(("blue/1"));
    hand.setCards(handList);

    given(userService.getUseryById(Mockito.any())).willReturn(user);
    given(gameService.getHandById(Mockito.any())).willReturn(hand);

    // when
    MockHttpServletRequestBuilder getRequest = get("/users/{id}/hands", 1L)
            .contentType(MediaType.APPLICATION_JSON);

    mockMvc.perform(getRequest)
            .andExpect(status().isOk())
            .andExpect(jsonPath("id", is(1)))
            .andExpect(jsonPath("$.cards[0]", is("red/8")));


        Mockito.verify(userService, Mockito.times(1)).getUseryById(Mockito.any());
        Mockito.verify(gameService, Mockito.times(1)).getHandById(Mockito.any());

    ;}


    @Test
    // PUT method for User profile update. test Status
    public void updateUser_validInput_returnsNothing() throws Exception {
        // given
        User user = new User();
        user.setId(1L);
        user.setUsername("Test User");
        user.setStatus(UserStatus.ONLINE);

        UserPostDTO userPostDTO = new UserPostDTO();
        userPostDTO.setUsername("New testUsername");

        given(userService.logout(Mockito.any())).willReturn(user);
        given(userService.getUseryById(Mockito.any())).willReturn(user);

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder putRequest = put("/users/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userPostDTO));;



        // then !!! Just check for NULL output and status code
        mockMvc.perform(putRequest)
                .andExpect(status().isOk());
        Mockito.verify(userService, Mockito.times(1)).editUser(Mockito.any());
    }

    @Test
    // PUT method logingout User
    public void logoutUser_validInput_returnsNothing() throws Exception {
        // given
        User user = new User();
        user.setId(1L);
        user.setUsername("Test User");
        user.setStatus(UserStatus.ONLINE);

        UserTokenDTO userTokenDTO = new UserTokenDTO();

        given(userService.logout(Mockito.any())).willReturn(user);
        given(userService.getUseryById(Mockito.any())).willReturn(user);

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder putRequest = put("/logout", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userTokenDTO));;



        // then !!! Just check for NULL output and status code
        mockMvc.perform(putRequest)
                .andExpect(status().isOk());
        Mockito.verify(userService, Mockito.times(1)).logout(Mockito.any());
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

