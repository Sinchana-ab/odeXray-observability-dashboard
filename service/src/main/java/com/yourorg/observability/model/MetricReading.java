package com.yourorg.observability.model;


import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "metric_readings")
public class MetricReading {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String type; // CPU or MEMORY
    private Double value;
    private Instant timestamp;

    public MetricReading() {}
    public MetricReading(String type, Double value, Instant timestamp) {
        this.type = type;
        this.value = value;
        this.timestamp = timestamp;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public Double getValue() { return value; }
    public void setValue(Double value) { this.value = value; }

    public Instant getTimestamp() { return timestamp; }
    public void setTimestamp(Instant timestamp) { this.timestamp = timestamp; }
}
