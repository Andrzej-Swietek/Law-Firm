package pl.swietek.law_firm.models

data class ContactDetails(
    val id: Int?,
    val phoneNumber: String,
    val email: String,
    val street: String,
    val city: String,
    val state: String,
    val zipCode: String,
    val country: String
)