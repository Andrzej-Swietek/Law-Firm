package pl.swietek.law_firm

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class LawFirmApplication

fun main(args: Array<String>) {
	runApplication<LawFirmApplication>(*args)
}
