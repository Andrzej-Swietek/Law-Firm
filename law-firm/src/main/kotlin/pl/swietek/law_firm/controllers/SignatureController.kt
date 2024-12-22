package pl.swietek.law_firm.controllers

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import pl.swietek.law_firm.models.Signature
import pl.swietek.law_firm.services.SignatureService

@RestController
@RequestMapping("/api/v1/signatures")
class SignatureController(private val signatureService: SignatureService) {

    @GetMapping("/{id}")
    fun getSignatureById(@PathVariable id: Int): ResponseEntity<Signature> {
        val signature = signatureService.getSignatureById(id)
        return if (signature != null) {
            ResponseEntity.ok(signature)
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @GetMapping("/document/{documentId}")
    fun getSignaturesByDocumentId(@PathVariable documentId: Int): ResponseEntity<List<Signature>> {
        val signatures = signatureService.getSignaturesByDocumentId(documentId)
        return ResponseEntity.ok(signatures)
    }

    @GetMapping("/case/{caseId}")
    fun getSignaturesByCaseId(@PathVariable caseId: Int): ResponseEntity<List<Signature>> {
        val signatures = signatureService.getSignaturesByCaseId(caseId)
        return ResponseEntity.ok(signatures)
    }

    @PostMapping
    fun saveSignature(@RequestBody signature: Signature): ResponseEntity<Signature> {
        val savedSignature = signatureService.saveSignature(signature)
        return ResponseEntity.status(HttpStatus.CREATED).body(savedSignature)
    }

    @PutMapping("/{id}")
    fun updateSignature(
        @PathVariable id: Int,
        @RequestBody signature: Signature
    ): ResponseEntity<Signature> {
        if (signature.id != id) {
            return ResponseEntity.badRequest().build()
        }
        val updatedSignature = signatureService.updateSignature(signature)
        return ResponseEntity.ok(updatedSignature)
    }

    @DeleteMapping("/{id}")
    fun deleteSignature(@PathVariable id: Int): ResponseEntity<Void> {
        return if (signatureService.deleteSignature(id)) {
            ResponseEntity.noContent().build()
        } else {
            ResponseEntity.notFound().build()
        }
    }
}