package ch.uzh.ifi.hase.soprafs21.controller;

import ch.uzh.ifi.hase.soprafs21.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs21.entity.Chat;
import ch.uzh.ifi.hase.soprafs21.entity.Lobby;
import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.rest.dto.ChatPostDTO;
import ch.uzh.ifi.hase.soprafs21.rest.dto.FunPostDTO;
import ch.uzh.ifi.hase.soprafs21.rest.dto.LobbyPostDTO;
import ch.uzh.ifi.hase.soprafs21.rest.dto.PlayByNamePutDTO;
import ch.uzh.ifi.hase.soprafs21.service.ChatService;
import ch.uzh.ifi.hase.soprafs21.service.GameService;
import ch.uzh.ifi.hase.soprafs21.service.LobbyService;
import ch.uzh.ifi.hase.soprafs21.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.web.client.RestTemplate;
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

@WebMvcTest(ChatController.class)
public class ChatControllerTest {

    @Autowired
    private MockMvc mockMvc;


    @MockBean
    private ChatService chatService;

    @MockBean
    private GameService gameService;

    @MockBean
    private LobbyService lobbyService;

    @MockBean
    private UserService userService;

    @MockBean
    private Chat chat;

    @Mock
    private RestTemplate restTemplate;


    @Test
    public void createChat_validInput_ChatCreated() throws Exception {
        // given
        ChatPostDTO chatPostDTO = new ChatPostDTO();

        Chat testChat = new Chat();
        testChat.setId(1L);
        testChat.setlobby(2L);
        testChat.setMessage("I am a test message");
        testChat.setTimestamp("01/01/2021");

        given(chatService.createChat(Mockito.any())).willReturn(testChat);

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder postRequest = post("/chats")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(chatPostDTO));

        // then !! Just Check for the expected output output and for the status type
        mockMvc.perform(postRequest)
                .andExpect(status().isCreated())
        .andExpect(jsonPath("$.lobby", is(testChat.getlobby().intValue())))
        .andExpect(jsonPath("$.message", is(testChat.getMessage())))
        .andExpect(jsonPath("$.timestamp", is(testChat.getTimestamp())));
    }

    @Test
    public void createChat_inValidInput_throwsException() throws Exception {
        // given
        ChatPostDTO chatPostDTO = new ChatPostDTO();

        Chat testChat = new Chat();
        testChat.setId(1L);
        testChat.setlobby(2L);
        testChat.setMessage("I am a test message");
        testChat.setTimestamp("01/01/2021");

        given(chatService.createChat(Mockito.any())).willThrow( new ResponseStatusException(HttpStatus.CONFLICT));

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder postRequest = post("/chats")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(chatPostDTO));

        // then !! Just Check for the expected output output and for the status type
        mockMvc.perform(postRequest)
                .andExpect(status().isConflict());
    }

    //valid GET for all Chats, returns array
    @Test
    public void whenValidGetChats_thenReturnJsonArray() throws Exception {
        // given
        ChatPostDTO chatPostDTO = new ChatPostDTO();

        Chat testChat = new Chat();
        testChat.setId(1L);
        testChat.setlobby(2L);
        testChat.setMessage("I am a test message");
        testChat.setTimestamp("01/01/2021");

        List<Chat> allChats = Collections.singletonList(testChat);

        // this mocks the UserService -> we define above what the userService should return when getUsers() is called
        given(chatService.getChats(Mockito.any())).willReturn(allChats);

        // when
        MockHttpServletRequestBuilder getRequest = get("/chats/2").contentType(MediaType.APPLICATION_JSON);

        // then
        mockMvc.perform(getRequest).andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].lobby", is(testChat.getlobby().intValue())))
                .andExpect(jsonPath("$[0].message", is(testChat.getMessage())))
                .andExpect(jsonPath("$[0].timestamp", is(testChat.getTimestamp())))
              ;
    }

    //Invalid GET for all Chats, throw error
    @Test
    public void whenInvalidGetChats_thenThrowExcpetion() throws Exception {
        // given
        ChatPostDTO chatPostDTO = new ChatPostDTO();

        Chat testChat = new Chat();
        testChat.setId(1L);
        testChat.setlobby(2L);
        testChat.setMessage("I am a test message");
        testChat.setTimestamp("01/01/2021");

        List<Chat> allChats = Collections.singletonList(testChat);

        // this mocks the UserService -> we define above what the userService should return when getUsers() is called
        given(chatService.getChats(Mockito.any())).willThrow( new ResponseStatusException(HttpStatus.CONFLICT));

        // when
        MockHttpServletRequestBuilder getRequest = get("/chats/2").contentType(MediaType.APPLICATION_JSON);

        // then
        mockMvc.perform(getRequest).andExpect(status().isConflict())

        ;
    }

    // DELETE test for succesfully deleting a Chat
    @Test
    public void deleteLobby_succesfully() throws Exception {
        // given
        ChatPostDTO chatPostDTO = new ChatPostDTO();

        Chat testChat = new Chat();
        testChat.setId(1L);
        testChat.setlobby(2L);
        testChat.setMessage("I am a test message");
        testChat.setTimestamp("01/01/2021");

        MockHttpServletRequestBuilder deleteRequest = delete("/chats/1", 1L);

        mockMvc.perform(deleteRequest)
                .andExpect(status().isOk());}

    // test if external API is called. Test if a chat was created and if it has right timestamp and lobbyid
    @Test
    public void externalAPI_FunTranslation_validInput_ChatCreated() throws Exception {
        // given
        ChatPostDTO chatPostDTO = new ChatPostDTO();
        chatPostDTO.setMessage("Im a test message for the postDTO");

        FunPostDTO funPostDTO = new FunPostDTO();

        Chat testChat = new Chat();
        testChat.setId(1L);
        testChat.setlobby(2L);
        testChat.setMessage("I am a test message");
        testChat.setTimestamp("01/01/2021");

        given(chatService.createChat(Mockito.any())).willReturn(testChat);
        given(restTemplate.postForObject(Mockito.any(), Mockito.any(), Mockito.any())).willReturn(funPostDTO);


        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder postRequest = post("/chat/funTranslation")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(chatPostDTO));

        // then !! Just Check for the expected output output and for the status type
        mockMvc.perform(postRequest)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.lobby", is(testChat.getlobby().intValue())))
                .andExpect(jsonPath("$.timestamp", is(testChat.getTimestamp())));

    }
    // send an empty message to the external API will give a 400 with text "text is missing"
    @Test
    public void externalAPI_FunTranslation_invalidInput_noMessage_badRequest() throws Exception {
        // given (chatPostDTO has no message)
        ChatPostDTO chatPostDTO = new ChatPostDTO();
        FunPostDTO funPostDTO = new FunPostDTO();

        given(restTemplate.postForObject(Mockito.any(), Mockito.any(), Mockito.any())).willReturn(funPostDTO);

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder postRequest = post("/chat/funTranslation")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(chatPostDTO));

        // then !! Just Check for the expected output output and for the status type
        mockMvc.perform(postRequest)
                .andExpect(status().isBadRequest());

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