package pl.swietek.law_firm.repositories

import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository
import pl.swietek.law_firm.mappers.TrialMapper
import pl.swietek.law_firm.models.Trial

@Repository
class TrialRepository(
    private val jdbcTemplate: JdbcTemplate,
    private val trialRowMapper: TrialMapper
) {

    fun getTrials(): List<Trial> {
        val sql = """
            SELECT 
                t.id AS trial_id, 
                t.title AS trial_title, 
                t.description AS trial_description, 
                t.trial_status_id AS trial_status_id, 
                ts.name AS trial_status_name, 
                t.client_id AS client_id, 
                c.first_name AS client_first_name, 
                c.last_name AS client_last_name,
                t.lawyer_id AS lawyer_id, 
                l.first_name AS lawyer_first_name, 
                l.last_name AS lawyer_last_name,
                t.judge_id AS judge_id, 
                j.first_name AS judge_first_name, 
                j.last_name AS judge_last_name,
                t.date AS trial_date, 
                t.case_id AS case_id
            FROM LawFirm.trial t
            LEFT JOIN LawFirm.trial_status ts ON t.trial_status_id = ts.id
            LEFT JOIN LawFirm.client c ON t.client_id = c.id
            LEFT JOIN LawFirm.lawyer l ON t.lawyer_id = l.id
            LEFT JOIN LawFirm.judge j ON t.judge_id = j.id
        """.trimIndent()

        return jdbcTemplate.query(sql, trialRowMapper)
    }

    fun getAllTrials(page: Int, size: Int): List<Trial> {
        val offset = (page - 1) * size
        val sql = """
            SELECT 
                t.id AS trial_id, 
                t.title AS trial_title, 
                t.description AS trial_description, 
                t.trial_status_id AS trial_status_id, 
                ts.name AS trial_status_name, 
                t.client_id AS client_id, 
                c.first_name AS client_first_name, 
                c.last_name AS client_last_name,
                t.lawyer_id AS lawyer_id, 
                l.first_name AS lawyer_first_name, 
                l.last_name AS lawyer_last_name,
                t.judge_id AS judge_id, 
                j.first_name AS judge_first_name, 
                j.last_name AS judge_last_name,
                t.date AS trial_date, 
                t.case_id AS case_id
            FROM LawFirm.trial t
            LEFT JOIN LawFirm.trial_status ts ON t.trial_status_id = ts.id
            LEFT JOIN LawFirm.client c ON t.client_id = c.id
            LEFT JOIN LawFirm.lawyer l ON t.lawyer_id = l.id
            LEFT JOIN LawFirm.judge j ON t.judge_id = j.id
            LIMIT ? OFFSET ?
        """.trimIndent()

        return jdbcTemplate.query(sql, trialRowMapper, size, offset)
    }

    fun getTrialById(trialId: Int): Trial? {
        val sql = """
            SELECT 
                t.id AS trial_id, 
                t.title AS trial_title, 
                t.description AS trial_description, 
                t.trial_status_id AS trial_status_id, 
                ts.name AS trial_status_name, 
                t.client_id AS client_id, 
                c.first_name AS client_first_name, 
                c.last_name AS client_last_name,
                t.lawyer_id AS lawyer_id, 
                l.first_name AS lawyer_first_name, 
                l.last_name AS lawyer_last_name,
                t.judge_id AS judge_id, 
                j.first_name AS judge_first_name, 
                j.last_name AS judge_last_name,
                t.date AS trial_date, 
                t.case_id AS case_id
            FROM LawFirm.trial t
            LEFT JOIN LawFirm.trial_status ts ON t.trial_status_id = ts.id
            LEFT JOIN LawFirm.client c ON t.client_id = c.id
            LEFT JOIN LawFirm.lawyer l ON t.lawyer_id = l.id
            LEFT JOIN LawFirm.judge j ON t.judge_id = j.id
            WHERE t.id = ?
        """.trimIndent()

        return jdbcTemplate.query(sql, trialRowMapper, trialId).firstOrNull()
    }

    fun saveTrial(trial: Trial): Trial {
        val sql = """
            INSERT INTO LawFirm.trial (
                title, description, trial_status_id, client_id, lawyer_id, judge_id, date, case_id
            ) VALUES ( ?, ?, ?, ?, ?, ?, ?, ? )
        """.trimIndent()

        jdbcTemplate.update(
            sql,
            trial.title,
            trial.description,
            trial.trialStatusId,
            trial.clientId,
            trial.lawyerId,
            trial.judgeId,
            trial.date,
            trial.caseId
        )
        return trial
    }

    fun updateTrial(trial: Trial): Trial {
        val sql = """
            UPDATE LawFirm.trial
            SET title = ?, 
                description = ?, 
                trial_status_id = ?, 
                client_id = ?, 
                lawyer_id = ?, 
                judge_id = ?, 
                date = ?, 
                case_id = ?
            WHERE id = ?
        """.trimIndent()

        jdbcTemplate.update(
            sql,
            trial.title,
            trial.description,
            trial.trialStatusId,
            trial.clientId,
            trial.lawyerId,
            trial.judgeId,
            trial.date,
            trial.caseId,
            trial.id
        )
        return trial
    }

    fun deleteTrial(trialId: Int): Boolean {
        val sql = "DELETE FROM LawFirm.trial WHERE id = ?"
        val rowsAffected = jdbcTemplate.update(sql, trialId)
        return rowsAffected > 0
    }
}
