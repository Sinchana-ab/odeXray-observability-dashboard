package com.yourorg.observability.controller;


import com.yourorg.observability.service.LogAnalyzerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class LogController {
    private final LogAnalyzerService logAnalyzerService;

    public LogController(LogAnalyzerService logAnalyzerService) {
        this.logAnalyzerService = logAnalyzerService;
    }

    @GetMapping("/analyze-log")
    public ResponseEntity<?> analyze(@RequestParam(value="resource", defaultValue="/sample.log") String resourcePath) {
        try {
            Map<String, Long> counts = logAnalyzerService.analyzeFromResource(resourcePath);
            List<String> topErrors = logAnalyzerService.topNErrorsFromResource(resourcePath, 5);
            return ResponseEntity.ok(Map.of("counts", counts, "topErrors", topErrors));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
        }
    }
}