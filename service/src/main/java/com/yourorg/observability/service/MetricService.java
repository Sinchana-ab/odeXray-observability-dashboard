package com.yourorg.observability.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.yourorg.observability.controller.ThresholdController;
import com.yourorg.observability.model.MetricReading;
import com.yourorg.observability.repository.MetricRepository;

import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.GlobalMemory;

import java.time.Instant;
import java.util.List;

@Service
public class MetricService {

    private final MetricRepository metricRepository;
    private final AlertService alertService;

    private final SystemInfo systemInfo = new SystemInfo();
    private final CentralProcessor processor = systemInfo.getHardware().getProcessor();
    private final GlobalMemory memory = systemInfo.getHardware().getMemory();

    // value in milliseconds
    @Value("${app.metrics.collection-interval-ms:5000}")
    private long intervalMs;

    public MetricService(MetricRepository metricRepository, AlertService alertService) {
        this.metricRepository = metricRepository;
        this.alertService = alertService;
    }

    /**
     * Collect metrics at fixed delay interval
     */
 // Store last CPU ticks globally
    private long[] prevTicks = processor.getSystemCpuLoadTicks();

    @Scheduled(fixedDelayString = "${app.metrics.collection-interval-ms:5000}")
    public void collectMetrics() {
        long[] ticks = processor.getSystemCpuLoadTicks();
        double cpuLoad = processor.getSystemCpuLoadBetweenTicks(prevTicks) * 100.0;
        prevTicks = ticks;

        if (cpuLoad < 0) cpuLoad = 0.0; // some OS might return negative when first called

        double memUsedPct = (1.0 - ((double) memory.getAvailable() / (double) memory.getTotal())) * 100.0;

        MetricReading cpu = new MetricReading("CPU", cpuLoad, Instant.now());
        MetricReading mem = new MetricReading("MEMORY", memUsedPct, Instant.now());

        metricRepository.save(cpu);
        metricRepository.save(mem);

        double cpuLimit = ThresholdController.getCpuThreshold();
        double memLimit = ThresholdController.getMemoryThreshold();

        if (cpuLoad > cpuLimit) alertService.evaluateAndSaveIfNeeded("CPU", cpuLoad);
        if (memUsedPct > memLimit) alertService.evaluateAndSaveIfNeeded("MEMORY", memUsedPct);
    }


    /**
     * Compute average of last N readings for given type
     */
    public double averageLastN(String type, int n) {
    	List<MetricReading> list = metricRepository.findByTypeOrderByTimestampDesc(type, PageRequest.of(0, n));

        if (list == null || list.isEmpty()) return 0.0;

        double sum = 0.0;
        for (MetricReading m : list) {
            sum += m.getValue();
        }
        return sum / list.size();
    }
}
