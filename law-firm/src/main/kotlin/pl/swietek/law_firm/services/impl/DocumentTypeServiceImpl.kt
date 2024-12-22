package pl.swietek.law_firm.services.impl

import org.springframework.stereotype.Service
import pl.swietek.law_firm.models.DocumentType
import pl.swietek.law_firm.repositories.DocumentTypesRepository
import pl.swietek.law_firm.services.DocumentTypeService

@Service
class DocumentTypeServiceImpl(
    private val documentTypesRepository: DocumentTypesRepository
) : DocumentTypeService {

    override fun getAllDocumentTypes(): List<DocumentType> {
        return documentTypesRepository.getAllDocumentTypes()
    }

    override fun getDocumentTypeById(typeId: Long): DocumentType? {
        return documentTypesRepository.getDocumentTypeById(typeId)
    }

    override fun saveDocumentType(documentType: DocumentType): DocumentType {
        return documentTypesRepository.saveDocumentType(documentType)
    }

    override fun updateDocumentType(documentType: DocumentType): DocumentType {
        return documentTypesRepository.updateDocumentType(documentType)
    }

    override fun deleteDocumentType(typeId: Int): Boolean {
        return documentTypesRepository.deleteDocumentType(typeId)
    }

}
