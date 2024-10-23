package pl.swietek.law_firm.models

data class Lawyer(
    val id: Long,
    val name: String,
    val specialization: String,
    val contactDetailsId: Long,
)
