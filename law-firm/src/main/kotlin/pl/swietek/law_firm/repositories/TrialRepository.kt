package pl.swietek.law_firm.repositories

import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.jdbc.support.KeyHolder
import org.springframework.stereotype.Repository
import pl.swietek.law_firm.mappers.TrialMapper
import pl.swietek.law_firm.models.Trial
import pl.swietek.law_firm.requests.TrialRequest
import java.sql.Connection
import java.sql.PreparedStatement

@Repository
class TrialRepository(
    private val jdbcTemplate: JdbcTemplate,
    private val trialRowMapper: TrialMapper
) {

    fun getTrials(): List<Trial> {
        val sql = """
            SELECT * 
            FROM LawFirm.full_data_trial
        """.trimIndent()

        return jdbcTemplate.query(sql, trialRowMapper)
    }

    fun getAllTrials(page: Int, size: Int): List<Trial> {
        val offset = (page - 1) * size
        val sql = """
            SELECT * 
            FROM LawFirm.full_data_trial
            LIMIT ? OFFSET ?
        """.trimIndent()

        return jdbcTemplate.query(sql, trialRowMapper, size, offset)
    }

    fun getTrialById(trialId: Int): Trial? {
        val sql = """
            SELECT * 
            FROM LawFirm.full_data_trial
            WHERE trial_id = ?
        """.trimIndent()

        return jdbcTemplate.query(sql, trialRowMapper, trialId).firstOrNull()
    }

    fun getTrialsByClientId(clientId: Int): List<Trial> {
        val sql = """
            SELECT * 
            FROM LawFirm.full_data_trial
            WHERE client_id = ?
        """.trimIndent()

        return jdbcTemplate.query(sql, trialRowMapper, clientId)
    }

    fun getTrialsByLawyerId(lawyerId: Int): List<Trial> {
        val sql = """
            SELECT * 
            FROM LawFirm.full_data_trial
            WHERE lawyer_id = ?
        """.trimIndent()

        return jdbcTemplate.query(sql, trialRowMapper, lawyerId)
    }

    fun saveTrial(trial: TrialRequest): Trial {
        val sql = """
            INSERT INTO LawFirm.trial (
                title, description, trial_status_id, client_id, lawyer_id, judge_id, date, case_id
            ) VALUES ( ?, ?, ?, ?, ?, ?, ?, ? )
        """.trimIndent()

        val keyHolder: KeyHolder = GeneratedKeyHolder()

        jdbcTemplate.update({ connection: Connection ->
            val ps: PreparedStatement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)
            ps.setString(1, trial.title)
            ps.setString(2, trial.description)
            ps.setInt(3, trial.trialStatusId)
            ps.setInt(4, trial.clientId)
            ps.setInt(5,  trial.lawyerId)
            ps.setInt(6,  trial.judgeId)
            ps.setDate(7, java.sql.Date.valueOf(trial.date))
            ps.setInt(8, trial.caseId)
            ps
        }, keyHolder)

        val generatedId: Int = keyHolder.keyList
            .firstOrNull()
            ?.get("id")
            .toString().toInt()
            ?: throw Exception("Failed to retrieve generated ID")

        return this.getTrialById(generatedId)!!
    }

    fun updateTrial(trial: TrialRequest): Trial {
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
        return this.getTrialById(trial.id!!)!!
    }

    fun deleteTrial(trialId: Int): Boolean {
        val sql = "DELETE FROM LawFirm.trial WHERE id = ?"
        val rowsAffected = jdbcTemplate.update(sql, trialId)
        return rowsAffected > 0
    }
}
