package ch.uzh.ifi.hase.soprafs21.rest.dto;

import java.util.List;

public class HandGetDTO {

    private Long id;

    public int initialCards;

    private List<String> cards;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<String> getCards() {
        return cards;
    }

    public void setCards(List<String> cards) {
        this.cards = cards;
    }
}
