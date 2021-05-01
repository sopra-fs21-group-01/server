package ch.uzh.ifi.hase.soprafs21.repository;
import java.util.Optional;
import ch.uzh.ifi.hase.soprafs21.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs21.entity.Game;
import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.entity.Deck;
import javassist.NotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;


@DataJpaTest
public class DeckRepositoryIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private DeckRepository deckRepository;


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

    @Test
    public void deleteById_inValid() {
        Deck testDeck = new Deck();
        testDeck.setId(1L);


        entityManager.persist(testDeck);
        entityManager.flush();

        assertThrows(EmptyResultDataAccessException.class, () -> deckRepository.deleteById(100L));
    }
}
