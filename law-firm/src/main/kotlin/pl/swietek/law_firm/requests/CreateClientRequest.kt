package pl.swietek.law_firm.requests

import pl.swietek.law_firm.exceptions.ValidationException


data class CreateClientRequest(
    val name: String,
) : RequestData {

    override fun validate() {
        val errors: List<String> = ArrayList()
        if (errors.isNotEmpty()) throw ValidationException(errors);
    }

}
