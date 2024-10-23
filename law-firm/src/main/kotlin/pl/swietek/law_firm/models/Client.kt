package pl.swietek.law_firm.models

data class Client(
    val id: Long,
    val name: String,
    val lastName: String,
    val contactDetailsId: Long
)
