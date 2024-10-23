package pl.swietek.law_firm.models

data class Court(
    val id: Int,
    val name: String,
    val logo: String,
    val contactDetailsId: Int,
)
