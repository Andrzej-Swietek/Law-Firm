package pl.swietek.law_firm.controllers

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import pl.swietek.law_firm.models.ContactDetails
import pl.swietek.law_firm.services.ContactDetailsService
import pl.swietek.law_firm.services.impl.ContactDetailsServiceImpl

@RestController
@RequestMapping("/api/v1/contact-details")
class ContactDetailsController(
    private val contactDetailsService: ContactDetailsService
) {

    @PostMapping
    fun createContactDetails(@RequestBody contactDetails: ContactDetails): ResponseEntity<ContactDetails> {
        val savedDetails = contactDetailsService.saveContactDetails(contactDetails)
        return ResponseEntity.status(HttpStatus.CREATED).body(savedDetails)
    }

    @GetMapping("/{id}")
    fun getContactDetails(@PathVariable id: Int): ResponseEntity<ContactDetails> {
        val details = contactDetailsService.getContactDetailsById(id)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Contact details not found")
        return ResponseEntity.ok(details)
    }

    @GetMapping("/client/{id}")
    fun getContactDetailsByClient(@PathVariable id: Int): ResponseEntity<ContactDetails> {
        val details = contactDetailsService.getContactDetailsByClient(id)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Contact details not found")
        return ResponseEntity.ok(details)
    }

    @PutMapping("/{id}")
    fun updateContactDetails(
        @PathVariable id: Int,
        @RequestBody updatedDetails: ContactDetails
    ): ResponseEntity<ContactDetails> {
        val updated = contactDetailsService.updateContactDetails(id, updatedDetails)
        return ResponseEntity.ok(updated)
    }

    @DeleteMapping("/{id}")
    fun deleteContactDetails(@PathVariable id: Int): ResponseEntity<Void> {
        contactDetailsService.deleteContactDetails(id)
        return ResponseEntity.noContent().build()
    }
}