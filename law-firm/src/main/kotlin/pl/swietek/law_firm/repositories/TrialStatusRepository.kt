package pl.swietek.law_firm.repositories


import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository
import pl.swietek.law_firm.mappers.TrialStatusMapper
import pl.swietek.law_firm.models.TrialStatus

@Repository
class TrialStatusRepository(private val jdbcTemplate: JdbcTemplate) {

    private val trialStatusRowMapper = TrialStatusMapper()

    fun getAllTrialStatuses(): List<TrialStatus> {
        val sql = "SELECT * FROM LawFirm.trial_status"
        return jdbcTemplate.query(sql, trialStatusRowMapper)
    }

    fun getTrialStatusById(statusId: Long): TrialStatus? {
        val sql = "SELECT * FROM LawFirm.trial_status WHERE id = ?"
        return jdbcTemplate.query(sql, trialStatusRowMapper, statusId).firstOrNull()
    }

    fun saveTrialStatus(trialStatus: TrialStatus): TrialStatus {
        val sql = "INSERT INTO LawFirm.trial_status (id, name) VALUES (null, ?)"
        jdbcTemplate.update(sql, trialStatus.name)
        return trialStatus
    }

    fun updateTrialStatus(trialStatus: TrialStatus): TrialStatus {
        val sql = "UPDATE LawFirm.trial_status SET name = ? WHERE id = ?"
        jdbcTemplate.update(sql, trialStatus.name, trialStatus.id)
        return trialStatus
    }

    fun deleteTrialStatus(statusId: Int): Boolean {
        val sql = "DELETE FROM LawFirm.trial_status WHERE id = ?"
        val rowsAffected = jdbcTemplate.update(sql, statusId)
        return rowsAffected > 0
    }
}
