package com.example.healthcare;

import com.p6spy.engine.spy.appender.MessageFormattingStrategy;

public class CustomP6SpyLogger implements MessageFormattingStrategy {

    private static final long THRESHOLD = 50; // Threshold in milliseconds

    @Override
    public String formatMessage(int connectionId, String now, long elapsed, String category, String prepared, String sql, String url) {
        if (elapsed > THRESHOLD) {
            return String.format("Elapsed Time: %d ms | Category: %s | Connection: %d | Prepared: %s | SQL: %s",
                    elapsed, category, connectionId, prepared, sql);
        }
        return ""; // Return an empty string if the threshold is not met
    }
}