package com.Senla.BuySell.repository;

import com.Senla.BuySell.model.Review;
import com.Senla.BuySell.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByReceiver(User receiver);

}
