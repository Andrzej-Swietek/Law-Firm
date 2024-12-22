package pl.swietek.law_firm.controllers

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import pl.swietek.law_firm.models.CourtDivision
import pl.swietek.law_firm.services.CourtService

@RestController
@RequestMapping("/api/v1/courts")
class CourtController(private val courtService: CourtService) {

    @GetMapping("/all")
    fun getAllCourts(): ResponseEntity<List<CourtDivision>> {
        val courts = courtService.getAllCourts()
        return ResponseEntity.ok(courts)
    }

    @PostMapping
    fun createCourt(@RequestBody court: CourtDivision): ResponseEntity<CourtDivision> {
        val savedCourt = courtService.saveCourt(court)
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCourt)
    }

    @GetMapping("/{id}")
    fun getCourt(@PathVariable id: Int): ResponseEntity<CourtDivision> {
        val court = courtService.getCourtById(id)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Court not found")
        return ResponseEntity.ok(court)
    }

    @PutMapping("/{id}")
    fun updateCourt(@PathVariable id: Int, @RequestBody updatedCourt: CourtDivision): ResponseEntity<CourtDivision> {
        val updated = courtService.updateCourt(id, updatedCourt)
        return ResponseEntity.ok(updated)
    }

    @DeleteMapping("/{id}")
    fun deleteCourt(@PathVariable id: Int): ResponseEntity<Void> {
        courtService.deleteCourt(id)
        return ResponseEntity.noContent().build()
    }
}
