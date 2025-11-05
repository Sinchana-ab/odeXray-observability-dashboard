package com.yourorg.observability.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.yourorg.observability.model.AlertEntity;
import com.yourorg.observability.repository.AlertRepository;

import java.time.Instant;

@Service
public class AlertService {
    private final AlertRepository alertRepository;

    @Value("${app.metrics.cpu-threshold:80.0}")
    private double cpuThreshold;

    @Value("${app.metrics.mem-threshold:75.0}")
    private double memThreshold;

    public AlertService(AlertRepository alertRepository) {
        this.alertRepository = alertRepository;
    }

    public void evaluateAndSaveIfNeeded(String type, double value) {
        double threshold = "CPU".equalsIgnoreCase(type) ? cpuThreshold : memThreshold;
        if (value > threshold) {
            String msg = String.format("%s usage %.2f exceeded threshold %.2f", type, value, threshold);
            AlertEntity a = new AlertEntity(type, value, Instant.now(), msg);
            alertRepository.save(a);
        }
    }
}