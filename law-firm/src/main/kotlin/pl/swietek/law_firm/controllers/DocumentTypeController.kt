package pl.swietek.law_firm.controllers

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import pl.swietek.law_firm.models.DocumentType
import pl.swietek.law_firm.services.DocumentTypeService

@RestController
@RequestMapping("/api/v1/document-types")
class DocumentTypeController(private val documentTypeService: DocumentTypeService) {

    @GetMapping("/all")
    fun getAllDocumentTypes(): ResponseEntity<List<DocumentType>> {
        val documentTypes = documentTypeService.getAllDocumentTypes()
        return ResponseEntity.ok(documentTypes)
    }

    @GetMapping("/{id}")
    fun getDocumentTypeById(@PathVariable id: Long): ResponseEntity<DocumentType> {
        val documentType = documentTypeService.getDocumentTypeById(id)
        return if (documentType != null) {
            ResponseEntity.ok(documentType)
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @PostMapping
    fun saveDocumentType(@RequestBody documentType: DocumentType): ResponseEntity<DocumentType> {
        val savedDocumentType = documentTypeService.saveDocumentType(documentType)
        return ResponseEntity.status(HttpStatus.CREATED).body(savedDocumentType)
    }

    @PutMapping("/{id}")
    fun updateDocumentType(
        @PathVariable id: Long,
        @RequestBody documentType: DocumentType
    ): ResponseEntity<DocumentType> {
        if (documentType.id != id) {
            return ResponseEntity.badRequest().build()
        }
        val updatedDocumentType = documentTypeService.updateDocumentType(documentType)
        return ResponseEntity.ok(updatedDocumentType)
    }

    @DeleteMapping("/{id}")
    fun deleteDocumentType(@PathVariable id: Int): ResponseEntity<Void> {
        return if (documentTypeService.deleteDocumentType(id)) {
            ResponseEntity.noContent().build()
        } else {
            ResponseEntity.notFound().build()
        }
    }
}