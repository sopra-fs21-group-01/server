package ch.uzh.ifi.hase.soprafs21.controller;
import ch.uzh.ifi.hase.soprafs21.entity.Chat;
import ch.uzh.ifi.hase.soprafs21.entity.Game;
import ch.uzh.ifi.hase.soprafs21.rest.dto.ChatGetDTO;
import ch.uzh.ifi.hase.soprafs21.rest.dto.ChatPostDTO;
import ch.uzh.ifi.hase.soprafs21.rest.dto.GamePostDTO;
import ch.uzh.ifi.hase.soprafs21.rest.dto.LobbyGetDTO;
import ch.uzh.ifi.hase.soprafs21.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs21.service.ChatService;
import ch.uzh.ifi.hase.soprafs21.service.GameService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class ChatController {

    private final ChatService chatService;

    ChatController(ChatService chatService){this.chatService = chatService;

    }

    

    // post a chat
    @PostMapping("/chat")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public void createChat(@RequestBody ChatPostDTO chatPostDTO){

        // convert API Lobby to internal representation
        Chat userInput = DTOMapper.INSTANCE.convertChatPostDTOtoEntity(chatPostDTO);
        // create Chat instance
        chatService.createChat(userInput);

    }

    // delete the Chat by its ID
    @DeleteMapping("/chat/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void deleteChat(@PathVariable(value = "id") Long id){

        chatService.deleteChat(id);
    }

    // get mapping
    @GetMapping("/chat/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<ChatGetDTO> getChatContent(@PathVariable(value = "id") Long id){
        // fetch all chats in the internal representation
        List<Chat> chats = chatService.getChats(id);
        List<ChatGetDTO> chatGetDTOs = new ArrayList<>();

        // convert each Chat to the Api representation
        for(Chat chat : chats){
            chatGetDTOs.add(DTOMapper.INSTANCE.convertEntityToChatGetDTO(chat));
        }


        return chatGetDTOs;
    }







}
