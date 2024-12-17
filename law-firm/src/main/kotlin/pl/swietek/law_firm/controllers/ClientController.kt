package pl.swietek.law_firm.controllers

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import pl.swietek.law_firm.models.Client
import pl.swietek.law_firm.requests.ClientRequest
import pl.swietek.law_firm.services.ClientService

@RestController
@RequestMapping("/api/v1/client")
class ClientController(private val clientService: ClientService) {

    @GetMapping("/all")
    fun getAllClients()  : ResponseEntity<Void> {
        return ResponseEntity
            .ok()
            .build()
    }

    @GetMapping("/{id}")
    fun getClient(@PathVariable("id") id: String) : ResponseEntity<Void> {
        return ResponseEntity.ok().build();
    }

    @PostMapping
    fun createClient(@RequestBody createClientRequest: ClientRequest) : ResponseEntity<Client> {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .build();
    }

    @PutMapping("/{id}")
    fun updateClient(
        @PathVariable("id") id: String,
        @RequestBody updateClientRequest: ClientRequest
    ) : ResponseEntity<Client> {
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    fun deleteClient(@PathVariable("id") id: String): ResponseEntity<Client> {
        return ResponseEntity.ok().build();
    }
}