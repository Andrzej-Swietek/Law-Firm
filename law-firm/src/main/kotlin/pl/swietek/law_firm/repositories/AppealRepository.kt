package pl.swietek.law_firm.repositories

import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository
import pl.swietek.law_firm.mappers.AppealMapper
import pl.swietek.law_firm.models.Appeal

@Repository
class AppealRepository(
    private val jdbcTemplate: JdbcTemplate,
    private val appealMapper: AppealMapper
) {

    fun getAppealById(appealId: Int): Appeal? {
        val sql = """
            SELECT 
                a.*, 
                ir.id AS appeal_initial_ruling_id, 
                fr.id AS appeal_final_ruling_id, 
                t.id AS appeal_trial_id
            FROM LawFirm.appeal a
            LEFT JOIN LawFirm.ruling ir ON a.initial_ruling_id = ir.id
            LEFT JOIN LawFirm.ruling fr ON a.final_ruling_id = fr.id
            LEFT JOIN LawFirm.trial t ON a.trial_id = t.id
            WHERE a.id = ?
        """.trimIndent()

        return jdbcTemplate.queryForObject(sql, appealMapper, appealId)
    }

    fun getAllAppeals(page: Int, size: Int): List<Appeal> {
        val offset = (page - 1) * size
        val sql = """
            SELECT 
                a.*, 
                ir.id AS appeal_initial_ruling_id, 
                fr.id AS appeal_final_ruling_id, 
                t.id AS appeal_trial_id
            FROM LawFirm.appeal a
            LEFT JOIN LawFirm.ruling ir ON a.initial_ruling_id = ir.id
            LEFT JOIN LawFirm.ruling fr ON a.final_ruling_id = fr.id
            LEFT JOIN LawFirm.trial t ON a.trial_id = t.id
            LIMIT ? OFFSET ?
        """.trimIndent()

        return jdbcTemplate.query(sql, appealMapper, size, offset)
    }

    fun saveAppeal(newAppeal: Appeal): Appeal {
        val sql = """
            INSERT INTO LawFirm.appeal (initial_ruling_id, final_ruling_id, trial_id)
            VALUES (?, ?, ?)
        """.trimIndent()

        val id = jdbcTemplate.update(sql, newAppeal.initialRulingId, newAppeal.finalRulingId, newAppeal.trialId)
        newAppeal.id = id
        return newAppeal
    }

    fun updateAppeal(updatedAppeal: Appeal): Appeal {
        val sql = """
            UPDATE LawFirm.appeal 
            SET initial_ruling_id = ?, final_ruling_id = ?, trial_id = ?
            WHERE id = ?
        """.trimIndent()

        jdbcTemplate.update(sql, updatedAppeal.initialRulingId, updatedAppeal.finalRulingId, updatedAppeal.trialId, updatedAppeal.id)
        return updatedAppeal
    }

    fun deleteAppeal(appealId: Int): Boolean {
        val sql = "DELETE FROM LawFirm.appeal WHERE id = ?"
        val rowsAffected = jdbcTemplate.update(sql, appealId)
        return rowsAffected > 0
    }
}
