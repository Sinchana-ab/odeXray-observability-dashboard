package com.yourorg.observability.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.yourorg.observability.model.LogEntry;

public interface LogEntryRepository extends JpaRepository<LogEntry, Long> {
}