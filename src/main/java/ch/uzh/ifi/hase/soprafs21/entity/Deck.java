package ch.uzh.ifi.hase.soprafs21.entity;


import ch.uzh.ifi.hase.soprafs21.constant.Color;
import ch.uzh.ifi.hase.soprafs21.constant.Value;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck {

    private final ArrayList<Card> cardDeck = new ArrayList<Card>();
    private final ArrayList<Card> playedcardsDeck = new ArrayList<Card>();

    public Deck(){
        this.generateDeck();
    }

    private List<Color> createColorlist(){
        List<Color> colors = new ArrayList<>();
        colors.add(Color.Red);
        colors.add(Color.Blue);
        colors.add(Color.Green);
        colors.add(Color.Yellow);
        colors.add(Color.Wild);
        return colors;
    }

    private List<Value> createValuelist(){
        List<Value> values = new ArrayList<>();
        values.add(Value.Zero);
        values.add(Value.One);
        values.add(Value.Two);
        values.add(Value.Three);
        values.add(Value.Four);
        values.add(Value.Five);
        values.add(Value.Six);
        values.add(Value.Seven);
        values.add(Value.Eight);
        values.add(Value.Nine);
        values.add(Value.DrawTwo);
        values.add(Value.Skip);
        values.add(Value.Reverse);
        values.add(Value.Wild);
        values.add(Value.WildFour);
        return values;
    }

    private ArrayList<Card> generateDeck(){
        List<Color> colors = this.createColorlist();
        List<Value> values = this.createValuelist();
        int i;
        for(i=0;i<15;i++) {
            int j;
            for (j = 0; j < 4; j++) {
                if (i <= 9) {
                    this.cardDeck.add(new Card(colors.get(j), values.get(i)));
                    if (i != 0) {
                        this.cardDeck.add(new Card(colors.get(j), values.get(i)));
                    }
                }
                else if(i<=12){
                    this.cardDeck.add(new Card(colors.get(j), values.get(i)));
                    this.cardDeck.add(new Card(colors.get(j), values.get(i)));
                }else{
                    this.cardDeck.add(new Card(colors.get(4), values.get(i)));
                    this.cardDeck.add(new Card(colors.get(4), values.get(i)));
                    this.cardDeck.add(new Card(colors.get(4), values.get(i)));
                    this.cardDeck.add(new Card(colors.get(4), values.get(i)));

                }
            }

        }

        return this.cardDeck;

    }

    public Card drawCard(){
        boolean empty = this.cardDeck.isEmpty();
        if(empty) {
            this.Shuffle();
            this.cardDeck.addAll(this.playedcardsDeck);
        }

        return cardDeck.remove(cardDeck.size()-1);
    }

    private void Shuffle(){
        Collections.shuffle(this.playedcardsDeck);

    }
    public Card getlastCardDeck(){
        boolean empty = this.playedcardsDeck.isEmpty();
        if(!empty) {
            return this.playedcardsDeck.get(this.playedcardsDeck.size()-1);
        }
        return null;
    }

    public void addPlayedCards(Card card){
        this.playedcardsDeck.add(card);
    }
}
