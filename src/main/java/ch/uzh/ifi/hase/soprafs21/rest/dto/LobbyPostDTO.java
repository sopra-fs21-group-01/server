package ch.uzh.ifi.hase.soprafs21.rest.dto;

import java.util.ArrayList;
import java.util.List;

public class LobbyPostDTO {
    private String host;

    private String name;

    private String password;

    private List<String> playerList;

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

    public List<String> getPlayerList() {
        return playerList;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getGamemode() {
        return gamemode;
    }

    public void setGamemode(String gamemode) {
        this.gamemode = gamemode;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPlayerList(List<String> playerList) {
        this.playerList = playerList;
    }
}
