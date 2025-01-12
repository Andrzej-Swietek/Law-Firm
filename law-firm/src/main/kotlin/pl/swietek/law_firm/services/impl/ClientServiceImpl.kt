package pl.swietek.law_firm.services.impl

import org.springframework.stereotype.Service
import pl.swietek.law_firm.exceptions.ClientNotFoundException
import pl.swietek.law_firm.models.Client
import pl.swietek.law_firm.models.ContactDetails
import pl.swietek.law_firm.repositories.ClientRepository
import pl.swietek.law_firm.repositories.ContactDataRepository
import pl.swietek.law_firm.requests.ClientRequest
import pl.swietek.law_firm.services.ClientService


@Service
class ClientServiceImpl(
    private val clientRepository: ClientRepository,
    private val contactDataRepository: ContactDataRepository
) : ClientService {

    override fun getAllClients(page: Int, size: Int): List<Client> {
        return clientRepository.getClients(page, size)
    }

    override fun getClientById(id: Long): Client? {
        return clientRepository.getClientById(id)
    }

    override fun getClientByEmail(email: String): Client? {
        return clientRepository.getClientByEmail(email)
    }

    override fun getClientByLawyerId(lawyerId: Int): List<Client> {
        return clientRepository.getClientByLawyerId(lawyerId)
    }

    override fun createClient(clientRequest: ClientRequest): Client {
        clientRequest.validate()

        val contactData = ContactDetails(
            id = null,
            phoneNumber = clientRequest.contactDetails.phoneNumber,
            email = clientRequest.contactDetails.email,
            street = clientRequest.contactDetails.street,
            city = clientRequest.contactDetails.city,
            state = clientRequest.contactDetails.state,
            zipCode = clientRequest.contactDetails.zipCode,
            country = clientRequest.contactDetails.country
        )
        val savedContactData = contactDataRepository.saveContactData(contactData)

        val client = Client(
            id = null,
            firstName = clientRequest.firstName,
            lastName = clientRequest.lastName,
            email = clientRequest.email,
            contactDetailsId = savedContactData.id!!,
            contactDetails = savedContactData
        )

        return clientRepository.saveClient(client)
    }

    override fun updateClient(clientRequest: ClientRequest): Client {
        val existingClient = clientRepository.getClientById(clientRequest.id!!.toLong())
            ?: throw ClientNotFoundException("Client with ID ${clientRequest.id} not found.")

        val updatedContactData = ContactDetails(
            id = existingClient.contactDetailsId,
            phoneNumber = clientRequest.contactDetails.phoneNumber,
            email = clientRequest.contactDetails.email,
            street = clientRequest.contactDetails.street,
            city = clientRequest.contactDetails.city,
            state = clientRequest.contactDetails.state,
            zipCode = clientRequest.contactDetails.zipCode,
            country = clientRequest.contactDetails.country
        )
        contactDataRepository.updateContactData(updatedContactData)

        val updatedClient = existingClient.copy(
            firstName = clientRequest.firstName,
            lastName = clientRequest.lastName,
            email = clientRequest.email,
            contactDetails = updatedContactData
        )

        clientRepository.updateClient(updatedClient)
        return updatedClient
    }

    override fun deleteClient(clientId: Long) {
        val client = clientRepository.getClientById(clientId)
            ?: throw ClientNotFoundException("Client with ID $clientId not found.")

        clientRepository.deleteClient(clientId.toInt())
        contactDataRepository.deleteContactData(client.contactDetailsId)
    }
}