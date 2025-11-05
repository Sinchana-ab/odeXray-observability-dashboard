package com.yourorg.observability.model;


import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "log_entries")
public class LogEntry {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String rawLine;
    private String level; // INFO, WARN, ERROR, etc.
    private Instant timestamp;

    public LogEntry() {}
    public LogEntry(String rawLine, String level, Instant timestamp) {
        this.rawLine = rawLine;
        this.level = level;
        this.timestamp = timestamp;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getRawLine() { return rawLine; }
    public void setRawLine(String rawLine) { this.rawLine = rawLine; }

    public String getLevel() { return level; }
    public void setLevel(String level) { this.level = level; }

    public Instant getTimestamp() { return timestamp; }
    public void setTimestamp(Instant timestamp) { this.timestamp = timestamp; }
}