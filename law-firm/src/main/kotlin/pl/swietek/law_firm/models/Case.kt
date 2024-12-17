package pl.swietek.law_firm.models


data class Case(
    val id: Int,
    val name: String,
    val description: String,
    val responsibleLawyerId: Int,
    val clientId: Int,

    val responsibleLawyer: Lawyer? = null,
    val client: Client? = null,
    val documents: List<Document>?
)