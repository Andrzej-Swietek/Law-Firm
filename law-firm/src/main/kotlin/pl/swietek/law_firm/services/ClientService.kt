package pl.swietek.law_firm.services

import pl.swietek.law_firm.models.Client
import pl.swietek.law_firm.requests.ClientRequest

interface ClientService {
    fun getAllClients(): List<Client>;
    fun getClientById(id: Long): Client?;
    fun createClient(clientRequest: ClientRequest): Client;
    fun updateClient(clientRequest: ClientRequest): Client;
    fun deleteClient(clientRequest: ClientRequest);
}