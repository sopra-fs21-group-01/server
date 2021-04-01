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
    public Color getColor(Card card){
        return card.color;

    }
    public Value getValue(Card card){
        return card.value;
    }

    private void setValue(Value value){
        this.value = value;
    }

    // methode wird von Class deck aufgerufen
    public Card(Color color, Value value, Card card){
        this.card = card;
        this.card.setColor(color);
        this.card.setValue(value);
    }
}
