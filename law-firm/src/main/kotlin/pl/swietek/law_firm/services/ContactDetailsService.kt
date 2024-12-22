package pl.swietek.law_firm.services

import pl.swietek.law_firm.models.ContactDetails

interface ContactDetailsService {
    fun saveContactDetails(contactDetails: ContactDetails): ContactDetails

    fun getContactDetailsById(id: Int): ContactDetails?

    fun updateContactDetails(id: Int, updatedDetails: ContactDetails): ContactDetails

    fun deleteContactDetails(id: Int)
}