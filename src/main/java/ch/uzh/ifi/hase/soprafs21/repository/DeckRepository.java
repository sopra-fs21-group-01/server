package ch.uzh.ifi.hase.soprafs21.repository;

import ch.uzh.ifi.hase.soprafs21.entity.Deck;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("deckRepository")
public interface DeckRepository extends JpaRepository<Deck, Long> {

    // will return EmptyResultDataAccessException if the game could not be found. Calls delete() internally
    void deleteById(Long id);

}