package ch.uzh.ifi.hase.soprafs21.repository;

import ch.uzh.ifi.hase.soprafs21.entity.Lobby;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("lobbyRepository")
public interface LobbyRepository extends JpaRepository<Lobby, Long> {

    // will return EmptyResultDataAccessException if the Lobby could not be found. Calls delete() internally
        void deleteById(Long id);

}
