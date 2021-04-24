package ch.uzh.ifi.hase.soprafs21.service;


import ch.uzh.ifi.hase.soprafs21.constant.Color;
import ch.uzh.ifi.hase.soprafs21.constant.Value;
import ch.uzh.ifi.hase.soprafs21.entity.*;
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
    private final UserService userService;

    @Autowired
    public GameService(@Qualifier("gameRepository") GameRepository gameRepository, UserService userService) {
        this.gameRepository = gameRepository;
        this.userService = userService;
    }

    // the host is added to the list of players, the gamemode is set to standard
    public Game createGame(Game newGame){

        // newGame.setGamemode("standard");
        Deck deck = new Deck();
        newGame.setCardStack(deck);

         initializeHands(newGame);

        newGame = gameRepository.save(newGame);
        gameRepository.flush();

        log.debug("Created a new lobby for Host: {}", newGame.getHost());

        System.out.println("initilaized deck and hands succesfully");

        return newGame;
    }

    //initializes Hands for the start of the game,
    public void initializeHands(Game game){
        Deck deck = game.getCardStack();
        for (long player : game.getPlayerList()){
            Hand hand = new Hand(userService.getUseryById(player));
            for(int i=0; i<7; i++){
                Card drawnCard = deck.drawCard();
                hand.getCards().add(drawnCard);
            }
            userService.getUseryById(player).setHand(hand);

        }
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



     public Game playCard(Game game, User user, Card cardToPlay){


        // will be replaced by hand overed card by controller
        if (checkIfMoveAllowed(game, cardToPlay, user)){
            user.getHand().removeCard(game.getCardStack(),cardToPlay);

            game.setCurrentValue(cardToPlay.getValue());
            game.setCurrentColor(cardToPlay.getColor());

            checkWin(user);

            if (cardToPlay.getValue()== Value.WildFour || cardToPlay.getValue() == Value.Wild){
                wishColor(game);
            }

            determineNextPlayer(game,user,cardToPlay);

            checkIfExtraCard(game);
        }
        //want to play last card but no uno = player has to draw a card and next player is on.
        else if (user.getHand().getHandSize()==1 && !user.getHand().getUnoStatus()){
            System.out.println("Can't play because no UNO Status, draw a card");
            drawCard(game);
            game.setCurrentPlayerPlusOne();

        }
        else{
            System.out.println("Move not allowed, choose a different card!");
        }
        return game;

    }

    public void determineNextPlayer(Game game, User currentPlayer, Card card){
        if (card.getValue() == Value.Skip) {
            game.setCurrentPlayerPlusOne();
            game.setCurrentPlayerPlusOne();

        } else if (card.getValue()==Value.Reverse){
            game.reverseGameDirection();
            game.setCurrentPlayerPlusOne();
        } else{
            game.setCurrentPlayerPlusOne();
        }

    }

    public void checkIfExtraCard(Game game){
        if (game.getLastPlayedCard().getValue()== Value.DrawTwo){
            drawCard(game);
            drawCard(game);

        } else if (game.getLastPlayedCard().getValue()== Value.WildFour){
            drawCard(game);
            drawCard(game);
            drawCard(game);
            drawCard(game);
        }
    }

    public void drawCard(Game game){
        //draw a card and puts it into the hand and removed it from the deck
        //game.getPlayerList().get(game.getCurrentPlayer()).getHand().addCard(game.getCardStack().drawCard());
        //UNO Status back to false
        //game.getPlayerList().get(game.getCurrentPlayer()).getHand().setUnoStatus(false);
    }

    public void cantPlayDrawCard(Game game){
        drawCard(game);
        game.setCurrentPlayerPlusOne();
    }

    /*
    public void sayUno(Game game, int userId){
        if(game.getHandByPlayerId(userId).getHandSize()== 1){
            game.getHandByPlayerId(userId).setUnoStatus(true);
        }else{
            System.out.println("Liar!");
        }

    }*/

    public void checkWin(User user){
        if (user.getHand().getHandSize()==0){
            System.out.format("Player: %s wins!", user.getUsername());
        }
    }

    public void wishColor(Game game){
        game.setCurrentColor(Color.Blue);
    }

    public boolean checkIfMoveAllowed(Game game, Card card, User user){
        Card lastPlayedCard = game.getLastPlayedCard();

        Color color = game.getCurrentColor();
        Value value = game.getCurrentValue();

        //check if user status is uno
        if (user.getHand().getHandSize()==1 && !user.getHand().getUnoStatus()){
            return false;
        }

        if (lastPlayedCard == null){
            return true;
        }
        else if (color == card.getColor()){
            return true;
        }
        else if (value == card.getValue()){
            return true;
        }
        else if (color != Color.Wild && card.getColor()== Color.Wild){
            return true;
        }
        else {
            return false;
        }

    }

    //if after finished game, players want to play another round.
    public Game resetGame(Game game){
        Deck deck = new Deck();
        game.setCardStack(deck);
        game.setCurrentPlayer(0);
        game.setCurrentColor(null);
        game.setCurrentValue(null);

        if (!game.getGameDirection()){
            game.reverseGameDirection();
        }

         initializeHands(game);
        return game;

    }
}