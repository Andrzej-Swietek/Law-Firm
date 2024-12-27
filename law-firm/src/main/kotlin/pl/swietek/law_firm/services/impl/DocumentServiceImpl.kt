package pl.swietek.law_firm.services.impl

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import pl.swietek.law_firm.models.Document
import pl.swietek.law_firm.models.DocumentType
import pl.swietek.law_firm.models.RequiredDocumentForTrial
import pl.swietek.law_firm.reponses.DocumentResponse
import pl.swietek.law_firm.reponses.PaginatedResponse
import pl.swietek.law_firm.repositories.DocumentRepository
import pl.swietek.law_firm.repositories.RequiredDocumentsForTrialRepository
import pl.swietek.law_firm.requests.DocumentRequest
import pl.swietek.law_firm.services.DocumentService
import pl.swietek.law_firm.storage.Storage

@Service
class DocumentServiceImpl(
    private val documentRepository: DocumentRepository,
    private val requiredDocumentsForTrialRepository: RequiredDocumentsForTrialRepository,
    @Qualifier("localStorage") private val storage: Storage
): DocumentService {

//    override fun getAllDocuments(page: Int, size: Int): List<Pair<Document, DocumentType>> {
//        return documentRepository.getDocumentsWithAllRelations(page, size)
//    }

    override fun getAllDocuments(page: Int, size: Int): PaginatedResponse<DocumentResponse> {
        val documents = documentRepository.getDocumentsWithAllRelations(page, size)
        val totalCount = documentRepository.countDocuments()

        return PaginatedResponse(
            data = documents,
            currentPage = page,
            size = size,
            totalCount = totalCount
        )
    }


    override fun getDocumentById(documentId: Long): Pair<Document, DocumentType>? {
        return documentRepository.getDocumentWithAllRelationsById(documentId)
    }

    override fun saveDocument(documentRequest: DocumentRequest, file: MultipartFile): Document {
        val filePath = "document/${documentRequest.title}-${file.originalFilename}"
        storage.store(file, filePath)
        val document = Document(
            id = 0,
            title = documentRequest.title,
            description = documentRequest.description,
            typeId = documentRequest.typeId,
            filePath = filePath,
            documentType = null
        )
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