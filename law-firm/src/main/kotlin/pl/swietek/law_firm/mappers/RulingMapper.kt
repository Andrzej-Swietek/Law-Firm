package pl.swietek.law_firm.mappers

import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Service
import pl.swietek.law_firm.models.Ruling
import java.sql.ResultSet

@Service
class RulingMapper(private val trialMapper: TrialMapper) : RowMapper<Ruling> {
    override fun mapRow(rs: ResultSet, rowNum: Int): Ruling {
        return Ruling(
            id = rs.getInt("ruling_id"),
            isFinal = rs.getBoolean("ruling_is_final"),
            content = rs.getString("ruling_content"),
            trialId = rs.getInt("ruling_trial_id"),
            finalizationDate = rs.getDate("ruling_finalization_date").toLocalDate(),
            trial = trialMapper.mapRow(rs, rowNum)
        )
    }
}