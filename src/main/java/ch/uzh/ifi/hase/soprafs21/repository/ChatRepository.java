package ch.uzh.ifi.hase.soprafs21.repository;

import ch.uzh.ifi.hase.soprafs21.entity.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository("chatRepository")
public interface ChatRepository extends JpaRepository<Chat, Long> {
    Chat findById(String id);
    Chat findAllById(Long id);
    List<Chat> findAllBylobby(Long lobby);

    void deleteAllBylobby(Long lobby);
}
