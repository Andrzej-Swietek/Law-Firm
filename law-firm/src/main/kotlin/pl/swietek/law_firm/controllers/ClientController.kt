package pl.swietek.law_firm.controllers

import jakarta.validation.Valid
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
    fun getAllClients(
        @RequestParam(defaultValue = "1") page: Int,
        @RequestParam(defaultValue = "10") size: Int
    ): ResponseEntity<List<Client>> {
        val validatedPage = maxOf(0, page)
        val clients = clientService.getAllClients(validatedPage, size)
        return ResponseEntity
            .ok(clients)
    }

    @GetMapping("/{id}")
    fun getClientById(@PathVariable id: Long): ResponseEntity<Client> {
        val client = clientService.getClientById(id)
            ?: return ResponseEntity.notFound().build()
        return ResponseEntity.ok(client)
    }

    @GetMapping("/by-email/{email}")
    fun getClientByEmail(@PathVariable email: String): ResponseEntity<Client> {
        val client = clientService.getClientByEmail(email)
            ?: return ResponseEntity.notFound().build()
        return ResponseEntity.ok(client)
    }

    @GetMapping("/by-lawyer/{lawyerId}")
    fun getClientsByLawyerId(@PathVariable lawyerId: Int): ResponseEntity<List<Client>> {
        return ResponseEntity.ok(
            clientService.getClientByLawyerId(lawyerId)
        )
    }


    @PostMapping
    fun createClient(@RequestBody @Valid clientRequest: ClientRequest): ResponseEntity<Client> {
        val createdClient = clientService.createClient(clientRequest)
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(createdClient)
    }

    @PutMapping("/{id}")
    fun updateClient(
        @PathVariable id: Long,
        @RequestBody @Valid clientRequest: ClientRequest
    ): ResponseEntity<Client> {
        if (id.toInt() != clientRequest.id) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(null)
        }
        val updatedClient = clientService.updateClient(clientRequest)
        return ResponseEntity.ok(updatedClient)
    }

    @DeleteMapping("/{id}")
    fun deleteClient(@PathVariable id: Long): ResponseEntity<Void> {
        clientService.deleteClient(id)
        return ResponseEntity.noContent().build()
    }
}