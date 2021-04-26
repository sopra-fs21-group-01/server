package ch.uzh.ifi.hase.soprafs21.repository;

import ch.uzh.ifi.hase.soprafs21.entity.Hand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("handRepository")
public interface HandRepository extends JpaRepository<Hand, Long> {

    // will return EmptyResultDataAccessException if the game could not be found. Calls delete() internally
    void deleteById(Long id);

}