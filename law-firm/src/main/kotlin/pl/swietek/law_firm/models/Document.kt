package pl.swietek.law_firm.models

import java.time.LocalDate

data class Document(
    val id: Int,
    val typeId: Int,
    val filePath: String,
    val documentType: DocumentType? = null
)

data class DocumentType(
    val id: Int,
    val name: String
)

data class RequiredDocumentForTrial(
    val id: Int,
    val trialId: Int,
    val documentId: Int,
    val trial: Trial? = null,
    val document: Document? = null
)