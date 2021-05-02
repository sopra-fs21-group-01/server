package ch.uzh.ifi.hase.soprafs21.entity;
import ch.uzh.ifi.hase.soprafs21.repository.DeckRepository;
import ch.uzh.ifi.hase.soprafs21.repository.LobbyRepository;
import ch.uzh.ifi.hase.soprafs21.service.GameService;
import ch.uzh.ifi.hase.soprafs21.service.LobbyService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import static org.junit.jupiter.api.Assertions.*;

public class DeckTest {

    @Mock
    private DeckRepository deckRepository;

    @InjectMocks
    private GameService gameService;

    private Deck testDeck;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        testDeck = new Deck();
        testDeck.setId(1L);
       }

    // After initialization, the Deck should contain a list with all UNO Cards
    @Test
    public void initializationTest_cards(){
        assertTrue(testDeck.getCardDeck().contains("0/Blue"));
        assertTrue(testDeck.getCardDeck().contains("Skip/Red"));
        assertTrue(testDeck.getCardDeck().contains("Wild/Wild"));

        assertTrue(testDeck.getCardDeck().contains("3/Blue"));
        assertTrue(testDeck.getCardDeck().contains("Reverse/Red"));
        assertTrue(testDeck.getCardDeck().contains("WildFour/Wild"));

        assertTrue(testDeck.getCardDeck().contains("6/Blue"));
        assertTrue(testDeck.getCardDeck().contains("2/Red"));
        assertTrue(testDeck.getCardDeck().contains("Wild/Wild"));

        assertTrue(testDeck.getCardDeck().contains("9/Blue"));
        assertTrue(testDeck.getCardDeck().contains("8/Red"));
        assertTrue(testDeck.getCardDeck().contains("5/Yellow"));
    }

    // test number of cards in the deck
    @Test
    public void initializationTest_deckSize(){

        assertEquals(108, testDeck.getCardDeck().size());
    }


    // Test if drawn card is the same as actually was on top of deck
    @Test
    public void drawCardTest(){
        String topCard = testDeck.getCardDeck().get(testDeck.getCardDeck().size() - 1);

        assertSame(topCard, testDeck.drawCard());
    }

    // Test if deck keeps track of cards that are played
    @Test
    public void addPlayedCardTest(){
        String playedCard = "0/Blue";
        testDeck.addPlayedCards(playedCard);
        assertSame(playedCard, testDeck.getLastCardDeck());
    }
    
}
