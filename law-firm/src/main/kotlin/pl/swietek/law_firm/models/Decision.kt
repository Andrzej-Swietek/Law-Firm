package pl.swietek.law_firm.models

import java.time.LocalDate

data class Decision(
    val id: Int,
    val name: String,
    val description: String,
    val date: LocalDate,
    val caseId: Int,

    val case: Case? = null
)