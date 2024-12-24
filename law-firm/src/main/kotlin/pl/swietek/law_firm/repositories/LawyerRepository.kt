package pl.swietek.law_firm.repositories

import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository
import pl.swietek.law_firm.models.Lawyer
import pl.swietek.law_firm.mappers.LawyerMapper

@Repository
class LawyerRepository(private val jdbcTemplate: JdbcTemplate) {

    private val lawyerRowMapper = LawyerMapper()

    fun getLawyers(): List<Lawyer> {
        val sql = """
            SELECT id, first_name, last_name, specialization
            FROM LawFirm.lawyer
        """.trimIndent()

        return jdbcTemplate.query(sql, lawyerRowMapper)
    }

    fun getLawyerById(lawyerId: Int): Lawyer? {
        val sql = """
            SELECT id, first_name, last_name, specialization
            FROM LawFirm.lawyer
            WHERE id = ?
        """.trimIndent()

        return jdbcTemplate.query(sql, lawyerRowMapper, lawyerId).firstOrNull()
    }

    fun saveLawyer(lawyer: Lawyer): Lawyer {
        val sql = """
            INSERT INTO LawFirm.lawyer (first_name, last_name, specialization)
            VALUES (?, ?, ?)
        """.trimIndent()

        jdbcTemplate.update(sql, lawyer.firstName, lawyer.lastName, lawyer.specialization)
        return lawyer
    }

    fun updateLawyer(lawyer: Lawyer): Lawyer {
        val sql = """
            UPDATE LawFirm.lawyer
            SET first_name = ?, last_name = ?, specialization = ?
            WHERE id = ?
        """.trimIndent()

        jdbcTemplate.update(sql, lawyer.firstName, lawyer.lastName, lawyer.specialization, lawyer.id)
        return lawyer
    }

    fun deleteLawyer(lawyerId: Int): Boolean {
        val sql = """
            DELETE FROM LawFirm.lawyer
            WHERE id = ?
        """.trimIndent()

        val rowsAffected = jdbcTemplate.update(sql, lawyerId)
        return rowsAffected > 0
    }
}
