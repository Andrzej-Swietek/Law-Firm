package pl.swietek.law_firm.reponses

import pl.swietek.law_firm.models.Person

data class PersonResponse(
    override val id: Int?,
    override val firstName: String,
    override val lastName: String,
) : Person {

    fun toPerson(): Person {
        return this
    }
}