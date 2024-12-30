package pl.swietek.law_firm.requests

import pl.swietek.law_firm.exceptions.ValidationException

data class RequiredDocumentRequest(
    val id: Int?,
    val trialId: Int,
    val documentId: Int
): RequestData {

    override fun validate() {
        val errors:  MutableList<String> = mutableListOf<String>()

        if (trialId <= 0) errors.add("Trial ID must be a positive number")
        if (documentId <= 0) errors.add("Document ID must be a positive number")

        if (errors.isNotEmpty()) throw ValidationException(errors);
    }
}
