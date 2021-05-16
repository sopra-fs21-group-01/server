package ch.uzh.ifi.hase.soprafs21.service;

import ch.uzh.ifi.hase.soprafs21.entity.*;
import ch.uzh.ifi.hase.soprafs21.repository.DeckRepository;
import ch.uzh.ifi.hase.soprafs21.repository.GameRepository;
import ch.uzh.ifi.hase.soprafs21.repository.HandRepository;
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
import java.util.Optional;


@Service
@Transactional
@Qualifier("gameService")
public class GameService {

    private final Logger log = LoggerFactory.getLogger(GameService.class);

    private final GameRepository gameRepository;
    private final UserService userService;
    private final HandRepository handRepository;
    private final DeckRepository deckRepository;
    private final LobbyService lobbyService;

    @Autowired
    public GameService(@Qualifier("gameRepository") GameRepository gameRepository, @Qualifier("deckRepository") DeckRepository deckRepository, UserService userService, LobbyService lobbyService, @Qualifier("handRepository") HandRepository handRepository) {
        this.gameRepository = gameRepository;
        this.userService = userService;
        this.handRepository = handRepository;
        this.deckRepository = deckRepository;
        this.lobbyService = lobbyService;
    }

    // the host is added to the list of players, the gamemode is set to standard
    public Game createGame(Game newGame){

        initializeDeck(newGame);

        initializeHands(newGame);

        newGame.setCurrentColor("Blue");
        newGame.setCurrentValue("0");

        newGame = gameRepository.save(newGame);
        gameRepository.flush();

        return newGame;
    }

    // converts the list of usernames to the list of ID's
    public List<Long> convertUserNamesToIds(long id){

        //get PlayerList from Lobby with same Id like game
        List<String> playerListOfLobby = lobbyService.getLobbyById(id).getPlayerList();
        if (playerListOfLobby == null || playerListOfLobby.size()==0){
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, "The Lobby dosent contain any or enough players to start the game");
        }

        //Set all players from Lobby into Map playerList of Game class.
        List<Long> playerListForGame = new ArrayList<>();
        for (String playerName : playerListOfLobby){
            User user = userService.getUser(playerName);
            playerListForGame.add(user.getId());
        }
        return playerListForGame;
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
         gameRepository.deleteById(gameId);

        log.debug("Deleted the game with ID: {}", gameId);
    }

    public Game playCard(Game game, User user, String cardToPlay){

         Hand playerHand = getHandById(game.getCurrentPlayerId());

        // will be replaced by hand overed card by controller
        if (checkIfMoveAllowed(game, cardToPlay)){

            if(! playerHand.getCards().contains(cardToPlay))
            {throw new ResponseStatusException(HttpStatus.METHOD_NOT_ALLOWED,
                    "This card is not in the players hand!");}
            else {
            playerHand.removeCard(getDeckById(game.getId()),cardToPlay);

            game.setCurrentValue(getValueOfCard(cardToPlay));
            game.setCurrentColor(getColorOfCard(cardToPlay));


            gameRepository.save(game);
            gameRepository.flush();

            checkWin(game);

            determineNextPlayer(game, cardToPlay);

            checkIfExtraCard(game);}

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
        }}

    public void checkIfExtraCard(Game game){
        String lastPlayedCard = getValueOfCard(getDeckById(game.getId()).getLastCardDeck());

        if (lastPlayedCard.equals("DrawTwo")){
            drawCard(game);
            drawCard(game);

        } else if (lastPlayedCard.equals("WildFour")){
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
        hand.addCard(getDeckById(game.getId()).drawCard());
        //UNO Status to false
        hand.setUnoStatus(false);
    }

    public void cantPlayDrawCard(Game game){
        drawCard(game);
        game.setCurrentPlayerPlusOne();
    }

    public void wishColor(String wishedColor, Game game){
        game.setCurrentColor(wishedColor);
        gameRepository.save(game);
        gameRepository.flush();
    }

    public void sayUno(Game game, Long playerId){
        Hand hand = getHandById(playerId);

        if(hand.getHandSize()== 1){
            hand.setUnoStatus(true);
        }else{
            String username = userService.getUseryById(playerId).getUsername();
            System.out.format("User: %s is a Liar!",username);
        }
    }

    public void checkWin(Game game){

        Hand playerHand = getHandById(game.getCurrentPlayerId());

        if (playerHand.getHandSize()==0){

            String userName = userService.getUseryById(game.getCurrentPlayerId()).getUsername();

            removePlayerFromPlayerList(game, game.getCurrentPlayerId());

            System.out.format("Player: %s wins!", userName);
        }
    }

    public void removePlayerFromPlayerList(Game game, Long playerId){
        List<Long> currentPlayerList = game.getPlayerList();
        for (int i = 0; i < currentPlayerList.size(); i++){
            if (currentPlayerList.get(i).equals(playerId)){
                currentPlayerList.remove(i);
                break;
            }
        }
        game.setPlayerList(currentPlayerList);
        gameRepository.save(game);
        gameRepository.flush();
    }

    public void wishColor(Game game){
        game.setCurrentColor("Blue");
        gameRepository.save(game);
        gameRepository.flush();
    }

    public boolean checkIfMoveAllowed(Game game, String card){
        Hand playerHand = getHandById(game.getCurrentPlayerId());

        String lastPlayedCard = getDeckById(game.getId()).getLastCardDeck();
        String color = game.getCurrentColor();
        String value = game.getCurrentValue();


        //check if user status is uno
        if (playerHand.getHandSize()==1 && !playerHand.getUnoStatus()){
            return false;
        }

        if (lastPlayedCard == null){
            return true;
        }
        //allows any card after a wild card
        else if (color.equals("Wild")){
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
        } }

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

        Deck deck = getDeckById(game.getId());
        // gets every player and creates a hand with same Id as the player
        for (long player : game.getPlayerList()) {

            List<String> handCards = new ArrayList<String>();
            Hand newHand = new Hand();
            newHand.setId(userService.getUseryById(player).getId());
            for (int i = 0; i < newHand.getInitialCards(); i++) {

                // The decks seems to consisit of only WildFour/Wild cards
                String drawnCard = deck.drawCard();
                handCards.add(drawnCard);
                if (handCards.size() == 0){
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Error when filling a hand with cards");}
            }
            newHand.setCards(handCards);

            // save the hand
            handRepository.save(newHand);
            handRepository.flush();

            handCards.clear();
            userService.getUseryById(player).setHandId(newHand.getId());
        }  }

    ///////////////////// DECK SPECIFIC TASKS ////////////////////////////

    public Deck getDeckById(Long deckId){
        Deck deckFoundById = null;

        // check if there is a lobby with this ID and return it. If no lobby found, throw exception
        Optional<Deck> optionalDeck = this.deckRepository.findById(deckId);

        if (optionalDeck.isPresent()){
            deckFoundById = optionalDeck.get();

            log.debug("Found and returned Deck with ID: {}", deckId);
            return deckFoundById;
        }
        if (deckFoundById == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Deck with given ID " + deckId + " was not found");}
        return deckFoundById;
    }

    public void initializeDeck (Game game){

        Deck deck = new Deck();
        deck.setId(game.getId());

        // save deck
        deckRepository.save(deck);
        deckRepository.flush();
    }
}