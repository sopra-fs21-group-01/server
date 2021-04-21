package ch.uzh.ifi.hase.soprafs21.rest.dto;

public class ChatGetDTO {
    private String message;
    private long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessage(){return message;}

    public void setMessage(String message) {
        this.message = message;
    }
}
