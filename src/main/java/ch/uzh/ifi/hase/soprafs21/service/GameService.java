package ch.uzh.ifi.hase.soprafs21.service;


import ch.uzh.ifi.hase.soprafs21.constant.Color;
import ch.uzh.ifi.hase.soprafs21.entity.Card;
import ch.uzh.ifi.hase.soprafs21.entity.Deck;
import ch.uzh.ifi.hase.soprafs21.entity.Game;
import ch.uzh.ifi.hase.soprafs21.repository.GameRepository;
import ch.uzh.ifi.hase.soprafs21.repository.UserRepository;
import org.apache.catalina.Host;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.Optional;


@Service
@Transactional
public class GameService {

    private final Logger log = LoggerFactory.getLogger(GameService.class);

    private final GameRepository gameRepository;

    @Autowired
    public GameService(@Qualifier("gameRepository") GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    // the host is added to the list of players, the gamemode is set to standard
    public Game createGame(Game newGame){

        // newGame.setGamemode("standard");
        Deck deck = new Deck();
        newGame.setCardStack(deck.getCardDeck());


        newGame.initializeHands();

        newGame = gameRepository.save(newGame);
        gameRepository.flush();

        log.debug("Created a new lobby for Host: {}", newGame.getHost());

        return newGame;
    }

    public Game getGameById(Long gameID){
        Game gameFoundById = null;

        // check if there is a lobby with this ID and return it. If no lobby found, throw exception
        Optional<Game> optionalLobby = this.gameRepository.findById(gameID);

        if (optionalLobby.isPresent()){
            gameFoundById = optionalLobby.get();

            log.debug("Found and returned Game with ID: {}", gameID);
            return gameFoundById;
        }
        if (gameFoundById == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Game with given ID was not found");}
        return gameFoundById;
    }

    public Game updateGame(Game updatedGame){
        this.gameRepository.save(updatedGame);

        log.debug("Updated Game with ID {}", updatedGame.getId());
        return updatedGame;
    }

    public void deleteGame(Long gameId){

        Game dummyLobby = getGameById(gameId);
        gameRepository.deleteById(gameId);

        log.debug("Deleted the game with ID: {}", gameId);
    }

    public boolean checkIfMoveAllowed(Game game, int hand, Card card){
        Card lastPlayedCard = game.getLastPlayedCard();

        if (lastPlayedCard == null){
            return true;
        }
        else if (lastPlayedCard.getColor() == card.getColor()){
            return true;
        }
        else if (lastPlayedCard.getValue() == card.getValue()){
            return true;
        }
        else if (lastPlayedCard.getColor() != Color.Wild && card.getColor()== Color.Wild){
            return true;
        }
        else {
            return false;
        }

    }
}