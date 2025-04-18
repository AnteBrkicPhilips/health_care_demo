package com.example.healthcare.metrics;

import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.api.metrics.LongCounter;

public class CustomMetrics {

    private final LongCounter requestCounter;

    public CustomMetrics() {
        requestCounter = GlobalOpenTelemetry.getMeter("your-instrumentation-name")
                .counterBuilder("customMetric.count")
                .setDescription("Counts the number of requests received")
                .setUnit("1")
                .build();
    }

    public LongCounter getRequestCounter() {
        return requestCounter;
    }
}
