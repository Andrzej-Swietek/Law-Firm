package pl.swietek.law_firm.models

import lombok.AllArgsConstructor
import lombok.Builder
import lombok.NoArgsConstructor

@NoArgsConstructor
@AllArgsConstructor
@Builder
data class Client(
    override val id: Int,
    override val firstName: String,
    override val lastName: String,
    val email: String,
    val contactDetailsId: Int,
    val contactDetails: ContactDetails? = null
): Person