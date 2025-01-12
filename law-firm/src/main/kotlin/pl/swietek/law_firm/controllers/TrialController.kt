package pl.swietek.law_firm.controllers

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import pl.swietek.law_firm.models.Trial
import pl.swietek.law_firm.requests.TrialRequest
import pl.swietek.law_firm.services.TrialService

@RestController
@RequestMapping("/api/v1/trials")
class TrialController(private val trialService: TrialService) {

    @GetMapping("/all")
    fun getAllTrials(
        @RequestParam("page", defaultValue = "1") page: Int,
        @RequestParam("size", defaultValue = "10") size: Int
    ): ResponseEntity<List<Trial>> {
        val trials = trialService.getAllTrials(page, size)
        return ResponseEntity.ok(trials)
    }

    @GetMapping("/all-unpaginated")
    fun getAllTrials(): ResponseEntity<List<Trial>> {
        val trials = trialService.getTrials()
        return ResponseEntity.ok(trials)
    }

    @GetMapping("/{id}")
    fun getTrialById(@PathVariable id: Int): ResponseEntity<Trial> {
        val trial = trialService.getTrialById(id)
        return if (trial != null) {
            ResponseEntity.ok(trial)
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @GetMapping("/client/{id}")
    fun getTrialByClientId(@PathVariable id: Int): ResponseEntity<List<Trial>> {
        val trials = trialService.getTrialsByClientId(id)
        return ResponseEntity.ok(trials)
    }

    @GetMapping("/lawyer/{id}")
    fun getTrialByLawyerId(@PathVariable id: Int):  ResponseEntity<List<Trial>> {
        val trials = trialService.getTrialsByLawyerId(id)
        return ResponseEntity.ok(trials)
    }


    @PostMapping
    fun saveTrial(@RequestBody trial: TrialRequest): ResponseEntity<Trial> {
        trial.validate()
        val savedTrial = trialService.saveTrial(trial)
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(savedTrial)
    }

    @PutMapping("/{id}")
    fun updateTrial(@PathVariable id: Int, @RequestBody trial: TrialRequest): ResponseEntity<Trial> {
        if (trial.id != id) {
            return ResponseEntity.badRequest().build()
        }
        val updatedTrial = trialService.updateTrial(trial)
        return ResponseEntity.ok(updatedTrial)
    }

    @DeleteMapping("/{id}")
    fun deleteTrial(@PathVariable id: Int): ResponseEntity<Void> {
        return if (trialService.deleteTrial(id)) {
            ResponseEntity.noContent().build()
        } else {
            ResponseEntity.notFound().build()
        }
    }
}