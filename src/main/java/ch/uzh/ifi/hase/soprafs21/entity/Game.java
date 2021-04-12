package ch.uzh.ifi.hase.soprafs21.entity;
import javax.persistence.*;
import java.awt.*;
import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import ch.uzh.ifi.hase.soprafs21.constant.Color;
import ch.uzh.ifi.hase.soprafs21.constant.Value;

// Basic implementation of Game entity. Has a unique, autogenerated ID, Takes a host, and a list of players.
// Its further logic is yet to be implemented

@Entity
@Table(name = "GAME")
public class Game implements Serializable {

    // the serialVersionUID makes sure that the sender and receiver of a serialized object have loaded the same object
    @Serial
    private static final long serialVersionUID = 3L;


    // id will be the same as the lobby ID where the game was iniialized
    @Id
    private Long id;

    // is given the same host from the lobby that started the game
    @Column(nullable = false)
    private String host;

    @Transient
    private int currentPlayer = 0; //keeps track who is the current player

    @Transient
    private boolean gameDirection = true; //true = clockwise

    @Transient
    private Map<Integer, User> playerList;

    @Transient
    private Deck deck;

    @Enumerated
    private Color currentColor;

    @Enumerated
    private Value currentValue;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Map<Integer, User> getPlayerList() {
        return playerList;
    }

    public void setPlayerList(User user) {
        this.playerList.put(user.getPlayerId(),user);
    }

    public User getPlayerById(int playerId){
        return playerList.get(playerId);
    }

    public Hand getHandByPlayerId(int playerId){
        return playerList.get(playerId).getHand();
    }

    public Deck getCardStack() {
        return deck;
    }

    public void setCardStack(Deck deck) {
        this.deck = deck;
    }



    public Color getCurrentColor() {
        return currentColor;
    }

    public void setCurrentColor(Color wishedColor) {
        this.currentColor = wishedColor;
    }



    public Value getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(Value currentValue) {
        this.currentValue = currentValue;
    }




    public int getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayerPlusOne() {
        if (gameDirection) {
            this.currentPlayer = (this.currentPlayer + 1) % playerList.size();
        } else{
            this.currentPlayer = (this.currentPlayer - 1) % playerList.size();
        }
    }


    public boolean reverseGameDirection(){
        this.gameDirection = !gameDirection;
        return gameDirection;
    }

    public boolean getGameDirection(){
        return this.gameDirection;
    }




    public Card getLastPlayedCard(){
        return deck.getLastCardDeck();
    }




    /**
    @Column(nullable = false)
    private String gamemode; */

    /**
    public ArrayList<Hand> players = new ArrayList<Hand>();
    public ArrayList<Card> playedCards = new ArrayList<Card>();
    public boolean gameDirection = false; //true = clockwise

    //index of players ArrayList
    public int currentPlayer = 0;



    public void setPlayers(Hand hand){
        this.players.add(hand);
    }


    public void initializeHands() {
        for (Hand i : players) {
            i.initializeHand();
        }
    }



     */

}
