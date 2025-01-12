package pl.swietek.law_firm.controllers

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import pl.swietek.law_firm.models.Decision
import pl.swietek.law_firm.services.DecisionService

@RestController
@RequestMapping("/api/v1/decisions")
class DecisionController(private val decisionService: DecisionService) {

    @GetMapping("/all")
    fun getAllDecisions(
        @RequestParam("page", defaultValue = "1") page: Int,
        @RequestParam("size", defaultValue = "10") size: Int
    ): ResponseEntity<List<Decision>> {
        val decisions = decisionService.getAllDecisions(page, size)
        return ResponseEntity.ok(decisions)
    }

    @GetMapping("/{id}")
    fun getDecisionById(@PathVariable id: Int): ResponseEntity<Decision> {
        val decision = decisionService.getDecisionById(id)
        return if (decision != null) {
            ResponseEntity.ok(decision)
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @GetMapping("/case/{id}")
    fun getDecisionByCaseId(@PathVariable id: Int): ResponseEntity<List<Decision>> {
        val decisions = decisionService.getDecisionsByCaseId(id)
        return ResponseEntity.ok(decisions)
    }


    @PostMapping
    fun saveDecision(@RequestBody decision: Decision): ResponseEntity<Decision> {
        val savedDecision = decisionService.saveDecision(decision)
        return ResponseEntity.status(HttpStatus.CREATED).body(savedDecision)
    }

    @PutMapping("/{id}")
    fun updateDecision(@PathVariable id: Int, @RequestBody decision: Decision): ResponseEntity<Decision> {
        if (decision.id != id) {
            return ResponseEntity.badRequest().build()
        }
        val updatedDecision = decisionService.updateDecision(decision)
        return ResponseEntity.ok(updatedDecision)
    }

    @DeleteMapping("/{id}")
    fun deleteDecision(@PathVariable id: Int): ResponseEntity<Void> {
        return if (decisionService.deleteDecision(id)) {
            ResponseEntity.noContent().build()
        } else {
            ResponseEntity.notFound().build()
        }
    }
}
