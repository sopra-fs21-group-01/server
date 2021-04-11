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
        cards.put(1111, card);
    }

    public void removeCard(Deck deck, Card card){
        deck.addPlayedCards(cards.remove(card.getId()));
    }


}
