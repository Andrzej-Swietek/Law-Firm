package pl.swietek.law_firm.repositories

import org.springframework.stereotype.Repository

import org.springframework.jdbc.core.JdbcTemplate
import pl.swietek.law_firm.mappers.DecisionMapper
import pl.swietek.law_firm.models.Decision

@Repository
class DecisionRepository(
    private val jdbcTemplate: JdbcTemplate,
    private val decisionMapper: DecisionMapper
) {

    fun getDecisionById(decisionId: Int): Decision? {
        val sql = """
            SELECT 
                d.id as decision_id,
                d.name as decision_name,
                d.description as decision_description,
                d.date as decision_date,
                d.case_id as decision_case_id,
                
                c.id AS decision_case_id, 
                c.name AS case_name, 
                c.description AS case_description,
                c.responsible_lawyer_id as case_responsible_lawyer_id,
                c.client_id as case_client_id
                
            FROM LawFirm.decision d
            LEFT JOIN LawFirm.case c ON d.case_id = c.id
            LEFT JOIN LawFirm.client cl ON c.client_id = cl.id 
            WHERE d.id = ?
        """.trimIndent()

        return jdbcTemplate.queryForObject(sql, decisionMapper, decisionId)
    }

    fun getDecisionsByCaseId(caseId: Int): List<Decision> {
        val sql = """
            SELECT 
                d.id as decision_id,
                d.name as decision_name,
                d.description as decision_description,
                d.date as decision_date,
                d.case_id as decision_case_id,
                
                c.id AS decision_case_id, 
                c.name AS case_name, 
                c.description AS case_description,
                c.responsible_lawyer_id as case_responsible_lawyer_id,
                c.client_id as case_client_id
                
            FROM LawFirm.decision d
            LEFT JOIN LawFirm.case c ON d.case_id = c.id
            LEFT JOIN LawFirm.client cl ON c.client_id = cl.id 
            WHERE d.case_id = ?
        """.trimIndent()

        return jdbcTemplate.query(sql, decisionMapper, caseId)
    }

    fun getAllDecisions(page: Int, size: Int): List<Decision> {
        val offset = (page - 1) * size
        val sql = """
            SELECT 
                d.id as decision_id,
                d.name as decision_name,
                d.description as decision_description,
                d.date as decision_date,
                d.case_id as decision_case_id,
                
                c.id AS decision_case_id, 
                c.name AS case_name, 
                c.description AS case_description
            FROM LawFirm.decision d
            LEFT JOIN LawFirm.case c ON d.case_id = c.id
            LIMIT ? OFFSET ?
        """.trimIndent()

        return jdbcTemplate.query(sql, decisionMapper, size, offset)
    }

    fun saveDecision(newDecision: Decision): Decision {
        val sql = """
            INSERT INTO LawFirm.decision (name, description, date, case_id)
            VALUES (?, ?, ?, ?)
        """.trimIndent()

        val id = jdbcTemplate.update(sql, newDecision.name, newDecision.description, newDecision.date, newDecision.caseId)
        newDecision.id = id
        return newDecision
    }

    fun updateDecision(updatedDecision: Decision): Decision {
        val sql = """
            UPDATE LawFirm.decision 
            SET name = ?, description = ?, date = ?, case_id = ?
            WHERE id = ?
        """.trimIndent()

        jdbcTemplate.update(sql, updatedDecision.name, updatedDecision.description, updatedDecision.date, updatedDecision.caseId, updatedDecision.id)
        return updatedDecision
    }

    fun deleteDecision(decisionId: Int): Boolean {
        val sql = "DELETE FROM LawFirm.decision WHERE id = ?"
        val rowsAffected = jdbcTemplate.update(sql, decisionId)
        return rowsAffected > 0
    }
}
