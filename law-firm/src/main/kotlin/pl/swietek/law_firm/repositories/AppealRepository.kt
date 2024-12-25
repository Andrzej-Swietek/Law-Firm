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
                a.id AS appeal_id,
                a.initial_ruling_id AS appeal_initial_ruling_id,
                a.final_ruling_id AS appeal_final_ruling_id,
                a.trial_id AS appeal_trial_id,
            
                -- Kolumny dla initialRuling
                ir.id AS appeal_initial_ruling_id,
                ir.content AS appeal_initial_ruling_content,
                ir.trial_id AS appeal_initial_trial_id,
                ir.finalization_date AS appeal_initial_finalization_date,
                ir.is_final AS appeal_initial_is_final,
            
                -- Kolumny dla finalRuling
                fr.id AS appeal_final_ruling_id,
                fr.content AS appeal_final_ruling_content,
                fr.trial_id AS appeal_final_trial_id,
                fr.finalization_date AS appeal_final_finalization_date,
                fr.is_final AS appeal_final_is_final,
            
                -- Kolumny dla trial 
                t.id AS trial_id,
                t.title AS trial_title,
                t.description AS trial_description,
                t.trial_status_id AS trial_trial_status_id,
                t.client_id AS trial_client_id,
                t.lawyer_id AS trial_lawyer_id,
                t.judge_id AS trial_judge_id,
                t.date AS trial_date,
                t.case_id AS trial_case_id,
                    
                    -- trial > lawyer
                    l.id AS lawyer_id,
                    l.first_name AS lawyer_first_name,
                    l.last_name AS lawyer_last_name,
                    l.specialization AS specialization,
                    
                    -- trial > client
                    c.first_name as client_first_name,
                    c.last_name AS client_lastname,
                    c.email as client_email,
                    c.contact_data_id as client_contact_data_id
                
                
            FROM LawFirm.appeal a
            LEFT JOIN LawFirm.ruling ir ON a.initial_ruling_id = ir.id
            LEFT JOIN LawFirm.ruling fr ON a.final_ruling_id = fr.id
            LEFT JOIN LawFirm.trial t ON a.trial_id = t.id
            LEFT JOIN LawFirm.client c ON t.client_id = c.id
            LEFT JOIN LawFirm.lawyer l ON t.lawyer_id = l.id
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
