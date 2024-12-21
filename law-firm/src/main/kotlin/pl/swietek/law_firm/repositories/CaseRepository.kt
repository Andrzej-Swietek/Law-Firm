package pl.swietek.law_firm.repositories

import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository
import pl.swietek.law_firm.mappers.CaseMapper
import pl.swietek.law_firm.mappers.DocumentMapper
import pl.swietek.law_firm.models.Case
import pl.swietek.law_firm.models.Document

@Repository
class CaseRepository(
    private val jdbcTemplate: JdbcTemplate,
    private val caseMapper: CaseMapper,
    private val documentMapper: DocumentMapper
) {

    fun getAllCases(page: Int, size: Int): List<Case> {
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
                l.id AS case_responsible_lawyer_id, 
                l.first_name AS lawyer_first_name, 
                l.last_name AS lawyer_last_name
            FROM LawFirm.case c
            LEFT JOIN LawFirm.client cl ON c.client_id = cl.id
            LEFT JOIN LawFirm.lawyer l ON c.responsible_lawyer_id = l.id
            WHERE c.id = ?
        """.trimIndent()

        val case = jdbcTemplate.queryForObject(sql, caseMapper, caseId)

        case?.documents = getDocumentsForCase(caseId)

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

    fun saveCase(newCase: Case): Case {
        val sql = """
            INSERT INTO LawFirm.case (name, description, responsible_lawyer_id, client_id)
            VALUES (?, ?, ?, ?)
        """.trimIndent()

        // Zapisz przypadek i zwróć go
        val id = jdbcTemplate.update(sql, newCase.name, newCase.description, newCase.responsibleLawyerId, newCase.clientId)
        newCase.id = id
        return newCase
    }

    fun updateCase(updatedCase: Case): Case {
        val sql = """
            UPDATE LawFirm.case 
            SET name = ?, description = ?, responsible_lawyer_id = ?, client_id = ?
            WHERE id = ?
        """.trimIndent()

        jdbcTemplate.update(sql, updatedCase.name, updatedCase.description, updatedCase.responsibleLawyerId, updatedCase.clientId, updatedCase.id)
        return updatedCase
    }

    fun deleteCase(caseId: Int): Boolean {
        val sql = "DELETE FROM LawFirm.case WHERE id = ?"
        val rowsAffected = jdbcTemplate.update(sql, caseId)
        return rowsAffected > 0
    }
}