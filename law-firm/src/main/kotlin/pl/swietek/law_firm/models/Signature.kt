package pl.swietek.law_firm.models

import java.time.LocalDate

data class Signature(
    val id: Int,
    val personId: Int,
    val role: String,
    val requiredDocumentId: Int,
    val requiredDocument: RequiredDocumentForTrial? = null,
    val person: Person? = null,
    val date: LocalDate? = null,
)