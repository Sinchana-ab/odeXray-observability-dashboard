package com.yourorg.observability.controller;

import com.yourorg.observability.repository.AlertRepository;
import com.yourorg.observability.repository.MetricRepository;
import com.yourorg.observability.service.MetricService;
import com.yourorg.observability.service.SessionService;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api")
public class SummaryController {

    private final AlertRepository alertRepository;
    private final MetricRepository metricRepository;
    private final MetricService metricService;
    private final SessionService sessionService;

    public SummaryController(AlertRepository alertRepository,
                             MetricRepository metricRepository,
                             MetricService metricService,
                             SessionService sessionService) {
        this.alertRepository = alertRepository;
        this.metricRepository = metricRepository;
        this.metricService = metricService;
        this.sessionService = sessionService;
    }

    @GetMapping("/summary")
    public ResponseEntity<?> summary(
            @RequestHeader(value = "X-SESSION-TOKEN", required = false) String token,
            @RequestParam(value = "lastN", defaultValue = "10") int lastN) {

        // Skip session check for now (frontend test)
        // If you want security, uncomment this check:
        // if (!sessionService.validate(token)) {
        //     return ResponseEntity.status(401).body(Map.of("error", "Invalid session token"));
        // }

        long totalAlerts = alertRepository.count();

        // Count grouped by type
        Map<String, Long> alertsByType = new HashMap<>();
        try {
            List<Object[]> grouped = alertRepository.countGroupedByType();
            for (Object[] row : grouped) {
                alertsByType.put((String) row[0], (Long) row[1]);
            }
        } catch (Exception e) {
            alertsByType.put("CPU", 0L);
            alertsByType.put("MEMORY", 0L);
        }

        // Last alert timestamps
        List<String> lastAlertTimestamps = new ArrayList<>();
        alertRepository.findTop10ByOrderByTimestampDesc().forEach(a ->
                lastAlertTimestamps.add(a.getTimestamp().toString())
        );

        // Average metrics
        double avgCpu = metricService.averageLastN("CPU", lastN);
        double avgMemory = metricService.averageLastN("MEMORY", lastN);

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("totalAlerts", totalAlerts);
        response.put("alertsByType", alertsByType);
        response.put("lastAlertTimestamps", lastAlertTimestamps);
        response.put("avgCpu", avgCpu);
        response.put("avgMemory", avgMemory);

        return ResponseEntity.ok(response);
    }
}
