package pl.swietek.law_firm.services

import org.springframework.web.multipart.MultipartFile
import pl.swietek.law_firm.models.Document
import pl.swietek.law_firm.models.DocumentType
import pl.swietek.law_firm.models.RequiredDocumentForTrial
import pl.swietek.law_firm.reponses.DocumentResponse
import pl.swietek.law_firm.reponses.PaginatedResponse
import pl.swietek.law_firm.requests.DocumentRequest

interface DocumentService {

    fun getAllDocuments(page: Int, size: Int): PaginatedResponse<DocumentResponse>

    fun getDocumentById(documentId: Long): Pair<Document, DocumentType>?

    fun saveDocument(documentRequest: DocumentRequest, file: MultipartFile): Document

    fun updateDocument(document: Document): Document

    fun deleteDocument(documentId: Long): Boolean

    fun getAllRequiredDocumentsForTrials(): List<RequiredDocumentForTrial>

    fun getRequiredDocumentForTrialById(requiredDocumentId: Int): RequiredDocumentForTrial?

    fun saveRequiredDocumentForTrial(requiredDocumentForTrial: RequiredDocumentForTrial): RequiredDocumentForTrial

    fun deleteRequiredDocumentForTrial(requiredDocumentId: Int): Boolean
}