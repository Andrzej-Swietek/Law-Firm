package pl.swietek.law_firm.mappers

import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Service
import pl.swietek.law_firm.models.Appeal
import java.sql.ResultSet

@Service
class AppealMapper(private val rulingMapper: RulingMapper, private val trialMapper: TrialMapper) : RowMapper<Appeal> {
    override fun mapRow(rs: ResultSet, rowNum: Int): Appeal {
        return Appeal(
            id = rs.getInt("appeal_id"),
            initialRulingId = rs.getInt("appeal_initial_ruling_id"),
            finalRulingId = rs.getInt("appeal_final_ruling_id"),
            trialId = rs.getInt("appeal_trial_id"),
            initialRuling = rulingMapper.mapRow(rs, rowNum),
            finalRuling = rulingMapper.mapRow(rs, rowNum),
            trial = trialMapper.mapRow(rs, rowNum),
        )
    }
}