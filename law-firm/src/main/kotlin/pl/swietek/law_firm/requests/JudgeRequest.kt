package pl.swietek.law_firm.requests

import pl.swietek.law_firm.exceptions.ValidationException
import pl.swietek.law_firm.models.ContactDetails

data class JudgeRequest(
    val id: Int?,
    val firstName: String,
    val lastName: String,
    var courtDivisionId: Int,
) : RequestData {

    override fun validate() {
        val errors: MutableList<String> = mutableListOf<String>()
        if (firstName.isBlank()) errors.add("First name must not be blank")
        if (lastName.isBlank()) errors.add("Last name cannot be blank")
        if (errors.isNotEmpty()) throw ValidationException(errors);
    }

}