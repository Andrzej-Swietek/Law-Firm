package pl.swietek.law_firm.requests

import pl.swietek.law_firm.exceptions.ValidationException

data class CaseRequest(
    var id: Int,
    val name: String,
    val description: String,
    val responsibleLawyerId: Int,
    val clientId: Int,
): RequestData {

    override fun validate() {
        val errors:  MutableList<String> = mutableListOf()
        if ( name.isBlank() ) errors.add("First name must not be blank")
        if ( description.isBlank() ) errors.add("Description name cannot be blank")
        if ( responsibleLawyerId <= 0 ) errors.add("Lawyer field cannot be empty")
        if ( clientId <= 0 ) errors.add("Client field cannot be empty")
        if (errors.isNotEmpty()) throw ValidationException(errors);
    }

}
