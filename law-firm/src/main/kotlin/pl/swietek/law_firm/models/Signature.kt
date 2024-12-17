package pl.swietek.law_firm.models

data class Signature(
    val id: Int,
    val personId: Int,
    val role: String,
    val requiredDocumentId: Int,
    val requiredDocument: RequiredDocumentForTrial? = null
)