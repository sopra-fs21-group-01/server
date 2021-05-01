package ch.uzh.ifi.hase.soprafs21.rest.mapper.DTOMapperInstancesTest;
import ch.uzh.ifi.hase.soprafs21.rest.dto.DeckGetDTO;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;


public class DeckGetDTOTest {

    @Mock
    private DeckGetDTO testDeckGetDTO;

    @Test
    public void test_getters_and_setter(){

        MockitoAnnotations.openMocks(this);
        ArrayList<String> cardDeckTest = new ArrayList<>();
        cardDeckTest.add("0/Blue");
        cardDeckTest.add("1/Red");

        ArrayList<String> playedCardsDeckTest = new ArrayList<>();
        playedCardsDeckTest.add("7/Blue");
        testDeckGetDTO = new DeckGetDTO();
        testDeckGetDTO.setId(1L);
        testDeckGetDTO.setCardDeck(cardDeckTest);
        testDeckGetDTO.setPlayedCardsDeck(playedCardsDeckTest);

        assertSame(1L, testDeckGetDTO.getId());
        assertSame(playedCardsDeckTest, testDeckGetDTO.getPlayedCardsDeck());
        assertSame(cardDeckTest, testDeckGetDTO.getCardDeck());

    }
}
