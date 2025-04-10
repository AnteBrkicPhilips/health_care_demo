package epam;

import io.opentelemetry.api.common.AttributeKey;
import io.opentelemetry.context.Context;
import io.opentelemetry.sdk.trace.ReadWriteSpan;
import io.opentelemetry.sdk.trace.ReadableSpan;
import io.opentelemetry.sdk.trace.SpanProcessor;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

public class SlowQuerySpanProcessor implements SpanProcessor {
//    private static final Logger logger = LoggerFactory.getLogger(epam.SlowQuerySpanProcessor.class);
    private static final long SLOW_QUERY_THRESHOLD_MS = 500; // Define threshold in milliseconds

    @Override
    public void onEnd(ReadableSpan span) {
        if ("db.query".equals(span.getName()) && span.getLatencyNanos() > SLOW_QUERY_THRESHOLD_MS) {
            String dbStatement = span.getAttribute(AttributeKey.stringKey("db.statement"));
            System.out.println("!!!!");
//            logger.warn("Slow query detected: {}", dbStatement);
        }else {
            System.out.println("!!!! working");
        }
    }

    @Override
    public void onStart(Context parentContext, ReadWriteSpan span) {
        // No action needed on span start
    }

    @Override
    public boolean isStartRequired() {
        return false;
    }

    @Override
    public boolean isEndRequired() {
        return true;
    }
}
