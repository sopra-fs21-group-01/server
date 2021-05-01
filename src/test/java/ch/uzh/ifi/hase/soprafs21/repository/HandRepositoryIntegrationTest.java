package ch.uzh.ifi.hase.soprafs21.repository;
import java.util.Optional;
import ch.uzh.ifi.hase.soprafs21.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs21.entity.Game;
import ch.uzh.ifi.hase.soprafs21.entity.Hand;
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
public class HandRepositoryIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private HandRepository handRepository;


    @Test
    public void deleteById_valid() {
        Hand testHand = new Hand();
        testHand.setId(1L);

        entityManager.persist(testHand);
        entityManager.flush();

        // delete it, second deletion throws expeption because the deletion was seuccesfull
        handRepository.deleteById(1L);

        assertThrows(EmptyResultDataAccessException.class, () -> handRepository.deleteById(1L));
    }

    @Test
    public void deleteById_inValid() {
        Hand testHand = new Hand();
        testHand.setId(1L);

        entityManager.persist(testHand);
        entityManager.flush();

        // deleting a non existing Hand will throw error
        assertThrows(EmptyResultDataAccessException.class, () -> handRepository.deleteById(100L));
    }
}
