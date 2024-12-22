package pl.swietek.law_firm.controllers

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import pl.swietek.law_firm.models.Lawyer
import pl.swietek.law_firm.services.LawyerService

@RestController
@RequestMapping("/api/v1/lawyers")
class LawyerController(private val lawyerService: LawyerService) {

    @GetMapping("/all")
    fun getAllLawyers(): ResponseEntity<List<Lawyer>> =
        ResponseEntity.ok(lawyerService.getAllLawyers())

    @GetMapping("/{id}")
    fun getLawyerById(@PathVariable id: Int): ResponseEntity<Lawyer> {
        val lawyer = lawyerService.getLawyerById(id)
        return if (lawyer != null) ResponseEntity.ok(lawyer) else ResponseEntity.notFound().build()
    }

    @PostMapping
    fun createLawyer(@RequestBody lawyer: Lawyer): ResponseEntity<Lawyer> =
        ResponseEntity.ok(lawyerService.saveLawyer(lawyer))

    @PutMapping("/{id}")
    fun updateLawyer(@PathVariable id: Int, @RequestBody lawyer: Lawyer): ResponseEntity<Lawyer> =
        ResponseEntity.ok(lawyerService.updateLawyer(lawyer.copy(id = id)))

    @DeleteMapping("/{id}")
    fun deleteLawyer(@PathVariable id: Int): ResponseEntity<Void> =
        if (lawyerService.deleteLawyer(id)) ResponseEntity.noContent().build() else ResponseEntity.notFound().build()
}