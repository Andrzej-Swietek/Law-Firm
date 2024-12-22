package pl.swietek.law_firm.controllers

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import pl.swietek.law_firm.models.Case
import pl.swietek.law_firm.services.CaseService

@RestController
@RequestMapping("/api/v1/cases")
class CaseController(private val caseService: CaseService) {

    @GetMapping("/all")
    fun getAllCases(
        @RequestParam page: Int,
        @RequestParam size: Int
    ): ResponseEntity<List<Case>> {
        val cases = caseService.getAllCases(page, size)
        return ResponseEntity.ok(cases)
    }

    @GetMapping("/{id}")
    fun getCaseById(@PathVariable id: Int): ResponseEntity<Case> {
        val case = caseService.getCaseById(id)
        return if (case != null) ResponseEntity.ok(case) else ResponseEntity.notFound().build()
    }

    @GetMapping("/lawyer/{lawyerId}")
    fun getCasesByLawyerId(
        @PathVariable lawyerId: Int,
        @RequestParam page: Int,
        @RequestParam size: Int
    ): ResponseEntity<List<Case>> {
        val cases = caseService.getCasesByLawyerId(lawyerId, page, size)
        return ResponseEntity.ok(cases)
    }

    @GetMapping("/client/{clientId}")
    fun getCasesByClientId(
        @PathVariable clientId: Int,
        @RequestParam page: Int,
        @RequestParam size: Int
    ): ResponseEntity<List<Case>> {
        val cases = caseService.getCasesByClientId(clientId, page, size)
        return ResponseEntity.ok(cases)
    }

    @PostMapping
    fun saveCase(@RequestBody newCase: Case): ResponseEntity<Case> {
        val savedCase = caseService.saveCase(newCase)
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCase)
    }

    @PutMapping("/{id}")
    fun updateCase(
        @PathVariable id: Int,
        @RequestBody updatedCase: Case
    ): ResponseEntity<Case> {
        if (updatedCase.id != id) return ResponseEntity.badRequest().build()
        val case = caseService.updateCase(updatedCase)
        return ResponseEntity.ok(case)
    }

    @DeleteMapping("/{id}")
    fun deleteCase(@PathVariable id: Int): ResponseEntity<Void> {
        return if (caseService.deleteCase(id)) ResponseEntity.noContent().build()
        else ResponseEntity.notFound().build()
    }
}
