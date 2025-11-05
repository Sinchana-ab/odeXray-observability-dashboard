package com.yourorg.observability.service;
import org.springframework.stereotype.Service;

import com.yourorg.observability.model.LogEntry;
import com.yourorg.observability.repository.LogEntryRepository;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class LogAnalyzerService {
    private final LogEntryRepository logEntryRepository;

    public LogAnalyzerService(LogEntryRepository logEntryRepository) {
        this.logEntryRepository = logEntryRepository;
    }

    // Very simple parser using keywords ERROR/WARN/INFO
    public Map<String, Long> analyzeFromResource(String resourcePath) throws Exception {
        InputStream is = getClass().getResourceAsStream(resourcePath);
        if (is == null) throw new IllegalArgumentException("Resource not found: " + resourcePath);

        try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
            List<String> lines = br.lines().collect(Collectors.toList());
            // optionally store parsed lines
            List<LogEntry> entries = new ArrayList<>();
            for (String line : lines) {
                String lvl = parseLevel(line);
                entries.add(new LogEntry(line, lvl, Instant.now()));
            }
            logEntryRepository.saveAll(entries);

            Map<String, Long> counts = lines.stream()
                    .map(this::parseLevel)
                    .filter(Objects::nonNull)
                    .collect(Collectors.groupingBy(s -> s, Collectors.counting()));
            return counts;
        }
    }

    public List<String> topNErrorsFromResource(String resourcePath, int n) throws Exception {
        InputStream is = getClass().getResourceAsStream(resourcePath);
        if (is == null) throw new IllegalArgumentException("Resource not found: " + resourcePath);

        try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
            Map<String, Long> freq = br.lines()
                    .filter(l -> parseLevel(l) != null && parseLevel(l).equalsIgnoreCase("ERROR"))
                    .collect(Collectors.groupingBy(l -> l, Collectors.counting()));

            return freq.entrySet().stream()
                    .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                    .limit(n)
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toList());
        }
    }

    private String parseLevel(String line) {
        if (line.contains("ERROR")) return "ERROR";
        if (line.contains("WARN")) return "WARN";
        if (line.contains("INFO")) return "INFO";
        return "OTHER";
    }
}