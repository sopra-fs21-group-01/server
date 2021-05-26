package ch.uzh.ifi.hase.soprafs21.service;

import ch.uzh.ifi.hase.soprafs21.entity.Chat;
import ch.uzh.ifi.hase.soprafs21.entity.Lobby;

import ch.uzh.ifi.hase.soprafs21.repository.ChatRepository;
import ch.uzh.ifi.hase.soprafs21.repository.LobbyRepository;
import com.sun.jdi.CharValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

public class ChatServiceTest {

    @Mock
    private ChatRepository chatRepository;

    @InjectMocks
    private ChatService chatService;

    @Mock
    private Chat testChat;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        testChat = new Chat();
        testChat.setId(1L);
        testChat.setlobby(2L);
        testChat.setMessage("I am a test message");
        testChat.setTimestamp("01/01/2021");

        // when -> any object is being save in the lobbyRepository -> return the dummy testLobby
        Mockito.when(chatRepository.save(Mockito.any())).thenReturn(testChat);

    }

    // test creation of chat with valid inputs
    @Test
    public void createChat_validInputs_success(){
        // save the dummy chat to database and return it
        Chat createdChat = chatService.createChat(testChat);

        // then test if the data was correctly saved
        Mockito.verify(chatRepository, Mockito.times(1)).save(Mockito.any());

        assertEquals(testChat.getId(), createdChat.getId());
        assertEquals(testChat.getlobby(), createdChat.getlobby());
        assertEquals(testChat.getMessage(), createdChat.getMessage());
        assertEquals(testChat.getTimestamp(), createdChat.getTimestamp());

    }

    // test for wrong host
    @Test
    public void getAllChats_InvalidID_exception(){
        testChat.setId(5L);
        assertThrows(ResponseStatusException.class, () -> chatService.getChats(testChat.getId()));
    }
    }

