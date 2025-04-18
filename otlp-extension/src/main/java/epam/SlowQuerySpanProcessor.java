package epam;

import epam.metrics.CustomMetrics;
import io.opentelemetry.api.common.AttributeKey;
import io.opentelemetry.context.Context;
import io.opentelemetry.sdk.common.CompletableResultCode;
import io.opentelemetry.sdk.trace.ReadWriteSpan;
import io.opentelemetry.sdk.trace.ReadableSpan;
import io.opentelemetry.sdk.trace.SpanProcessor;
import io.opentelemetry.sdk.trace.export.SpanExporter;

public class SlowQuerySpanProcessor implements SpanProcessor {
    private static final long SLOW_QUERY_THRESHOLD_MS = 7 * 1000_000 ; // Define threshold in milliseconds

    private final SpanExporter wrappedExporter;


    private CustomMetrics customMetrics ;

    public SlowQuerySpanProcessor(SpanExporter wrappedExporter) {
        this.wrappedExporter = wrappedExporter;

    }

    private CustomMetrics getCustomMetrics() {
        if (this.customMetrics==null){
            this.customMetrics = new CustomMetrics();
        }
        return customMetrics;
    }

    @Override
    public CompletableResultCode shutdown() {
        return wrappedExporter.shutdown();
    }

    @Override
    public CompletableResultCode forceFlush() {
        return wrappedExporter.flush();
    }

    @Override
    public void onEnd(ReadableSpan span) {
        String dbStatement = span.getAttribute(AttributeKey.stringKey("db.statement"));
        if(dbStatement != null && !dbStatement.isEmpty() ){
            if ( span.getLatencyNanos() > SLOW_QUERY_THRESHOLD_MS) {
                System.out.println("!!!! slow query " +dbStatement +span.getLatencyNanos());

                getCustomMetrics().getRequestCounter().add(1);
            }else {
                System.out.println("!!!! Filter span "+span.getSpanContext()+span.getLatencyNanos());
            }
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
