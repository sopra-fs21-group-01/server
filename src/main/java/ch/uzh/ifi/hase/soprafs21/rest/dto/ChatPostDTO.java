package ch.uzh.ifi.hase.soprafs21.rest.dto;

public class ChatPostDTO {
    private long id;
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
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


}
