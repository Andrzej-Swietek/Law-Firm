package pl.swietek.law_firm.requests

import pl.swietek.law_firm.exceptions.ValidationException

data class SignatureRequest(
    val id: Int?,
    val personId: Int,
    val role: String,
    val requiredDocumentId: Int,
): RequestData {
    override fun validate() {
        val errors: MutableList<String> = mutableListOf<String>()

        if (personId <= 0) errors.add("Person must be selected")
        if (role.isBlank()) errors.add("Role must be selected")
        if (requiredDocumentId <= 0) errors.add("Required document ID must be specified")

        if (errors.isNotEmpty()) throw ValidationException(errors);
    }

}
