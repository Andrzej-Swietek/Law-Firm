package pl.swietek.law_firm.services

import pl.swietek.law_firm.models.Document
import pl.swietek.law_firm.models.DocumentType
import pl.swietek.law_firm.models.RequiredDocumentForTrial

interface DocumentService {
    fun getAllDocuments(page: Int, size: Int): List<Pair<Document, DocumentType>>

    fun getDocumentById(documentId: Long): Pair<Document, DocumentType>?

    fun saveDocument(document: Document): Document

    fun updateDocument(document: Document): Document

    fun deleteDocument(documentId: Long): Boolean

    fun getAllRequiredDocumentsForTrials(): List<RequiredDocumentForTrial>

    fun getRequiredDocumentForTrialById(requiredDocumentId: Int): RequiredDocumentForTrial?

    fun saveRequiredDocumentForTrial(requiredDocumentForTrial: RequiredDocumentForTrial): RequiredDocumentForTrial

    fun deleteRequiredDocumentForTrial(requiredDocumentId: Int): Boolean
}