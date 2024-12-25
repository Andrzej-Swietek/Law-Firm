package pl.swietek.law_firm.mappers

import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Service
import pl.swietek.law_firm.models.Ruling
import pl.swietek.law_firm.models.Trial
import java.sql.Date
import java.sql.ResultSet
import java.time.LocalDate

@Service
class RulingMapper(private val trialMapper: TrialMapper) : RowMapper<Ruling> {
    override fun mapRow(rs: ResultSet, rowNum: Int): Ruling {
        return Ruling(
            id = rs.getInt("id"),
            isFinal = rs.getBoolean("is_final"),
            content = rs.getString("content"),
            trialId = rs.getInt("trial_id"),
            finalizationDate = rs.getDate("finalization_date").toLocalDate(),
            trial = if (rs.getObject("ruling_trial_id") != null) {
                Trial(
                    id = rs.getInt("ruling_trial_id"),
                    title = rs.getString("trial_title"),
                    description = "",
                    trialStatusId = 0,
                    clientId = 0,
                    lawyerId = 0,
                    judgeId = 0,
                    date = LocalDate.now(),
                    caseId = 0,
                    trialStatus = null,
                )
            } else null
        )
    }

    fun mapRowFromPrefix(rs: ResultSet, prefix: String): Ruling {
        return Ruling(
            id = rs.getInt("${prefix}ruling_id"),
            content = rs.getString("${prefix}ruling_content"),
            trialId = rs.getInt("${prefix}trial_id"),
            finalizationDate = rs.getDate("${prefix}finalization_date").toLocalDate(),
            isFinal = rs.getBoolean("${prefix}is_final")
        )
    }
}