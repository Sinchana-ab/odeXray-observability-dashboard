package com.yourorg.observability.repository;

import com.yourorg.observability.model.MetricReading;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MetricRepository extends JpaRepository<MetricReading, Long> {
    List<MetricReading> findByTypeOrderByTimestampDesc(String type, Pageable pageable);
}
