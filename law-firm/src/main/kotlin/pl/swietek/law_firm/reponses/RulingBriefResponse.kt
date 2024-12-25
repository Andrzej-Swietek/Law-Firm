package pl.swietek.law_firm.reponses

import java.time.LocalDate

data class RulingBriefResponse(
    val id: Int,
    val content: String?,
    val trialId: Int?,
    val finalizationDate: LocalDate?,
    val isFinal: Boolean
)
