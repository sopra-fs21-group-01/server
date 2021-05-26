package ch.uzh.ifi.hase.soprafs21.service;

import ch.uzh.ifi.hase.soprafs21.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs21.entity.Chat;
import ch.uzh.ifi.hase.soprafs21.entity.Lobby;
import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.exceptions.DuplicatedUserException;
import ch.uzh.ifi.hase.soprafs21.repository.GameRepository;
import ch.uzh.ifi.hase.soprafs21.repository.LobbyRepository;
import ch.uzh.ifi.hase.soprafs21.repository.ChatRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.server.ResponseStatusException;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@WebAppConfiguration
@SpringBootTest
public class ChatServiceIntegrationTest {

    @Qualifier("chatRepository")
    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private ChatService chatService;

    @BeforeEach
    public void setup() {chatRepository.deleteAll();}

    // test for valid Chat creation
    @Test
    public void createChat_validInputs_success() {
        // given
        assertThrows(NoSuchElementException.class, () -> {chatRepository.findById(1L).get();});


        Chat testChat = new Chat();
        testChat.setId(3L);
        testChat.setlobby(2L);
        testChat.setMessage("I am a test message");
        testChat.setTimestamp("01/01/2021");

        Chat createdChat = chatService.createChat(testChat);

        // then
        assertEquals(testChat.getId(), createdChat.getId());
        assertEquals(testChat.getlobby(), createdChat.getlobby());
        assertEquals(testChat.getMessage(), createdChat.getMessage());
        assertEquals(testChat.getTimestamp(), createdChat.getTimestamp());
    }


    // If a message was posted (with an Id), it should result in an error if a second message is posted
    @Test
    public void createChat_duplicateID_throwsException() {
        // given
        assertThrows(NoSuchElementException.class, () -> {chatRepository.findById(1L).get();});

        Chat testChat = new Chat();
        testChat.setId(1L);
        testChat.setlobby(2L);
        testChat.setMessage("I am a test message");
        testChat.setTimestamp("01/01/2021");

        Chat createdChat = chatService.createChat(testChat);

        Chat testChat2 = new Chat();
        testChat.setId(1L);
        testChat.setlobby(2L);
        testChat.setMessage("I am a test message");
        testChat.setTimestamp("01/01/2021");

        // check that an error is thrown
        assertThrows(DataIntegrityViolationException.class, () ->   chatService.createChat(testChat2));
    }

}
