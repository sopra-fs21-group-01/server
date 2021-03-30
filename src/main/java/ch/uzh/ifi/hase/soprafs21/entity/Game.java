package ch.uzh.ifi.hase.soprafs21.entity;
import java.util.ArrayList;


public abstract class Game {

    public ArrayList<Hand> players = new ArrayList<Hand>();
    //public ArrayList<Card> playedCards = new ArrayList<Card>();

    public boolean gameDirection = false; //true = clockwise

    public void setPlayers(Hand hand){
        players.add(hand);
    }


    public void initializeHands() {
        for (Hand i : players) {
            i.initializeHand();
        }
    }



    /* public Card getLastCard(){
        return playedCards.get(playedCards.size() - 1);
    }
*/
}
