package ch.uzh.ifi.hase.soprafs21.rest.dto;

import java.util.ArrayList;

public class GameGetDTO {

    private long id;

    private String host;

    private ArrayList playerList;

    private ArrayList cardStack;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getHost() {

        return host;
    }

    public ArrayList getPlayerList() {

        return playerList;
    }

    public void setHost(String host) {

        this.host = host;
    }

    public void setPlayerList(ArrayList playerList) {
        this.playerList = playerList;
    }

    public ArrayList getCardStack() {
        return cardStack;
    }

    public void setCardStack(ArrayList cardStack) {
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
