package ch.uzh.ifi.hase.soprafs21.entity;

import ch.uzh.ifi.hase.soprafs21.repository.DeckRepository;
import ch.uzh.ifi.hase.soprafs21.service.GameService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class GameTest {

    @Mock
    private DeckRepository deckRepository;

    @InjectMocks
    private GameService gameService;

    @Mock
    private Game testGame;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        List<Long> myPlayers = new ArrayList<>();
        myPlayers.add(2L);
        myPlayers.add(3L);

        testGame.setHost("Hans");
        testGame.setId(1L);
        testGame.setPlayerList(myPlayers);
        testGame.setCurrentPlayer(1);

    }

    // test if game direction boolean is changed
    @Test
    public void reverseGameDirectionTest(){
        testGame.reverseGameDirection();
        assertFalse(testGame.getGameDirection());
    }

    // test to set the current one further
    @Test
    public void setCurrentPlayerPlusOneTest(){

        // clockwise
        testGame.setCurrentPlayerPlusOne();
        assertEquals(0, testGame.getCurrentPlayer());
    }
}
