package pl.swietek.law_firm.repositories

import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository
import pl.swietek.law_firm.mappers.SignatureMapper
import pl.swietek.law_firm.models.Signature
import pl.swietek.law_firm.repositories.queryBuilders.SignatureQueryBuilder

@Repository
class SignatureRepository(
    private val jdbcTemplate: JdbcTemplate,
    private val signatureMapper: SignatureMapper
) {

    fun getAllSignatures(page: Int, size: Int): List<Signature> {
        val offset = (page - 1) * size

        val queryBuilder = SignatureQueryBuilder()
            .selectBasic()
            .withPersonData()
            .withRequiredDocument()
            .withTrial()

        val sql = """
            ${queryBuilder.build()}
            LIMIT ? OFFSET ?
        """.trimIndent()

        return jdbcTemplate.query(sql, signatureMapper, size, offset)
    }

    fun getSignatureById(signatureId: Int): Signature? {

        val queryBuilder = SignatureQueryBuilder()
            .selectBasic()
            .withPersonData()
            .withRequiredDocument()
            .withTrial()
            .where("s.id = ?")

        val sql = queryBuilder.build()
        println(sql)

        return jdbcTemplate.query(sql, signatureMapper, signatureId).firstOrNull()
    }

    fun getSignaturesByDocumentId(documentId: Int): List<Signature> {
        val sql = """
            SELECT s.*, 
                   LawFirm.get_signature_person_data(s.id) AS person_data
            FROM LawFirm.signature s
            JOIN LawFirm.required_documents_for_trial rdft ON s.required_document_id = rdft.id
            WHERE rdft.document_id = ?
        """.trimIndent()

        return jdbcTemplate.query(sql, signatureMapper, documentId)
    }

    fun getSignaturesByRequiredDocumentId(requiredDocumentId: Int): List<Signature> {
//        val sql = """
//            SELECT s.*,
//                   LawFirm.get_signature_person_data(s.id) AS person_data
//            FROM LawFirm.signature s
//            JOIN LawFirm.required_documents_for_trial rdft ON s.required_document_id = rdft.id
//            WHERE rdft.id = ?
//        """.trimIndent()

        val queryBuilder = SignatureQueryBuilder()
            .selectBasic()
            .withPersonData()
            .withRequiredDocument()
            .withTrial()
            .where("rdft.id = ?")

        val sql = queryBuilder.build()

        return jdbcTemplate.query(sql, signatureMapper, requiredDocumentId)
    }

    fun getSignaturesByCaseId(caseId: Int): List<Signature> {
        val sql = """
            SELECT s.*, 
                   LawFirm.get_signature_person_data(s.id) AS person_data
            FROM LawFirm.signature s
            JOIN LawFirm.required_documents_for_trial rdft ON s.required_document_id = rdft.id
            JOIN LawFirm.trial t ON rdft.trial_id = t.id
            WHERE t.case_id = ?
        """.trimIndent()

        return jdbcTemplate.query(sql, signatureMapper, caseId)
    }

    fun saveSignature(signature: Signature): Signature {
        val sql = """
            INSERT INTO LawFirm.signature (person_id, role, required_document_id, date)
            VALUES ( ?, ?, ?, ?)
        """.trimIndent()

        jdbcTemplate.update(
            sql,
            signature.personId,
            signature.role,
            signature.requiredDocumentId,
            signature.date
        )

        return signature
    }

    fun updateSignature(signature: Signature): Signature {
        val sql = """
            UPDATE LawFirm.signature
            SET person_id = ?, role = ?, required_document_id = ?
            WHERE id = ?
        """.trimIndent()

        jdbcTemplate.update(
            sql,
            signature.personId,
            signature.role,
            signature.requiredDocumentId,
            signature.id
        )

        return signature
    }

    fun deleteSignature(signatureId: Int): Boolean {
        val sql = "DELETE FROM LawFirm.signature WHERE id = ?"
        val rowsAffected = jdbcTemplate.update(sql, signatureId)
        return rowsAffected > 0
    }
}