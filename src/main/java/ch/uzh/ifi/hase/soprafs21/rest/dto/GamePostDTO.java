package ch.uzh.ifi.hase.soprafs21.rest.dto;

import java.util.ArrayList;

public class GamePostDTO {

    private long id;

    private String host;

    private Long initialCards;

    public Long getInitialCards() {
        return initialCards;
    }

    public void setInitialCards(Long initialCards) {
        this.initialCards = initialCards;
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


    /** private String gamemode;

     public String getGamemode() {
     return gamemode;
     }

     public void setGamemode(String gamemode) {
     this.gamemode = gamemode;
     }
     */

}
