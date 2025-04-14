package epam;


import io.opentelemetry.exporter.otlp.http.trace.OtlpHttpSpanExporter;
import io.opentelemetry.sdk.autoconfigure.spi.AutoConfigurationCustomizer;
import io.opentelemetry.sdk.autoconfigure.spi.AutoConfigurationCustomizerProvider;
import io.opentelemetry.sdk.trace.export.SpanExporter;

public class CustomSpanProcessorProvider implements AutoConfigurationCustomizerProvider {



    @Override
    public void customize(AutoConfigurationCustomizer customizer) {

        SpanExporter spanExporter = OtlpHttpSpanExporter.builder()
                .setEndpoint("http://localhost:4318") // Replace with your collector's endpoint
                .addHeader("authorization","52a2ce46-3cad-468f-8d7d-844d73c72133")
                .build();

        System.out.println("Running CustomSpanProcessorProvider...");
        customizer.addTracerProviderCustomizer((sdkTracerProviderBuilder, configProperties) ->
                sdkTracerProviderBuilder.addSpanProcessor(new SlowQuerySpanProcessor(spanExporter))
        );
    }

}
