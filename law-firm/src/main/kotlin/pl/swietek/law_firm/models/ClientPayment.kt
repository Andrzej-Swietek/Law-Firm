package pl.swietek.law_firm.models

data class ClientPayment(
    val id: Int,
    val firstName: String?,
    val lastName: String?,
    val email: String?,
    val contactDataId: Int?,
    val payment: Double
)