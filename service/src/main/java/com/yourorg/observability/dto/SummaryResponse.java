package com.yourorg.observability.dto;


import java.util.List;
import java.util.Map;

public class SummaryResponse {
    private long totalAlerts;
    private Map<String, Long> alertsByType;
    private List<?> lastAlertTimestamps;
    private double avgCpu;
    private double avgMemory;

    public SummaryResponse() {}

    public SummaryResponse(long totalAlerts, Map<String, Long> alertsByType, List<?> lastAlertTimestamps, double avgCpu, double avgMemory) {
        this.totalAlerts = totalAlerts;
        this.alertsByType = alertsByType;
        this.lastAlertTimestamps = lastAlertTimestamps;
        this.avgCpu = avgCpu;
        this.avgMemory = avgMemory;
    }

    public long getTotalAlerts() { return totalAlerts; }
    public void setTotalAlerts(long totalAlerts) { this.totalAlerts = totalAlerts; }

    public Map<String, Long> getAlertsByType() { return alertsByType; }
    public void setAlertsByType(Map<String, Long> alertsByType) { this.alertsByType = alertsByType; }

    public List<?> getLastAlertTimestamps() { return lastAlertTimestamps; }
    public void setLastAlertTimestamps(List<?> lastAlertTimestamps) { this.lastAlertTimestamps = lastAlertTimestamps; }

    public double getAvgCpu() { return avgCpu; }
    public void setAvgCpu(double avgCpu) { this.avgCpu = avgCpu; }

    public double getAvgMemory() { return avgMemory; }
    public void setAvgMemory(double avgMemory) { this.avgMemory = avgMemory; }
}