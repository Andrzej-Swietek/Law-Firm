package pl.swietek.law_firm.controllers

import io.micrometer.core.instrument.MeterRegistry
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class MetricsController(private val meterRegistry: MeterRegistry) {

    @GetMapping("/example")
    fun exampleEndpoint(): String {
        meterRegistry.counter("custom.metric", "type", "example").increment()
        return "Hello, World!"
    }
}