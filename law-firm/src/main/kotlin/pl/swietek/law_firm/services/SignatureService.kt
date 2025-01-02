package pl.swietek.law_firm.services

import pl.swietek.law_firm.models.Signature
import pl.swietek.law_firm.requests.SignatureRequest

interface SignatureService {

    fun getAllSignatures(page: Int, size: Int): List<Signature>

    fun getSignatureById(signatureId: Int): Signature?

    fun getSignaturesByDocumentId(documentId: Int): List<Signature>

    fun getSignaturesByCaseId(caseId: Int): List<Signature>

    fun saveSignature(signature: SignatureRequest): Signature

    fun updateSignature(signature: Signature): Signature

    fun deleteSignature(signatureId: Int): Boolean

}