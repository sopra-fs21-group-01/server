package ch.uzh.ifi.hase.soprafs21.rest.dto;

import java.util.Map;

public class ChatPostDTO {
    private long lobby;
    private String message;
    private String timestamp;



    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getMessage(){return message;}

    public void setMessage(String message){
        this.message = message;
    }
    public long getlobby() {
        return lobby;
    }

    public void setlobby(long lobby) {
        this.lobby = lobby;
    }


}
