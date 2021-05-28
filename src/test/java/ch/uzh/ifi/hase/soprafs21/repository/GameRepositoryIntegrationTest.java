package ch.uzh.ifi.hase.soprafs21.repository;
import ch.uzh.ifi.hase.soprafs21.entity.Game;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.EmptyResultDataAccessException;
import static org.junit.jupiter.api.Assertions.*;


@DataJpaTest
public class GameRepositoryIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private GameRepository gameRepository;

    // delete a game succesfully, a second attempt will throw an exception because it is already deleted
    @Test
    public void deleteById_valid() {
        Game testGame = new Game();
        testGame.setId(1L);
        testGame.setHost("Player");

        entityManager.persist(testGame);
        entityManager.flush();

        // delete it, second deletion throws expeption because the deletion was seuccesfull
        gameRepository.deleteById(1L);

        assertThrows(EmptyResultDataAccessException.class, () -> gameRepository.deleteById(1L));
    }

    // test with invalid id, throws exception
    @Test
    public void deleteById_inValid() {
        Game testGame = new Game();
        testGame.setId(1L);
        testGame.setHost("Player");

        entityManager.persist(testGame);
        entityManager.flush();

        assertThrows(EmptyResultDataAccessException.class, () -> gameRepository.deleteById(100L));
    }
}
