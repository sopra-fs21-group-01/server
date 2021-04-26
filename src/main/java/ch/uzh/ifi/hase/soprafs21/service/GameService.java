package ch.uzh.ifi.hase.soprafs21.service;


import ch.uzh.ifi.hase.soprafs21.constant.Color;
import ch.uzh.ifi.hase.soprafs21.constant.Value;
import ch.uzh.ifi.hase.soprafs21.entity.*;
import ch.uzh.ifi.hase.soprafs21.repository.GameRepository;
import ch.uzh.ifi.hase.soprafs21.repository.HandRepository;
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
    private final HandRepository handRepository;

    @Autowired
    public GameService(@Qualifier("gameRepository") GameRepository gameRepository, UserService userService, @Qualifier("handRepository") HandRepository handRepository) {
        this.gameRepository = gameRepository;
        this.userService = userService;
        this.handRepository = handRepository;

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



     public Game playCard(Game game, User user, String cardToPlay){

         Hand playerHand = getHandById(game.getCurrentPlayerId());

        // will be replaced by hand overed card by controller
        if (checkIfMoveAllowed(game, cardToPlay)){
            playerHand.removeCard(game.getCardStack(),cardToPlay);

            game.setCurrentValue(getValueOfCard(cardToPlay));
            game.setCurrentColor(getColorOfCard(cardToPlay));

            checkWin(game);

            if (getValueOfCard(cardToPlay).equals("WildFour") || getColorOfCard(cardToPlay).equals("Wild")){
                wishColor(game);
            }

            determineNextPlayer(game, cardToPlay);

            checkIfExtraCard(game);
        }
        //want to play last card but no uno = player has to draw a card and next player is on.
        else if (playerHand.getHandSize()==1 && !playerHand.getUnoStatus()){
            System.out.println("Can't play because no UNO Status, draw a card");
            drawCard(game);
            game.setCurrentPlayerPlusOne();

        }
        else{
            System.out.println("Move not allowed, choose a different card!");
        }
        return game;

    }




    String[] getCardValuies(String cardName){
        String[] values = cardName.split("/");
        return values;
    }

    String getValueOfCard(String cardName){
        String[] values = getCardValuies(cardName);
        return values[0];
    }

    String getColorOfCard(String cardName){
        String[] values = getCardValuies(cardName);
        return values[1];
    }




    public void determineNextPlayer(Game game, String card){
        if (getValueOfCard(card).equals("Skip")) {
            game.setCurrentPlayerPlusOne();
            game.setCurrentPlayerPlusOne();

        } else if (getValueOfCard(card).equals("Reverse")){
            game.reverseGameDirection();
            game.setCurrentPlayerPlusOne();
        } else{
            game.setCurrentPlayerPlusOne();
        }

    }

    public void checkIfExtraCard(Game game){
        if (getValueOfCard(game.getLastPlayedCard()).equals("DrawTwo")){
            drawCard(game);
            drawCard(game);

        } else if (getValueOfCard(game.getLastPlayedCard()).equals("WildFour")){
            drawCard(game);
            drawCard(game);
            drawCard(game);
            drawCard(game);
        }
    }

    public void drawCard(Game game){
        //get hand from current player
        Hand hand = getHandById(game.getCurrentPlayerId());

        //draw a card and puts it into the hand and removed it from the deck
        hand.addCard(game.getCardStack().drawCard());

        //UNO Status to false
        hand.setUnoStatus(false);
    }

    public void cantPlayDrawCard(Game game){
        drawCard(game);
        game.setCurrentPlayerPlusOne();
    }


    public void sayUno(Game game){
        Hand hand = getHandById(game.getCurrentPlayerId());

        if(hand.getHandSize()== 1){
            hand.setUnoStatus(true);
        }else{
            System.out.println("Liar!");
        }

    }

    public void checkWin(Game game){

        Hand playerHand = getHandById(game.getCurrentPlayerId());

        if (playerHand.getHandSize()==0){
            System.out.format("Player: %s wins!", userService.getUseryById(game.getCurrentPlayerId()).getUsername());
        }
    }

    public void wishColor(Game game){
        game.setCurrentColor("Blue");
    }

    public boolean checkIfMoveAllowed(Game game, String card){
        Hand playerHand = getHandById(game.getCurrentPlayerId());

        String lastPlayedCard = game.getLastPlayedCard();

        String color = game.getCurrentColor();
        String value = game.getCurrentValue();

        //check if user status is uno
        if (playerHand.getHandSize()==1 && !playerHand.getUnoStatus()){
            return false;
        }

        if (lastPlayedCard == null){
            return true;
        }
        else if (color.equals(getColorOfCard(card))){
            return true;
        }
        else if (value.equals(getValueOfCard(card))){
            return true;
        }
        else if (!color.equals("Wild") && getColorOfCard(card).equals("Wild")){
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

    ///////////////////// HAND SPECIFIC TASKS ////////////////////////////

    public Hand getHandById(Long handId){
        Hand handFoundById = null;

        // check if there is a lobby with this ID and return it. If no lobby found, throw exception
        Optional<Hand> optionalLobby = this.handRepository.findById(handId);

        if (optionalLobby.isPresent()){
            handFoundById = optionalLobby.get();

            log.debug("Found and returned Hand with ID: {}", handId);
            return handFoundById;
        }
        if (handFoundById == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Hand with given ID " + handId + " was not found");}
        return handFoundById;
    }


    //initializes Hands for the start of the game,
    public void initializeHands(Game game) {
        Deck deck = game.getCardStack();
        System.out.println(deck.cardDeck);

        // gets every player and creates a hand with same Id as the player
        for (long player : game.getPlayerList()) {

            List<String> handCards = new ArrayList<String>();
            Hand newHand = new Hand();
            newHand.setId(userService.getUseryById(player).getId());
            for (int i = 0; i < 7; i++) {

                // The decks seems to consisit of only WildFour/Wild cards

                String drawnCard = deck.drawCard();
                System.out.println("drew card" + drawnCard);
                handCards.add(drawnCard);

                if (handCards.size() == 0){
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Error when filling a hand with cards");}

            }
            newHand.setCards(handCards);
            System.out.println("hand with ID:"+ newHand.getId() +" has " +newHand.getCards());




            newHand = handRepository.save(newHand);
            handRepository.flush();

            handCards.clear();

            System.out.println("hand with ID:"+ newHand.getId() +" has " +getHandById(newHand.getId()).getCards());
            userService.getUseryById(player).setHandId(newHand.getId());


        }
    }
}