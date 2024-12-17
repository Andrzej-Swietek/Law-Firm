package pl.swietek.law_firm.models

data class Lawyer(
    val id: Int,
    val firstName: String,
    val lastName: String,
    val specialization: String
)