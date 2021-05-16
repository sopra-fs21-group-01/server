package ch.uzh.ifi.hase.soprafs21.rest.dto;

public  class ChatGetDTO {
    private String message;
    private long lobby;
    private String timestamp;

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public Long getlobby() {
        return lobby;
    }

    public void setlobby(Long id) {
        this.lobby = id;
    }

    public String getMessage(){return message;}

    public void setMessage(String message) {
        this.message = message;
    }
}
