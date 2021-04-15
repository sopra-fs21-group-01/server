package ch.uzh.ifi.hase.soprafs21.rest.dto;

import ch.uzh.ifi.hase.soprafs21.entity.Deck;

import java.util.ArrayList;

public class GameGetDTO {

    private long id;

    private String host;

    // private ArrayList playerList;

    private Deck cardStack;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getHost() {

        return host;
    }

/**    public ArrayList getPlayerList() {

        return playerList;
    } */

    public void setHost(String host) {

        this.host = host;
    }

   /** public void setPlayerList(ArrayList playerList) {
        this.playerList = playerList;
    } */

    public Deck getCardStack() {
        return cardStack;
    }

    public void setCardStack(Deck cardStack) {
        this.cardStack = cardStack;
    }

    /** private String gamemode;

     public String getGamemode() {
     return gamemode;
     }

     public void setGamemode(String gamemode) {
     this.gamemode = gamemode;
     }
     */

}
