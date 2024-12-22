package pl.swietek.law_firm.services.impl

import org.springframework.stereotype.Service
import pl.swietek.law_firm.exceptions.ClientNotFoundException
import pl.swietek.law_firm.models.ContactDetails
import pl.swietek.law_firm.repositories.ContactDataRepository
import pl.swietek.law_firm.services.ContactDetailsService

@Service
class ContactDetailsServiceImpl(
    private val contactDetailsRepository: ContactDataRepository
): ContactDetailsService  {
    override fun saveContactDetails(contactDetails: ContactDetails): ContactDetails {
        return contactDetailsRepository.saveContactData(contactDetails)
    }

    override fun getContactDetailsById(id: Int): ContactDetails? {
        return contactDetailsRepository.getContactDataById(id.toLong())
    }

    override fun updateContactDetails(id: Int, updatedDetails: ContactDetails): ContactDetails {
        val existingDetails = contactDetailsRepository.getContactDataById(id.toLong())
            ?: throw ClientNotFoundException("Contact details with ID $id not found")
        val mergedDetails = existingDetails.copy(
            phoneNumber = updatedDetails.phoneNumber,
            email = updatedDetails.email,
            street = updatedDetails.street,
            city = updatedDetails.city,
            state = updatedDetails.state,
            zipCode = updatedDetails.zipCode,
            country = updatedDetails.country
        )
        return contactDetailsRepository.saveContactData(mergedDetails)
    }

    override fun deleteContactDetails(id: Int) {
        contactDetailsRepository.deleteContactData(id)
    }
}