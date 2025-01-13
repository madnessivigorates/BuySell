package com.Senla.BuySell.repository;

import com.Senla.BuySell.model.Ad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdRepository extends JpaRepository<Ad,Long> {
    @Query("SELECT a FROM Ad a JOIN a.user u " +
            "WHERE a.promotedUntil IS NULL OR a.promotedUntil > CURRENT_TIMESTAMP " +
            "ORDER BY a.isPromoted DESC, a.promotedUntil DESC, u.rating DESC")
    List<Ad> findAllByOrderByPromotedAndUserRatingDesc();
}
