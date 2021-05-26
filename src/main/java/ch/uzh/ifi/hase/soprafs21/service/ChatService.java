package ch.uzh.ifi.hase.soprafs21.service;

import ch.uzh.ifi.hase.soprafs21.entity.Chat;
import ch.uzh.ifi.hase.soprafs21.repository.ChatRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@Transactional
@Qualifier("chatService")
public class ChatService {

    private final Logger log = LoggerFactory.getLogger(ChatService.class);
    private final ChatRepository chatRepository;

    @Autowired
    public ChatService(@Qualifier("chatRepository") ChatRepository chatRepository){
        this.chatRepository = chatRepository;
    }

    public Chat createChat(Chat newChat){

        newChat = chatRepository.save(newChat);
        chatRepository.flush();

        log.debug("Created a new Chat for the game: {}");

        return newChat;

    }

    public List<Chat> getChats(Long id){
        if(this.chatRepository.findAllBylobby(id).size() > 0){
            return this.chatRepository.findAllBylobby(id);
        }else{
            throw new ResponseStatusException(HttpStatus.CONFLICT, "The chat with this id doesn't exist");
        }
    }


    public void deleteChat(Long id) {
        chatRepository.deleteAllBylobby(id);
    }

}
