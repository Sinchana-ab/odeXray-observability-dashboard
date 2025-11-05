package com.yourorg.observability.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.yourorg.observability.model.AlertEntity;

import java.util.List;

public interface AlertRepository extends JpaRepository<AlertEntity, Long> {
    long countByType(String type);

    @Query("SELECT a.type, COUNT(a) FROM AlertEntity a GROUP BY a.type")
    List<Object[]> countGroupedByType();

    List<AlertEntity> findTop10ByOrderByTimestampDesc();
}
