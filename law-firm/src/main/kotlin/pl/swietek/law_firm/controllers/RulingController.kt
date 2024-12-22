package pl.swietek.law_firm.controllers

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import pl.swietek.law_firm.models.Ruling
import pl.swietek.law_firm.services.RulingService

@RestController
@RequestMapping("/api/v1/rulings")
class RulingController(private val rulingService: RulingService) {

    @GetMapping("/{id}")
    fun getRulingById(@PathVariable id: Int): ResponseEntity<Ruling> {
        val ruling = rulingService.getRulingById(id)
        return if (ruling != null) {
            ResponseEntity.ok(ruling)
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @GetMapping("/all")
    fun getAllRulings(
        @RequestParam(defaultValue = "1") page: Int,
        @RequestParam(defaultValue = "10") size: Int
    ): ResponseEntity<List<Ruling>> {
        val rulings = rulingService.getAllRulings(page, size)
        return ResponseEntity.ok(rulings)
    }

    @PostMapping
    fun saveRuling(@RequestBody ruling: Ruling): ResponseEntity<Ruling> {
        val savedRuling = rulingService.saveRuling(ruling)
        return ResponseEntity.status(HttpStatus.CREATED).body(savedRuling)
    }

    @PutMapping("/{id}")
    fun updateRuling(
        @PathVariable id: Int,
        @RequestBody ruling: Ruling
    ): ResponseEntity<Ruling> {
        if (ruling.id != id) {
            return ResponseEntity.badRequest().build()
        }
        val updatedRuling = rulingService.updateRuling(ruling)
        return ResponseEntity.ok(updatedRuling)
    }

    @DeleteMapping("/{id}")
    fun deleteRuling(@PathVariable id: Int): ResponseEntity<Void> {
        return if (rulingService.deleteRuling(id)) {
            ResponseEntity.noContent().build()
        } else {
            ResponseEntity.notFound().build()
        }
    }
}
