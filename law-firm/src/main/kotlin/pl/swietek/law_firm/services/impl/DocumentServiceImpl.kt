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
import pl.swietek.law_firm.requests.RequiredDocumentRequest
import pl.swietek.law_firm.services.DocumentService
import pl.swietek.law_firm.storage.Storage
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths


@Service
class DocumentServiceImpl(
    private val documentRepository: DocumentRepository,
    private val requiredDocumentsForTrialRepository: RequiredDocumentsForTrialRepository,
    @Qualifier("localStorage") private val storage: Storage
): DocumentService {

    private val uploadDirectory = "/documents"

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

    @Throws(IOException::class)
    fun saveDocumentFile(file: MultipartFile, fileName: String): String {
        val uploadDir = Paths.get("store/documents/")
        if (!Files.exists(uploadDir)) {
            Files.createDirectories(uploadDir)
        }

        val filePath =  uploadDir.resolve(fileName)
        Files.write(filePath, file.bytes)

        return filePath.toString()
    }

    override fun saveDocument(documentRequest: DocumentRequest, file: MultipartFile): Document {
        val fileName = "${documentRequest.title}-${file.originalFilename}"
        val filePath = "$uploadDirectory/$fileName"
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

    override fun updateDocument(
        id: Long,
        title: String?,
        description: String?,
        typeId: Long?,
        file: MultipartFile?
    ): Document {
        val existingDocument = documentRepository.getDocumentWithAllRelationsById(id)?.first
        val newTitle = title ?: existingDocument?.title
        val newDescription = description ?: existingDocument?.description
        val newTypeId = typeId ?: existingDocument?.typeId

        if (file != null) {
            val fileName = existingDocument?.filePath
            val filePath = "$uploadDirectory/$fileName"
            storage.store(file, filePath)
        }

        return documentRepository.updateDocument(Document(
            id = id,
            title = newTitle!!,
            description = newDescription!!,
            typeId = newTypeId!!,
            filePath = existingDocument?.filePath!!,
            documentType = null
        ))
    }

    override fun deleteDocument(documentId: Long): Boolean {
        try {
            val documentResult = getDocumentById(documentId);
            val document = documentResult?.first
            if (document != null) {
                storage.delete(document.filePath)
            }
            return documentRepository.deleteDocument(documentId)
        } catch (e: Exception) {
            return false
        }
    }



    override fun getAllRequiredDocumentsForTrials(): List<RequiredDocumentForTrial> {
        return requiredDocumentsForTrialRepository.getRequiredDocumentsForTrialWithAllRelations()
    }

    override fun getRequiredDocumentForTrialById(requiredDocumentId: Int): RequiredDocumentForTrial? {
        return requiredDocumentsForTrialRepository.getRequiredDocumentForTrialById(requiredDocumentId)
    }

    override fun saveRequiredDocumentForTrial(requiredDocumentForTrial: RequiredDocumentRequest): RequiredDocumentForTrial {
        return requiredDocumentsForTrialRepository.saveRequiredDocumentForTrial(
            requiredDocumentForTrial
        )
    }

    override fun updateRequiredDocumentForTrial(requiredDocumentForTrial: RequiredDocumentRequest): RequiredDocumentForTrial {
        return requiredDocumentsForTrialRepository.updateRequiredDocumentForTrial(
            requiredDocumentForTrial
        )
    }

    override fun deleteRequiredDocumentForTrial(requiredDocumentId: Int): Boolean {
        return requiredDocumentsForTrialRepository.deleteRequiredDocumentForTrial(requiredDocumentId)
    }
}