package pl.swietek.law_firm.controllers

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import pl.swietek.law_firm.models.Judge
import pl.swietek.law_firm.services.JudgeService

@RestController
@RequestMapping("/api/v1/judges")
class JudgeController(private val judgeService: JudgeService) {

    @GetMapping("/all")
    fun getAllJudges(): ResponseEntity<List<Judge>> =
        ResponseEntity.ok(judgeService.getAllJudges())

    @GetMapping("/{id}")
    fun getJudgeById(@PathVariable id: Int): ResponseEntity<Judge> {
        val judge = judgeService.getJudgeById(id)
        return if (judge != null) ResponseEntity.ok(judge) else ResponseEntity.notFound().build()
    }

    @PostMapping
    fun createJudge(@RequestBody judge: Judge): ResponseEntity<Judge> =
        ResponseEntity.ok(judgeService.saveJudge(judge))

    @PutMapping("/{id}")
    fun updateJudge(@PathVariable id: Int, @RequestBody judge: Judge): ResponseEntity<Judge> =
        ResponseEntity.ok(judgeService.updateJudge(judge.copy(id = id)))

    @DeleteMapping("/{id}")
    fun deleteJudge(@PathVariable id: Int): ResponseEntity<Void> =
        if (judgeService.deleteJudge(id)) ResponseEntity.noContent().build() else ResponseEntity.notFound().build()
}