package ch.uzh.ifi.hase.soprafs21.controller;

import ch.uzh.ifi.hase.soprafs21.entity.Chat;
import ch.uzh.ifi.hase.soprafs21.entity.Hand;
import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.rest.dto.*;
import ch.uzh.ifi.hase.soprafs21.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs21.service.ChatService;
import ch.uzh.ifi.hase.soprafs21.service.GameService;
import ch.uzh.ifi.hase.soprafs21.service.UserService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@Qualifier("chatController")
public class ChatController {

    private final ChatService chatService;

    ChatController(ChatService chatService) {
        this.chatService = chatService;
            }

    // GET for all chats
    @GetMapping("/chats/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<ChatGetDTO> getAllChats(@PathVariable(value = "id") Long id) {
        // fetch all chats in the internal representation
        List<Chat> chats = chatService.getChats(id);
        List<ChatGetDTO> chatGetDTOS = new ArrayList<>();

        // convert each user to the API representation
        for (Chat chat : chats) {
            chatGetDTOS.add(DTOMapper.INSTANCE.convertEntityToChatGetDTO(chat));
        }
        return chatGetDTOS;
    }

    // POST a new Chat
    @PostMapping("/chats")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public ChatGetDTO createChat(@RequestBody ChatPostDTO chatPostDTO) {
        // convert API user to internal representation
        Chat userInput = DTOMapper.INSTANCE.convertChatPostDTOtoEntity(chatPostDTO);

        // create user
        Chat createdChat = chatService.createChat(userInput);

        // convert internal representation of user back to API
        return DTOMapper.INSTANCE.convertEntityToChatGetDTO(createdChat);
    }

    // DELETE the Chat by its ID
    @DeleteMapping("/chats/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void deleteLobby(@PathVariable(value = "id") Long id){

        chatService.deleteChat(id);
    }
}
