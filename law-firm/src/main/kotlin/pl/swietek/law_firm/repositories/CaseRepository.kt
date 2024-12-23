package pl.swietek.law_firm.repositories

import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.jdbc.support.KeyHolder
import org.springframework.stereotype.Repository
import pl.swietek.law_firm.mappers.CaseMapper
import pl.swietek.law_firm.mappers.DocumentMapper
import pl.swietek.law_firm.models.Case
import pl.swietek.law_firm.models.Document
import pl.swietek.law_firm.requests.CaseRequest
import java.sql.Connection
import java.sql.PreparedStatement

@Repository
class CaseRepository(
    private val jdbcTemplate: JdbcTemplate,
    private val caseMapper: CaseMapper,
    private val documentMapper: DocumentMapper
) {

    fun getAllCases(page: Int, size: Int): List<Case> {
        val offset = (page - 1) * size
//        val sql = """
//            SELECT
//                c.*,
//                cl.id AS case_client_id,
//                cl.first_name AS client_first_name,
//                cl.last_name AS client_last_name,
//                l.id AS case_responsible_lawyer_id,
//                l.first_name AS lawyer_first_name,
//                l.last_name AS lawyer_last_name
//            FROM LawFirm.case c
//            LEFT JOIN LawFirm.client cl ON c.client_id = cl.id
//            LEFT JOIN LawFirm.lawyer l ON c.responsible_lawyer_id = l.id
//            LIMIT ? OFFSET ?
//        """.trimIndent()

        val sql = """
            SELECT *
            FROM LawFirm.full_data_case
            LIMIT ? OFFSET ?
        """.trimIndent()

        val cases = jdbcTemplate.query(sql, caseMapper, size, offset)

        return cases
    }

    fun getCaseById(caseId: Int): Case? {
        val sql = """
             SELECT 
                c.*, 
                cl.id AS case_client_id, 
                cl.first_name AS client_first_name, 
                cl.last_name AS client_last_name,
                cl.email AS client_email,
                cl.contact_data_id AS client_contact_details_id,
                l.id AS case_responsible_lawyer_id, 
                l.first_name AS lawyer_first_name, 
                l.last_name AS lawyer_last_name,
                l.specialization AS lawyer_specialization
            FROM LawFirm.case c
            LEFT JOIN LawFirm.client cl ON c.client_id = cl.id
            LEFT JOIN LawFirm.lawyer l ON c.responsible_lawyer_id = l.id
            WHERE c.id = ?
        """.trimIndent()

        val case = jdbcTemplate.queryForObject(sql, caseMapper, caseId)

//        case?.documents = getDocumentsForCase(caseId)

        return case
    }

    private fun getDocumentsForCase(caseId: Int): List<Document> {
        val sql = """
            SELECT d.* 
            FROM LawFirm.document d
            JOIN LawFirm.required_documents_for_trial rdt ON rdt.document_id = d.id
            JOIN LawFirm.trial t ON t.id = rdt.trial_id
            WHERE t.case_id = ?
        """.trimIndent()

        return jdbcTemplate.query(sql, documentMapper, caseId)
    }

    fun getCasesByClientId(clientId: Int, page: Int, size: Int): List<Case> {
        val offset = (page - 1) * size
        val sql = """
            SELECT 
                c.*, 
                cl.id AS case_client_id, 
                cl.first_name AS client_first_name, 
                cl.last_name AS client_last_name,
                l.id AS case_responsible_lawyer_id, 
                l.first_name AS lawyer_first_name, 
                l.last_name AS lawyer_last_name
            FROM LawFirm.case c
            LEFT JOIN LawFirm.client cl ON c.client_id = cl.id
            LEFT JOIN LawFirm.lawyer l ON c.responsible_lawyer_id = l.id
            WHERE c.client_id = ?
            LIMIT ? OFFSET ?
        """.trimIndent()

        return jdbcTemplate.query(sql, caseMapper, clientId, size, offset)
    }

    fun getCasesByLawyerId(lawyerId: Int, page: Int, size: Int): List<Case> {
        val offset = (page - 1) * size
        val sql = """
            SELECT 
                c.*, 
                cl.id AS case_client_id, 
                cl.first_name AS client_first_name, 
                cl.last_name AS client_last_name,
                cl.email AS client_email,
                l.id AS case_responsible_lawyer_id, 
                l.first_name AS lawyer_first_name, 
                l.last_name AS lawyer_last_name
            FROM LawFirm.case c
            LEFT JOIN LawFirm.client cl ON c.client_id = cl.id
            LEFT JOIN LawFirm.lawyer l ON c.responsible_lawyer_id = l.id
            WHERE c.responsible_lawyer_id = ?
            LIMIT ? OFFSET ?
        """.trimIndent()

        return jdbcTemplate.query(sql, caseMapper, lawyerId, size, offset)
    }

    fun saveCase(newCase: CaseRequest): Case {
        val sql = """
            INSERT INTO LawFirm.case (name, description, responsible_lawyer_id, client_id)
            VALUES (?, ?, ?, ?)
        """.trimIndent()

        val keyHolder: KeyHolder = GeneratedKeyHolder()

        jdbcTemplate.update({ connection: Connection ->
            val ps: PreparedStatement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)
            ps.setString(1, newCase.name)
            ps.setString(2, newCase.description)
            ps.setInt(3, newCase.responsibleLawyerId)
            ps.setInt(4, newCase.clientId)
            ps
        }, keyHolder)

        val generatedId: Int = keyHolder.keyList
            .firstOrNull()
            ?.get("id")
            .toString().toInt()
            ?: throw Exception("Failed to retrieve generated ID")

        return this.getCaseById(generatedId) ?: throw RuntimeException("Failed to retrieve generated ID")
    }

    fun updateCase(updatedCase: CaseRequest): Case {
        val sql = """
            UPDATE LawFirm.case 
            SET name = ?, description = ?, responsible_lawyer_id = ?, client_id = ?
            WHERE id = ?
        """.trimIndent()

        jdbcTemplate.update(sql, updatedCase.name, updatedCase.description, updatedCase.responsibleLawyerId, updatedCase.clientId, updatedCase.id)
        return this.getCaseById(updatedCase.id)!!
    }

    fun deleteCase(caseId: Int): Boolean {
        val sql = "DELETE FROM LawFirm.case WHERE id = ?"
        val rowsAffected = jdbcTemplate.update(sql, caseId)
        return rowsAffected > 0
    }
}