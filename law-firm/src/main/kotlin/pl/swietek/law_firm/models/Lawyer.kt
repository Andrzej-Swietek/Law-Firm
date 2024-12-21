package pl.swietek.law_firm.models

data class Lawyer(
    override val id: Int,
    override val firstName: String,
    override val lastName: String,
    val specialization: String
): Person