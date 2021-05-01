package ch.uzh.ifi.hase.soprafs21.repository;
import java.util.Optional;
import ch.uzh.ifi.hase.soprafs21.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs21.entity.Game;
import ch.uzh.ifi.hase.soprafs21.entity.User;
import javassist.NotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;


@DataJpaTest
public class GameRepositoryIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private GameRepository gameRepository;


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
