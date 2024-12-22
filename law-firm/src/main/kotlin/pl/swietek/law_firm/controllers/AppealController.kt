package pl.swietek.law_firm.controllers

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import pl.swietek.law_firm.models.Appeal
import pl.swietek.law_firm.services.AppealService

@RestController
@RequestMapping("/api/v1/appeals")
class AppealController(private val appealService: AppealService) {

    @GetMapping
    fun getAllAppeals(
        @RequestParam("page", defaultValue = "1") page: Int,
        @RequestParam("size", defaultValue = "10") size: Int
    ): ResponseEntity<List<Appeal>> {
        val appeals = appealService.getAllAppeals(page, size)
        return ResponseEntity.ok(appeals)
    }

    @GetMapping("/{id}")
    fun getAppealById(@PathVariable id: Int): ResponseEntity<Appeal> {
        val appeal = appealService.getAppealById(id)
        return if (appeal != null) {
            ResponseEntity.ok(appeal)
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @PostMapping
    fun saveAppeal(@RequestBody appeal: Appeal): ResponseEntity<Appeal> {
        val savedAppeal = appealService.saveAppeal(appeal)
        return ResponseEntity.status(HttpStatus.CREATED).body(savedAppeal)
    }

    @PutMapping("/{id}")
    fun updateAppeal(@PathVariable id: Int, @RequestBody appeal: Appeal): ResponseEntity<Appeal> {
        if (appeal.id != id) {
            return ResponseEntity.badRequest().build()
        }
        val updatedAppeal = appealService.updateAppeal(appeal)
        return ResponseEntity.ok(updatedAppeal)
    }

    @DeleteMapping("/{id}")
    fun deleteAppeal(@PathVariable id: Int): ResponseEntity<Void> {
        return if (appealService.deleteAppeal(id)) {
            ResponseEntity.noContent().build()
        } else {
            ResponseEntity.notFound().build()
        }
    }
}
