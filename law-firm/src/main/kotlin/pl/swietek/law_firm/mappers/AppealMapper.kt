package pl.swietek.law_firm.mappers

import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Service
import pl.swietek.law_firm.models.Appeal
import pl.swietek.law_firm.reponses.RulingBriefResponse
import java.sql.ResultSet

@Service
class AppealMapper(private val rulingMapper: RulingMapper, private val trialMapper: TrialMapper) : RowMapper<Appeal> {
    override fun mapRow(rs: ResultSet, rowNum: Int): Appeal {
        return Appeal(
            id = rs.getInt("appeal_id"),
            initialRulingId = rs.getInt("appeal_initial_ruling_id"),
            finalRulingId = rs.getInt("appeal_final_ruling_id"),
            trialId = rs.getInt("appeal_trial_id"),

            initialRuling = rulingMapper.mapRowFromPrefix(rs, "appeal_initial_"),
            finalRuling = rulingMapper.mapRowFromPrefix(rs, "appeal_final_"),
//            trial = trialMapper.mapRow(rs, rowNum),
        )
    }
}