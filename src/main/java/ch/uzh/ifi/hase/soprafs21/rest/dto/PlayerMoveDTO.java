package ch.uzh.ifi.hase.soprafs21.rest.dto;

public class PlayerMoveDTO {
    private long playerId;

    private String color;

    private String value;

    private String wishedColor;

    public long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(long playerId) {
        this.playerId = playerId;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getWishedColor() {
        return wishedColor;
    }

    public void setWishedColor(String wishedColor) {
        this.wishedColor = wishedColor;
    }
}
