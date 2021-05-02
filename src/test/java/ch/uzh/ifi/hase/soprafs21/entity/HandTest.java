package ch.uzh.ifi.hase.soprafs21.entity;

import ch.uzh.ifi.hase.soprafs21.repository.DeckRepository;
import ch.uzh.ifi.hase.soprafs21.repository.HandRepository;
import ch.uzh.ifi.hase.soprafs21.service.GameService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class HandTest {

    @Mock
    private HandRepository handRepository;

    @InjectMocks
    private GameService gameService;

    @Mock
    private Hand testHand;

    @Mock
    private Deck testDeck;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        testDeck = new Deck();
        testDeck.setId(1L);

        List<String> handCards= new ArrayList<>();
        handCards.add("0/Blue");
        testHand = new Hand();
        testHand.setCards(handCards);
    }

    // a card removed from the Hand should be added to the playedCard of the deck
    @Test
    public void testRemoveCard_valid(){

        testHand.removeCard(testDeck, "0/Blue");

        // hand should be empty and the card should be in the playedCards of the deck now
        assertEquals(testHand.getCards().size(), 0);
        assertTrue(testDeck.getPlayedCardsDeck().contains("0/Blue"));
    }

    // The test for when a card is not in the players hand is already provided in gameService

}
