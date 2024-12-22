package pl.swietek.law_firm.controllers

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import pl.swietek.law_firm.models.TrialStatus
import pl.swietek.law_firm.services.TrialStatusService

@RestController
@RequestMapping("/api/v1/trial-statuses")
class TrialStatusController(private val trialStatusService: TrialStatusService) {

    @GetMapping("/all")
    fun getAllTrialStatuses(): ResponseEntity<List<TrialStatus>> {
        val trialStatuses = trialStatusService.getAllTrialStatuses()
        return ResponseEntity.ok(trialStatuses)
    }

    @GetMapping("/{id}")
    fun getTrialStatusById(@PathVariable id: Long): ResponseEntity<TrialStatus> {
        val trialStatus = trialStatusService.getTrialStatusById(id)
        return if (trialStatus != null) {
            ResponseEntity.ok(trialStatus)
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @PostMapping
    fun saveTrialStatus(@RequestBody trialStatus: TrialStatus): ResponseEntity<TrialStatus> {
        val savedTrialStatus = trialStatusService.saveTrialStatus(trialStatus)
        return ResponseEntity.status(HttpStatus.CREATED).body(savedTrialStatus)
    }

    @PutMapping("/{id}")
    fun updateTrialStatus(
        @PathVariable id: Long,
        @RequestBody trialStatus: TrialStatus
    ): ResponseEntity<TrialStatus> {
        if (trialStatus.id != id) {
            return ResponseEntity.badRequest().build()
        }
        val updatedTrialStatus = trialStatusService.updateTrialStatus(trialStatus)
        return ResponseEntity.ok(updatedTrialStatus)
    }

    @DeleteMapping("/{id}")
    fun deleteTrialStatus(@PathVariable id: Int): ResponseEntity<Void> {
        return if (trialStatusService.deleteTrialStatus(id)) {
            ResponseEntity.noContent().build()
        } else {
            ResponseEntity.notFound().build()
        }
    }
}