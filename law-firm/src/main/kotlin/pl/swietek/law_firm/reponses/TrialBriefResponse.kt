package pl.swietek.law_firm.reponses

import pl.swietek.law_firm.models.*
import java.time.LocalDate

data class TrialBriefResponse(
    val id: Int?,
    val title: String?,
    val description: String?,
    val trialStatusId: Int?,
    val clientId: Int?,
    val lawyerId: Int?,
    val judgeId: Int?,
    val date: LocalDate?,
    val caseId: Int?,

    val trialStatus: TrialStatus? = null,
    val client: Client? = null,
    val lawyer: Lawyer? = null,
    val judge: Judge? = null,
    val case: Case? = null
) {
}