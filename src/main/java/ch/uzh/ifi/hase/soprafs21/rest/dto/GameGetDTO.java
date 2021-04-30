package ch.uzh.ifi.hase.soprafs21.rest.dto;

import ch.uzh.ifi.hase.soprafs21.entity.Deck;
import ch.uzh.ifi.hase.soprafs21.entity.User;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GameGetDTO {

    private int currentPlayer;

    private long id;

    private String host;

    private String currentColor;

    private String currentValue;

    private String chat;

    public List<String> getOpponentListHands() {
        return opponentListHands;
    }

    public void setOpponentListHands(List<String> opponentListHands) {
        this.opponentListHands = opponentListHands;
    }

    private List<String> opponentListHands;

    public int getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(int currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public String getCurrentColor() {
        return currentColor;
    }

    public void setCurrentColor(String currentColor) {
        this.currentColor = currentColor;
    }

    public String getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(String currentValue) {
        this.currentValue = currentValue;
    }

    public String getChat(){
        return chat;
    }

    public void setChat(String chat){
        this.chat = chat;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getHost() {

        return host;
    }
    public void setHost(String host) {

        this.host = host;
    }

 /**   public Deck getCardStack() {
        return cardStack;
    }

    public void setCardStack(Deck cardStack) {
        this.cardStack = cardStack;
    }

     private String gamemode;

     public String getGamemode() {
     return gamemode;
     }

     public void setGamemode(String gamemode) {
     this.gamemode = gamemode;
     }
     */

}
