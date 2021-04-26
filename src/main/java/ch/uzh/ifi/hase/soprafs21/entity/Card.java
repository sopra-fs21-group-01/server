package ch.uzh.ifi.hase.soprafs21.entity;

import ch.uzh.ifi.hase.soprafs21.constant.Color;
import ch.uzh.ifi.hase.soprafs21.constant.Value;

public class Card {

    private String cardName;
    private String color;
    private String value;
    private int cardId;

    public void setColor(String color){
        this.color = color;

    }
    // ist getColor überhaupt nötig?
    public String getColor(){
        return this.color;

    }
    // ist getValue überhaupt nötig?
    public String getValue(){
        return this.value;
    }

    private void setValue(String value){
        this.value = value;
    }


    public int getCardId() {
        return cardId;
    }

    public void setCardId(int cardId) {
        this.cardId = cardId;
    }

    public String getCardName() {
        return this.getValue()+"/"+this.getColor();
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    // methode wird von Class deck aufgerufen
    public Card(String color, String value){
        this.setColor(color);
        this.setValue(value);
    }
}
