package pl.swietek.law_firm.config

import io.micrometer.core.instrument.MeterRegistry
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class MetricsConfig {

    @Bean
    fun customMetrics(): (MeterRegistry) -> Unit {
        return { meterRegistry ->
            meterRegistry.counter("custom.metric", "type", "example")
        }
    }
}