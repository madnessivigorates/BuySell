package com.Senla.BuySell.repository;

import com.Senla.BuySell.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message,Long> {
}
