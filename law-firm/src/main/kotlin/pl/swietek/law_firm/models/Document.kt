package pl.swietek.law_firm.models

import java.time.LocalDate

data class Document(
    val id: Long,
    val caseId: Long,
    val submitted: Boolean,
    val type: String,
    val dateSubmitted: LocalDate
)
