package ch.uzh.ifi.hase.soprafs21.repository;

import ch.uzh.ifi.hase.soprafs21.entity.Lobby;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.EmptyResultDataAccessException;


import static org.junit.jupiter.api.Assertions.*;


@DataJpaTest
public class LobbyRepositoryIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private LobbyRepository lobbyRepository;

    // consider that lobby creates its own ID
    @Test
    public void deleteById_valid() {
        Lobby testLobby = new Lobby();
        testLobby.setGamemode("standard");
        testLobby.setHost("Hans");

        entityManager.persist(testLobby);
        entityManager.flush();

        // Lobby ID is creates automatically so we have to pull it
        long Id = testLobby.getId();

        // delete it, second deletion throws expeption because the deletion was seuccesfull
        lobbyRepository.deleteById(Id);

        assertThrows(EmptyResultDataAccessException.class, () -> lobbyRepository.deleteById(Id));
    }

    @Test
    public void deleteById_inValid() {
        Lobby testLobby = new Lobby();
        testLobby.setGamemode("standard");
        testLobby.setHost("Hans");

        entityManager.persist(testLobby);
        entityManager.flush();

        // make sure testId is not the lobby Id
        long Id = testLobby.getId();
        long testID = Id + 2;

        // deleting a non existing Hand will throw error
        assertThrows(EmptyResultDataAccessException.class, () -> lobbyRepository.deleteById(testID));
    }
}
