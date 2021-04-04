package ch.uzh.ifi.hase.soprafs21.rest.dto;

import java.util.ArrayList;

public class LobbyGetDTO {
    private String host;

    private String name;

    private String password;

    private Long id;

    private String[] playerList;

    private String gamemode;

    public String getHost() {
        return host;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public Long getId() {
        return id;
    }

    public String[] getPlayerList() {
        return playerList;
    }

    public String getGamemode() {
        return gamemode;
    }


    public void setGamemode(String gamemode) {
        this.gamemode = gamemode;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setPlayerList(String[] playerList) {
        this.playerList = playerList;
    }
}
