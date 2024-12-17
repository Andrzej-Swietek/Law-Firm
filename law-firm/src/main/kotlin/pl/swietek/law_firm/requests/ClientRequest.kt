package pl.swietek.law_firm.requests

import lombok.Builder
import pl.swietek.law_firm.exceptions.ValidationException
import pl.swietek.law_firm.models.ContactDetails

@Builder
data class ClientRequest(
    val id: Int,
    val firstName: String,
    val lastName: String,
    val email: String,
    val contactDetails: ContactDetails
) : RequestData {

    override fun validate() {
        val errors:  MutableList<String> = mutableListOf<String>()
        if ( firstName.isBlank() ) errors.add("First name must not be blank")
        if ( lastName.isBlank() ) errors.add("Last name cannot be blank")
        if ( email.isBlank() ) errors.add("Email field cannot be empty")
        if (errors.isNotEmpty()) throw ValidationException(errors);
    }

}
