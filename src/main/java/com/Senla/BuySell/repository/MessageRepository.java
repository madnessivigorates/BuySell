package com.Senla.BuySell.repository;

import com.Senla.BuySell.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message,Long> {
    @Query("SELECT m FROM Message m WHERE (m.sender.id = :userId1 AND m.receiver.id = :userId2) OR (m.sender.id = :userId2 AND m.receiver.id = :userId1) ORDER BY m.sentAt ASC")
    List<Message> findChatHistory(Long userId1, Long userId2);
}
