package ch.uzh.ifi.hase.soprafs21.entity;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serial;
import java.util.*;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "DECK")
public class Deck implements Serializable {



    @Serial
    private static final long serialVersionUID = 5L;

    // Same ID as Lobby and Game
    @Id
    private Long id;

    @ElementCollection
    private final List<String> cardDeck = new ArrayList<String>();

    @ElementCollection
    private final List<String> playedCardsDeck = new ArrayList<String>();

    /////////////////////////////////////


    public Deck(){
        this.generateDeck();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<String> getCardDeck() {
        return cardDeck;
    }

    public List<String> getPlayedCardsDeck() {
        return playedCardsDeck;
    }

    private List<String> createColorlist(){
        List<String> colors = new ArrayList<>();
        colors.add("Red");
        colors.add("Blue");
        colors.add("Green");
        colors.add("Yellow");
        colors.add("Wild");
        return colors;
    }
    private List<String> createValuelist(){
        List<String> values = new ArrayList<>();
        values.add("0");
        values.add("1");
        values.add("2");
        values.add("3");
        values.add("4");
        values.add("5");
        values.add("6");
        values.add("7");
        values.add("8");
        values.add("9");
        values.add("DrawTwo");
        values.add("Skip");
        values.add("Reverse");
        values.add("Wild");
        values.add("WildFour");
        return values;
    }

    private List<String> generateDeck(){
        List<String> colors = this.createColorlist();
        List<String> values = this.createValuelist();
        int i;
        for(i=0;i<13;i++) {
            int j;
            for (j = 0; j < 4; j++) {
                if (i <= 9) {
                    this.cardDeck.add((new Card(colors.get(j), values.get(i)).getCardName()));
                    if (i != 0) {
                        this.cardDeck.add((new Card(colors.get(j), values.get(i)).getCardName()));
                    }
                }
                else{
                    this.cardDeck.add((new Card(colors.get(j), values.get(i)).getCardName()));
                    this.cardDeck.add((new Card(colors.get(j), values.get(i)).getCardName()));
                }
            }

        }
        for(i=13;i<15;i++) {
            this.cardDeck.add((new Card(colors.get(4), values.get(i)).getCardName()));
            this.cardDeck.add((new Card(colors.get(4), values.get(i)).getCardName()));
            this.cardDeck.add((new Card(colors.get(4), values.get(i)).getCardName()));
            this.cardDeck.add((new Card(colors.get(4), values.get(i)).getCardName()));
        }
        Collections.shuffle(this.cardDeck);
        return this.cardDeck;

    }

    public String drawCard(){
        boolean empty = this.cardDeck.isEmpty();
        if(empty) {
            System.out.println("Deck Shuffled");
            this.Shuffle();
            this.cardDeck.addAll(this.playedCardsDeck);
        }

        return cardDeck.remove(cardDeck.size()-1);
    }

    private void Shuffle(){
        Collections.shuffle(this.playedCardsDeck);

    }
    public String getLastCardDeck(){
        boolean empty = this.playedCardsDeck.isEmpty();
        if(!empty) {
            return this.playedCardsDeck.get(this.playedCardsDeck.size()-1);
        }
        return null;
    }

    public void addPlayedCards(String card){
        this.playedCardsDeck.add(card);
    }
}
