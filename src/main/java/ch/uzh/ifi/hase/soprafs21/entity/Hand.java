package ch.uzh.ifi.hase.soprafs21.entity;
import java.util.ArrayList;

public class Hand {

    public final User user;
    public boolean unoStatus = false;
    //private ArrayList<Card> cards = new ArrayList<Card>();
    public final int initialCards = 7;

    public Hand(User user) {
        this.user = user;
    }

    public void initializeHand(){
        for(int i=0; i<initialCards; i++){
            //cards.add(addCard());
        }
    }

    /* public void addCard(Card card){
        cards.add(card);
    }

     */
}
