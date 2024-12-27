package pl.swietek.law_firm.reponses

import pl.swietek.law_firm.models.Document
import pl.swietek.law_firm.models.DocumentType

data class DocumentResponse(
    var document: Document,
    var documentType: DocumentType
) {
}