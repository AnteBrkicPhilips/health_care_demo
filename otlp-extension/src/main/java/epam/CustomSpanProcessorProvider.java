package epam;

//import io.opentelemetry.javaagent.extension.config.ConfigProperties;
//import io.opentelemetry.javaagent.extension.sdk.autoconfigure.spi.AutoConfigurationCustomizer;
//import io.opentelemetry.javaagent.extension.sdk.autoconfigure.spi.AutoConfigurationCustomizerProvider;

import io.opentelemetry.sdk.autoconfigure.spi.AutoConfigurationCustomizer;
import io.opentelemetry.sdk.autoconfigure.spi.AutoConfigurationCustomizerProvider;

//import com.google.auto.service.AutoService;
import io.opentelemetry.sdk.autoconfigure.spi.AutoConfigurationCustomizer;
import io.opentelemetry.sdk.autoconfigure.spi.AutoConfigurationCustomizerProvider;

//@AutoService(AutoConfigurationCustomizerProvider.class)
public class CustomSpanProcessorProvider implements AutoConfigurationCustomizerProvider {
    @Override
    public void customize(AutoConfigurationCustomizer customizer) {
//        if(2!=1)throw new RuntimeException("aaaaa");
        System.out.println("Running CustomSpanProcessorProvider...");
        customizer.addTracerProviderCustomizer((sdkTracerProviderBuilder, configProperties) -> sdkTracerProviderBuilder.addSpanProcessor(new SlowQuerySpanProcessor())
        );
    }

//    @Override
//    public void customize(AutoConfigurationCustomizer customizer) {
////        customizer.addSpanProcessorCustomizer((spanProcessor, configProperties) -> {
////
////        });
//        customizer.addSpanProcessor(new CustomSpanProcessor());
//    }
}
