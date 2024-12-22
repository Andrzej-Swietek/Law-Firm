package pl.swietek.law_firm.services.impl

import org.springframework.stereotype.Service
import pl.swietek.law_firm.models.Document
import pl.swietek.law_firm.models.DocumentType
import pl.swietek.law_firm.models.RequiredDocumentForTrial
import pl.swietek.law_firm.repositories.DocumentRepository
import pl.swietek.law_firm.repositories.RequiredDocumentsForTrialRepository
import pl.swietek.law_firm.services.DocumentService

@Service
class DocumentServiceImpl(
    private val documentRepository: DocumentRepository,
    private val requiredDocumentsForTrialRepository: RequiredDocumentsForTrialRepository
): DocumentService {

    override fun getAllDocuments(page: Int, size: Int): List<Pair<Document, DocumentType>> {
        return documentRepository.getDocumentsWithAllRelations(page, size)
    }

    override fun getDocumentById(documentId: Long): Pair<Document, DocumentType>? {
        return documentRepository.getDocumentWithAllRelationsById(documentId)
    }

    override fun saveDocument(document: Document): Document {
        return documentRepository.saveDocument(document)
    }

    override fun updateDocument(document: Document): Document {
        return documentRepository.updateDocument(document)
    }

    override fun deleteDocument(documentId: Long): Boolean {
        return documentRepository.deleteDocument(documentId)
    }



    override fun getAllRequiredDocumentsForTrials(): List<RequiredDocumentForTrial> {
        return requiredDocumentsForTrialRepository.getRequiredDocumentsForTrialWithAllRelations()
    }

    override fun getRequiredDocumentForTrialById(requiredDocumentId: Int): RequiredDocumentForTrial? {
        return requiredDocumentsForTrialRepository.getRequiredDocumentForTrialById(requiredDocumentId)
    }

    override fun saveRequiredDocumentForTrial(requiredDocumentForTrial: RequiredDocumentForTrial): RequiredDocumentForTrial {
        return requiredDocumentsForTrialRepository.saveRequiredDocumentForTrial(requiredDocumentForTrial)
    }

    override fun deleteRequiredDocumentForTrial(requiredDocumentId: Int): Boolean {
        return requiredDocumentsForTrialRepository.deleteRequiredDocumentForTrial(requiredDocumentId)
    }
}