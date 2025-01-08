package pl.swietek.law_firm.services.impl

import org.springframework.stereotype.Service
import pl.swietek.law_firm.models.Signature
import pl.swietek.law_firm.repositories.SignatureRepository
import pl.swietek.law_firm.requests.SignatureRequest
import pl.swietek.law_firm.services.SignatureService
import java.time.LocalDate
import java.time.LocalDateTime

@Service
class SignatureServiceImpl(
    private val signatureRepository: SignatureRepository
) : SignatureService {
    override fun getAllSignatures(page: Int, size: Int): List<Signature> {
        return this.signatureRepository.getAllSignatures(page, size);
    }

    override fun getSignatureById(signatureId: Int): Signature? {
       return signatureRepository.getSignatureById(signatureId)
    }

    override fun getSignaturesByDocumentId(documentId: Int): List<Signature> {
        return signatureRepository.getSignaturesByDocumentId(documentId)
    }

    override fun getSignaturesByCaseId(caseId: Int): List<Signature> {
        return signatureRepository.getSignaturesByCaseId(caseId)
    }

    override fun getSignaturesByRequiredDocumentId(requiredDocumentId: Int): List<Signature> {
        return this.signatureRepository.getSignaturesByRequiredDocumentId(requiredDocumentId)
    }

    override fun saveSignature(request: SignatureRequest): Signature {
        val signature = Signature(
            id = request.id ?: 0,
            requiredDocumentId = request.requiredDocumentId,
            personId = request.personId,
            role = request.role,
            date = LocalDate.now(),
        )
        return signatureRepository.saveSignature(signature)
    }

    override fun updateSignature(signature: Signature): Signature {
        return signatureRepository.updateSignature(signature)
    }

    override fun deleteSignature(signatureId: Int): Boolean {
        return signatureRepository.deleteSignature(signatureId)
    }

}