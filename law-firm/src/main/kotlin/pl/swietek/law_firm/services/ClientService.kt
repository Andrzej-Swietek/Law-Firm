package pl.swietek.law_firm.services

import pl.swietek.law_firm.models.Client
import pl.swietek.law_firm.requests.ClientRequest

interface ClientService {
    fun getAllClients(page: Int, size: Int): List<Client>
    fun getClientById(id: Long): Client?
    fun getClientByLawyerId(lawyerId: Int): List<Client>
    fun getClientByEmail(email: String): Client?
    fun createClient(clientRequest: ClientRequest): Client
    fun updateClient(clientRequest: ClientRequest): Client
    fun deleteClient(clientId: Long)
}