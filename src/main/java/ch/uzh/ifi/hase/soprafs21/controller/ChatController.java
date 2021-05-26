package ch.uzh.ifi.hase.soprafs21.controller;

import ch.uzh.ifi.hase.soprafs21.entity.Chat;
import ch.uzh.ifi.hase.soprafs21.entity.Hand;
import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.rest.dto.*;
import ch.uzh.ifi.hase.soprafs21.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs21.service.ChatService;
import ch.uzh.ifi.hase.soprafs21.service.GameService;
import ch.uzh.ifi.hase.soprafs21.service.UserService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.json.*;

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


    @PostMapping("game/funTranslation")
    public ChatGetDTO funTranslation(@RequestBody ChatPostDTO chatPostDTO){

        List<String> languages = new ArrayList<String>(){{
            add("yoda");
            add("pirate");
            add("minion");
            add("dothraki");
            add("redneck");
            add("irish");
        }};
        Collections.shuffle(languages);

        final String url = "https://api.funtranslations.com/translate/"+ languages.get(0) +".json";

        String text = chatPostDTO.getMessage();
        String[] arrOfStr = text.split("/");
        String message = arrOfStr[1];

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        JSONObject json = new JSONObject();
        json.put("text", message);

        HttpEntity<String> request =
                new HttpEntity<String>(json.toString(), headers);

        RestTemplate restTemplate = new RestTemplate();

        FunPostDTO funPostDTO = restTemplate.postForObject(url, request, FunPostDTO.class);

        assert funPostDTO != null;
        String newMessage = arrOfStr[0] + "/" + funPostDTO.getContents().get("translated");
        chatPostDTO.setMessage(newMessage);

        Chat userInput = DTOMapper.INSTANCE.convertChatPostDTOtoEntity(chatPostDTO);
        Chat createdChat = chatService.createChat(userInput);

        return DTOMapper.INSTANCE.convertEntityToChatGetDTO(createdChat);


    }
}
