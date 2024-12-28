package pl.swietek.law_firm.controllers

import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import pl.swietek.law_firm.models.Document
import pl.swietek.law_firm.models.DocumentType
import pl.swietek.law_firm.models.RequiredDocumentForTrial
import pl.swietek.law_firm.reponses.DocumentResponse
import pl.swietek.law_firm.reponses.PaginatedResponse
import pl.swietek.law_firm.requests.DocumentRequest
import pl.swietek.law_firm.services.DocumentService

@RestController
@RequestMapping("/api/v1/documents")
class DocumentController(private val documentService: DocumentService) {

    @GetMapping("/all")
    fun getAllDocuments(
        @RequestParam(defaultValue = "1") page: Int,
        @RequestParam(defaultValue = "10") size: Int
    ): ResponseEntity<PaginatedResponse<DocumentResponse>> {
        val paginatedResponse = documentService.getAllDocuments(page, size)
        return ResponseEntity.ok(paginatedResponse)
    }

    @GetMapping("/{id}")
    fun getDocumentById(@PathVariable id: Long): ResponseEntity<DocumentResponse> {
        val document = documentService.getDocumentById(id)
        return if (document != null) {
            ResponseEntity.ok(
                DocumentResponse(document.first, document.second)
            )
        } else {
            ResponseEntity.notFound().build()
        }
    }

//    @PostMapping
//    fun saveDocument(@RequestBody document: Document): ResponseEntity<Document> {
//        val savedDocument = documentService.saveDocument(document)
//        return ResponseEntity.status(HttpStatus.CREATED).body(savedDocument)
//    }

    @PostMapping(consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun createDocument(
        @RequestParam("title") title: String,
        @RequestParam("description") description: String,
        @RequestParam("typeId") typeId: Long,
        @RequestPart("file") file: MultipartFile
    ): ResponseEntity<Document> {
        val documentRequest = DocumentRequest(title, description, typeId)
        documentRequest.validate()

        val documentResponse = documentService.saveDocument(documentRequest, file)
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(documentResponse)
    }

//    @PutMapping("/{id}")
//    fun updateDocument(
//        @PathVariable id: Long,
//        @RequestBody document: Document
//    ): ResponseEntity<Document> {
//        if (document.id != id) {
//            return ResponseEntity.badRequest().build()
//        }
//        val updatedDocument = documentService.updateDocument(document)
//        return ResponseEntity.ok(updatedDocument)
//    }

    @PutMapping("/{id}", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun updateDocument(
        @PathVariable id: Long,
        @RequestParam("title") title: String?,
        @RequestParam("description") description: String?,
        @RequestParam("typeId") typeId: Long?,
        @RequestPart("file", required = false) file: MultipartFile?
    ): ResponseEntity<Document> {
        if (title == null && description == null && typeId == null && file == null) {
            return ResponseEntity.badRequest().body(null)
        }

        val updatedDocument = documentService.updateDocument(id, title, description, typeId, file)
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
