package com.yourorg.observability.controller;

import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

@RestController
@RequestMapping("/api")
public class ThresholdController {

    private static final AtomicReference<Double> cpuThreshold = new AtomicReference<>(80.0);
    private static final AtomicReference<Double> memoryThreshold = new AtomicReference<>(75.0);

    @PostMapping("/thresholds")
    public String updateThresholds(@RequestBody Map<String, Double> thresholds) {
        if (thresholds.containsKey("cpu")) {
            cpuThreshold.set(thresholds.get("cpu"));
        }
        if (thresholds.containsKey("memory")) {
            memoryThreshold.set(thresholds.get("memory"));
        }
        return "Thresholds updated!";
    }

    @GetMapping("/thresholds")
    public Map<String, Double> getThresholds() {
        return Map.of(
                "cpu", cpuThreshold.get(),
                "memory", memoryThreshold.get()
        );
    }

    public static double getCpuThreshold() {
        return cpuThreshold.get();
    }

    public static double getMemoryThreshold() {
        return memoryThreshold.get();
    }
}
