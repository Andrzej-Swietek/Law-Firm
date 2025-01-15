package pl.swietek.law_firm

import io.github.cdimascio.dotenv.dotenv
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class LawFirmApplication

fun main(args: Array<String>) {
	val dotenv = dotenv()
	System.setProperty("DB_USERNAME", dotenv["DB_USERNAME"] ?: "")
	System.setProperty("DB_PASSWORD", dotenv["DB_PASSWORD"] ?: "")

	runApplication<LawFirmApplication>(*args)
}
