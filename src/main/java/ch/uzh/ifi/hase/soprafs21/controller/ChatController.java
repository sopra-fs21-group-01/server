package ch.uzh.ifi.hase.soprafs21.controller;
import ch.uzh.ifi.hase.soprafs21.entity.Chat;
import ch.uzh.ifi.hase.soprafs21.entity.Lobby;
import ch.uzh.ifi.hase.soprafs21.rest.dto.LobbyGetDTO;
import ch.uzh.ifi.hase.soprafs21.rest.dto.LobbyPostDTO;
import ch.uzh.ifi.hase.soprafs21.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs21.service.ChatService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
public class ChatController {

    private final ChatService chatService;

    ChatController(ChatService chatService){this.chatService = chatService;}

    // post a chat
    @PostMapping("/chat")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public String createChat(@RequestBody ChatPostDTO chatPostDTO){

        // convert API Lobby to internal representation
        Chat userInput = DTOMapper.INSTANCE.convertChatPostDTOtoEntity(chatPostDTO);

        // create Chat instance
        Chat createdChat = chatService.createChat(userInput);

        // return URL
        String url = "chat/" + createdChat.getId();
        return url;


    }

    // delete the Chat by its ID
    @DeleteMapping("/chat/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void deleteChat(@PathVariable(value = "id") Long id){

        chatService.deleteLobby(id);
    }

    // put mapping to update the chat
    @PutMapping("/chat/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    public void updateLobby(@PathVariable(value = "id") Long id, @RequestBody ChatPostDTO updatedChatDetails) {
        // convert API Lobby to internal representation
        Chat userInput = DTOMapper.INSTANCE.convertChatPostDTOtoEntity(updatedChatDetails);
        // update the chat
        chatService.updateChat(id,userInput);

    }

    // get mapping
    @GetMapping("/chat/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public LobbyGetDTO getChatcontent(@PathVariable(value = "id") Long id){

        // get Lobby from repository
        Chat chatOfId = chatService.getLobbyById(id);

        // convert internal representation of chat back to API
        return DTOMapper.INSTANCE.convertEntityToChatGetDTO(chatOfId);
    }







}
