package pl.swietek.law_firm.models

import java.time.LocalDate

data class Ruling(
    var id: Int,
    val isFinal: Boolean,
    val content: String,
    val trialId: Int,
    val finalizationDate: LocalDate,
    val trial: Trial? = null
)
