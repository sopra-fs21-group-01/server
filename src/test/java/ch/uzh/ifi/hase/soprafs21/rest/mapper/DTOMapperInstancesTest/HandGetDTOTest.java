package ch.uzh.ifi.hase.soprafs21.rest.mapper.DTOMapperInstancesTest;
import ch.uzh.ifi.hase.soprafs21.rest.dto.HandGetDTO;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;


public class HandGetDTOTest{

    @Mock
    private HandGetDTO testhandGetDTO;

    @Test
    public void test_getters_and_setter(){

        ArrayList<String> cardDeckTest = new ArrayList<>();
        cardDeckTest.add("0/Blue");
        cardDeckTest.add("1/Red");

        testhandGetDTO = new HandGetDTO();
        testhandGetDTO.setId(1L);
        testhandGetDTO.setCards(cardDeckTest);

        assertSame(1L, testhandGetDTO.getId());
        assertSame(cardDeckTest, testhandGetDTO.getCards());
    }
}
