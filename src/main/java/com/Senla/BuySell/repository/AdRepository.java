package com.Senla.BuySell.repository;

import com.Senla.BuySell.enums.AdType;
import com.Senla.BuySell.model.Ad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdRepository extends JpaRepository<Ad,Long> {
    @Query("SELECT a FROM Ad a JOIN a.seller s " +
            "LEFT JOIN Review r ON r.receiver.id = s.id " +
            "WHERE (:adType IS NULL OR a.adType = :adType) " +
            "AND (:keyword IS NULL OR LOWER(a.title) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(a.description) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
            "AND (a.promotedUntil IS NULL OR a.promotedUntil > CURRENT_TIMESTAMP) " +
            "GROUP BY a.id " +
            "ORDER BY COALESCE(AVG(r.rating), 0) DESC, a.isPromoted DESC, a.promotedUntil DESC")
    List<Ad> findAllByAdTypeAndKeyword(@Param("adType") AdType adType,
                                       @Param("keyword") String keyword);

}
