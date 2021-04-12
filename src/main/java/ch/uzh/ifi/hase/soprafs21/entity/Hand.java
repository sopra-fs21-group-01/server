package ch.uzh.ifi.hase.soprafs21.entity;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Hand {

    public final User user;
    public boolean unoStatus = false;
    private Map<Integer, Card> cards = new HashMap<Integer, Card>();
    public final int initialCards = 7;

    public Hand(User user) {
        this.user = user;
    }

    public Map<Integer, Card> getCards() {
        return cards;
    }

    public void addCard(Card card){
        this.cards.put(card.getId(), card);
    }

    public void removeCard(Deck deck, Card card){
        deck.addPlayedCards(cards.remove(card.getId()));
    }

    public boolean getUnoStatus(){
        return unoStatus;
    }

    public void setUnoStatus(boolean status){
        this.unoStatus = status;
    }

    public int getHandSize(){
        return this.cards.size();
    }


}
