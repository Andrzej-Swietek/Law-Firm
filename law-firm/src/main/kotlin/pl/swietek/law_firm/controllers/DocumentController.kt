package pl.swietek.law_firm.controllers

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import pl.swietek.law_firm.models.Document
import pl.swietek.law_firm.models.DocumentType
import pl.swietek.law_firm.models.RequiredDocumentForTrial
import pl.swietek.law_firm.services.DocumentService

@RestController
@RequestMapping("/api/v1/documents")
class DocumentController(private val documentService: DocumentService) {

    @GetMapping("/all")
    fun getAllDocuments(
        @RequestParam page: Int = 1,
        @RequestParam size: Int = 10
    ): ResponseEntity<List<Pair<Document, DocumentType>>> {
        val documents = documentService.getAllDocuments(page, size)
        return ResponseEntity.ok(documents)
    }

    @GetMapping("/{id}")
    fun getDocumentById(@PathVariable id: Long): ResponseEntity<Pair<Document, DocumentType>> {
        val document = documentService.getDocumentById(id)
        return if (document != null) {
            ResponseEntity.ok(document)
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @PostMapping
    fun saveDocument(@RequestBody document: Document): ResponseEntity<Document> {
        val savedDocument = documentService.saveDocument(document)
        return ResponseEntity.status(HttpStatus.CREATED).body(savedDocument)
    }

    @PutMapping("/{id}")
    fun updateDocument(
        @PathVariable id: Long,
        @RequestBody document: Document
    ): ResponseEntity<Document> {
        if (document.id != id) {
            return ResponseEntity.badRequest().build()
        }
        val updatedDocument = documentService.updateDocument(document)
        return ResponseEntity.ok(updatedDocument)
    }

    @DeleteMapping("/{id}")
    fun deleteDocument(@PathVariable id: Long): ResponseEntity<Void> {
        return if (documentService.deleteDocument(id)) {
            ResponseEntity.noContent().build()
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @GetMapping("/required-documents-for-trials")
    fun getAllRequiredDocumentsForTrials(): ResponseEntity<List<RequiredDocumentForTrial>> {
        val requiredDocuments = documentService.getAllRequiredDocumentsForTrials()
        return ResponseEntity.ok(requiredDocuments)
    }

    @GetMapping("/required-documents-for-trials/{id}")
    fun getRequiredDocumentForTrialById(@PathVariable id: Int): ResponseEntity<RequiredDocumentForTrial> {
        val requiredDocument = documentService.getRequiredDocumentForTrialById(id)
        return if (requiredDocument != null) {
            ResponseEntity.ok(requiredDocument)
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @PostMapping("/required-documents-for-trials")
    fun saveRequiredDocumentForTrial(
        @RequestBody requiredDocumentForTrial: RequiredDocumentForTrial
    ): ResponseEntity<RequiredDocumentForTrial> {
        val savedRequiredDocument = documentService.saveRequiredDocumentForTrial(requiredDocumentForTrial)
        return ResponseEntity.status(HttpStatus.CREATED).body(savedRequiredDocument)
    }

    @DeleteMapping("/required-documents-for-trials/{id}")
    fun deleteRequiredDocumentForTrial(@PathVariable id: Int): ResponseEntity<Void> {
        return if (documentService.deleteRequiredDocumentForTrial(id)) {
            ResponseEntity.noContent().build()
        } else {
            ResponseEntity.notFound().build()
        }
    }
}
