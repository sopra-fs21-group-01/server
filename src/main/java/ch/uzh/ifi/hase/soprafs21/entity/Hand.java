package ch.uzh.ifi.hase.soprafs21.entity;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Hand {

    public final User user;
    public boolean unoStatus = false;
    private ArrayList<Card> cards = new ArrayList<Card>();
    public final int initialCards = 7;

    public Hand(User user) {
        this.user = user;
    }

    public ArrayList<Card> getCards() {
        return cards;
    }

    public void addCard(Card card){
        this.cards.add(card);
    }

    public void removeCard(Deck deck, Card card){
        deck.addPlayedCards(card);
        cards.remove(card);
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
