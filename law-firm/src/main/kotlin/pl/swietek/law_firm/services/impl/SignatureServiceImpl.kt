package pl.swietek.law_firm.services.impl

import org.springframework.stereotype.Service
import pl.swietek.law_firm.models.Signature
import pl.swietek.law_firm.repositories.SignatureRepository
import pl.swietek.law_firm.services.SignatureService

@Service
class SignatureServiceImpl(
    private val signatureRepository: SignatureRepository
) : SignatureService {

    override fun getSignatureById(signatureId: Int): Signature? {
       return signatureRepository.getSignatureById(signatureId)
    }

    override fun getSignaturesByDocumentId(documentId: Int): List<Signature> {
        return signatureRepository.getSignaturesByDocumentId(documentId)
    }

    override fun getSignaturesByCaseId(caseId: Int): List<Signature> {
        return signatureRepository.getSignaturesByCaseId(caseId)
    }

    override fun saveSignature(signature: Signature): Signature {
        return signatureRepository.saveSignature(signature)
    }

    override fun updateSignature(signature: Signature): Signature {
        return signatureRepository.updateSignature(signature)
    }

    override fun deleteSignature(signatureId: Int): Boolean {
        return signatureRepository.deleteSignature(signatureId)
    }

}