package ch.uzh.ifi.hase.soprafs21.entity;


import ch.uzh.ifi.hase.soprafs21.constant.Color;
import ch.uzh.ifi.hase.soprafs21.constant.Value;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck {

    private final ArrayList<String> cardDeck = new ArrayList<String>();
    private final ArrayList<String> playedCardsDeck = new ArrayList<String>();

    public Deck(){
        this.generateDeck();
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

    private ArrayList<String> generateDeck(){
        List<String> colors = this.createColorlist();
        List<String> values = this.createValuelist();
        int i;
        for(i=0;i<15;i++) {
            int j;
            for (j = 0; j < 4; j++) {
                if (i <= 9) {
                    this.cardDeck.add((new Card(colors.get(j), values.get(i)).getCardName()));
                    if (i != 0) {
                        this.cardDeck.add((new Card(colors.get(j), values.get(i)).getCardName()));
                    }
                }
                else if(i<=12){
                    this.cardDeck.add((new Card(colors.get(j), values.get(i)).getCardName()));
                    this.cardDeck.add((new Card(colors.get(j), values.get(i)).getCardName()));
                }else{
                    this.cardDeck.add((new Card(colors.get(4), values.get(i)).getCardName()));
                    this.cardDeck.add((new Card(colors.get(4), values.get(i)).getCardName()));
                    this.cardDeck.add((new Card(colors.get(4), values.get(i)).getCardName()));
                    this.cardDeck.add((new Card(colors.get(4), values.get(i)).getCardName()));

                }
            }

        }

        return this.cardDeck;

    }

    public String drawCard(){
        boolean empty = this.cardDeck.isEmpty();
        if(empty) {
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
