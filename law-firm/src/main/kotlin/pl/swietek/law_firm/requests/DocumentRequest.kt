package pl.swietek.law_firm.requests

import pl.swietek.law_firm.exceptions.ValidationException

data class DocumentRequest(
    val title: String,
    val description: String,
    val typeId: Long,

): RequestData {

    override fun validate() {
        val errors:  MutableList<String> = mutableListOf<String>()
        if ( title.isBlank() ) errors.add("Title must not be blank")
        if ( description.isBlank() ) errors.add("Description must not be blank")
        if (errors.isNotEmpty()) throw ValidationException(errors);
    }

}