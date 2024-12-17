package pl.swietek.law_firm.models

data class Judge(
    val id: Int,
    val firstName: String,
    val lastName: String,
    val courtDivisionId: Int,
    val courtDivision: CourtDivision? = null
)
