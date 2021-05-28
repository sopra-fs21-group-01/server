package ch.uzh.ifi.hase.soprafs21.repository;
import ch.uzh.ifi.hase.soprafs21.entity.Deck;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.EmptyResultDataAccessException;

import static org.junit.jupiter.api.Assertions.*;


@DataJpaTest
public class DeckRepositoryIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private DeckRepository deckRepository;


    // test to delete a Deck with valid id
    @Test
    public void deleteById_valid() {
        Deck testDeck = new Deck();
        testDeck.setId(1L);

        entityManager.persist(testDeck);
        entityManager.flush();

        // delete it, second deletion throws expeption because the deletion was seuccesfull
        deckRepository.deleteById(1L);

        assertThrows(EmptyResultDataAccessException.class, () -> deckRepository.deleteById(1L));
    }

    // test to delete a deck with non existing id, throws exception
    @Test
    public void deleteById_inValid() {
        Deck testDeck = new Deck();
        testDeck.setId(1L);

        entityManager.persist(testDeck);
        entityManager.flush();

        assertThrows(EmptyResultDataAccessException.class, () -> deckRepository.deleteById(100L));
    }
}
