package ch.uzh.ifi.hase.soprafs21.rest.dto;

import java.util.ArrayList;

public class DeckGetDTO {

    private Long id;

    private  ArrayList<String> cardDeck;

    private ArrayList<String> playedCardsDeck;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ArrayList<String> getCardDeck() {
        return cardDeck;
    }

    public void setCardDeck(ArrayList<String> cardDeck) {
        this.cardDeck = cardDeck;
    }

    public ArrayList<String> getPlayedCardsDeck() {
        return playedCardsDeck;
    }

    public void setPlayedCardsDeck(ArrayList<String> playedCardsDeck) {
        this.playedCardsDeck = playedCardsDeck;
    }
}
