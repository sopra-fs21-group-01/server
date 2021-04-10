package ch.uzh.ifi.hase.soprafs21.entity;

import ch.uzh.ifi.hase.soprafs21.constant.Color;
import ch.uzh.ifi.hase.soprafs21.constant.Value;

public class Card {

    private Card card;
    private Color color;
    private Value value;

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

    // methode wird von Class deck aufgerufen
    public Card(Color color, Value value){
        this.card.setColor(color);
        this.card.setValue(value);
    }
}
