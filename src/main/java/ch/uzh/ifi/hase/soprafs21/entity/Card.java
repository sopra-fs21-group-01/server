package ch.uzh.ifi.hase.soprafs21.entity;

import ch.uzh.ifi.hase.soprafs21.constant.Color;
import ch.uzh.ifi.hase.soprafs21.constant.Value;

public class Card {

    private Card card;
    private Color color;
    private Value value;
    private int cardId;

    public void setColor(Color color){
        this.color = color;

    }
    // ist getColor überhaupt nötig?
    public Color getColor(){
        return this.color;

    }
    // ist getValue überhaupt nötig?
    public Value getValue(){
        return this.value;
    }

    private void setValue(Value value){
        this.value = value;
    }


    public int getCardId() {
        return cardId;
    }

    public void setCardId(int cardId) {
        this.cardId = cardId;
    }




    // methode wird von Class deck aufgerufen
    public Card(Color color, Value value){
        this.setColor(color);
        this.setValue(value);
    }
}
