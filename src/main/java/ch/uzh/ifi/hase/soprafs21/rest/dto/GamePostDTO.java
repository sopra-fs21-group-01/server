package ch.uzh.ifi.hase.soprafs21.rest.dto;

import java.util.ArrayList;

public class GamePostDTO {

    private long id;

    private String host;

    private String[] playerList;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getHost() {

        return host;
    }

    public String[] getPlayerList() {

        return playerList;
    }

    public void setHost(String host) {

        this.host = host;
    }

    public void setPlayerList(String[] playerList) {
        this.playerList = playerList;
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
