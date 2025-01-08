package pl.swietek.law_firm.services

import org.springframework.web.multipart.MultipartFile
import pl.swietek.law_firm.models.Case
import pl.swietek.law_firm.models.Document
import pl.swietek.law_firm.models.DocumentType
import pl.swietek.law_firm.models.RequiredDocumentForTrial
import pl.swietek.law_firm.reponses.DocumentResponse
import pl.swietek.law_firm.reponses.PaginatedResponse
import pl.swietek.law_firm.requests.DocumentRequest
import pl.swietek.law_firm.requests.RequiredDocumentRequest

interface DocumentService {

    fun getAllDocuments(page: Int, size: Int): PaginatedResponse<DocumentResponse>

    fun getDocumentById(documentId: Long): Pair<Document, DocumentType>?

    fun saveDocument(documentRequest: DocumentRequest, file: MultipartFile): Document

    fun updateDocument(
        id: Long,
        title: String?,
        description: String?,
        typeId: Long?,
        file: MultipartFile?
    ): Document

    fun deleteDocument(documentId: Long): Boolean

    fun getAllRequiredDocumentsForTrials(): List<RequiredDocumentForTrial>

    fun getRequiredDocumentForTrialById(requiredDocumentId: Int): RequiredDocumentForTrial?

    fun getRequiredDocumentsByTrialId(trialId: Int): List<RequiredDocumentForTrial>

    fun saveRequiredDocumentForTrial(requiredDocumentForTrial: RequiredDocumentRequest): RequiredDocumentForTrial

    fun updateRequiredDocumentForTrial(requiredDocumentForTrial: RequiredDocumentRequest): RequiredDocumentForTrial

    fun deleteRequiredDocumentForTrial(requiredDocumentId: Int): Boolean

    fun getCasesForRequiredDocument(requiredDocumentId: Int): List<Case>
}