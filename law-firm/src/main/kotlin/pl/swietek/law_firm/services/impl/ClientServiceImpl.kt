package pl.swietek.law_firm.services.impl

import org.springframework.stereotype.Service
import pl.swietek.law_firm.models.Client
import pl.swietek.law_firm.repositories.ClientRepository
import pl.swietek.law_firm.requests.ClientRequest
import pl.swietek.law_firm.services.ClientService

@Service
class ClientServiceImpl(private val clientRepository: ClientRepository) : ClientService {
    override fun getAllClients(): List<Client> {
        return this.clientRepository.getClients(0,10);
    }

    override fun getClientById(id: Long): Client? {
        val client =  this.clientRepository.getClientById(id);
        return client;
    }

    override fun createClient(clientRequest: ClientRequest): Client {
        clientRequest.validate()
        TODO("Not yet implemented")
    }

    override fun updateClient(clientRequest: ClientRequest): Client {
        TODO("Not yet implemented")
    }

    override fun deleteClient(clientRequest: ClientRequest) {
        TODO("Not yet implemented")
    }
}