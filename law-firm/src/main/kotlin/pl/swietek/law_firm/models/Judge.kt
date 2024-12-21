package pl.swietek.law_firm.models

data class Judge(
    override val id: Int,
    override val firstName: String,
    override val lastName: String,
    val courtDivisionId: Int,
    val courtDivision: CourtDivision? = null
) : Person
