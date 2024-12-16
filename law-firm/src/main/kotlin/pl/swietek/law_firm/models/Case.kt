package pl.swietek.law_firm.models

data class Case(
    val id: Long,
    val title: String,
    val description: String,
    val lawyerId: Long,
    val clientId: Long,
    val status: String,
    val documents: List<Document>?
) {
}