package com.Senla.BuySell.repository;

import com.Senla.BuySell.model.RemovedAd;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RemovedAdRepository extends JpaRepository<RemovedAd, Long> {
    List<RemovedAd> findBySellerId(Long sellerId);
}
