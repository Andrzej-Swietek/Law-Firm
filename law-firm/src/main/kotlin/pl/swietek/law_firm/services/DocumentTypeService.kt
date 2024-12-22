package pl.swietek.law_firm.services

import pl.swietek.law_firm.models.DocumentType

interface DocumentTypeService {

    fun getAllDocumentTypes(): List<DocumentType>

    fun getDocumentTypeById(typeId: Long): DocumentType?

    fun saveDocumentType(documentType: DocumentType): DocumentType

    fun updateDocumentType(documentType: DocumentType): DocumentType

    fun deleteDocumentType(typeId: Int): Boolean

}