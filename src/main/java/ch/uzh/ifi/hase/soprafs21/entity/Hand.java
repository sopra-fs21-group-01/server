package ch.uzh.ifi.hase.soprafs21.entity;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serial;
import java.util.List;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "HAND")
public class Hand implements Serializable {

    @Serial
    private static final long serialVersionUID = 4L;

    @Id
    private Long id;

    @Column
    public boolean unoStatus = false;

    @ElementCollection
    private List<String> cards;

    @Column
    public int initialCards = 7;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isUnoStatus() {
        return unoStatus;
    }

    public void setCards(List<String> cards) {
        this.cards = cards;
    }

    public int getInitialCards() {
        return initialCards;
    }

    public List<String> getCards() {
        return cards;
    }

    public void addCard(String card){
        this.cards.add(card);
    }

    public void removeCard(Deck deck, String card){
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
