package pl.swietek.law_firm.repositories

import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository
import pl.swietek.law_firm.mappers.RulingMapper
import pl.swietek.law_firm.models.Ruling

@Repository
class RulingRepository(
    private val jdbcTemplate: JdbcTemplate,
    private val rulingMapper: RulingMapper
) {

    fun getRulingById(rulingId: Int): Ruling? {
        val sql = """
            SELECT 
                r.*, 
                t.id AS ruling_trial_id, 
                t.title AS trial_title
            FROM LawFirm.ruling r
            LEFT JOIN LawFirm.trial t ON r.trial_id = t.id
            WHERE r.id = ?
        """.trimIndent()

        return jdbcTemplate.queryForObject(sql, rulingMapper, rulingId)
    }

    fun getAllRulings(page: Int, size: Int): List<Ruling> {
        val offset = (page - 1) * size
        val sql = """
            SELECT 
                r.*, 
                t.id AS ruling_trial_id, 
                t.title AS trial_title
            FROM LawFirm.ruling r
            LEFT JOIN LawFirm.trial t ON r.trial_id = t.id
            LIMIT ? OFFSET ?
        """.trimIndent()

        return jdbcTemplate.query(sql, rulingMapper, size, offset)
    }

    fun saveRuling(newRuling: Ruling): Ruling {
        val sql = """
            INSERT INTO LawFirm.ruling (is_final, content, trial_id, finalization_date)
            VALUES (?, ?, ?, ?)
        """.trimIndent()

        val id = jdbcTemplate.update(sql, newRuling.isFinal, newRuling.content, newRuling.trialId, newRuling.finalizationDate)
        newRuling.id = id
        return newRuling
    }

    fun updateRuling(updatedRuling: Ruling): Ruling {
        val sql = """
            UPDATE LawFirm.ruling 
            SET is_final = ?, content = ?, trial_id = ?, finalization_date = ?
            WHERE id = ?
        """.trimIndent()

        jdbcTemplate.update(sql, updatedRuling.isFinal, updatedRuling.content, updatedRuling.trialId, updatedRuling.finalizationDate, updatedRuling.id)
        return updatedRuling
    }

    fun deleteRuling(rulingId: Int): Boolean {
        val sql = "DELETE FROM LawFirm.ruling WHERE id = ?"
        val rowsAffected = jdbcTemplate.update(sql, rulingId)
        return rowsAffected > 0
    }
}