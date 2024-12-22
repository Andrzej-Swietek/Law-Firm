package pl.swietek.law_firm.requests

import pl.swietek.law_firm.exceptions.ValidationException

data class TrialRequest(
    val id: Int?,
) : RequestData {

    override fun validate() {
        val errors: MutableList<String> = mutableListOf<String>()
        if (errors.isNotEmpty()) throw ValidationException(errors);
    }
}