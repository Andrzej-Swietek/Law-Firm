package pl.swietek.law_firm.models

data class Document(
    val id: Long,
    val typeId: Long,
    val filePath: String,
    val documentType: DocumentType? = null
)

data class DocumentType(
    val id: Long,
    val name: String
)

data class RequiredDocumentForTrial(
    val id: Int,
    val trialId: Int,
    val documentId: Int,
    val trial: Trial? = null,
    val document: Document? = null
)