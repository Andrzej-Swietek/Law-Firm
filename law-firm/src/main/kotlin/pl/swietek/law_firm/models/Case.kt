package pl.swietek.law_firm.models


data class Case(
    var id: Int,
    val name: String,
    val description: String,
    val responsibleLawyerId: Int,
    val clientId: Int,

    val responsibleLawyer: Lawyer? = null,
    val client: Client? = null,
    var documents: List<Document>?
)