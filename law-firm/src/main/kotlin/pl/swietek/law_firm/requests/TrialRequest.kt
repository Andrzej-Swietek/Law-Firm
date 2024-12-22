package pl.swietek.law_firm.requests

import pl.swietek.law_firm.exceptions.ValidationException
import java.time.LocalDate

data class TrialRequest(
    val id: Int?,
    val title: String,
    val description: String,
    val trialStatusId: Int,
    val clientId: Int,
    val lawyerId: Int,
    val judgeId: Int,
    val date: LocalDate,
    val caseId: Int,
) : RequestData {

    override fun validate() {
        val errors: MutableList<String> = mutableListOf<String>()
        if (title.isBlank()) errors.add("Title must not be blank")
        if (description.isBlank()) errors.add("Description must not be blank")
        if (trialStatusId > 0) errors.add("Trial Status must not be blank")
        if (lawyerId > 0) errors.add("Lawyer Status must not be blank")
        if (clientId > 0) errors.add("Client must not be blank")
        if (caseId > 0) errors.add("Case must not be blank")
        if (errors.isNotEmpty()) throw ValidationException(errors);
    }
}