package ch.uzh.ifi.hase.soprafs21.rest.mapper.DTOMapperInstancesTest;
import ch.uzh.ifi.hase.soprafs21.rest.dto.GameGetDTO;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import static org.junit.jupiter.api.Assertions.*;


public class GameGetDTOTest {

    @Mock
    private GameGetDTO testGameGetDTO;

    @Test
    public void test_getters_and_setter(){


        testGameGetDTO = new GameGetDTO();
        testGameGetDTO.setId(1L);
        testGameGetDTO.setCurrentColor("Blue");
        testGameGetDTO.setCurrentPlayer(2);
        testGameGetDTO.setCurrentValue("0");
        testGameGetDTO.setHost("Hans");

        assertSame(1L, testGameGetDTO.getId());
        assertSame("Blue", testGameGetDTO.getCurrentColor());
        assertSame("0", testGameGetDTO.getCurrentValue());
        assertSame(2, testGameGetDTO.getCurrentPlayer());

    }
}
